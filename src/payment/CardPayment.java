package payment;

public class CardPayment implements Payment {
    private String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "[ERROR]: Payment amount must be greater than zero."
                );
            }

            if (cardNumber == null || cardNumber.trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "[ERROR]: No card number entered. Payment cancelled."
                );
            }

            return true;
        }
    }