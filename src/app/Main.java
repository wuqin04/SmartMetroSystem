package app;

import java.util.Scanner;
import java.util.ArrayList;

import ui.PassengerUI;
import ui.AdminUI;

import model.Ticket;
import model.User;
import model.Admin;
import model.Passenger;
import model.Route;

import fare.StandardFareCalculator;

import enums.UserRole;

import repository.JsonFileManager;

import service.UserService;
import service.TicketService;
import service.TrainService;
import service.PaymentService;
import service.RouteService;
import service.StationService;

import exception.InvalidLoginException;

public class Main {
	public static void main(String[] args) {
		JsonFileManager 			jsonFM			= new JsonFileManager();
		
		String 						stationFile		= "data/stations.json";
		String 						userFile		= "data/users.json";
		String 						ticketFile		= "data/tickets.json";
		String 						routeFile		= "data/routes.json";
		String						trainFile		= "data/trains.json";
		String 						paymentFile		= "data/payments.json";
		
		Scanner 					sc 	   			= new Scanner(System.in);
		StandardFareCalculator 		stdFareCalc 	= new StandardFareCalculator();
		
		ArrayList<Ticket> 			tickets 		= new ArrayList<Ticket>();
		ArrayList<Route>			routes			= new ArrayList<Route>();
		
		StationService				stationService  = new StationService(jsonFM, stationFile);
		UserService 				userService     = new UserService(jsonFM, userFile);
		TicketService 				ticketService	= new TicketService(tickets, jsonFM, stdFareCalc, ticketFile, userService, stationService);
		RouteService				routeService	= new RouteService(routes, jsonFM, routeFile);
		TrainService                trainService    = new TrainService(jsonFM, trainFile); 
		PaymentService              paymentService  = new PaymentService(jsonFM, paymentFile);
		
		// load all data
		stationService.loadStations();
		userService.loadUsers();
		trainService.loadTrains();
		paymentService.loadPayments();
		routeService.loadRoutes();  
		ticketService.loadTickets();
		
		PassengerUI 				passengerUI 	= new PassengerUI(sc, ticketService, routeService);
		AdminUI 					adminUI			= new AdminUI(sc, routeService, stationService, routes);
		
		String choice = "";
		
		while (!choice.equals("99")) {
			System.out.println("\n[SMART METRO SYSTEM]");
			System.out.println("(1)  Login ");
			System.out.println("(2)  Register");
			System.out.println("(99) Exit Program ");
			System.out.print("Enter your choice: ");
			
			choice = sc.nextLine().trim();
			
			switch (choice) {
			case "1":
				loginMenu(sc, userService, passengerUI, adminUI);
				break;
				
			case "2":
				registerMenu(sc, userService);
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
		
		System.out.println("\n[LOGIN PAGE]");
		System.out.println("Enter 99 to return to the main menu.");
		
		boolean isSuccess = false;
		
		while (!isSuccess) {
			System.out.print("Enter your email: ");
			String email = sc.nextLine();
			
			if (email.equalsIgnoreCase("99")) {
				return;
			}
			
			System.out.print("Enter your password: ");
			String password = sc.nextLine().trim();
			
			if (password.equalsIgnoreCase("99")) {
				return;	
			}
			
			try {
				User loggedInUser = us.login(email, password);
				isSuccess = true;
				
				System.out.printf("[SUCCESS]: Welcome back, %s!\n", loggedInUser.getName());
				
				if (loggedInUser.getRole() == UserRole.PASSENGER) {
					passengerUI.loadDashboard((Passenger)loggedInUser);
				}
				else if (loggedInUser.getRole() == UserRole.ADMIN) {
					adminUI.loadDashboard((Admin)loggedInUser);
				}
			} catch (InvalidLoginException e) {
				System.out.println("Login Failed!\n" + e.getMessage());
				System.out.println("Please try again.\n");
			}
		}
	}
	
	public static void registerMenu(Scanner sc, UserService us) {
		
		System.out.println("\n[REGISTER PAGE]");
		
		System.out.print("Create your name: ");
		String name = sc.nextLine().trim();
		
		System.out.print("Enter your email: ");
		String email = sc.nextLine().trim();
		
		System.out.print("Create your password: ");
		String password = sc.nextLine().trim();
		
		String userId = "USER" + System.currentTimeMillis();
		
		try {
			Passenger passenger = new Passenger(userId, name, email, password, UserRole.PASSENGER, 0);
			us.registerUser(passenger);
						
			System.out.println("[SUCCESS]: Register successfully!");
		} catch (IllegalArgumentException e) {
			System.out.print("Register Failed!\n" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Register Failed!\n" + e.getMessage());
		}
	}
}
