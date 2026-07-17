package payment;

import java.util.Scanner;

public class PaymentMenu {
    private final Scanner scanner;
    private final PaymentService paymentService = new PaymentService();

    public PaymentMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showMenu(double amount) {
        if (amount <= 0) {
            System.out.println("[ERROR] The ticket fare is invalid. Payment cannot continue.");
            return;
        }

        System.out.println("[PAYMENT METHOD]");
        System.out.printf("Amount to pay: %.2f%n", amount);
        System.out.println("(1) Card payment");
        System.out.println("(2) Cash payment");
        System.out.print("Choose a payment method: ");

        int choice = readInt();
        Payment payment;

        if (choice == 1) {
            System.out.print("Enter card number: ");
            payment = new CardPayment(scanner.nextLine());
        } else if (choice == 2) {
            payment = new CashPayment();
        } else {
            System.out.println("[ERROR] Invalid payment method.");
            return;
        }

        PaymentService.processPayment(payment, amount);
    }

    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }

        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}