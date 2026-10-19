package main.java;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;

public final class SudokuImageReader {
    private static final int GRID_SIZE = 9;
    private static final int OCR_CELL_SIZE = 150;
    private static final int MAX_SCALE = 8;

    private SudokuImageReader() {
    }

    public static int[][] read(Path imagePath) throws IOException {
        if (imagePath == null || !Files.isRegularFile(imagePath) || !Files.isReadable(imagePath)) {
            throw new IOException("Image file does not exist or cannot be read: " + imagePath);
        }

        BufferedImage image = ImageIO.read(imagePath.toFile());
        if (image == null) {
            throw new IOException("Unsupported image format. Use a PNG, JPEG, or BMP image.");
        }
        if (image.getWidth() < GRID_SIZE * 12 || image.getHeight() < GRID_SIZE * 12) {
            throw new IOException("The Sudoku image is too small. Use a higher-resolution image.");
        }

        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            int top = row * image.getHeight() / GRID_SIZE;
            int bottom = (row + 1) * image.getHeight() / GRID_SIZE;
            for (int column = 0; column < GRID_SIZE; column++) {
                int left = column * image.getWidth() / GRID_SIZE;
                int right = (column + 1) * image.getWidth() / GRID_SIZE;
                board[row][column] = recognizeCell(
                        image, left, top, right - left, bottom - top, row, column);
            }
        }
        return board;
    }

    private static int recognizeCell(
            BufferedImage image, int left, int top, int width, int height, int row, int column)
            throws IOException {
        int insetX = Math.max(1, width / 6);
        int insetY = Math.max(1, height / 6);
        int cropWidth = width - 2 * insetX;
        int cropHeight = height - 2 * insetY;
        if (cropWidth <= 0 || cropHeight <= 0) {
            throw new IOException("Image cells are too small to read.");
        }

        BufferedImage cell = new BufferedImage(cropWidth, cropHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = cell.createGraphics();
        try {
            graphics.drawImage(
                    image,
                    0, 0, cropWidth, cropHeight,
                    left + insetX, top + insetY,
                    left + insetX + cropWidth, top + insetY + cropHeight,
                    null);
        } finally {
            graphics.dispose();
        }

        int scale = Math.max(1, Math.min(MAX_SCALE,
                OCR_CELL_SIZE / Math.max(cropWidth, cropHeight)));
        BufferedImage enlarged = new BufferedImage(
                cropWidth * scale, cropHeight * scale, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D enlargedGraphics = enlarged.createGraphics();
        try {
            enlargedGraphics.drawImage(
                    cell, 0, 0, enlarged.getWidth(), enlarged.getHeight(), null);
        } finally {
            enlargedGraphics.dispose();
        }

        String text = runTesseract(enlarged, row, column).replaceAll("[^1-9]", "");
        if (text.isEmpty()) {
            return 0;
        }
        if (text.length() != 1) {
            throw new IOException(
                    "Could not read row " + (row + 1) + ", column " + (column + 1)
                            + " unambiguously. Use a clearer, tightly cropped image.");
        }
        return text.charAt(0) - '0';
    }

    private static String runTesseract(BufferedImage cell, int row, int column) throws IOException {
        Path errorLog = Files.createTempFile("sudoku-tesseract-", ".log");
        try {
            List<String> command = List.of(
                    findTesseract(),
                    "stdin",
                    "stdout",
                    "--psm", "10",
                    "-c", "tessedit_char_whitelist=123456789",
                    "-c", "classify_bln_numeric_mode=1");
            Process process;
            try {
                process = new ProcessBuilder(command)
                        .redirectError(errorLog.toFile())
                        .start();
            } catch (IOException exception) {
                throw new IOException(
                        "Tesseract was not found. Install Tesseract OCR and make sure "
                                + "the 'tesseract' command is available, or set TESSERACT_PATH.",
                        exception);
            }

            try (OutputStream input = process.getOutputStream();
                    ByteArrayOutputStream png = new ByteArrayOutputStream()) {
                ImageIO.write(cell, "png", png);
                input.write(png.toByteArray());
            }

            final boolean finished;
            try {
                finished = process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                process.destroyForcibly();
                Thread.currentThread().interrupt();
                throw new IOException("OCR was interrupted.", exception);
            }
            if (!finished) {
                process.destroyForcibly();
                throw new IOException(
                        "Tesseract timed out reading row " + (row + 1)
                                + ", column " + (column + 1) + ".");
            }

            String output = new String(process.getInputStream().readAllBytes(),
                    java.nio.charset.StandardCharsets.UTF_8);
            if (process.exitValue() != 0) {
                String details = Files.readString(errorLog).trim();
                throw new IOException("Tesseract failed reading the image"
                        + (details.isEmpty() ? "." : ": " + details));
            }
            return output;
        } finally {
            Files.deleteIfExists(errorLog);
        }
    }

    private static String findTesseract() {
        String configuredPath = System.getenv("TESSERACT_PATH");
        if (configuredPath != null && !configuredPath.isBlank()) {
            return configuredPath;
        }

        for (String candidate : List.of(
                "/opt/homebrew/bin/tesseract",
                "/usr/local/bin/tesseract",
                "/usr/bin/tesseract")) {
            if (Files.isExecutable(Paths.get(candidate))) {
                return candidate;
            }
        }
        return "tesseract";
    }
}
