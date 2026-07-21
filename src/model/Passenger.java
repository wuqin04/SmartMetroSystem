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
		if (ticket == null) {
			throw new IllegalArgumentException("[ERROR]: Ticket cannot be null.");
		}
		
		double fare = ticket.getFare();
		
		if (fare <= 0 ) {
			throw new IllegalArgumentException("[ERROR]: Ticket fare must be greather than 0.");
		}
		
		if (this.balance < fare) {
			throw new IllegalArgumentException(String.format("[ERROR]: Insufficient balance. You need RM%.2f but only have RM%.2f ", fare, this.balance));
		}
		
		this.balance -= ticket.getFare();
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
