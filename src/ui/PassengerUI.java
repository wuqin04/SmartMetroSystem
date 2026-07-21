package ui;

import java.util.Scanner;

import enums.TicketType;
import model.Passenger;
import model.Station;
import model.Ticket;
import payment.Payment;
import payment.CardPayment;
import payment.CashPayment;
import service.TicketService;

public class PassengerUI {
	private Scanner sc;
	private TicketService ts;
	
	public PassengerUI(Scanner sc, TicketService ts) {
		this.sc = sc;
		this.ts = ts;
	}
	
	public void loadDashboard(Passenger passenger) {
		boolean loggedIn = true;
		
		while (loggedIn) {
			System.out.printf("\n[%s's PASSENGER MENU]\n", passenger.getName());
			
            System.out.println("(1) Profile Menu");
            System.out.println("(2) Ticket Menu");
            System.out.println("(0) Log Out");
            System.out.println("(99) Exit Program");
            System.out.print("Enter your choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch(choice) {
            case 1:
            	profileMenu(passenger);
            	break;
            case 2:
            	ticketMenu(passenger);
            	break;
            case 0:
            	System.out.println("Logging out...");
            	loggedIn = false;
            	break;
            case 99:
            	System.out.println("Exiting program...");
            	System.exit(0);
            default:
            	System.out.println("[ERROR]: Invalid choice.");
            	break;
            }
		}
	}
	
