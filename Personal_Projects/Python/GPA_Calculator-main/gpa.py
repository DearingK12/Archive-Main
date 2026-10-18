# gpa.py

from utils import extract_year, calculate_gpa


def get_semester_gpa(student, semester_name):
    for term in student["terms"]:
        if term["semester"].lower() == semester_name.lower():
            return calculate_gpa(term["courses"])
    return None


def get_year_gpa(student, year):
    all_courses = []

    for term in student["terms"]:
        if extract_year(term["semester"]) == year:
            all_courses.extend(term["courses"])

    return calculate_gpa(all_courses) if all_courses else None


def get_cumulative_gpa(student):
    all_courses = []

    for term in student["terms"]:
        all_courses.extend(term["courses"])

    return calculate_gpa(all_courses) if all_courses else None