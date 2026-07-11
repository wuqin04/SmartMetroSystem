package model;

import enums.UserRole;

public class Passenger extends User {
	private double balance;
	
	public void topUp(double amount) {
		
	}
	
	public void buyTicket(Ticket ticket) {
		
	}
	
	public Passenger(String userId, String name, String email, String password, UserRole role) {
		super(userId, name, email, password, role);
		
	}
}