	private void profileMenu(Passenger passenger) {
		boolean back = false;
		
		while (!back) {
			System.out.println("\n[PROFILE MANAGEMENT]");
			
            System.out.println("(1) View Profile");
            System.out.println("(2) Top Up Balance");
            System.out.println("(0) Back to Dashboard");
            System.out.print("Enter your choice: ");
            
            if (!sc.hasNextInt()) {
				System.out.print("[ERROR]: That is not a valid number. Try again.\n");
				sc.next();
				continue;
			}
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
            case 1:
                passenger.viewProfile();
                break;
            case 2:
            	double amount = 0;
				while (amount != -1) {
					System.out.print("Enter amount to top up (-1 back to menu): RM");
					
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
						System.out.printf("[SUCCESS]: Successfully added RM%.2f to your balance.\n", amount);
						break;
					} catch (IllegalArgumentException e) {
						System.out.println("Top-up Failed!\n" + e.getMessage());
					}
				}
				break;
            case 0:
                back = true; 
                break;
            default:
                System.out.println("[ERROR]: Invalid choice. Enter 0-2 only");
            }
		}
	}
	
	private void ticketMenu(Passenger passenger) {
		boolean back = false;
		
		while (!back) {
			System.out.println("\n[TICKET MANAGEMENT]");
			
			System.out.printf("Account Balance: RM%.2f\n", passenger.getBalance());
            System.out.println("(1) View Ticket");
            System.out.println("(2) Buy Ticket");
            System.out.println("(3) Cancel Ticket");
            System.out.println("(0) Back to Dashboard");
            System.out.print("Enter your choice: ");
            
            if (!sc.hasNextInt()) {
				System.out.print("[ERROR]: That is not a valid number. Try again.\n");
				sc.next();
				continue;
			}
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
            case 1:
            	// call view ticket method
            	viewTicketAction(passenger);
            	break;
            	
            case 2:
            	// call buy ticket method
            	buyTicketAction(passenger);
            	break;
            	
            case 3:
            	// call cancel ticket method
            	cancelTicketAction(passenger);
            	break;
            	
            case 0:
            	back = true;
            	break;
            	
            default:
            	System.out.println("[ERROR]: Invalid choice. Enter 0-3 only.");
            }
		}
	}
	
	private void viewTicketAction(Passenger passenger) {
		try {
			ts.viewTickets(passenger);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void buyTicketAction(Passenger passenger) {
		while (true) {
			System.out.println("\n[BUY TICKET]");
		    
		    System.out.println("Choose your ticket type:");
		    System.out.println("(1) Single Trip (Calculated by distance)");
		    System.out.println("(2) Daily Pass (RM15.00)");
		    System.out.println("(3) Monthly Pass (RM50.00)");
		    System.out.println("(0) Back to Ticket Menu");
		    System.out.print("Enter your choice: ");
		    
		    if (!sc.hasNextInt()) {
		        System.out.println("[ERROR]: Please enter a valid number.");
		        sc.next();
		        continue; 
		    }
		    
		    int ticketChoice = sc.nextInt();
		    sc.nextLine();
		    
		    if (ticketChoice == 0) {
		    	return;
		    }
		    
		    // Variables to hold the details before we create the ticket
		    TicketType selectedType = null;
		    Station source = null;
		    Station destination = null;
		    Route route = null;
		    double fare = 0.0;
		    
		    switch (ticketChoice) {
		        case 1:
		            selectedType = TicketType.SINGLE;
		            
		            // TODO: The source and destination are created by admin, user cannot choose any by their own
		            System.out.print("Enter Source Station: ");
		            String srcInput = sc.nextLine();
		            
		            System.out.print("Enter Destination Station: ");
		            String destInput = sc.nextLine();
		            
		            // TODO: need Route and RouteService
		            route = rs.findRoute(source, destination);
		            
		            // TODO: Get real station object from stationService
		            
		            // TODO: Use StandardFareCalculator here later
		            // current fare is just assumption without StandardFareCalculator
		            fare = 5.00; 
		            break;
		            
		        case 2:
		            selectedType = TicketType.DAILY;
		            fare = 15.00; // Flat rate
		            break;
		            
		        case 3:
		            selectedType = TicketType.MONTHLY;
		            fare = 50.00; // Flat rate
		            break;
		            
		        default:
		            System.out.println("[ERROR]: Invalid ticket type.");
		            continue;
		    }
		    
		    // If a valid type was selected, proceed to checkout
		    if (selectedType != null) {
		    	System.out.println("\n[ORDER SUMMARY]");
		        System.out.println("Ticket Type : " + selectedType);
		        if (selectedType == TicketType.SINGLE) {
		            System.out.println("Route     : " + source.getName() + " to " + destination.getName());
		        }
		        System.out.println("Total Fare: RM " + fare);
		        System.out.print("Confirm purchase? (Y/N): ");
		        
		        String confirm = sc.nextLine();
		        
		        while (true) {
		        	if (confirm.equalsIgnoreCase("Y")) {
			            System.out.println("\n[PAYMENT METHOD]");
			            System.out.println("1. Cash");
			            System.out.println("2. Card");
			            System.out.print("Enter your choice: ");
			            
			            if (!sc.hasNextInt()) {
			                System.out.println("[FAILED]: Invalid payment option selected. Retrying payment.");
							sc.next();
							continue;
						}
			            
			            int payOption = sc.nextInt();
			            
			            Payment paymentMethod = null;
			            
			            switch (payOption) {
			            case 1:
			            	paymentMethod = new CashPayment();
			            	break;
			            case 2:
			            	System.out.println("Enter Card Number: ");
			            	String cardNum = sc.nextLine();
			            	paymentMethod = new CardPayment(cardNum);
			            	break;
			            default:
			                System.out.println("[FAILED]: Invalid payment option selected. Retrying payment.");
			                continue;
			            }
			            
			            try {
			                if (paymentMethod.pay(fare)) {
			                    String ticketId = "T" + System.currentTimeMillis();
			                    ts.buyTicket(passenger, route, selectedType);
			                }
			            } catch (IllegalArgumentException e) {
			                System.out.println(e.getMessage()); 
			            }
			            
			        } else {
			            System.out.println("Purchase cancelled. Returning to menu...");
			        }
			    }
	        }   
		}
		
	}
	
	private void cancelTicketAction(Passenger passenger) {
		try {
			ts.cancelTicket(ticketId, passenger);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
}
