from yahtzee_game import YahtzeeGame
from high_scores import HighScores
from settings import Settings

class YahtzeeCLI:
    def __init__(self):
        self.game = YahtzeeGame()
        self.high_scores = HighScores()
        self.settings = Settings()

    def play(self):
        print("Welcome to Yahtzee!")
        while not self.is_game_over():
            self.show_status()
            self.roll_phase()
            self.choose_category()

            # Ask if player wants to see current score
            see_score = input("Do you want to see your current score? (y/n): ").lower()
            if see_score == "y":
                self.show_current_score()

        self.end_game()

    def is_game_over(self):
        return len(self.game.score_sheet) == 13

    def show_status(self):
        print(f"\nCurrent Dice: {self.game.dice}")
        print(f"Rolls left: {self.game.rolls_left}")
        print(f"Score Sheet: {self.game.score_sheet}")

    def roll_phase(self):
        self.game.rolls_left = 3
        keep = [False] * 5
        while self.game.rolls_left > 0:
            dice = self.game.roll_dice(keep)
            print(f"Rolled: {dice}")
            if self.game.rolls_left == 0:
                break

            keep_input = input(
                "Enter the positions (1-5) of dice to KEEP (e.g. 1 3 5), or press Enter to reroll all: "
            )
            if keep_input.strip() == "":
                keep = [False] * 5
            else:
                keep_positions = keep_input.split()
                keep = [str(i+1) in keep_positions for i in range(5)]

    def choose_category(self):
        available = [cat for cat in self.get_categories() if cat not in self.game.score_sheet]
        print(f"Available categories: {available}")

        while True:
            category = input("Choose a category to score: ")
            if category not in available:
                print("Invalid. Choose a valid category.")
                continue

            score = self.calculate_score(category)
            print(f"If you put your dice in '{category}', you will score: {score}")
            confirm = input("Confirm this category? (y/n): ").lower()
            if confirm == "y":
                self.game.score_sheet[category] = score
                print(f"Scored {score} in {category}")
                break
            else:
                print("Choose a different category.")

    def show_current_score(self):
        if not self.game.score_sheet:
            print("No scores yet.")
            return
        total_score = sum(self.game.score_sheet.values())
        print("\n--- Current Score ---")
        for category, score in self.game.score_sheet.items():
            print(f"{category}: {score}")
        print(f"Total Score: {total_score}")
        print("--------------------\n")

    def get_categories(self):
        return ["ones","twos","threes","fours","fives","sixes",
                "three_of_a_kind","four_of_a_kind","full_house",
                "small_straight","large_straight","yahtzee","chance"]

    def calculate_score(self, category):
        dice = self.game.dice
        counts = [dice.count(i) for i in range(1,7)]
        if category == "ones": return counts[0]*1
        elif category == "twos": return counts[1]*2
        elif category == "threes": return counts[2]*3
        elif category == "fours": return counts[3]*4
        elif category == "fives": return counts[4]*5
        elif category == "sixes": return counts[5]*6
        elif category == "three_of_a_kind": return sum(dice) if max(counts)>=3 else 0
        elif category == "four_of_a_kind": return sum(dice) if max(counts)>=4 else 0
        elif category == "full_house": return 25 if 3 in counts and 2 in counts else 0
        elif category == "small_straight":
            straights = [set([1,2,3,4]), set([2,3,4,5]), set([3,4,5,6])]
            return 30 if any(straight.issubset(set(dice)) for straight in straights) else 0
        elif category == "large_straight": 
            return 40 if set([1,2,3,4,5]).issubset(set(dice)) or set([2,3,4,5,6]).issubset(set(dice)) else 0
        elif category == "yahtzee": return 50 if max(counts)==5 else 0
        elif category == "chance": return sum(dice)
        return 0

    def end_game(self):
        total_score = sum(self.game.score_sheet.values())
        print(f"Game over! Your total score: {total_score}")
        name = input("Enter your name for high scores: ")
        self.high_scores.add_score(name, total_score)
        print("\n--- High Scores ---")
        for entry in self.high_scores.scores[:5]:
            print(f"{entry['name']}: {entry['score']}")
        print("-------------------")

if __name__ == "__main__":
    cli = YahtzeeCLI()
    cli.play()
