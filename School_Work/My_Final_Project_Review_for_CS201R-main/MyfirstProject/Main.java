/*
Name: Koda R Dearing
Date: 12-15-25
Description: Main class to create Student and Grades objects
*/
public class Main {
    public static void main(String[] args) {
    // Create students
    java.util.Scanner sc = new java.util.Scanner(System.in);
    System.out.println("Create a student by typing their details:");
    Student s1 = Student.fromScanner(sc);
        s1.printStudent();

        System.out.println();
        System.out.println("Would you like to run the grade calculator and save a summary for this student? (y/n)");
        String runGrades = sc.nextLine().trim().toLowerCase();
        if (runGrades.startsWith("y")) {
            Grades.fromScannerAndPrint(sc, s1.name);
        }
    }
}

