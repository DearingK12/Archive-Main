@echo off
cd /d "%~dp0"
if not exist out mkdir out
dir /s /b src\main\java\*.java > sources.txt
javac -d out @sources.txt
if errorlevel 1 (
  echo Compilation failed. Make sure Java 17 or newer is installed.
  pause
  exit /b 1
)
java -cp out com.virtualos.Main
pause
