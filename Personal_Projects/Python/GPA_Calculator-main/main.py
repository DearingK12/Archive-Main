# main.py

import os
from storage import (
    ensure_dirs,
    load_students,
    add_student,
    archive_previous_report,
    NEW_DIR,
    REPORT_FILE
)

from gpa import (
    calculate_gpa,
    get_semester_gpa,
    get_year_gpa,
    get_cumulative_gpa
)

from utils import build_report, export_to_csv

import traceback
from datetime import datetime


# -----------------------------
# INPUT SYSTEM
# -----------------------------

def input_courses():
    courses = []

    print("\nEnter courses (type 'done' to finish)\n")

    while True:
        name = input("Course name: ").strip()
        if name.lower() == "done":
            break

        while True:
            grade = input("Grade (A-F): ").strip().upper()
            if grade in ["A", "B", "C", "D", "F"]:
                break
            print("Invalid grade. Please enter A, B, C, D, or F.")

        while True:
            try:
                credits = float(input("Credits: "))
                if credits > 0:
                    break
                else:
                    print("Credits must be positive.")
            except ValueError:
                print("Invalid credits. Please enter a number.")

        while True:
            ctype = input("Type (regular/honors/ap): ").strip().lower()
            if ctype in ["regular", "honors", "ap"]:
                break
            print("Invalid type. Please enter regular, honors, or ap.")

        multiplier = {
            "regular": 1.0,
            "honors": 1.1,
            "ap": 1.2
        }.get(ctype, 1.0)

        courses.append({
            "name": name,
            "grade": grade,
            "credits": credits,
            "type": ctype,
            "weight": multiplier
        })

    return courses


def create_student():
    data = load_students()

    while True:
        sid = input("Student ID: ").strip()
        if sid in data:
            print("Student ID already exists. Please choose a different ID.")
        else:
            break

    student = {
        "name": input("Student Name: ").strip(),
        "id": sid,
        "terms": []
    }

    while True:
        semester = input("\nSemester name (e.g. Fall 2026, or 'done'): ").strip()
        if semester.lower() == "done":
            break

        term = {
            "semester": semester,
            "courses": input_courses()
        }

        student["terms"].append(term)

    add_student(student)
    print("\nStudent saved successfully!")


def view_students():
    data = load_students()

    print("\n=== STUDENTS ===")
    for sid, s in data.items():
        print(f"{sid} - {s['name']}")


# -----------------------------
# GPA MENU SYSTEM
# -----------------------------

def calculate_gpa_menu(student):
    print("\n===== GPA OPTIONS =====")
    print("1. Semester GPA")
    print("2. Year GPA")
    print("3. Cumulative GPA")

    choice = input("Select option: ").strip()

    if choice == "1":
        semester = input("Enter semester (e.g. Fall 2026): ").strip()
        gpa = get_semester_gpa(student, semester)

        if gpa is None:
            print("Semester not found.")
            return

        mode = "SEMESTER GPA"
        extra_label = semester
        report = build_report(student, gpa, mode, extra_label)

    elif choice == "2":
        year = int(input("Enter year (e.g. 2026): "))
        gpa = get_year_gpa(student, year)

        if gpa is None:
            print("No data for that year.")
            return

        mode = "YEAR GPA"
        extra_label = str(year)
        report = build_report(student, gpa, mode, extra_label)

    elif choice == "3":
        gpa = get_cumulative_gpa(student)

        if gpa is None:
            print("No courses found for this student.")
            return

        mode = "CUMULATIVE GPA"
        extra_label = "All Terms"
        report = build_report(student, gpa, mode, extra_label)

    else:
        print("Invalid option.")
        return

    archive_previous_report()

    path = os.path.join(NEW_DIR, REPORT_FILE)

    with open(path, "w") as f:
        f.write(report)

    print("\nReport generated successfully!")
    print(f"Saved to: {path}")
    print(f"GPA Calculated: {gpa}")

    # Offer CSV export
    export_choice = input("Export courses to CSV? (y/n): ").strip().lower()
    if export_choice == "y":
        csv_path = os.path.join(NEW_DIR, "report.csv")
        export_to_csv(student, mode, extra_label, gpa, csv_path)
        print(f"CSV exported to: {csv_path}")


def select_student():
    data = load_students()

    sid = input("Enter Student ID: ")

    if sid not in data:
        print("Student not found.")
        return

    calculate_gpa_menu(data[sid])


# -----------------------------
# MAIN MENU
# -----------------------------

def main():
    ensure_dirs()

    try:
        while True:
            print("\n===== SUPER GPA SYSTEM =====")
            print("1. Add Student")
            print("2. View Students")
            print("3. Calculate GPA")
            print("4. Help")
            print("5. Exit")

            choice = input("Select option: ").strip()

            if choice == "1":
                create_student()
            elif choice == "2":
                view_students()
            elif choice == "3":
                select_student()
            elif choice == "4":
                print_help()
            elif choice == "5":
                break
            else:
                print("Invalid option.")
    except Exception as e:
        error_msg = f"Error occurred at {datetime.now()}:\n{traceback.format_exc()}"
        date_str = datetime.now().strftime("%Y-%m-%d")
        time_str = datetime.now().strftime("%H%M%S")
        date_dir = f"errors/{date_str}"
        os.makedirs(date_dir, exist_ok=True)
        error_file = f"{date_dir}/error_{time_str}.txt"
        with open(error_file, "w") as f:
            f.write(error_msg)
        print(f"An error occurred. Details saved to {error_file}")
        print("Please check the errors folder and try again.")


def print_help():
    print("\n===== HELP =====")
    print("1. Add Student: Enter student details and courses for multiple terms.")
    print("2. View Students: List all stored students by ID and name.")
    print("3. Calculate GPA: Select a student and choose semester, year, or cumulative GPA.")
    print("   - Reports are saved to new_reports/ and archived to past_reports/.")
    print("   - Option to export course data to CSV after calculation.")
    print("Grades: A=4.0, B=3.0, C=2.0, D=1.0, F=0.0")
    print("Weighting: Regular=1.0, Honors=1.1, AP=1.2 (capped at 4.0)")
    print("Type 'done' to finish entering courses or terms.")
    print("Data is stored in data/students.json.")


if __name__ == "__main__":
    main()