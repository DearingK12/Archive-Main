/*
Name: Koda R Dearing
Date: 12-15-25
Description: Main class to create Student and Grades objects
*/

import java.util.Scanner;

public class Age {
    int year;
    int age;

    // interactive constructor: reads year and age from System.in
    public Age() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the current year: ");
        try {
            this.year = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid year entered — defaulting to 2025.");
            this.year = 2025;
        }
        System.out.print("Enter your age: ");
        try {
            this.age = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid age entered — defaulting to 20.");
            this.age = 20;
        }
        // do not close scanner (closing would close System.in)
    }

    public void printAge() {
        System.out.println("The year is " + year + " and I am " + age + " years old.");
        System.out.println("I will be " + (age + 1) + " years old in " + (year + 1) + ".");
    }
}
