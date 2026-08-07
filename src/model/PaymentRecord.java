package model;

public class PaymentRecord {
    private final String paymentMethod;
    private final double amount;
    private final String status;
    private final String paidAt;

    public PaymentRecord(String paymentMethod, double amount, String status, String paidAt) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
    }

    public String getPaymentMethod() { return paymentMethod; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getPaidAt() { return paidAt; }

    @Override
    public String toString() {
        return "Payment Method: " + paymentMethod + "\n" +
               "Amount: RM " + String.format("%.2f", amount) + "\n" +
               "Status: " + status + "\n" +
               "Date/Time: " + paidAt;
    }
}