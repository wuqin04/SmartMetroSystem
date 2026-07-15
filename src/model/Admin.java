package model;

import enums.UserRole;

public class Admin extends User {
	public void addStation(Station station) {
		
	}
	
	public void addTrain(Train train) {
		
	}
	
	public void viewReports() {
		
	}
	
	public Admin(String userId, String name, String email, String password, UserRole role) {
		super(userId, name, email, password, role);
	}
}
