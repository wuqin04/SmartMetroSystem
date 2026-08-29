package payment;

public class CardPayment implements Payment {
    private String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("[ERROR]: Payment amount must be greater than zero.");
        }

        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: No card number entered. Payment cancelled.");
        }

        if (!cardNumber.matches("\\d{16}")) {
            throw new IllegalArgumentException("[ERROR]: Invalid card format. Must be exactly 16 digits.");
        }

        System.out.printf("[SUCCESS]: Card ending in %s charged RM%.2f.\n", 
                          cardNumber.substring(12), amount);
        return true;
    }
}