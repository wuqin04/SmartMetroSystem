package model;

import enums.UserRole;

public class Passenger extends User {
	private double balance;
	
	public void topUp(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("[ERROR]: Top-up amount must be greater than 0.");
		}
		
		this.balance += amount;
	}
	
	public void buyTicket(Ticket ticket) {
		
	}
	
	public void viewProfile() {
		super.viewProfile();
		
		System.out.printf("Wallet Balance: RM%,.2f\n", balance);
		System.out.println("--------------------");
	}
	
	public double getBalance() {
		return balance;
	}
	
	public Passenger(String userId, String name, String email, String password, UserRole role, double balance) {
		super(userId, name, email, password, role);
		this.balance = balance;
	}
}
