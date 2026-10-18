# password_security_checker

A simple Python password strength checker that:
- scores a password from 0 to 100
- detects common and weak passwords
- explains why the password is weak

Usage:

python password_checker.py "YourPassword123!"

Example output:

{
  "score": 86,
  "level": "Strong",
  "weak_reasons": []
}
