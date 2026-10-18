# utils.py

from datetime import datetime
import csv


GRADE_POINTS = {
    "A": 4.0,
    "B": 3.0,
    "C": 2.0,
    "D": 1.0,
    "F": 0.0
}


def calculate_gpa(courses):
    total_points = 0.0
    total_credits = 0.0

    for c in courses:
        base = GRADE_POINTS.get(c["grade"].upper(), 0.0)
        weighted = min(base * c.get("weight", 1.0), 4.0)

        total_points += weighted * c["credits"]
        total_credits += c["credits"]

    return round(total_points / total_credits, 3) if total_credits else 0.0


def get_cumulative_gpa(student):
    all_courses = []

    for term in student["terms"]:
        all_courses.extend(term["courses"])

    return calculate_gpa(all_courses) if all_courses else None


def extract_year(semester):
    # "Fall 2026" -> 2026
    return int(semester.split()[-1])


def build_report(student, gpa_value, mode, extra_label):
    lines = []

    lines.append("=" * 50)
    lines.append("           SUPER GPA SYSTEM")
    lines.append("=" * 50)
    lines.append(f"Student: {student['name']}")
    lines.append(f"ID: {student['id']}")
    lines.append(f"Generated: {datetime.now()}")
    lines.append("")
    lines.append(f"MODE: {mode} ({extra_label})")
    lines.append("")
    lines.append("COURSES:")
    lines.append("-" * 50)

    # Filter terms based on mode
    filtered_terms = []
    if mode == "SEMESTER GPA":
        for term in student["terms"]:
            if term["semester"].lower() == extra_label.lower():
                filtered_terms.append(term)
    elif mode == "YEAR GPA":
        year = int(extra_label)
        for term in student["terms"]:
            if extract_year(term["semester"]) == year:
                filtered_terms.append(term)
    else:
        filtered_terms = student["terms"]

    for term in filtered_terms:
        lines.append(f"\n{term['semester']}:")
        for c in term["courses"]:
            lines.append(
                f"  {c['name']} | Grade: {c['grade']} | "
                f"Credits: {c['credits']} | Type: {c['type']}"
            )

    lines.append("-" * 50)
    lines.append(f"FINAL GPA: {gpa_value}")
    lines.append("=" * 50)

    return "\n".join(lines)


def export_to_csv(student, mode, extra_label, gpa_value, filename="report.csv"):
    # Filter terms as in build_report
    filtered_terms = []
    if mode == "SEMESTER GPA":
        for term in student["terms"]:
            if term["semester"].lower() == extra_label.lower():
                filtered_terms.append(term)
    elif mode == "YEAR GPA":
        year = int(extra_label)
        for term in student["terms"]:
            if extract_year(term["semester"]) == year:
                filtered_terms.append(term)
    else:
        filtered_terms = student["terms"]

    # Prepare data
    rows = []
    for term in filtered_terms:
        for c in term["courses"]:
            rows.append([term["semester"], c["name"], c["grade"], str(c["credits"]), c["type"], str(c["weight"])])

    # Calculate GPAs
    all_courses = [c for term in filtered_terms for c in term["courses"]]
    unweighted_gpa = calculate_gpa([{**c, "weight": 1.0} for c in all_courses]) if all_courses else 0.0
    weighted_gpa = gpa_value  # This is the weighted GPA already calculated
    cumulative_gpa = get_cumulative_gpa(student)

    # Write formatted table
    with open(filename, "w") as f:
        # Header
        f.write("=" * 80 + "\n")
        f.write(f"COURSE REPORT - {mode} ({extra_label})\n")
        f.write(f"Student: {student['name']} (ID: {student['id']})\n")
        f.write(f"Generated: {datetime.now()}\n")
        f.write("=" * 80 + "\n")
        f.write("\n")

        # Table header
        f.write(f"{'Semester':<15} {'Course Name':<15} {'Grade':<6} {'Credits':<8} {'Type':<10} {'Weight':<6}\n")
        f.write("-" * 80 + "\n")

        # Rows
        for row in rows:
            f.write(f"{row[0]:<15} {row[1]:<15} {row[2]:<6} {row[3]:<8} {row[4]:<10} {row[5]:<6}\n")

        f.write("-" * 80 + "\n")
        f.write("\n")

        # GPA Summary
        f.write("GPA SUMMARY\n")
        f.write("-" * 80 + "\n")
        f.write(f"Unweighted GPA: {unweighted_gpa:.2f}\n")
        f.write(f"Weighted GPA: {weighted_gpa:.2f}\n")
        f.write(f"Cumulative GPA: {cumulative_gpa:.2f}\n")
        f.write("-" * 80 + "\n")
        f.write("End of Report\n")


def extract_year(semester):
    # "Fall 2026" -> 2026
    return int(semester.split()[-1])