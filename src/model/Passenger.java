package model;

import enums.DiscountType;
import enums.UserRole;
import java.time.LocalDate;
import java.time.Period;

public class Passenger extends User {
	private double balance;
	private LocalDate dateOfBirth;
	
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
			throw new IllegalArgumentException("[ERROR]: Ticket fare must be greater than 0.");
		}
		
		if (this.balance < fare) {
			throw new IllegalArgumentException(String.format("[ERROR]: Insufficient balance. You need RM%.2f but only have RM%.2f ", fare, this.balance));
		}
		
		this.balance -= fare;
	}
	
	public void viewProfile() {
		super.viewProfile();
		
		System.out.printf("Wallet Balance: RM%,.2f\n", balance);
		System.out.println("--------------------");
	}
	
	public double getBalance() {
		return balance;
	}
	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	
	public Passenger(String userId, String name, String email, String password, UserRole role, double balance, LocalDate dateOfBirth) {
		super(userId, name, email, password, role);
		this.balance = balance;
		
		if(dateOfBirth == null) {
			throw new IllegalArgumentException("[ERROR]: Date of birth cannot be null or blank.");
		} else if(dateOfBirth.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("[ERROR]: Date of birth cannot be in the future.");
		}
		this.dateOfBirth = dateOfBirth;
		
		int age = Period.between(this.dateOfBirth, LocalDate.now()).getYears();
		
		if (age <= 12) {
			this.setDiscountType(DiscountType.CHILD);
		} else if (age >= 60) {
			this.setDiscountType(DiscountType.SENIOR);
		} else {
            this.setDiscountType(DiscountType.NONE);
		}
	}
}
