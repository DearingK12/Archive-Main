import os
import json
import shutil
from datetime import datetime

# -----------------------------
# PATH CONFIG
# -----------------------------
DATA_DIR = "data"
STUDENT_FILE = os.path.join(DATA_DIR, "students.json")

NEW_DIR = "new_reports"
OLD_DIR = "past_reports"
REPORT_FILE = "report.txt"

BACKUP_DIR = os.path.join(DATA_DIR, "backup")


# -----------------------------
# SETUP DIRECTORIES
# -----------------------------
def ensure_dirs():
    os.makedirs(DATA_DIR, exist_ok=True)
    os.makedirs(NEW_DIR, exist_ok=True)
    os.makedirs(OLD_DIR, exist_ok=True)
    os.makedirs(BACKUP_DIR, exist_ok=True)
    os.makedirs("errors", exist_ok=True)

    if not os.path.exists(STUDENT_FILE):
        safe_write_json(STUDENT_FILE, {})


# -----------------------------
# SAFE JSON READ (NO CRASH)
# -----------------------------
def load_students():
    try:
        with open(STUDENT_FILE, "r") as f:
            data = json.load(f)

        if not isinstance(data, dict):
            return {}

        return data

    except (json.JSONDecodeError, FileNotFoundError):
        backup_corrupt_file()
        return {}


# -----------------------------
# SAFE JSON WRITE (ATOMIC)
# -----------------------------
def safe_write_json(path, data):
    temp_path = path + ".tmp"

    with open(temp_path, "w") as f:
        json.dump(data, f, indent=4)

    os.replace(temp_path, path)


def save_students(data):
    if not isinstance(data, dict):
        raise ValueError("Student database must be a dictionary")

    safe_write_json(STUDENT_FILE, data)


# -----------------------------
# STUDENT VALIDATION (LIGHTWEIGHT SCHEMA GUARD)
# -----------------------------
def validate_student(student):
    required_keys = ["name", "id", "terms"]

    for key in required_keys:
        if key not in student:
            return False

    if not isinstance(student["terms"], list):
        return False

    return True


def add_student(student):
    if not validate_student(student):
        raise ValueError("Invalid student structure")

    data = load_students()
    data[student["id"]] = student
    save_students(data)


# -----------------------------
# REPORT ARCHIVING
# -----------------------------
def archive_previous_report():
    report_path = os.path.join(NEW_DIR, REPORT_FILE)

    if os.path.exists(report_path):
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        archived_name = f"report_{timestamp}.txt"
        archived_path = os.path.join(OLD_DIR, archived_name)

        shutil.move(report_path, archived_path)


# -----------------------------
# CORRUPTION SAFETY BACKUP
# -----------------------------
def backup_corrupt_file():
    if not os.path.exists(STUDENT_FILE):
        return

    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    backup_path = os.path.join(BACKUP_DIR, f"students_{timestamp}.json")

    shutil.copy(STUDENT_FILE, backup_path)