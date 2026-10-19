@echo off
setlocal
cd /d "%~dp0"

if not exist out mkdir out
javac -d out src\main\java\Main.java src\main\java\SudokuSolver.java src\main\java\SudokuImageReader.java
if errorlevel 1 exit /b 1

java -cp out main.java.Main %*
exit /b %errorlevel%