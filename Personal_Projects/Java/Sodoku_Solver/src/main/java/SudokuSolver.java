package main.java;

import java.util.Arrays;

public final class SudokuSolver {
    private static final int SIZE = 9;
    private static final int ALL_DIGITS = (1 << SIZE) - 1;

    private SudokuSolver() {
    }

    public static boolean isValidBoard(int[][] board) {
        if (board == null || board.length != SIZE) {
            return false;
        }

        int[] rows = new int[SIZE];
        int[] columns = new int[SIZE];
        int[] boxes = new int[SIZE];

        for (int row = 0; row < SIZE; row++) {
            if (board[row] == null || board[row].length != SIZE) {
                return false;
            }

            for (int column = 0; column < SIZE; column++) {
                int digit = board[row][column];
                if (digit < 0 || digit > SIZE) {
                    return false;
                }
                if (digit == 0) {
                    continue;
                }

                int bit = 1 << (digit - 1);
                int box = boxIndex(row, column);
                if ((rows[row] & bit) != 0
                        || (columns[column] & bit) != 0
                        || (boxes[box] & bit) != 0) {
                    return false;
                }

                rows[row] |= bit;
                columns[column] |= bit;
                boxes[box] |= bit;
            }
        }

        return true;
    }

    public static boolean solve(int[][] board) {
        if (!isValidBoard(board)) {
            return false;
        }

        int[][] solution = new int[SIZE][SIZE];
        int[] rows = new int[SIZE];
        int[] columns = new int[SIZE];
        int[] boxes = new int[SIZE];

        for (int row = 0; row < SIZE; row++) {
            solution[row] = Arrays.copyOf(board[row], SIZE);
            for (int column = 0; column < SIZE; column++) {
                int digit = solution[row][column];
                if (digit != 0) {
                    int bit = 1 << (digit - 1);
                    rows[row] |= bit;
                    columns[column] |= bit;
                    boxes[boxIndex(row, column)] |= bit;
                }
            }
        }

        if (!solveRemaining(solution, rows, columns, boxes)) {
            return false;
        }

        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(solution[row], 0, board[row], 0, SIZE);
        }
        return true;
    }

    private static boolean solveRemaining(
            int[][] board, int[] rows, int[] columns, int[] boxes) {
        int bestRow = -1;
        int bestColumn = -1;
        int bestCandidates = 0;
        int fewestCandidates = SIZE + 1;

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (board[row][column] != 0) {
                    continue;
                }

                int candidates = ALL_DIGITS
                        & ~(rows[row] | columns[column] | boxes[boxIndex(row, column)]);
                int candidateCount = Integer.bitCount(candidates);
                if (candidateCount == 0) {
                    return false;
                }
                if (candidateCount < fewestCandidates) {
                    bestRow = row;
                    bestColumn = column;
                    bestCandidates = candidates;
                    fewestCandidates = candidateCount;
                }
            }
        }

        if (bestRow == -1) {
            return true;
        }

        int box = boxIndex(bestRow, bestColumn);
        while (bestCandidates != 0) {
            int bit = bestCandidates & -bestCandidates;
            bestCandidates &= bestCandidates - 1;
            int digit = Integer.numberOfTrailingZeros(bit) + 1;

            board[bestRow][bestColumn] = digit;
            rows[bestRow] |= bit;
            columns[bestColumn] |= bit;
            boxes[box] |= bit;

            if (solveRemaining(board, rows, columns, boxes)) {
                return true;
            }

            board[bestRow][bestColumn] = 0;
            rows[bestRow] &= ~bit;
            columns[bestColumn] &= ~bit;
            boxes[box] &= ~bit;
        }

        return false;
    }

    private static int boxIndex(int row, int column) {
        return (row / 3) * 3 + column / 3;
    }
}