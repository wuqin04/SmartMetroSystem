package service;
import java.util.ArrayList;
import enums.TicketType;
import enums.UserRole;
import fare.FareCalculator;
import fare.StandardFareCalculator;
import model.Ticket;
import model.Passenger;
import model.Route;
import model.Station;
import model.User;
import exception.TicketNotFoundException;


public class TicketService {

	private ArrayList<Ticket> tickets;
	private FareCalculator fareCalculator;
	
	public TicketService(ArrayList<Ticket> tickets, FareCalculator fareCalculator) {
		this.tickets = new ArrayList<Ticket>();
		this.fareCalculator= new FareCalculator();
	}
	
	// Buy ticket function
	public Ticket buyTicket(Passenger passenger, Route route, TicketType type) {
		if(passenger == null) {
			throw new IllegalArgumentException("[ERROR]: Passenger cannot be null or blank.");
		}
		
		if(route == null) {
			throw new IllegalArgumentException("[ERROR]: Route cannot be null or blank.");
		}
		
		if(type == null) {
			throw new IllegalArgumentException("[ERROR]: Ticket type cannot be null or blank.");
		}
		
		// Generate ticket ID
		int ticketNumber = tickets.size() + 1;
		String generatedId = String.format("T%3d", ticketNumber);
		
		Station source = route.getSource();
		Station destination = route.getDestination();

		// fare calculator not create yet.
		double ticketFare = fareCalculator.calculateFare(route, type);
				
		// Check passenger balance
		if(passenger.getBalance() < ticketFare) {
			throw new IllegalArgumentException("[ERROR]: Passenger has insufficient balance.");
		}
		
		// Create ticket 
		Ticket ticket = new Ticket(generatedId, passenger, route.getSource(), route.getDestination(), type, ticketFare);
		passenger.buyTicket(ticket);
		
		// Add the ticket into the ArrayList
		tickets.add(ticket);
		System.out.printf("[SUCCESS]: Ticket purchase successful! Remaining Balance: RM%.2f\n", passenger.getBalance());
		
		return ticket;
	}
	
	public void cancelTicket(String ticketId, Passenger passenger) throws TicketNotFoundException {
		
		if(ticketId == null || ticketId.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Ticket ID cannot be null or empty.");
		}
		
		if(passenger == null){
			throw new IllegalArgumentException("[ERROR]: Passenger cannot be null.");
		}
		
		String enteredId = ticketId.trim();
		
		for(int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			
			if(ticket.getTicketId().equalsIgnoreCase(enteredId)) {
				
				if(ticket.getPassenger().getUserId().equals(passenger.getUserId())) {
					ticket.cancelTicket();
					return;
				}
				throw new IllegalArgumentException("[ERROR]: Ticket does not belongs to passenger" + passenger.getUserId() + ".");
			}
		}
		throw new TicketNotFoundException("[ERROR]: Ticket ID not found.");
	}
	
	public void viewTickets(User user) {
		
		if(user == null) {
			throw new IllegalArgumentException("[ERROR]: User cannot be null.");
		}
		
		if(user.getRole() == UserRole.ADMIN) {
			
			if(tickets.isEmpty()) {
				throw new IllegalArgumentException("[ERROR]: No ticket found.");
			}
			
			for(int i = 0; i < tickets.size(); i++) {
				Ticket ticket = tickets.get(i);
				ticket.printTicket();
			}
		} else if(user.getRole() == UserRole.PASSENGER ) {
			
			boolean ticketFound = false;
	
			for(int i = 0; i < tickets.size(); i++) {
				
				Ticket ticket = tickets.get(i);
				
				if(ticket.getPassenger().getUserId().equals(user.getUserId())){
					 ticket.printTicket();
					 ticketFound = true;
				}
			}
			
			if(!ticketFound) {
				throw new IllegalArgumentException("[ERROR]: Passenger has no tickets.");
			} 
			
		} else {
			throw new IllegalArgumentException("[ERROR]: Invalid user role.");
		}
	}
}