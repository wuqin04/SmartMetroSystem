package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import exception.FileProcessingException;
import payment.Payment;
import repository.FileManager;
import repository.JSONFileManager;

public class PaymentService {

    private static final String PAYMENT_FILE = "data/payments.json";

    private final FileManager jsonFileManager = new JSONFileManager();
    private final List<JSONObject> paymentHistory = new ArrayList<>();

    public PaymentService() {
        try {
            loadPaymentsFromJson();
        } catch (FileProcessingException e) {
            System.out.println("[INFO]: Starting with empty payment history.");
        }
    }

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

        boolean paymentSuccessful = payment.pay(amount);

        if (paymentSuccessful) {
            JSONObject paymentRecord = new JSONObject();
            paymentRecord.put("paymentMethod",
                payment.getClass().getSimpleName());
            paymentRecord.put("amount", amount);
            paymentRecord.put("status", "SUCCESS");
            paymentRecord.put("paidAt", LocalDateTime.now().toString());

            paymentHistory.add(paymentRecord);

            try {
                savePaymentsToJson();
            } catch (FileProcessingException e) {
                throw new IllegalStateException(
                    "[ERROR]: Payment succeeded but could not be saved."
                );
            }
        }

        return paymentSuccessful;
    }

    private void savePaymentsToJson() throws FileProcessingException {
        jsonFileManager.saveData(paymentHistory, PAYMENT_FILE);
    }

    private void loadPaymentsFromJson() throws FileProcessingException {
        Object loadedData = jsonFileManager.loadData(PAYMENT_FILE);

        if (!(loadedData instanceof List<?>)) {
            throw new FileProcessingException(
                "[ERROR]: Invalid payment JSON data."
            );
        }

        for (Object item : (List<?>) loadedData) {
            if (item instanceof JSONObject) {
                paymentHistory.add((JSONObject) item);
            }
        }
    }


    public void displayPaymentHistory() {
        if (paymentHistory.isEmpty()) {
            System.out.println("No payment records found.");
            return;
        }

        for (JSONObject paymentRecord : paymentHistory) {
            System.out.println(paymentRecord.toString(2));
        }
    }
}