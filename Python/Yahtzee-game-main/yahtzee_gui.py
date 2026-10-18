from PyQt5.QtWidgets import QMainWindow

class YahtzeeWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Yahtzee - Advanced Single Player")
        self.setGeometry(100, 100, 800, 600)
        # TODO: Add dice animation, score sheet, sound, settings
