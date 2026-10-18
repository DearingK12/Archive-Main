import json
import re
import sys

COMMON_PASSWORDS = {
    "password",
    "password1",
    "password123",
    "123456",
    "123456789",
    "qwerty",
    "abc123",
    "letmein",
    "welcome",
    "monkey",
    "dragon",
    "football",
    "admin",
    "login",
    "iloveyou",
    "sunshine",
    "princess",
    "hello",
    "passw0rd",
    "master",
    "shadow",
    "baseball",
    "donald",
    "trustno1",
    "111111",
    "000000",
}


def _add_reason(reasons, message):
    if message not in reasons:
        reasons.append(message)


def evaluate_password(password):
    if not isinstance(password, str):
        return {
            "score": 0,
            "level": "Weak",
            "weak_reasons": ["Password must be a string value."],
        }

    if password == "":
        return {
            "score": 0,
            "level": "Weak",
            "weak_reasons": ["Password is empty."],
        }

    reasons = []
    score = 100
    lowered = password.lower()

    if len(password) < 8:
        _add_reason(reasons, "Password is too short; use at least 8 characters.")
        score -= 35
    elif len(password) < 12:
        score -= 10

    if len(password) >= 16:
        score += 10

    has_lower = any(ch.islower() for ch in password)
    has_upper = any(ch.isupper() for ch in password)
    has_digit = any(ch.isdigit() for ch in password)
    has_symbol = any(not ch.isalnum() for ch in password)

    if not has_lower:
        _add_reason(reasons, "Add lowercase letters.")
        score -= 15
    if not has_upper:
        _add_reason(reasons, "Add uppercase letters.")
        score -= 15
    if not has_digit:
        _add_reason(reasons, "Add numbers to increase complexity.")
        score -= 10
    if not has_symbol:
        _add_reason(reasons, "Add symbols like !, @, or #.")
        score -= 10

    if password.isdigit():
        _add_reason(reasons, "Only numbers are easy to guess.")
        score -= 25

    if re.search(r"(.)\1{2,}", password):
        _add_reason(reasons, "Avoid repeated characters such as 111 or aaa.")
        score -= 10

    if re.search(r"(?:012|123|234|345|456|567|678|789|890)", password):
        _add_reason(reasons, "Avoid sequential numbers like 1234.")
        score -= 10

    if lowered in COMMON_PASSWORDS or any(keyword in lowered for keyword in ("password", "admin", "welcome", "qwerty", "login", "letmein", "dragon", "monkey")):
        _add_reason(reasons, "This is a common password.")
        score -= 35

    if any(keyword in lowered for keyword in ("name", "user", "email", "test")):
        _add_reason(reasons, "Avoid using personal or predictable words.")
        score -= 10

    score = max(0, min(100, score))

    if score >= 80:
        level = "Strong"
    elif score >= 60:
        level = "Moderate"
    else:
        level = "Weak"

    return {
        "score": int(score),
        "level": level,
        "weak_reasons": reasons,
    }


if __name__ == "__main__":
    if len(sys.argv) > 1:
        password = sys.argv[1]
    else:
        password = input("Enter a password to check: ")

    result = evaluate_password(password)
    print(json.dumps(result, indent=2))
