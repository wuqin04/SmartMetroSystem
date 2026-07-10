package model;

import enums.UserRole;

public abstract class User {
	private String userId;
	private String name;
	private String email;
	private UserRole role;
	
	public boolean login(String email, String password) {
		return true;
	}
	
	public void viewProfile() {
		
	}
	
	public void editProfile(String name, String email) {
		
	}
}
