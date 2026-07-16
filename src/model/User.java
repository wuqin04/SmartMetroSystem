package model;

import enums.UserRole;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class User {
	private String userId;
	private String name;
	private String email;
	private String password;
	private UserRole role;
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
	
	// Accessors
	public String getUserId() {
		return userId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public UserRole getRole() {
		return role;
	}
	
	// Mutators
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmail(String aEmail) {
		this.email = aEmail;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
	}
	
	public User(String userId, String name, String email, String password, UserRole role) {
		
		if (userId == null || userId.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: User ID cannot be null.");
		}
		
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Username cannot be null.");
		}
		
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Email cannot be null.");
		}
		
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Password cannot be null.");
		}
		
		if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("[ERROR]: Invalid email format.");
        }
		
		Objects.requireNonNull(role, "[ERROR]: User role cannot be null.");
		
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	public boolean login(String email, String password) {
		return this.email.equals(email) && this.password.equals(password);
	}
	
	public void viewProfile() {
		System.out.println("\n[PROFILE INFORMATION]");
		System.out.println("Name: " + this.name);
		System.out.println("Email: " + this.email);
		System.out.println("Role: " + this.role);
		System.out.println("Password: " + this.password);
	}
	
	public void editProfile(String name, String email) {
		
	}
}
