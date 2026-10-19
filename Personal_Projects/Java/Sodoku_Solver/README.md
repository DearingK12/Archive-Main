# Sudoku Solver

A command-line Sudoku solver written in Java. It checks that the clues are
within range and do not conflict, then uses backtracking with a
fewest-candidates-first strategy to find a solution.

## Requirements

Install a Java Development Kit (JDK) so both `java` and `javac` are available.
This is an Eclipse/VS Code Java project with `src` as its source root. The
`main.java` package matches the source file paths and works without Maven.

To avoid VS Code inferring package names from the parent repository folders,
open `Sudoku-Solver.code-workspace` in VS Code rather than opening the
repository root.

## Run on Windows

Double-click `run.bat` to enter a puzzle interactively (enter 9 rows or one
81-cell line). To read an image, run the application with `--image`; it opens
a file chooser. Or pass an image path:

```text
run.bat --image "C:\path\to\sudoku.png"
```

## Run on macOS or Linux

From this folder, compile and run:

```sh
mkdir -p out
javac -d out src/main/java/Main.java src/main/java/SudokuSolver.java src/main/java/SudokuImageReader.java
java -cp out main.java.Main
```

To read a screenshot, pass its path. Omit the path to choose an image in a
file picker:

```sh
java -cp out main.java.Main --image "/path/to/sudoku.png"
```

```sh
java -cp out main.java.Main --image
```

Install Tesseract OCR and make the `tesseract` command available on `PATH`.
On macOS with Homebrew:

```sh
brew install tesseract
tesseract --version
```

On Windows, install Tesseract and add its installation folder to `PATH`.
If it is installed in a custom location, set the `TESSERACT_PATH` environment
variable to its executable path. OCR runs locally; screenshots are not
uploaded. Crop the image tightly around the complete, straight-on 9-by-9 grid,
with the board edges filling the image. The recognized clues are displayed for
review before solving. Press Enter to accept them, or type a corrected
81-cell puzzle. Use `0` or `.` for empty cells.

For text input, enter exactly 81 cells, either as 9 rows or as one sequence.
Use `0` or `.` for empty cells. Spaces, commas, and common grid separators
(`|`, `+`, `-`) are ignored. Invalid or conflicting clues are reported;
a valid but unsolvable puzzle is reported separately.

The solver is also available as `SudokuSolver.solve(int[][])` in
`src/main/java/SudokuSolver.java`. It returns
`true` and fills the board when a solution is found; for an invalid or
unsolvable board it returns `false` and leaves the input board unchanged.