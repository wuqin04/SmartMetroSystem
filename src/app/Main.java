package app;

import java.util.Scanner;

import model.User;
import enums.UserRole;
import service.UserService;
import exception.InvalidLoginException;
import model.Passenger;
import model.Admin;

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
				loginMenu(sc, us);
				break;
			case 2:
				sc.nextLine();
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
	
	public static void loginMenu(Scanner sc, UserService us) {
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
				userMenu(sc, (Passenger)loggedInUser);
			}
			else if (loggedInUser.getRole() == UserRole.ADMIN) {
				adminMenu(sc, (Admin)loggedInUser);
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
		
		userId = "USER" + idCounter++;
		
		try {
			passenger = new Passenger(userId, name, email, password, UserRole.PASSENGER, 0.0);
			us.registerUser(passenger);
			
			System.out.println("[SUCCESS]: Register successfully!");
		} catch (IllegalArgumentException e) {
			System.out.print("Register Failed!\n" + e.getMessage());
		}
	}
	
	public static void userMenu(Scanner sc, Passenger passenger) {
		int choice = -1;
		
		while (choice != 0) {
			System.out.printf("\n[%s's PASSENGER MENU]\n", passenger.getName());
			
			System.out.println("(1) View Profile");
			System.out.println("(2) Top Up Balance");
			System.out.println("(3) Buy Ticket");
			System.out.println("(4) Cancel Ticket");
			System.out.println("(5) View Ticket");
			System.out.println("(0) Log Out");
			System.out.println("(99) Exit Program");
			
			System.out.print("Enter your choice: ");
			
			if (!sc.hasNextInt()) {
				System.out.print("[ERROR]: That is not a valid number. Try again.\n");
				sc.next();
				continue;
			}
			
			choice = sc.nextInt();
			
			switch (choice) {
			case 1:
				// call profile method
				passenger.viewProfile();
				break;
			case 2:
				double amount = 0;
				while (amount != -1) {
					System.out.print("Enter amount to top up (-1 back to menu): RM ");
					
					if (!sc.hasNextDouble()) {
						System.out.println("[ERROR]: Please enter a valid number.");
						sc.next();
						continue;
					}
					
					amount = sc.nextDouble();
					sc.nextLine();
					
					if (amount == -1) {
						System.out.println("Returning to menu...\n");
						break;
					}
					
					try {
						passenger.topUp(amount);
						System.out.println("[SUCCESS]: Successfully added RM" + amount + " to your balance.");
					} catch (IllegalArgumentException e) {
						System.out.println("Top-up Failed!\n" + e.getMessage());
					}
				}
				break;
			case 3:
				// call buy ticket method
				passenger.buyTicket(null);
				break;
			case 4:
				// call cancel ticket method
				
				break;
			case 5:
				// call view ticket method
				break;
			case 0:
				System.out.println("Logging out...");
				break;
			case 99:
				System.out.println("Exiting program...");
				System.exit(0);
				break;
			default:
				System.out.println("[ERROR]: Invalid choice. Only input 0-5 or 99 to exit.");
				continue;
			}
		}
	}
	
	public static void adminMenu(Scanner sc, Admin admin) {
		
	}
}
