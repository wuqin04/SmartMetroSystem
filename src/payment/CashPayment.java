package payment;

import java.util.Scanner;

public class CashPayment implements Payment {

    @Override
    public boolean pay(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("[ERROR]: Payment amount must be greater than zero.");
        }

        Scanner sc = new Scanner(System.in);
        System.out.printf("\n[CASHIER] Amount due is RM%.2f\n", amount);
        System.out.print("Enter amount of cash tendered: RM ");
        
        try {
            double cashTendered = Double.parseDouble(sc.nextLine().trim());

            if (cashTendered < amount) {
                throw new IllegalArgumentException(String.format("Insufficient cash. You are short RM%.2f.", (amount - cashTendered)));
            }

            double change = cashTendered - amount;
            System.out.printf("[SUCCESS]: Payment accepted. Your change is RM%.2f.\n", change);
            return true;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR]: Invalid input. Please enter a valid numerical amount.");
        }
    }
}