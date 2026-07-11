package app;

import java.util.Scanner;

import enums.UserRole;
import service.UserService;
import exception.InvalidLoginException;
import model.Passenger;

public class Main {
	private static int idCounter = 0;
	
	public static void main(String[] args) {
		Scanner sc 	   = new Scanner(System.in);
		UserService us = new UserService();
		
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
				clearScreen();
				loginMenu(sc, us);
				break;
			case 2:
				sc.nextLine();
				clearScreen();
				registerMenu(sc, us);
				break;
			case 99:
				System.out.println("Exiting program...");
				break;
			default:
				System.out.println("[ERROR]: Invalid choice. only input 1-2 and 99 to exit.");
				continue;
			}
		}
	}
	
	public static void clearScreen() {
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
	}
	
	public static void loginMenu(Scanner sc, UserService us) {
		// temp information
		String email;
		String password;
		
		System.out.println("[LOGIN PAGE]");
		
		System.out.print("Enter your email: ");
		email = sc.nextLine();
		
		System.out.print("Enter your password: ");
		password = sc.nextLine();
		
		try {
			us.login(email, password);
			
			System.out.printf("[SUCCESS]: Welcome back!");
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
		
		System.out.println("[REGISTER PAGE]");
		
		System.out.print("Create your name: ");
		name = sc.nextLine();
		
		System.out.print("Enter your email: ");
		email = sc.nextLine();
		
		System.out.print("Create your password: ");
		password = sc.nextLine();
		
		userId = "USER" + idCounter++;
		
		try {
			passenger = new Passenger(userId, name, email, password, UserRole.PASSENGER);
			us.registerUser(passenger);
			
			System.out.println("[SUCCESS]: Register successfully!");
		} catch (IllegalArgumentException e) {
			System.out.print("Register Failed!\n" + e.getMessage());
		}
	}
}
