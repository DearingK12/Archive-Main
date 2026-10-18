import java.util.Scanner;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import calculus.Calculus;
import algebra.Algebra;

public class Main {
    private static ExprEvaluator util = new ExprEvaluator();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Math Toolbox ====");
            System.out.println("1. Derivative");
            System.out.println("2. Integral");
            System.out.println("3. Simplify Expression");
            System.out.println("4. Solve Equation");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice, try again.");
                continue;
            }

            if (choice == 0) {
                System.out.println("Goodbye!");
                break;
            }

            System.out.print("Enter expression (use x as variable): ");
            String input = sc.nextLine();

            try {
                IExpr result;
                switch (choice) {
                    case 1:
                        result = Calculus.derivative(util, input);
                        System.out.println("Derivative: " + result);
                        break;
                    case 2:
                        result = Calculus.integral(util, input);
                        System.out.println("Integral: " + result);
                        break;
                    case 3:
                        result = Calculus.simplify(util, input);
                        System.out.println("Simplified: " + result);
                        break;
                    case 4:
                        result = Algebra.solve(util, input);
                        System.out.println("Solutions: " + result);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error while evaluating: " + e.getMessage());
            }
        }
        sc.close();
    }
}
