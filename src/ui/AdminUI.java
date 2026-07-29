 package ui;

import java.util.ArrayList;
import java.util.Scanner;

import model.Admin;
import model.Route;
import model.Station;
import service.RouteService;
import service.StationService;

public class AdminUI {
	private Scanner sc;
	private RouteService rs;
	private StationService ss;
	private ArrayList<Route> routes;
	
	public AdminUI(Scanner sc, RouteService rs, StationService ss, ArrayList<Route> routes) {
		this.sc = sc;
		this.rs = rs;
		this.ss = ss;
		this.routes = routes;
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
	        	System.out.println("\n[ADD NEW ROUTE]");
	        	
	        	try {
	        		ArrayList<Station> allStations = ss.getAllStations();
	        		
	        		if (allStations == null || allStations.isEmpty()) {
	                    System.out.println("[ERROR]: No stations available in the system.");
	                    System.out.println("[INFO]: Please add stations first before creating routes.");
	                    return; // Immediately exit back to the menu
	                }
	        		
	        		System.out.println("Available Stations:");
	                for (int i = 0; i < allStations.size(); i++) {
	                    Station s = allStations.get(i);
	                    System.out.printf("(%d) %s [%s]\n", (i + 1), s.getName(), s.getStationId());
	                }
	                
	        		int routeNum = routes.size() + 1;
	        		String routeId = String.format("R%03d", routeNum);
	        		
	        		System.out.print("Select Source Station (Enter number 1-" + allStations.size() + "): ");
	                int sourceChoice = Integer.parseInt(sc.nextLine().trim());
	                
	                if (sourceChoice < 1 || sourceChoice > allStations.size()) {
	                    System.out.println("[ERROR]: Invalid selection. Please enter a valid number from the list.");
	                    return;
	                }
	                
	                Station source = allStations.get(sourceChoice - 1);
	                
	                System.out.print("Select Destination Station (Enter number 1-" + allStations.size() + "): ");
	                int destChoice = Integer.parseInt(sc.nextLine().trim());
	                
	                if (destChoice < 1 || destChoice > allStations.size()) {
	                    System.out.println("[ERROR]: Invalid selection. Please enter a valid number from the list.");
	                    return;
	                }
	                Station destination = allStations.get(destChoice - 1);
	                
	                if (source.equals(destination)) {
	                    System.out.println("[ERROR]: Source and Destination stations cannot be the same.");
	                    return;
	                }
	                
	                System.out.print("Enter Route Distance in km (e.g., 5.5): ");
	                String distanceInput = sc.nextLine().trim();
	                double distance = Double.parseDouble(distanceInput);
	                
	                Route newRoute = new Route(routeId, source, destination, distance);
	                
	                rs.addRoute(newRoute);
	                System.out.println("[SUCCESS]: Route " + routeId + " added and saved to routes.json successfully!");
	                
	        	} catch (NumberFormatException e) {
	        		System.out.println("[ERROR]: Invalid distance format. Please enter a valid number.");
	        	} catch (IllegalArgumentException | IllegalStateException e) {
	                System.out.println(e.getMessage());
	            } catch (Exception e) {
	                System.out.println("[ERROR]: An unexpected error occurred: " + e.getMessage());
	            }
	        	
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