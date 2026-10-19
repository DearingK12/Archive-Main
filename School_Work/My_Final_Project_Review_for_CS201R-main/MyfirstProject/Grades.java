/*
Name: Koda R Dearing
Date: 12-15-25
Description: Main class to create Student and Grades objects
*/

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Simple grades calculator that allows interactive entry of components.
 * Each component has a name, weight (percentage of final grade),
 * points earned and points possible. Final percentage is sum(weight * componentPercentage).
 */
public class Grades {
    // Default pass threshold constant (use 70% per request)
    public static final double DEFAULT_PASS_THRESHOLD = 70.0;

    // File paths
    private static final String TEXT_OUT_FILE = "grades_summaries.txt";
    private static final String JSON_OUT_FILE = "grades_summaries.jsonl"; // JSON lines, one object per line

    static class Component {
        String name;
        double weight; // 0-100 (percent)
        double earned;
        double possible;

        Component(String name, double weight, double earned, double possible) {
            this.name = name;
            this.weight = weight;
            this.earned = earned;
            this.possible = possible;
        }

        double percent() {
            if (possible <= 0) return 0.0;
            return earned / possible * 100.0;
        }
    }

    private final List<Component> components = new ArrayList<>();

    public void addComponent(String name, double weightPercent, double earned, double possible) {
        components.add(new Component(name, weightPercent, earned, possible));
    }

    /**
     * Calculates the weighted final percentage. If total weight does not equal 100,
     * components are scaled proportionally so the returned value still represents
     * the weighted contribution relative to the provided weights.
     */
    public double calculateFinalPercentage() {
        double totalWeight = 0.0;
        for (Component c : components) totalWeight += c.weight;
        if (components.isEmpty()) return 0.0;

        double scale = (totalWeight == 0) ? 0.0 : (100.0 / totalWeight);
        double finalPercent = 0.0;
        for (Component c : components) {
            double compPercent = c.percent();
            double effectiveWeight = c.weight * scale; // scaled to sum to 100
            finalPercent += (compPercent * effectiveWeight / 100.0);
        }
        return finalPercent;
    }

    public boolean isPassing(double thresholdPercent) {
        return calculateFinalPercentage() >= thresholdPercent;
    }

    public void printBreakdown() {
        System.out.println("Grade breakdown:");
        for (Component c : components) {
            System.out.printf("- %s: %.2f/%.2f => %.2f%% (weight %.2f%%)\n",
                    c.name, c.earned, c.possible, c.percent(), c.weight);
        }
        System.out.printf("Final weighted percentage: %.2f%%\n", calculateFinalPercentage());
    }

    /**
     * Interactive factory: prompts user to enter components.
     * Example flow:
     * - How many components? 3
     * - For each: name, weight, earned, possible
     * - Optional threshold for passing
     */
    public static void fromConsoleAndPrint() {
        Scanner sc = new Scanner(System.in);
        fromScannerAndPrint(sc, null);
    }

