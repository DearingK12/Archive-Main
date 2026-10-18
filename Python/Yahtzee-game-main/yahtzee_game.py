import random

class YahtzeeGame:
    def __init__(self):
        self.dice = [0] * 5
        self.rolls_left = 3
        self.score_sheet = {}

    def roll_dice(self, keep):
        for i in range(5):
            if not keep[i]:
                self.dice[i] = random.randint(1,6)
        self.rolls_left -= 1
        return self.dice
