package payment;

public class CashPayment implements Payment {

    @Override
    public boolean pay(double amount) {
        if (amount <= 0) {
            System.out.println("[ERROR] Payment amount must be greater than zero.");
            return false;
        }

        System.out.printf("Cash payment of %.2f received.%n", amount);
        return true;
    }
}