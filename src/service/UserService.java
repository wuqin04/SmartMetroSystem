package service;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import model.User;
import model.Passenger;
import model.Admin;
import enums.UserRole;
import repository.JsonFileManager;
import util.JsonUtil;
import exception.InvalidLoginException;
import exception.FileProcessingException;

public class UserService {

	private HashMap<String, User> users = new HashMap<>();
	private JsonFileManager fileManager;
	private String fileName;
	
	public UserService(JsonFileManager fileManager, String fileName) {
		
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
		
		loadUsers();
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
		
		if(email == null || email.trim().isEmpty()) {
			throw new InvalidLoginException("[ERROR]: Invalid email.");
		}
		
		if(password == null || password.trim().isEmpty()) {
			throw new InvalidLoginException("[ERROR]: Invalid password.");
		}
		
		String emailKey = email.trim().toLowerCase(Locale.ROOT);
		User foundUser = users.get(emailKey);
		
		if(foundUser == null){
			throw new InvalidLoginException("[ERROR]: Invalid email or password.");
		}
		
		boolean isSuccess = foundUser.login(foundUser.getEmail(), password);
		
		if (!isSuccess) {
			throw new InvalidLoginException("[ERROR]: Invalid login credentials, check your password or email.");
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
			
			if (loadedObject == null) return;
			
			String jsonString = String.valueOf(loadedObject).trim();
			
        	if (jsonString.isEmpty() || jsonString.replaceAll("\\s+", "").equals("[]")) return;
        	
        	users.clear();
        	
			String[] userBlocks = jsonString.split("}");
			
			for (String block : userBlocks) {
				if (block.trim().isEmpty() || block.trim().equals("]")) continue;
				
				String userId = JsonUtil.extractString(block, "userId");
				String name = JsonUtil.extractString(block, "name");
				String email = JsonUtil.extractString(block, "email");
				String password = JsonUtil.extractString(block, "password");
				
				String roleText = JsonUtil.extractString(block, "role").toUpperCase(Locale.ROOT);
				UserRole role = UserRole.valueOf(roleText); 
			
				User user;
				if (role == UserRole.PASSENGER) {
					double balance = JsonUtil.extractNumber(block, "balance");
					
					String dateOfBirthText = JsonUtil.extractString(block, "dateOfBirth"); 
					LocalDate dateOfBirth = LocalDate.parse(dateOfBirthText);
					
					user = new Passenger(userId, name, email, password, UserRole.PASSENGER, balance, dateOfBirth);
				} else if (role == UserRole.ADMIN) {
					user = new Admin(userId, name, email, password);
				} else {
					throw new IllegalArgumentException("[ERROR]: Invalid user role.");
				}
				
				String emailKey = email.trim().toLowerCase(Locale.ROOT);
				if (users.containsKey(emailKey)) {
					throw new IllegalStateException("[ERROR]: Duplicate email found.");
				}
				
				users.put(emailKey, user);
			}
			
		} catch (FileProcessingException e) {
			System.out.println("[INFO]: Creating new data.");
		} catch (RuntimeException e) {
			throw new IllegalStateException("[ERROR]: Unable to load user data from " + fileName + ".", e);
		}
	}
	
	public void saveUsers() throws FileProcessingException {
		
		String jsonString = "[\n";
		
		List<User> userList = new ArrayList<>(users.values());
		
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			
			if (user.getRole() == UserRole.PASSENGER) {
				Passenger p = (Passenger) user;
				
				jsonString += """
							{
								"userId": "%s",
								"name": "%s",
								"email": "%s",
								"password": "%s",
								"role": "%s",
								"balance": %s
								"date of birth": "%s"
							}
						""".formatted(
								p.getUserId(),
								p.getName(),
								p.getEmail(),
								p.getPassword(),
								p.getRole(),
								p.getBalance(),
								p.getDateOfBirth()
								);
			}
			
			if (i < userList.size() - 1) {
				jsonString += ",\n";
			}
			else {
				jsonString += "\n";
			}
		}
		
		jsonString += "]";
		
		try {
			fileManager.saveData(jsonString, fileName);
		} catch (FileProcessingException e) {
			System.out.println("[ERROR]: Critical failure while saving users to" + fileName);
			throw e;
		}
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
	
