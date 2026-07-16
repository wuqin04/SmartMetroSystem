package payment;
import java.util.Scanner;

public class CashPayment {
    private final Scanner scanner;

    public CashPayment(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu() {
        System.out.println("[CASH PAYMENT]");
        System.out.print("Enter cash amount: ");
        String amount = scanner.nextLine();

        if (amount.isBlank()) {
            System.out.println("No amount entered. Payment cancelled.");
        } else {
            System.out.println("Cash payment of " + amount + " received.");
        }
        returnToPaymentMenu();
    }

    private void returnToPaymentMenu() {
        while (true) {
            System.out.println("[0] Return to Payment menu");
            System.out.println("[99] Exit");
            System.out.print("Choose: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 0) {
                    return;
                }
                if (choice == 99) {
                    System.out.println("Goodbye.");
                    System.exit(0);
                }
            } else {
                scanner.next();
            }
            System.out.println("[ERROR] Invalid choice. Enter 0 to return or 99 to exit.");
        }
    }
}