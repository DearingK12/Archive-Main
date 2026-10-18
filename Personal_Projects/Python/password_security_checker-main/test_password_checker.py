import unittest

from password_checker import evaluate_password


class PasswordCheckerTests(unittest.TestCase):
    def test_strong_password_is_accepted(self):
        result = evaluate_password("StrongPass!2026")
        self.assertGreaterEqual(result["score"], 80)
        self.assertEqual(result["level"], "Strong")
        self.assertEqual(result["weak_reasons"], [])

    def test_common_password_is_detected(self):
        result = evaluate_password("password123")
        self.assertLess(result["score"], 50)
        self.assertIn("common password", " ".join(result["weak_reasons"]))

    def test_simple_numeric_password_is_explained(self):
        result = evaluate_password("123456")
        self.assertEqual(result["level"], "Weak")
        self.assertTrue(any("too short" in reason.lower() for reason in result["weak_reasons"]))
        self.assertTrue(any("common" in reason.lower() for reason in result["weak_reasons"]))


if __name__ == "__main__":
    unittest.main()
