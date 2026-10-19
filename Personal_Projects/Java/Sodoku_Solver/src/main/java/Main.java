package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFileChooser;

public class Main {
    private Main() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("--image")) {
            try {
                solveImage(args);
            } catch (IOException | IllegalArgumentException exception) {
                System.err.println("Could not solve Sudoku image: " + exception.getMessage());
                System.exit(1);
            }
            return;
        }

        String input;
        if (args.length > 0) {
            input = String.join(" ", args);
        } else {
            System.out.println("Enter 81 cells as 9 rows or one sequence; use 0 or . for empty cells:");
            input = readStandardInput();
        }

        final int[][] board;
        try {
            board = parsePuzzle(input);
        } catch (IllegalArgumentException exception) {
            System.err.println("Invalid puzzle: " + exception.getMessage());
            System.exit(1);
            return;
        }

        if (!SudokuSolver.isValidBoard(board)) {
            System.err.println("Invalid puzzle: a clue conflicts with another clue.");
            System.exit(1);
            return;
        }

        if (!SudokuSolver.solve(board)) {
            System.out.println("This puzzle has no solution.");
            System.exit(2);
            return;
        }

        printBoard(board);
    }

    private static void solveImage(String[] args) throws IOException {
        Path imagePath = args.length > 1
                ? Path.of(String.join(" ", Arrays.copyOfRange(args, 1, args.length)))
                : chooseImage();
        if (imagePath == null) {
            System.out.println("Image selection cancelled.");
            return;
        }

        System.out.println("Reading Sudoku image locally with Tesseract...");
        int[][] board = SudokuImageReader.read(imagePath);
        if (!SudokuSolver.isValidBoard(board)) {
            throw new IllegalArgumentException(
                    "OCR found conflicting clues. Use a clearer image or enter the puzzle manually.");
        }

        System.out.println("OCR recognized these clues:");
        printBoard(board);
        System.out.println("Press Enter to solve, or type a corrected 81-cell puzzle:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String correction = reader.readLine();
        if (correction != null && !correction.isBlank()) {
            board = parsePuzzle(correction);
        }

        if (!SudokuSolver.isValidBoard(board)) {
            throw new IllegalArgumentException("The puzzle contains conflicting clues.");
        }
        if (!SudokuSolver.solve(board)) {
            System.out.println("This puzzle has no solution.");
            System.exit(2);
            return;
        }

        System.out.println("Solution:");
        printBoard(board);
    }

    private static Path chooseImage() {
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            throw new IllegalArgumentException(
                    "Provide an image path: Main --image <image-file>");
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a Sudoku image");
        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return chooser.getSelectedFile().toPath();
    }

    static int[][] parsePuzzle(String input) {
        if (input == null) {
            throw new IllegalArgumentException("no puzzle was provided.");
        }

        List<Integer> cells = new ArrayList<>(81);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (character >= '1' && character <= '9') {
                cells.add(character - '0');
            } else if (character == '0' || character == '.') {
                cells.add(0);
            } else if (!isSeparator(character)) {
                throw new IllegalArgumentException(
                        "use only digits 1-9, 0 or ., and spaces or grid separators.");
            }
        }

        if (cells.size() != 81) {
            throw new IllegalArgumentException(
                    "expected exactly 81 cells but received " + cells.size() + ".");
        }

        int[][] board = new int[9][9];
        for (int i = 0; i < cells.size(); i++) {
            board[i / 9][i % 9] = cells.get(i);
        }
        return board;
    }

    private static boolean isSeparator(char character) {
        return Character.isWhitespace(character)
                || character == ','
                || character == '|'
                || character == '+'
                || character == '-';
    }

    private static String readStandardInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder input = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            input.append(line).append('\n');
            if (countCells(input) >= 81) {
                break;
            }
        }
        return input.toString();
    }

    private static int countCells(CharSequence input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if ((character >= '0' && character <= '9') || character == '.') {
                count++;
            }
        }
        return count;
    }

    private static void printBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            if (row > 0 && row % 3 == 0) {
                System.out.println("------+-------+------");
            }
            for (int column = 0; column < 9; column++) {
                if (column > 0 && column % 3 == 0) {
                    System.out.print("| ");
                }
                System.out.print(board[row][column]);
                if (column < 8) {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
    }
}