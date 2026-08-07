package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.PaymentRecord;
import exception.FileProcessingException;
import payment.Payment;
import repository.FileManager;
import util.JsonUtil; 

public class PaymentService {

	 private final FileManager jsonFileManager;
	 private final String paymentFile;
	 private final List<PaymentRecord> paymentHistory = new ArrayList<>();
	 
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
        	PaymentRecord record = new PaymentRecord(
                    payment.getClass().getSimpleName(),
                    amount,
                    "SUCCESS",
                    LocalDateTime.now().toString()
            );
        

            paymentHistory.add(record);

            try {
                savePayments();
            } catch (FileProcessingException e) {
            	paymentHistory.remove(paymentHistory.size() - 1);
                throw new IllegalStateException("[ERROR]: Payment succeeded but could not be saved.");
            }
        }

        return paymentSuccessful;
    }

    public void savePayments() throws FileProcessingException {
    	String jsonString = "[\n";

        for (int i = 0; i < paymentHistory.size(); i++) {
            PaymentRecord record = paymentHistory.get(i);
            
            jsonString += """
	              {
	            		"paymentMethod": "%s",
		                "amount": %s,
		                "status": "%s",
		                "paidAt": "%s"
	              }
              """.formatted(
                      record.getPaymentMethod(),
                      record.getAmount(),
                      record.getStatus(),
                      record.getPaidAt()
            		  );

            if (i < paymentHistory.size() - 1) {
                jsonString += ",\n";
            } else {
                jsonString += "\n";
            }
        }

        jsonString += "]";

        try {
            jsonFileManager.saveData(jsonString, paymentFile);
        } catch (FileProcessingException e) {
            System.out.println("[ERROR]: Critical failure while saving payments to " + paymentFile);
            throw e;
        }
    }

    public void loadPayments() {
    	try {
            Object loadedData = jsonFileManager.loadData(paymentFile);
            
            if (loadedData == null) return;
            
            String jsonString = String.valueOf(loadedData).trim();
            
        	if (jsonString.isEmpty() || jsonString.replaceAll("\\s+", "").equals("[]")) return;
            
            paymentHistory.clear();
            
            String[] blocks = jsonString.split("}");
            
            for (String block : blocks) {
                if (block.trim().isEmpty() || block.trim().equals("]")) {
                    continue;
                }
                
                String method = JsonUtil.extractString(block, "paymentMethod");
                double amount = JsonUtil.extractNumber(block, "amount");
                String status = JsonUtil.extractString(block, "status");
                String paidAt = JsonUtil.extractString(block, "paidAt");
                
                paymentHistory.add(new PaymentRecord(method, amount, status, paidAt));
            }
            
        } catch (FileProcessingException e) {
            System.out.println("[INFO]: No existing payment records found. Starting fresh.");
        } catch (Exception e) {
            throw new IllegalStateException("[ERROR]: Unable to load payment data from " + paymentFile + ".", e);
        }
    }

    public void displayPaymentHistory() {
        if (paymentHistory.isEmpty()) {
            System.out.println("No payment records found.");
            return;
        }

        for (PaymentRecord record : paymentHistory) {
            System.out.println(record);
            System.out.println("-------------------------");
        }
    }
}