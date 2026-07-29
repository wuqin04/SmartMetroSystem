package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import exception.FileProcessingException;
import payment.Payment;
import repository.FileManager;

public class PaymentService {

	 private final FileManager jsonFileManager;
	 private final String paymentFile;
	 private final List<JSONObject> paymentHistory = new ArrayList<>();

    public PaymentService(FileManager jsonFileManager, String paymentFile) {
    	
    	this.jsonFileManager = jsonFileManager;
        this.paymentFile = paymentFile;
        
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
                savePayments();
            } catch (FileProcessingException e) {
                throw new IllegalStateException(
                    "[ERROR]: Payment succeeded but could not be saved."
                );
            }
        }

        return paymentSuccessful;
    }

    public void savePayments() throws FileProcessingException {
        jsonFileManager.saveData(paymentHistory, paymentFile);
    }

    public void loadPayments() throws FileProcessingException {
        Object loadedData = jsonFileManager.loadData(paymentFile);

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