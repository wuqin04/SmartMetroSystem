package app;

import java.util.Scanner;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import ui.PassengerUI;
import ui.AdminUI;
import model.Ticket;
import model.User;
import model.Admin;
import model.Passenger;
import model.Route;
import fare.FareCalculator;
import fare.StandardFareCalculator;
import enums.UserRole;
import repository.FileManager;
import repository.JSONFileManager;
import service.UserService;
import service.TicketService;
import service.RouteService;
import exception.FileProcessingException;
import exception.InvalidLoginException;

public class Main {
	public static void main(String[] args) {
		JSONFileManager 			jsonFM			= new JSONFileManager();
		String 						userFile		= "data/users.json";
		
		Scanner 					sc 	   			= new Scanner(System.in);
		StandardFareCalculator 		stdFareCalc 	= new StandardFareCalculator();
		
		ArrayList<Ticket> 			tickets 		= new ArrayList<Ticket>();
		ArrayList<Route>			routes			= new ArrayList<Route>();
		
		UserService 				us 				= new UserService();
		TicketService 				ts				= new TicketService(tickets, stdFareCalc);
		RouteService				rs				= new RouteService(routes);
		
		
		PassengerUI 				passengerUI 	= new PassengerUI(sc, ts);
		AdminUI 					adminUI			= new AdminUI(sc, rs);
		
		String choice = null;
		
		while (!choice.equals("99")) {
			System.out.println("\n[SMART METRO SYSTEM]");
			System.out.println("(1)  Login ");
			System.out.println("(2)  Register");
			System.out.println("(99) Exit Program ");
			System.out.print("Enter your choice: ");
			
			choice = sc.nextLine();
			
			switch (choice) {
			case "1":
				loginMenu(sc, us, passengerUI, adminUI);
				break;
				
			case "2":
				registerMenu(sc, us);
				break;
				
			case "99":
				System.out.println("Exiting program...");
				break;
				
			default:
				System.out.println("[ERROR]: Invalid choice. Only input 1-2 and 99 to exit.");
				continue;
			}
		}
		
		sc.close();
	}
	
	public static void loginMenu(Scanner sc, UserService us, PassengerUI passengerUI, AdminUI adminUI) {
		// temp information
		String email;
		String password;
		
		System.out.println("\n[LOGIN PAGE]");
		
		System.out.print("Enter your email: ");
		email = sc.nextLine();
		
		System.out.print("Enter your password: ");
		password = sc.nextLine();
		
		JSONFileManager jsonFM = new JSONFileManager();
		String fileName = "data/users.json";
		
		try {
			User loggedInUser = us.login(email, password);
			
			System.out.printf("[SUCCESS]: Welcome back, %s!\n", loggedInUser.getName());
			
			if (loggedInUser.getRole() == UserRole.PASSENGER) {
				passengerUI.loadDashboard((Passenger)loggedInUser);
			}
			else if (loggedInUser.getRole() == UserRole.ADMIN) {
				adminUI.loadDashboard((Admin)loggedInUser);
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
			passenger = new Passenger(userId, name, email, password, UserRole.PASSENGER, 0);
			us.registerUser(passenger);
						
			System.out.println("[SUCCESS]: Register successfully!");
		} catch (IllegalArgumentException | FileProcessingException e) {
			System.out.print("Register Failed!\n" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Register Failed!\n[ERROR]: " + e.getMessage());
		}
	}
}
