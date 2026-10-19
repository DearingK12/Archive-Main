/*
Name: Koda R Dearing
Date: 12-15-25
Description: Main class to create Student and Grades objects
*/

public class Student {
    String name;
    int id;
    String major;
    String university;

    Student() {
        this.name = "Koda Dearing";
        this.id = 12345678;
        this.major = "Computer Science";
        this.university = "University of UMKC";
        System.out.println();
        System.out.println("Student created: " + name + " (" + id + ") - " + major + ", " + university);
    }

    Student(String name, int id, String major, String university) {
        this.name = name;
        this.id = id;
        this.major = major;
        this.university = university;
        System.out.println();
        System.out.println("Student created: " + name + " (" + id + ") - " + major + ", " + university);
    }

    void printStudent() {
        System.out.println(name + " is studying " + major + " (ID: " + id + ") at " + university + ".");
    }

    // Allow changing name after creation
    void setName(String name) {
        this.name = name;
    }

    // Create a Student by prompting the user on the console
    // Usage: Student s = Student.fromConsole();
    public static Student fromConsole() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        Student s = fromScanner(scanner);
        // Do NOT close the scanner because it would close System.in for the whole program
        return s;
    }

    /**
     * Create a Student using an existing Scanner instance. This avoids creating
     * multiple Scanners on System.in which can interfere with piped input.
     */
    public static Student fromScanner(java.util.Scanner scanner) {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter student id (number): ");
        int id = 0;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid id entered — defaulting to 0.");
        }
        System.out.print("Enter major: ");
        String major = scanner.nextLine().trim();
        System.out.print("Enter university: ");
        String university = scanner.nextLine().trim();

        Student s = new Student(name.isEmpty() ? "Unnamed" : name,
                                id,
                                major.isEmpty() ? "Undeclared" : major,
                                university.isEmpty() ? "Unknown" : university);
        return s;
    }
}
