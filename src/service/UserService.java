package service;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import java.util.Locale;
import model.User;
import model.Passenger;
import model.Admin;
import enums.UserRole;
import repository.JSONFileManager;
import exception.InvalidLoginException;
import exception.FileProcessingException;

public class UserService {

	private HashMap<String, User> users = new HashMap<>();
	private JSONFileManager fileManager;
	private String fileName;
	
	public UserService(JSONFileManager fileManager, String fileName) {
		
		if (fileManager == null) {
			throw new IllegalArgumentException(
				"[ERROR]: JSON file manager cannot be null."
			);
		}

		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException(
				"[ERROR]: File name cannot be null or blank."
			);
		}
		
		this.fileManager = fileManager;
		this.fileName = fileName;
	}
	
	
	// --- REGISTER ---
	public void registerUser(User user) throws FileProcessingException {
		
		if(user == null) {
			throw new IllegalArgumentException("[ERROR]: User cannot be null.");
		}
		
		if(user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("User email cannot be null.");
		}
		String emailKey = user.getEmail().trim().toLowerCase(Locale.ROOT);
		
		if (users.containsKey(emailKey)) {
			throw new IllegalArgumentException("[ERROR]: Email is already registered.\n");
		}
		
		users.put(emailKey, user);

		try {
			saveUsers();
		} catch (FileProcessingException e) {

			users.remove(emailKey);
			throw e;
		}
	}
		
	// --- LOGIN ---
	public User login(String email, String password) throws InvalidLoginException {
		// Because we loaded the data in the constructor, we just check the HashMap!
		
		loadUsers();
		
		if(email == null || email.trim().isEmpty()) {
			throw new InvalidLoginException("[ERROR]: Invalid email.\n");
		}
		
		if(password == null || password.trim().isEmpty()) {
			throw new InvalidLoginException("[ERROR]: Invalid password.\n");
		}
		
		String emailKey = email.trim().toLowerCase(Locale.ROOT);
		User foundUser = users.get(emailKey);
		
		if(foundUser == null){
			throw new InvalidLoginException("[ERROR]: Invalid email or password.");
		}
		
		boolean isSuccess = foundUser.login(foundUser.getEmail(), password);
		
		if (!isSuccess) {
			throw new InvalidLoginException("[ERROR]: Invalid login credentials, check your password or email.\n");
		}
		
		return foundUser;
	}
	
	// --- VIEW ALL ---
	public void viewAllUsers() {
		System.out.println("\n--- ALL REGISTERED USERS ---");
		
		if(users.isEmpty()){
			System.out.println("No registered users found.");
			System.out.println("----------------------------");
			return;
		}
		for (User user : users.values()) {
			System.out.println(user);
		}
		System.out.println("----------------------------");
	}
	
	public void loadUsers() {
		
		try {
			Object loadedObject = fileManager.loadData(fileName);
			
			if(loadedObject == null) {
				return;
			}
			
			if(!(loadedObject instanceof List<?>)) {
				throw new IllegalStateException("[ERROR]: Invalid user file format.");
			}
			
			List<?> loadedData = (List<?>) loadedObject;	
			
			for (Object obj : loadedData) {
				JSONObject jsonObj;
				if (obj instanceof JSONObject) {
					jsonObj = (JSONObject) obj;
				} else {
					jsonObj = new JSONObject(obj);
				}
				
				// Extract data from JSON
				String userId = jsonObj.getString("userId");
				String name = jsonObj.getString("name");
				String email = jsonObj.getString("email");
				String password = jsonObj.getString("password");
				
				// accept "Admin, "admin", " ADMIN"
				String roleText = jsonObj.getString("role").trim().toUpperCase(Locale.ROOT);
				UserRole role = UserRole.valueOf(roleText); 
				
				User user;
				// Rebuild the correct object type
				if (role == UserRole.PASSENGER) {
					double balance = jsonObj.optDouble("balance", 0.0);
					user = new Passenger(userId, name, email, password, UserRole.PASSENGER, balance);
				} else if(role == UserRole.ADMIN){
					user = new Admin(userId, name, email, password);
				} else {
					throw new IllegalArgumentException("[ERROR]: Invalid user role.");
				}
				
				String emailKey = email.trim().toLowerCase(Locale.ROOT);
				
				if(users.containsKey(emailKey)) {
					throw new IllegalStateException("[ERROR]: Duplicate email found in user file: " + email);
				}
				
				// Put the user into the lecturer's HashMap
				users.put(emailKey, user);
			}
			
		} catch (FileProcessingException e) {
			// If the file doesn't exist yet, it just means no users are registered.
			// The HashMap stays empty, which is perfectly fine.
			System.out.println("[INFO]: No existing users found. Starting fresh.");
		} catch (RuntimeException e) {
			throw new IllegalStateException("[ERROR]: Unable to load user data from " + fileName + ".", e);
		}
	}
	
	public void saveUsers() throws FileProcessingException {
		
		List<User> userList = new ArrayList<>(users.values());	
		
		fileManager.saveData(userList, fileName);
	}
	
	public Passenger findPassengerById(String passengerId) {
	
		if (passengerId == null || passengerId.trim().isEmpty()) {

				throw new IllegalArgumentException("[ERROR]: Passenger ID cannot be null or blank.");
			}

			String enteredId = passengerId.trim();

			for (User user : users.values()) {

				if (user instanceof Passenger && user.getUserId().equalsIgnoreCase(enteredId)) {

					return (Passenger) user;
				}
			}

			throw new IllegalArgumentException("[ERROR]: Passenger ID not found: " + enteredId);
	}
}	