package payment;

public class CashPayment implements Payment {

    @Override
    public boolean pay(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "[ERROR]: Payment amount must be greater than zero."
                );
            }

            return true;
        }
    }