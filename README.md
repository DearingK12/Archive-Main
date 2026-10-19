# Archive Main

This repository contains personal projects and school work. Each project is
documented separately below. Run commands from the project folder unless
otherwise noted; this avoids putting generated data or build files in the
repository root.

## Personal projects

### ASKODA

**Description:** A Node.js and Express AI workspace. It serves the browser app,
provides authenticated chat and workspace APIs, and supports local JSON storage
for development and PostgreSQL for production.

**How to use**

1. Install Node.js 18 or newer.
2. Open a terminal in `Personal_Projects/ASKODA/`.
3. Install dependencies and create a local environment file:

   ```sh
   npm ci
   cp .env.example .env
   ```

4. Edit `.env` for local development. Set `NODE_ENV=development`; do not use
   the example production placeholders as real credentials. Add a valid
   `OPENAI_API_KEY` to use AI chat. Google sign-in and email registration
   require their corresponding OAuth and SMTP settings.
5. Start the server:

   ```sh
   npm start
   ```

6. Open <http://localhost:3000>. `npm run dev` starts the optional nodemon
   development server.

Local development stores data in the project’s `data/` folder (or the
directory specified by `ASKODA_DATA_DIR`). Production requires real secrets
and `DATABASE_URL`; follow [ASKODA's security and deployment notes](Personal_Projects/ASKODA/SECURITY.md)
before deploying. Never commit `.env` or put secrets in browser code.

**If something goes wrong**

- If the server cannot bind its port, set a free `PORT` in `.env`.
- If `npm ci` fails, check that Node.js is version 18 or newer and rerun it
  from this project folder.
- Visit <http://localhost:3000/api/health> to check whether the server is up.
- Chat needs a working `OPENAI_API_KEY`; email registration needs working
  SMTP credentials; Google sign-in needs a Google OAuth client ID.
- Production startup intentionally stops when required credentials or
  `DATABASE_URL` are absent or still placeholders. Do not fix this by
  committing secrets.

### Joda's Adventure

**Description:** A 2D action-platformer made with GDScript and Godot 4. The
project is provided as a ZIP archive rather than an extracted Godot project.

**How to use**

1. Install Godot 4.x.
2. Extract the ZIP in
   `Personal_Projects/GDScript/Jodas-Adventures-main/`.
3. In Godot Project Manager, import the extracted folder that contains
   `project.godot`.
4. Open the project and run it from the editor.
5. Use the arrow keys or **A/D** to move, **Space** to jump, **F** to attack,
   and **Esc** to pause or open the menu.

**If something goes wrong**

- If Godot cannot find a project, open the extracted inner folder containing
  `project.godot`, not the folder containing only the ZIP.
- Use Godot 4.x; a different major version may report scene or script
  compatibility errors.
- If imported assets or scenes fail to load, let Godot finish importing the
  project, then reopen it. Check the Godot Output panel for the first
  missing-resource error.

### D&D Ultimate Dice Roller

**Description:** A browser-based dice roller and D&D character sheet with
normal/advantage/disadvantage rolls, editable saved characters, portrait
uploads, and character import from text, CSV, PDF, or images.

**How to use**

1. Open `Personal_Projects/HTML/dnd-dice-main/index.html` in a modern browser.
   If the browser blocks local-file features, start a local server from that
   folder with `python3 -m http.server 8000`, then open
   <http://localhost:8000>.
2. Choose dice and a roll mode, then select **Roll**.
3. Fill out the character sheet, upload an optional portrait, and use **Save**,
   **Load**, **Delete**, and **Edit** to manage characters.
4. Choose a supported file and select **Import Character** to try importing
   character details.

**If something goes wrong**

- PDF and image import use browser libraries loaded from CDNs; check your
  internet connection and allow those scripts to load.
- If import reads a field incorrectly, edit the character sheet manually.
  OCR depends on image quality and file layout.
- Saved characters are stored in browser local storage. They do not sync
  between browsers/devices; use the same browser and site address to see them.
- If locally opened files behave inconsistently, use the local HTTP server
  method above.

### Math Toolbox (Java)

**Description:** A Java console program for symbolic derivatives, integrals,
simplification, and equation solving with Symja. The repository includes
`lib/matheclipse-core-3.0.0.jar` and Java sources. Some linear algebra methods
are placeholders, and `Utils/OCRHelper.java` does not implement OCR.

**How to use**

1. Install a JDK.
2. Open a terminal in `Personal_Projects/Java/MathToolbox.java-main/`.
3. Compile and run on macOS/Linux:

   ```sh
   mkdir -p out
   javac -cp "lib/*" -d out src/Main.java src/algebra/Algebra.java src/calculus/Calculus.java
   java -cp "out:lib/*" Main
   ```

   On Windows, use `;` instead of `:` in the runtime classpath:

   ```bat
   java -cp "out;lib/*" Main
   ```

4. Choose an operation from the menu and enter an expression using `x`.
   Examples: `sin(x)` for a derivative or `x^2=4` to solve an equation.
   Choose `0` to exit.

**If something goes wrong**

- Check `java -version` and `javac -version`; install a full JDK if `javac` is
  missing.
- If compilation reports missing `org.matheclipse` classes, ensure the
  supplied JAR is present and the classpath includes `lib/*`.
- If runtime reports `NoClassDefFoundError`, the Symja core JAR may require
  additional dependencies not included in `lib/`. The project also contains
  source and ZIP archives; this is a dependency setup issue rather than an
  expression error.
- Use expressions supported by Symja. An operation error is printed in the
  console; try a simpler expression.

### Sudoku Solver

**Description:** A Java Sudoku solver with clue validation and
fewest-candidates-first backtracking. It can read puzzle text or use local
Tesseract OCR to recognize a screenshot. Screenshot OCR is optional.

**How to use**

- In VS Code, run **Sudoku Solver** for text entry or
  **Sudoku Solver (choose screenshot)** to open an image picker.
- From the project folder on macOS/Linux:

  ```sh
  mkdir -p out
  javac -d out src/main/java/*.java
  java -cp out main.java.Main
  ```

- On Windows, run `run.bat`. You can also pass an image path:

  ```text
  run.bat --image "C:\path\to\sudoku.png"
  ```

- For text input, enter exactly 81 cells as nine rows or one sequence. Use
  `0` or `.` for blanks.
- For OCR, install Tesseract and ensure `tesseract` is on `PATH`; on macOS use
  `brew install tesseract`. Run
  `java -cp out main.java.Main --image "/path/to/sudoku.png"` or choose the
  screenshot launch option in VS Code.
  Review the recognized clues and press Enter to solve, or enter a corrected
  81-cell puzzle.

OCR runs locally. A clear, straight-on image cropped to the full 9-by-9 board
works best.

**If something goes wrong**

- For `Tesseract was not found`, install Tesseract or set `TESSERACT_PATH` to
  its executable.
- For unreadable cells or conflicting clues, use a clearer/tighter crop and
  check the recognized grid; enter a corrected puzzle when prompted.
- If VS Code reports an obsolete package or class, refresh the Java project
  and run the current `main.java.Main` launch configuration.
- A valid puzzle with no solution is reported separately from invalid clues.

See the [Sudoku-specific README](Personal_Projects/Java/Sodoku_Solver/README.md)
for additional details.

### Python Calculator

**Description:** A small interactive calculator for addition,
multiplication, subtraction, and division.

**How to use**

```sh
cd Personal_Projects/Python
python3 calculator.py
```

Choose an operation, enter two numbers, and select option `5` to exit.

**If something goes wrong**

- Use a supported numeric value for both inputs; invalid numbers are rejected
  and the program asks again.
- Division by zero displays an error instead of a result.
- If `python3` is not found, install Python 3 or use the Python command for
  your system.

### GPA Calculator

**Description:** A menu-driven student and GPA tracker. It stores students
and terms in JSON, calculates semester/year/cumulative GPA, creates text
reports, and optionally exports course data to CSV. Grades are A-F; course
weights are regular 1.0, honors 1.1, AP 1.2, capped at 4.0.

**How to use**

1. Open a terminal in `Personal_Projects/Python/GPA_Calculator-main/`.
2. Run `python3 main.py`.
3. Use the menu to add a student with a unique ID, add terms, and enter each
   course's name, grade, positive credit value, and course type. Enter `done`
   to finish entering courses or terms.
4. View students, then choose **Calculate GPA** to select a semester, year, or
   cumulative GPA. Reports are written under `new_reports/`; older reports
   are archived under `past_reports/`. Choose CSV export when prompted.

Student data is saved in `data/students.json`. Run the program from this
project folder so its data and generated reports stay with the project.

**If something goes wrong**

- Enter grades as A, B, C, D, or F; credits must be numeric and greater than
  zero; course type must be `regular`, `honors`, or `ap`.
- A semester must match the term entered for that student. Year calculations
  require a year represented in their term names.
- If an unexpected error occurs, read the dated log under `errors/`.
- If stored JSON is damaged, check for a generated backup under `data/backup/`
  before changing or replacing student data.

### Yahtzee

**Description:** A beginner single-player Yahtzee project with a console
interface, five dice, up to three rolls per turn, and the standard 13 scoring
categories.

**How to use**

1. Open a terminal in `Personal_Projects/Python/Yahtzee-game-main/`.
2. Run `python3 main.py`.
3. At each roll, enter the positions `1`-`5` of dice to keep, separated by
   spaces; press Enter to reroll all dice. Choose an available category using
   its displayed name and confirm the score. Complete all 13 categories to
   finish the game.

**If something goes wrong**

- Enter only positions from `1` through `5` when keeping dice and use a
  category exactly as displayed (for example, `three_of_a_kind`).
- `main.py` starts the console game. `yahtzee_gui.py` is only a placeholder
  window, not a complete GUI.
- High scores and settings are currently in-memory/placeholders and are not
  persisted between runs.

### Password Security Checker

**Description:** A local Python utility that scores a password from 0 to 100,
labels it Weak/Moderate/Strong, and explains weak patterns such as common
passwords, short length, repetition, or missing character types.

**How to use**

Run interactively:

```sh
cd Personal_Projects/Python/password_security_checker-main
python3 password_checker.py
```

Or pass a sample password as an argument:

```sh
python3 password_checker.py "Example-Only-123!"
```

The result is printed as JSON. Do not put a real password in a command line:
shell history and process listings may expose it. Use the interactive prompt
for private local checks.

**If something goes wrong**

- If a password is not provided as text, the program reports that it must be
  a string. An empty password is always Weak.
- Run the included tests with `python3 -m unittest` from this folder.
- This is a heuristic checker, not a password manager or guarantee of account
  security; use a unique password and a reputable password manager.

### Python Task Manager

**Description:** An interactive to-do list for adding, viewing, completing,
and deleting tasks. Tasks are stored in `tasks.json`.

**How to use**

```sh
cd Personal_Projects/Python/python_task_manager
python3 main.py
```

Choose a menu number, enter a non-empty task name, and use the task numbers
shown in the list to complete or delete tasks. Choose `5` to exit.

**If something goes wrong**

- Use the displayed task numbers, starting at 1; invalid choices are rejected.
- Run from this project folder so the program reads and writes its
  `tasks.json` here.
- If the JSON file is malformed or unreadable, the program reports that it
  could not load tasks and starts with an empty list. Make a copy of the file
  before repairing it, since saving afterward replaces its contents.

### ZipZap

**Description:** A Python utility that compresses a selected folder and its
contents into a ZIP archive. It supports a graphical folder/save dialog or
command-line paths.

**How to use**

```sh
cd Personal_Projects/Python/zipzap-main
python3 zipzap.py
```

Choose a folder and a destination ZIP file in the dialogs. For command-line
use, provide both paths:

```sh
python3 zipzap.py ./MyProject ./MyProject.zip
```

Choose an output archive outside the folder being zipped.

**If something goes wrong**

- The first path must be an existing folder, not a file.
- When using dialogs, run in a desktop session. If Tkinter is missing, install
  the Python Tk package provided by your operating system.
- If a dialog is cancelled, the program exits without creating an archive.
- Check that the destination directory exists and that you have permission to
  write there.

## School work

### CS201R Student and Grade Calculator

**Description:** A Java console assignment that creates and displays a student
record, then optionally calculates a weighted course grade. It can save text
and JSON-lines grade summaries.

**How to use**

1. Install a JDK.
2. Open a terminal in
   `School_Work/My_Final_Project_Review_for_CS201R-main/MyfirstProject/`.
3. Compile and run:

   ```sh
   javac *.java
   java Main
   ```

4. Enter the student information. At the prompt, answer `y` to run the grade
   calculator. Enter each component's name, weight, earned points, and
   possible points, then the pass threshold (default 70%). The components'
   weights are normalized to total 100% if they do not add up to 100%.
5. Choose whether to save the summary. Output files are written in the current
   project directory.

**If something goes wrong**

- Check that a JDK is installed and that you ran `javac *.java` from the
  directory containing all four `.java` files.
- Enter numeric values for IDs, weights, scores, and thresholds when
  prompted. The grade calculator repeats prompts for invalid numeric input.
- `possible` points should be greater than zero for a meaningful component
  percentage.
- If summary files cannot be written, check write permissions for the project
  folder and make sure the files are not read-only.

## General troubleshooting

- Run each project from its own folder unless its section explicitly says
  otherwise.
- Check the project-specific README for details and check that the required
  language runtime (Node.js, Python, Java, or Godot) is installed.
- Do not commit local `.env` files, API keys, passwords, or other secrets.
