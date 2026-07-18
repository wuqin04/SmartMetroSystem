package service;

import payment.Payment;

public class PaymentService {

    public boolean processPayment(Payment payment, double amount) {
        if (payment == null) {
            throw new IllegalArgumentException(
                "[ERROR]: Select a payment method first."
            );
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "[ERROR]: Payment amount must be greater than zero."
            );
        }

        return payment.pay(amount);
    }
}