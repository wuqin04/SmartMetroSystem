package ui;

import java.util.Scanner;

import model.Admin;
import service.RouteService;

public class AdminUI {
	private Scanner sc;
	private RouteService rs;
	
	public AdminUI(Scanner sc, RouteService rs) {
		this.sc = sc;
		this.rs = rs;
	}
	
	public void loadDashboard(Admin admin) {
		boolean loggedIn = true;
		
		while (loggedIn) {
			System.out.printf("\n[%s's ADMIN MENU]\n", admin.getName());
			
			System.out.println("(1) Stations & Routes");
	        System.out.println("(2) Trains");
	        System.out.println("(3) User Accounts");
	        System.out.println("(4) Reports");
	        System.out.println("(0) Logout");
            System.out.println("(99) Exit Program");
            
            System.out.print("Enter your choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch(choice) {
            case 1:
            	stationsAndRoutesMenu();
            	break;
            case 2:
            	// call Train method
            	break;
            case 3:
            	// call User acc method
            	break;
            case 4:
            	// call Report method
            	break;
            case 0:
            	System.out.println("Logging out... Returning to main menu.");
            	loggedIn = false;
            	break;
            case 99:
            	System.out.println("Exiting program...");
            	System.exit(0);
            default:
            	System.out.println("[ERROR]: Invalid choice. Please try again.");
            	break;
            }
		}
	}
	
	private void stationsAndRoutesMenu() {
	    boolean back = false;
	    
	    while (!back) {
	        System.out.println("\n[STATIONS & ROUTES MENU]");
	        System.out.println("(1) Manage Stations");
	        System.out.println("(2) Manage Routes");
	        System.out.println("(0) Back to Main Dashboard");
	        System.out.print("Enter your choice: ");
	        
	        String choice = sc.nextLine();
	        
	        switch (choice) {
	            case "1":
	                stationsMenu();
	                break;
	                
	            case "2":
	            	routesMenu();
	                break;
	                
	            case "0":
	                back = true; 
	                break;
	                
	            default:
	                System.out.println("[ERROR]: Invalid input. Try again with 0-2.");
	                break;
	        }
	    }
	}
	
	private void stationsMenu() {
		
	}
	
	private void routesMenu() {
		boolean back = false;
		
		while (!back) {
			System.out.println("\n[ROUTES MANAGEMENT]");
			
			System.out.println("(1) View Routes");
			System.out.println("(2) Add Routes");
			System.out.println("(0) Back to Main Dashboard");
	        System.out.print("Enter your choice: ");
	        
	        String choice = sc.nextLine();
	        
	        switch (choice) {
	        case "1":
	        	// view routes method
	        	rs.displayAllRoutes();
	        	break;
	        	
	        case "2":
	        	// add routes method
	        	rs.addRoute(Route);
	        	break;
	        	
	        case "0":
	        	back = true;
	        	break;
	        	
	    	default:
	    		System.out.println("[ERROR]: Invalid input, please enter 0-2.");
	    		break;
	        }
		}		
	}
}
