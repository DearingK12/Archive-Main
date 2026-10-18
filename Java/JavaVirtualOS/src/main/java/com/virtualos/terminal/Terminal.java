package com.virtualos.terminal;

import com.virtualos.filesystem.VirtualFileSystem;
import com.virtualos.security.User;
import com.virtualos.security.UserManager;

import java.util.List;
import java.util.Scanner;

public final class Terminal {
    private final Scanner scanner = new Scanner(System.in);
    private final VirtualFileSystem fileSystem = new VirtualFileSystem();
    private final UserManager users = new UserManager();
    private String currentDirectory = "/";
    private boolean running = true;

    public void run() {
        while (running) {
            System.out.print(users.currentUser().username() + "@jvos:" + currentDirectory + "$ ");
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                execute(line);
            }
        }
        System.out.println("Shutting down Java Virtual OS.");
    }

    private void execute(String line) {
        String[] parts = line.split("\\\\s+", 3);
        String command = parts[0].toLowerCase();
        String arg1 = parts.length > 1 ? parts[1] : "";
        String arg2 = parts.length > 2 ? parts[2] : "";

        switch (command) {
            case "help" -> help();
            case "exit", "shutdown" -> running = false;
            case "clear" -> {
                for (int i = 0; i < 35; i++) System.out.println();
            }
            case "whoami" -> System.out.println(users.currentUser().username());
            case "users" -> users.listUsers().forEach(System.out::println);
            case "login" -> {
                if (arg1.isBlank()) {
                    System.out.println("Usage: login <username>");
                } else if (users.login(arg1)) {
                    System.out.println("Logged in as " + arg1 + ".");
                } else {
                    System.out.println("Unknown user: " + arg1);
                }
            }
            case "pwd" -> System.out.println(currentDirectory);
            case "ls" -> fileSystem.list(currentDirectory).forEach(System.out::println);
            case "cd" -> changeDirectory(arg1);
            case "mkdir" -> {
                if (arg1.isBlank()) System.out.println("Usage: mkdir <directory>");
                else report(fileSystem.makeDirectory(resolve(arg1)));
            }
            case "touch" -> {
                if (arg1.isBlank()) System.out.println("Usage: touch <file>");
                else report(fileSystem.writeFile(resolve(arg1), ""));
            }
            case "write" -> {
                if (arg1.isBlank() || arg2.isBlank()) {
                    System.out.println("Usage: write <file> <text>");
                } else {
                    report(fileSystem.writeFile(resolve(arg1), arg2));
                }
            }
            case "cat" -> {
                if (arg1.isBlank()) System.out.println("Usage: cat <file>");
                else System.out.println(fileSystem.readFile(resolve(arg1)));
            }
            case "rm" -> {
                if (arg1.isBlank()) System.out.println("Usage: rm <file-or-empty-directory>");
                else report(fileSystem.remove(resolve(arg1)));
            }
            case "tree" -> fileSystem.printTree("/");
            case "date" -> System.out.println(java.time.LocalDateTime.now());
            case "about" -> System.out.println("J-VOS is a simulated OS written entirely in Java's standard library.");
            default -> System.out.println("Unknown command: " + command + ". Type 'help'.");
        }
    }

    private String resolve(String path) {
        if (path.startsWith("/")) return normalize(path);
        return normalize(currentDirectory.equals("/") ? "/" + path : currentDirectory + "/" + path);
    }

    private String normalize(String path) {
        java.util.Deque<String> parts = new java.util.ArrayDeque<>();
        for (String part : path.split("/")) {
            if (part.isBlank() || part.equals(".")) continue;
            if (part.equals("..")) {
                if (!parts.isEmpty()) parts.removeLast();
            } else {
                parts.addLast(part);
            }
        }
        return "/" + String.join("/", parts);
    }

    private void changeDirectory(String path) {
        if (path.isBlank()) {
            currentDirectory = "/";
        } else {
            String target = resolve(path);
            if (fileSystem.isDirectory(target)) currentDirectory = target;
            else System.out.println("Directory not found: " + target);
        }
    }

    private void report(String result) {
        System.out.println(result);
    }

    private void help() {
        System.out.println("""
                Available commands:
                  help                    Show this help
                  ls                      List files and directories
                  pwd                     Print current directory
                  cd <path>               Change directory
                  mkdir <name>            Create a directory
                  touch <file>            Create an empty file
                  write <file> <text>      Create or replace a file's contents
                  cat <file>              Display a file
                  rm <path>               Remove a file or empty directory
                  tree                    Show the virtual file tree
                  whoami                  Show current user
                  users                   List available demo users
                  login <username>        Switch demo user
                  date                    Show current date and time
                  clear                   Clear terminal (prints blank lines)
                  about                   About this project
                  exit                    Shut down
                Demo users: admin, guest
                """);
    }
}
