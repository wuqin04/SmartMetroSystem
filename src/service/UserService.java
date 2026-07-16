package service;

import java.util.HashMap;

import model.User;
import repository.FileManager;
import repository.TXTFileManager;
import exception.InvalidLoginException;

public class UserService {
	private HashMap<String, User> users = new HashMap<>();
	
	public void registerUser(User user) {
		
		if (users.containsKey(user.getEmail())) {
			throw new IllegalArgumentException("[ERROR]: Email is already registered.\n");
		}
		
		users.put(user.getEmail(), user);
	}
	
	public User login(String email, String password) throws InvalidLoginException {
		User foundUser = users.get(email);
		
		if (foundUser == null) {
			throw new InvalidLoginException("[ERROR]: Invalid email.\n");
		}
		
		boolean isSuccess = foundUser.login(email, password);
		
		if (!isSuccess) {
			throw new InvalidLoginException("[ERROR]: Invalid login credentials, check your password or email.\n");
		}
		
		return foundUser;
	}
	
	public void viewAllUsers() {
		
	}
}
