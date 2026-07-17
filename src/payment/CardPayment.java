package payment;

public class CardPayment implements Payment {
    private String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        if (amount <= 0) {
            System.out.println("[ERROR] Payment amount must be greater than zero.");
            return false;
        }

        if (cardNumber == null || cardNumber.isBlank()) {
            System.out.println("[ERROR] No card number entered. Payment cancelled.");
            return false;
        }

        System.out.printf("Card payment of %.2f processed.%n", amount);
        return true;
    }
}