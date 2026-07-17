package app;

import java.util.Scanner;

import ui.PassengerUI;
import model.User;
import model.Passenger;
import enums.UserRole;
import service.UserService;
import exception.InvalidLoginException;

public class Main {
	public static void main(String[] args) {
		Scanner sc 	   			= new Scanner(System.in);
		UserService us 			= new UserService();
		PassengerUI passengerUI = new PassengerUI(sc);
		
		int choice = 0;
		
		while (choice != 99) {
			System.out.println("\n[SMART METRO SYSTEM]");
			System.out.println("(1)  Login ");
			System.out.println("(2)  Register");
			System.out.println("(99) Exit Program ");
			System.out.print("Enter your choice: ");
			
			if (!sc.hasNextInt()) {
				System.out.print("[ERROR]: That is not a valid number. Try again.\n");
				sc.next();
				continue;
			}
			
			choice = sc.nextInt();
			
			switch (choice) {
			case 1:
				sc.nextLine();
				loginMenu(sc, us, passengerUI);
				break;
			case 2:
				sc.nextLine();
				registerMenu(sc, us);
				break;
			case 99:
				System.out.println("Exiting program...");
				break;
			default:
				System.out.println("[ERROR]: Invalid choice. Only input 1-2 and 99 to exit.");
				continue;
			}
		}
		
		sc.close();
	}
	
	public static void loginMenu(Scanner sc, UserService us, PassengerUI passengerUI) {
		// temp information
		String email;
		String password;
		
		System.out.println("\n[LOGIN PAGE]");
		
		System.out.print("Enter your email: ");
		email = sc.nextLine();
		
		System.out.print("Enter your password: ");
		password = sc.nextLine();
		
		try {
			User loggedInUser = us.login(email, password);
			
			System.out.printf("[SUCCESS]: Welcome back, %s!\n", loggedInUser.getName());
			
			if (loggedInUser.getRole() == UserRole.PASSENGER) {
				passengerUI.loadDashboard((Passenger)loggedInUser);
			}
			else if (loggedInUser.getRole() == UserRole.ADMIN) {
				// TODO: call adminUI.java method
			}
		} catch (InvalidLoginException e) {
			System.out.print("Login Failed!\n" + e.getMessage());
		}
	}
	
	public static void registerMenu(Scanner sc, UserService us) {
		// temp information
		String userId;
		String name;
		String email;
		String password;
		
		Passenger passenger;
		
		System.out.println("\n[REGISTER PAGE]");
		
		System.out.print("Create your name: ");
		name = sc.nextLine();
		
		System.out.print("Enter your email: ");
		email = sc.nextLine();
		
		System.out.print("Create your password: ");
		password = sc.nextLine();
		
		userId = "USER" + System.currentTimeMillis();
		
		try {
			passenger = new Passenger(userId, name, email, password, UserRole.PASSENGER, 0.0);
			us.registerUser(passenger);
			
			System.out.println("[SUCCESS]: Register successfully!");
		} catch (IllegalArgumentException e) {
			System.out.print("Register Failed!\n" + e.getMessage());
		}
	}
}
