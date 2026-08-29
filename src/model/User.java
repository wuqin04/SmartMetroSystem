package model;

import enums.DiscountType;
import enums.UserRole;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class User {
	private String userId;
	private String name;
	private String email;
	private String password;
	private UserRole role;
	private boolean isSuspended;
	private DiscountType discountType;
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@gmail\\.com$");
	
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
	
	public boolean isSuspended() {
		return isSuspended;
	}
	
	public DiscountType getDiscountType() {
	    return discountType;
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
	
	public void setSuspended(boolean suspended) {
		this.isSuspended = suspended;
	}
	
	public void setDiscountType(DiscountType discountType) {
	    this.discountType = discountType;
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
        
        this.isSuspended = false;
        this.discountType = DiscountType.NONE;
	}
	
	public boolean login(String email, String password) {
		if (this.email.equals(email) && this.password.equals(password)) {
			// Check if account is suspended before allowing successful login
			if (this.isSuspended) {
				throw new IllegalStateException("[ERROR]: Account is suspended. Please contact the administrator.");
			}
			return true;
		}
		return false;
	}
	
	public void viewProfile() {
		System.out.println("\n[PROFILE INFORMATION]");
		System.out.println("User Unique ID: " + this.userId);
		System.out.println("Name: " + this.name);
		System.out.println("Email: " + this.email);
		System.out.println("Role: " + this.role);
		System.out.println("Fare Tier: " + this.discountType);
		System.out.println("Account Status: " + (this.isSuspended ? "Suspended" : "Active"));
		System.out.println("Password: " + this.password);
	}
	
	public void editProfile(String name, String email, String currPassword, String newPassword, String confirmPassword) {
		
		// check name and email cannot be null
		if(name == null) {
			throw new IllegalArgumentException("[ERROR]: Name cannot be null.");
		}
		
		if(email == null) {
			throw new IllegalArgumentException("[ERROR]: Email cannot be null.");
		} 
		
		// remove the spaces from the existing name and email
		String newName = name.strip();
        String newEmail = email.strip();
		
        // to make sure name and email is not empty and match the format 
		if(newName.isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Name cannot be empty or spaces only.");
		}
			
		if(newEmail.isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Email cannot be empty or spaces only.");
		} 
		
		if (!EMAIL_PATTERN.matcher(newEmail).matches()) {
            throw new IllegalArgumentException("[ERROR]: Invalid email format.");
        }
	
		// Check whether password edit is requested
		boolean passwordChangeRequested = currPassword != null && !currPassword.isEmpty()
									|| newPassword != null && !newPassword.isEmpty()
		 							|| confirmPassword != null && !confirmPassword.isEmpty();		
		
		boolean passwordChanged = false;
		
		if(passwordChangeRequested) {
			
			if(currPassword == null || currPassword.isEmpty()) {
				throw new IllegalArgumentException("[ERROR]: Current password cannot be null.");
			}
			
			if(newPassword == null || newPassword.isEmpty()) {
				throw new IllegalArgumentException("[ERROR]: New password cannot be null.");
			}
			
			if(confirmPassword == null || confirmPassword.isEmpty()) {
				throw new IllegalArgumentException("[ERROR]: Confirm password cannot be null");
			}
			
			if(!this.password.equals(currPassword)) {
				throw new IllegalArgumentException("[ERROR]: Current password is incorrect.");
			}
			
			if(newPassword.contains(" ")) {
				throw new IllegalArgumentException("[ERROR]: New password cannot contain spaces.");
			}
			
			if(this.password.equals(newPassword)) {
				throw new IllegalArgumentException("[ERROR]: New password cannot be same as current password.");
			}
			
			if(!newPassword.equals(confirmPassword)) {
				throw new IllegalArgumentException("[ERROR]: Confirm password does not match new password.");
			}
			
			passwordChanged = true;
		}
		// to check whether the new name and email are same or not
		boolean nameChanged = !this.name.equals(newName);
		boolean emailChanged = !this.email.equalsIgnoreCase(newEmail);
		
		this.name = newName;
		this.email = newEmail;
		
		if (passwordChanged) {
			this.password = newPassword;
		}
		
		if (nameChanged) {
			System.out.println("[INFO]: Name has been changed.");
		} 
		
		if (emailChanged) {
			System.out.println("[INFO]: Email has been changed.");
		} 
		
		if (passwordChanged) {
			System.out.println("[INFO]: Password has been changed.");
		}
	}
}