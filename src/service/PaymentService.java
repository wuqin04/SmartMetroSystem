package service;

public class PaymentService {

    public boolean processPayment(Payment payment, double amount) {
        if (payment == null) {
            System.out.println("[ERROR] Select a payment method first.");
            return false;
        }

        return payment.pay(amount);
    }
}
