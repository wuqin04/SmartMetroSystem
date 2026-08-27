 package ui;

import java.util.ArrayList;
import java.util.Scanner;

import enums.DiscountType;
import model.Admin;
import model.Route;
import model.Station;
import model.Train;
import model.User;
import service.ReportService;
import service.RouteService;
import service.StationService;
import service.TrainService;
import service.UserService;

public class AdminUI {
	private Scanner sc;
	private UserService userService;
	private RouteService routeService;
	private StationService stationService;
	private TrainService trainService;
	private ArrayList<Route> routes;
	
	public AdminUI(Scanner sc, UserService userService, RouteService routeService, StationService stationService, TrainService trainService, 
			ArrayList<Route> routes) {
		this.sc = sc;
		this.userService = userService;
		this.routeService = routeService;
		this.stationService = stationService;
		this.trainService = trainService;
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
            	stationsAndRoutesMenu(admin);
            	break;
            case 2:
            	trainMenu(admin);
            	break;
            case 3:
            	userAccountsMenu(admin);
            	break;
            case 4:
            	reportMenu(admin);
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
	
	private void stationsAndRoutesMenu(Admin admin) {
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
	                stationsMenu(admin);
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
	
	private void stationsMenu(Admin admin) {
		boolean back = false;
		while (!back) {
			System.out.println("\n[STATIONS MANAGEMENT]");
			
			System.out.println("(1) View Stations");
			System.out.println("(2) Add Stations");
			System.out.println("(3) Edit Station");
			System.out.println("(0) Back to Main Dashboard");
			System.out.print("Enter your choice: ");
			
			String choice = sc.nextLine();
			
			switch (choice) {
			case "1":
                boolean viewBack = false;
                while (!viewBack) {
                    System.out.println("\n[VIEW STATIONS]");
                    System.out.println("(1) View All Stations");
                    System.out.println("(2) Search Station by Name");
                    System.out.println("(0) Back");
                    System.out.print("Enter your choice: ");
                    
                    String viewChoice = sc.nextLine().trim();
                    
                    switch (viewChoice) {
                        case "1":
                            stationService.viewStations();
                            break;
                            
                        case "2":
                            System.out.print("\nEnter Station Name to search: ");
                            String searchName = sc.nextLine().trim();
                            
                            try {
                                Station foundStation = stationService.searchStation(searchName);
                                
                                if (foundStation != null) {
                                    System.out.println("\n[STATION FOUND]");
                                    foundStation.displayInfo();
                                } else {
                                    System.out.println("[INFO]: No station found with the name '" + searchName + "'.");
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                            
                        case "0":
                            viewBack = true;
                            break;
                            
                        default:
                            System.out.println("[ERROR]: Invalid input, please enter 0-2.");
                            break;
                    }
                }
                break;
				
			case "2":
				System.out.println("\n[ADD NEW STATION]");
                
                System.out.print("Enter Station Name (Must start with a capital letter): ");
                String name = sc.nextLine().trim();
                
                System.out.print("Enter Station Location: ");
                String location = sc.nextLine().trim();
                
                try {
                    Station newStation = new Station("TEMP", name, location);                    
                    stationService.addStation(newStation);
                    
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
                
			case "3":
				System.out.println("\n[EDIT STATION]");
                System.out.print("Enter Station ID to edit (e.g., STN001): ");
                String editId = sc.nextLine().trim();
                
                try {
                    Station stationToEdit = stationService.findStationById(editId);
                    
                    System.out.println("\n[CURRENT DETAILS]");
                    stationToEdit.displayInfo();
                    
                    System.out.println("\n[INFO]: Leave blank and press Enter to keep current value.");
                    
                    System.out.print("Enter New Name (Must start with a capital letter): ");
                    String newName = sc.nextLine();
                    
                    System.out.print("Enter New Location: ");
                    String newLocation = sc.nextLine();
                    
                    stationService.editStation(editId, newName, newLocation);
                    
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println(e.getMessage());
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
	
	private void routesMenu() {
		boolean back = false;
		
		while (!back) {
			System.out.println("\n[ROUTES MANAGEMENT]");
			
			System.out.println("(1) View Routes");
			System.out.println("(2) Add Route");
			System.out.println("(3) Edit Route");
			System.out.println("(0) Back to Main Dashboard");
	        System.out.print("Enter your choice: ");
	        
	        String choice = sc.nextLine();
	        
	        switch (choice) {
	        case "1":
	        	routeService.displayAllRoutes();
	        	break;
	        	
	        case "2":
	        	System.out.println("\n[ADD NEW ROUTE]");
	        	
	        	try {
	        		ArrayList<Station> allStations = stationService.getAllStations();
	        		
	        		if (allStations == null || allStations.isEmpty()) {
	                    System.out.println("[ERROR]: No stations available in the system.");
	                    System.out.println("[INFO]: Please add stations first before creating routes.");
	                    break; 
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
	                    break;
	                }
	                
	                Station source = allStations.get(sourceChoice - 1);
	                
	                System.out.print("Select Destination Station (Enter number 1-" + allStations.size() + "): ");
	                int destChoice = Integer.parseInt(sc.nextLine().trim());
	                
	                if (destChoice < 1 || destChoice > allStations.size()) {
	                    System.out.println("[ERROR]: Invalid selection. Please enter a valid number from the list.");
	                    break;
	                }
	                
	                Station destination = allStations.get(destChoice - 1);
	                
	                if (source.getStationId().equals(destination.getStationId())) {
	                    System.out.println("[ERROR]: Source and Destination stations cannot be the same.");
	                    break; 
	                }
	                
	                ArrayList<Route> existingRoutes = routeService.getAllRoutes(); 
	                boolean hasError = false;

	                for (Route r : existingRoutes) {
	                    String existingSourceId = r.getSource().getStationId();
	                    String existingDestId = r.getDestination().getStationId();
	                    
	                    // Rule A: Prevent duplicate routes (Checking both A->B and B->A)
	                    if ((existingSourceId.equals(source.getStationId()) && existingDestId.equals(destination.getStationId())) ||
	                        (existingSourceId.equals(destination.getStationId()) && existingDestId.equals(source.getStationId()))) {
	                        System.out.println("[ERROR]: A route between these two stations already exists.");
	                        hasError = true;
	                        break;
	                    }
	                    
	                    // Rule B: Enforce single outgoing connection (Prevents branching lines)
	                    if (existingSourceId.equals(source.getStationId())) {
	                        System.out.println("[ERROR]: Station '" + source.getName() + "' already connects outward to '" + r.getDestination().getName() + "'.");
	                        hasError = true;
	                        break;
	                    }
	                    
	                    // Rule C: Enforce single incoming connection (Prevents merging lines)
	                    if (existingDestId.equals(destination.getStationId())) {
	                        System.out.println("[ERROR]: Station '" + destination.getName() + "' already receives a route from '" + r.getSource().getName() + "'.");
	                        hasError = true;
	                        break;
	                    }
	                }
	                
	                if (hasError) {
	                    break;
	                }
	                
	                System.out.print("Enter Route Distance in km (e.g., 5.5): ");
	                String distanceInput = sc.nextLine().trim();
	                double distance = Double.parseDouble(distanceInput);
	                
	                Route newRoute = new Route(routeId, source, destination, distance);
	                
	                routeService.addRoute(newRoute);
	                System.out.println("[SUCCESS]: Route " + routeId + " added successfully!");
	                
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
	
	private void trainMenu(Admin admin) {
		boolean back = false;
		
		while (!back) {
			System.out.println("\n[TRAINS MANAGEMENT]");
			
			System.out.println("(1) View Trains");
			System.out.println("(2) Add Train");
			System.out.println("(3) Edit Train");
			System.out.println("(0) Back to Main Dashboard");
			System.out.print("Enter your choice: ");
			
			String choice = sc.nextLine().trim();
			
			switch (choice) {
			case "1":
				trainService.viewTrains();
				break;
				
			case "2":
				System.out.println("\n[ADD NEW TRAIN]");
				
				System.out.print("Enter Train Name/Model: ");
				String name = sc.nextLine().trim();
				
				try {
					System.out.print("Enter Train Capacity (Number of seats): ");
					int capacity = Integer.parseInt(sc.nextLine().trim());
					
					String generatedId = trainService.generateTrainId();
					
					Train newTrain = new Train(generatedId, name, capacity);
					
					trainService.addTrain(newTrain);
					
				} catch (NumberFormatException e) {
					System.out.println("[ERROR]: Invalid capacity format. Please enter a valid number.");
				} catch (IllegalArgumentException | IllegalStateException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					System.out.println("[ERROR]: An unexpected error occurred: " + e.getMessage());
				}
				break;
				
			case "3":
				System.out.println("\n[EDIT TRAIN]");
				System.out.print("Enter Train ID to edit (e.g., TRN001): ");
				String editId = sc.nextLine().trim();
				
				try {			
					System.out.print("Enter New Name/Model: ");
					String newName = sc.nextLine().trim();
					
					System.out.print("Enter New Capacity: ");
					int newCapacity = Integer.parseInt(sc.nextLine().trim());
					
					Train updatedTrain = new Train(editId, newName, newCapacity);
					
					trainService.updateTrain(updatedTrain);
					
				} catch (NumberFormatException e) {
					System.out.println("[ERROR]: Invalid capacity format. Please enter a valid number.");
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
				System.out.println("[ERROR]: Invalid input, please enter 0-3.");
				break;
			}
		}
	}
	
	private void reportMenu(Admin admin) {
		boolean back = false;
		
		while (!back) {
			System.out.println("\n=================================");
			System.out.println("          [REPORTS MENU]         ");
			System.out.println("=================================");
			System.out.println("(1) View Total Sales Report");
			System.out.println("(2) View Total Revenue Report");
			System.out.println("(3) View Cancelled Tickets Report");
			System.out.println("(0) Back to Main Dashboard");
			System.out.println("=================================");
			System.out.print("Enter your choice: ");
			
			String choice = sc.nextLine().trim();
			
			switch (choice) {
			case "1":
				System.out.println("\n--------------------------------------------------");
				System.out.println("               TOTAL SALES REPORT                 ");
				System.out.println("--------------------------------------------------");
				admin.viewTotalSales();
				System.out.println("--------------------------------------------------");
				break;
				
			case "2":
				System.out.println("\n--------------------------------------------------");
				System.out.println("              TOTAL REVENUE REPORT                ");
				System.out.println("--------------------------------------------------");
				admin.viewTotalRevenue();
				System.out.println("--------------------------------------------------");
				break;
				
			case "3":
				System.out.println("\n--------------------------------------------------");
				System.out.println("             CANCELLED TICKETS REPORT             ");
				System.out.println("--------------------------------------------------");
				admin.viewCancelledTickets();
				System.out.println("--------------------------------------------------");
				break;
				
			case "0":
				back = true;
				break;
				
			default:
				System.out.println("[ERROR]: Invalid input. Please enter 0-3.");
				break;
			}
		}
	}
	
	private void userAccountsMenu(Admin admin) {
	    boolean back = false;
	    
	    while (!back) {
	        System.out.println("\n[USER ACCOUNTS MANAGEMENT]");
	        System.out.println("(1) View All Users");
	        System.out.println("(2) Search User by Email");
	        System.out.println("(3) Manage User Account (Suspend/Concession)");
	        System.out.println("(0) Back to Main Dashboard");
	        System.out.print("Enter your choice: ");
	        
	        String choice = sc.nextLine().trim();
	        
	        switch (choice) {
	            case "1":
	                userService.viewAllUsers();
	                break;
	            case "2":
	                searchUserUI();
	                break;
	            case "3":
	                manageUserUI();
	                break;
	            case "0":
	                back = true; 
	                break;
	            default:
	                System.out.println("[ERROR]: Invalid input. Try again with 0-3.");
	                break;
	        }
	    }
	}

	private void searchUserUI() {
	    System.out.println("\n[SEARCH USER]");
	    System.out.print("Enter User Email: ");
	    String email = sc.nextLine().trim();
	    
	    User foundUser = userService.searchUserByEmail(email);
	    
	    if (foundUser != null) {
	        System.out.println("\n[USER FOUND]");
	        foundUser.viewProfile(); 
	    } else {
	        System.out.println("[INFO]: No user found with the email '" + email + "'.");
	    }
	}

	private void manageUserUI() {
	    System.out.println("\n[MANAGE USER ACCOUNT]");
	    System.out.print("Enter User Email to manage: ");
	    String email = sc.nextLine().trim();
	    
	    User targetUser = userService.searchUserByEmail(email);
	    
	    if (targetUser == null) {
	        System.out.println("[ERROR]: User not found.");
	        return;
	    }
	    
	    boolean currentStatus = targetUser.isSuspended();
	    userService.setUserSuspension(email, !currentStatus);
	    
	    System.out.println("[SUCCESS]: Account " + targetUser.getEmail() + 
	                       " is now " + (!currentStatus ? "SUSPENDED" : "ACTIVE"));
	}
}