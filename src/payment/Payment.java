package payment;
import java.util.Scanner;

public class Payment {
    private final Scanner scanner;

    public Payment(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu() {
        while (true) {
            System.out.println("[PAYMENT METHOD]");
            System.out.println("(1) Card payment");
            System.out.println("(2) Cash payment");
            System.out.println("(0) Return to User menu");
            System.out.println("(99) Exit");
            System.out.print("Choose a payment method: ");

            int choice = readChoice();
            switch (choice) {
                case 1:
                    new CardPayment(scanner).showMenu();
                    break;
                case 2:
                    new CashPayment(scanner).showMenu();
                    break;
                case 0:
                    return;
                case 99:
                    System.out.println("Goodbye.");
                    System.exit(0);
                    return;
                default:
                    System.out.println("[ERROR] Invalid choice. Please enter 0, 1, 2, or 99.");
            }
        }
    }

    private int readChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
}

