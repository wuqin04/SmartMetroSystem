package service;
import java.util.ArrayList;
import enums.TicketType;
import fare.FareCalculator;
import fare.StandardFareCalculator;
import model.Ticket;
import model.Passenger;
import model.Route;
import model.Station;
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
		
		return ticket;
	}
	
	public void cancelTicket(String ticketId) throws TicketNotFoundException {
		
		if(ticketId == null || ticketId.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Ticket ID cannot be null or empty.");
		}
		
		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			
			if(ticket.getTicketId().equalsIgnoreCase(ticketId.trim())) {
				 ticket.cancelTicket();
				 return;
			}
			throw new TicketNotFoundException("[ERROR]: Ticket ID not found.");
		}
	}
	
	public void viewTicket() {
		
		if(tickets.isEmpty()) {
			System.out.println("[ERROR]: No tickets found.");
			return;
		}
		
		for(int i = 0; i < tickets.size(); i++) {
			System.out.println("-------------------------------\n");
			tickets.get(i).printTicket();
		}
	}
}
