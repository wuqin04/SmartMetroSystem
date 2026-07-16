package payment;
import java.util.Scanner;

public class CardPayment {
    private final Scanner scanner;

    public CardPayment(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu() {
        System.out.println("[CARD PAYMENT]");
        System.out.print("Enter card number: ");
        String cardNumber = scanner.nextLine();

        if (cardNumber.isBlank()) {
            System.out.println("No card number entered. Payment cancelled.");
        } else {
            System.out.println("Card payment selected. Payment processed.");
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
