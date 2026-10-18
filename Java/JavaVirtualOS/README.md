# Java Virtual OS (J-VOS)

A command-line operating system simulation written entirely in Java using the Java standard library. It is a learning project, not a real bootable operating system.

## Requirements
- Java 17 or newer

## Run

### Windows
Double-click `run.bat`, or open a terminal in this folder and run:
```bat
run.bat
```

### macOS / Linux
Open a terminal in this folder and run:
```bash
chmod +x run.sh
./run.sh
```

The scripts compile the source files and start the program. You can also compile manually:

```bash
javac -d out $(find src/main/java -name "*.java")
java -cp out com.virtualos.Main
```

On Windows PowerShell, compile with:
```powershell
$files = Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $files
java -cp out com.virtualos.Main
```

## Commands
- `help` - list commands
- `ls` - list files and folders
- `pwd` - print current directory
- `cd <path>` - change directory
- `mkdir <name>` - create a directory
- `touch <file>` - create an empty file
- `write <file> <text>` - create or replace file contents
- `cat <file>` - print a file
- `rm <path>` - remove a file or empty directory
- `tree` - display the virtual file tree
- `whoami` - show current demo user
- `users` - list demo users
- `login <username>` - switch to `admin` or `guest`
- `date` - show date and time
- `clear` - print blank lines
- `about` - project description
- `exit` - quit

## Example session
```text
admin@jvos:/$ ls
home/
system/
tmp/
admin@jvos:/$ cd /home/admin
admin@jvos:/home/admin$ write notes.txt Hello from Java
Saved /home/admin/notes.txt
admin@jvos:/home/admin$ cat notes.txt
Hello from Java
```

## Notes
- Files live in an in-memory virtual file system and reset when the program exits.
- The `login` command is a demo user switch, not secure authentication.
- This project uses no third-party dependencies.
