package service;

import java.util.HashMap;
import model.User;
import exception.InvalidLoginException;

public class UserService {
	private HashMap<String, User> users = new HashMap<>();
	
	public void registerUser(User user) {
		
		if (users.containsKey(user.getEmail())) {
			throw new IllegalArgumentException("[ERROR]: Email is already registered.");
		}
		
		users.put(user.getEmail(), user);
	}
	
	public User login(String email, String password) throws InvalidLoginException {
		User foundUser = users.get(email);
		
		if (foundUser == null) {
			throw new InvalidLoginException("[ERROR]: Invalid email.");
		}
		
		boolean isSuccess = foundUser.login(email, password);
		
		if (!isSuccess) {
			throw new InvalidLoginException("[ERROR]: Invalid login credentials, check your password or email.");
		}
		
		return foundUser;
	}
	
	public void viewAllUsers() {
		
	}
}
