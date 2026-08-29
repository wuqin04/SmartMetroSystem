package payment;

import model.Passenger;

public class WalletPayment implements Payment {
    private Passenger passenger;

    public WalletPayment(Passenger passenger) {
        this.passenger = passenger;
    }

    @Override
    public boolean pay(double amount) {
        if (passenger.getBalance() < amount) {
            throw new IllegalArgumentException(String.format("Insufficient Metro Wallet balance. You need RM%.2f but only have RM%.2f.", amount, passenger.getBalance()));
        }
        
        System.out.printf("[INFO]: Authorized RM%.2f from Metro Wallet.\n", amount);
      
        return true;
    }
}