    /**
     * Same as fromScannerAndPrint(Scanner) but associates saved summaries with a student name.
     */
    public static void fromScannerAndPrint(Scanner sc, String studentName) {
    // Allow multiple grade summaries per run, and save each summary to a file
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

        while (true) {
            Grades g = new Grades();
            int n = readInt(sc, "How many grade components (assignments/exams)? ", 0, false);

            for (int i = 1; i <= n; i++) {
                System.out.printf("Component %d name: ", i);
                String name = sc.nextLine().trim();
                if (name.isEmpty()) name = "Component " + i;
                double weight = readDouble(sc, String.format("%s weight (percent of final grade, e.g. 20): ", name), 0.0, false);
                double earned = readDouble(sc, String.format("%s points earned: ", name), 0.0, false);
                double possible = readDouble(sc, String.format("%s points possible: ", name), 0.0, false);

                g.addComponent(name, weight, earned, possible);
            }

            double threshold = readDouble(sc, String.format("Enter passing threshold percent (default %.0f): ", DEFAULT_PASS_THRESHOLD), DEFAULT_PASS_THRESHOLD, true);

            System.out.println();
            g.printBreakdown();
            System.out.printf("Passing threshold: %.2f%%\n", threshold);
            boolean pass = g.isPassing(threshold);
            System.out.println(pass ? "Result: PASS" : "Result: FAIL");

            // Build a text summary
            StringBuilder sb = new StringBuilder();
            sb.append("--- Grade Summary ---\n");
            sb.append("Timestamp: ").append(LocalDateTime.now().format(dtf)).append("\n");
            if (studentName != null && !studentName.trim().isEmpty()) {
                sb.append("Student: ").append(studentName.trim()).append("\n");
            }
            sb.append(String.format("Final weighted percentage: %.2f%%\n", g.calculateFinalPercentage()));
            sb.append(String.format("Passing threshold: %.2f%% -> %s\n", threshold, pass ? "PASS" : "FAIL"));
            sb.append("Breakdown:\n");
            for (Component c : g.components) {
                sb.append(String.format("- %s: %.2f/%.2f => %.2f%% (weight %.2f%%)\n",
                        c.name, c.earned, c.possible, c.percent(), c.weight));
            }
            sb.append("\n");

            System.out.print("Save this summary to '" + TEXT_OUT_FILE + "'? (y/n): ");
            String save = sc.nextLine().trim().toLowerCase();
            if (save.startsWith("y")) {
                // If studentName is empty, ask for one (allow skip)
                String nameToUse = studentName;
                if (nameToUse == null || nameToUse.trim().isEmpty()) {
                    System.out.print("Enter a student name to associate with this summary (leave blank to skip): ");
                    String entered = sc.nextLine().trim();
                    if (!entered.isEmpty()) nameToUse = entered;
                }
                // prepend Student line if available
                if (nameToUse != null && !nameToUse.trim().isEmpty()) {
                    sb.insert(sb.indexOf("Final weighted percentage"), "Student: " + nameToUse.trim() + "\n");
                }
                try {
                    Files.write(Paths.get(TEXT_OUT_FILE), sb.toString().getBytes(StandardCharsets.UTF_8),
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    System.out.println("Saved summary to " + TEXT_OUT_FILE);
                } catch (IOException ioe) {
                    System.out.println("Failed to save summary: " + ioe.getMessage());
                }

                // Also offer JSON-lines export for programmatic use
                System.out.print("Also save a JSON version to '" + JSON_OUT_FILE + "'? (y/n): ");
                String saveJson = sc.nextLine().trim().toLowerCase();
                if (saveJson.startsWith("y")) {
                    String json = buildJsonLine(nameToUse, g, threshold, pass);
                    try {
                        Files.write(Paths.get(JSON_OUT_FILE), (json + "\n").getBytes(StandardCharsets.UTF_8),
                                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        System.out.println("Saved JSON summary to " + JSON_OUT_FILE);
                    } catch (IOException ioe) {
                        System.out.println("Failed to save JSON summary: " + ioe.getMessage());
                    }
                }
            }

            System.out.print("Create another grade summary? (y/n): ");
            String again = sc.nextLine().trim().toLowerCase();
            if (!again.startsWith("y")) {
                break;
            }
            System.out.println();
        }
    }

    /**
     * Helper: read an integer from scanner with optional default when blank.
     */
    private static int readInt(Scanner sc, String prompt, int defaultValue, boolean allowBlank) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine();
            if (line == null) return defaultValue;
            line = line.trim();
            if (line.isEmpty()) {
                if (allowBlank) return defaultValue;
                System.out.println("Please enter a number.");
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer — please try again.");
            }
        }
    }

    /**
     * Helper: read a double from scanner with optional default when blank.
     */
    private static double readDouble(Scanner sc, String prompt, double defaultValue, boolean allowBlank) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine();
            if (line == null) return defaultValue;
            line = line.trim();
            if (line.isEmpty()) {
                if (allowBlank) return defaultValue;
                System.out.println("Please enter a number.");
                continue;
            }
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number — please try again.");
            }
        }
    }

    /**
     * Build a single-line JSON representation for programmatic consumption.
     */
    private static String buildJsonLine(String studentName, Grades g, double threshold, boolean pass) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"timestamp\":\"").append(escapeJson(LocalDateTime.now().toString())).append("\"");
        if (studentName != null && !studentName.trim().isEmpty()) {
            sb.append(",\"student\":\"").append(escapeJson(studentName.trim())).append("\"");
        }
        sb.append(String.format(",\"final\":%.2f", g.calculateFinalPercentage()));
        sb.append(String.format(",\"threshold\":%.2f", threshold));
        sb.append(String.format(",\"pass\":%s", pass ? "true" : "false"));
        sb.append(",\"components\":[");
        boolean first = true;
        for (Component c : g.components) {
            if (!first) sb.append(',');
            first = false;
            sb.append('{');
            sb.append("\"name\":\"").append(escapeJson(c.name)).append("\"");
            sb.append(String.format(",\"weight\":%.2f", c.weight));
            sb.append(String.format(",\"earned\":%.2f", c.earned));
            sb.append(String.format(",\"possible\":%.2f", c.possible));
            sb.append('}');
        }
        sb.append("]}");
        return sb.toString();
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    /**
     * Print saved text summaries if present.
     */
    public static void listSavedSummaries() {
        System.out.println("Saved grade summaries (text):");
        try {
            if (!Files.exists(Paths.get(TEXT_OUT_FILE))) {
                System.out.println("(no saved summaries)");
                return;
            }
            List<String> lines = Files.readAllLines(Paths.get(TEXT_OUT_FILE), StandardCharsets.UTF_8);
            for (String l : lines) System.out.println(l);
        } catch (IOException e) {
            System.out.println("Failed to read saved summaries: " + e.getMessage());
        }
    }
}
