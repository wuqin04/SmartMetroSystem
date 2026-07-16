package model;
import enums.TicketStatus;
import enums.TicketType;

public class Ticket {
	
	public String ticketId;
	private Passenger passenger;
	private Station source;
	private Station destination;
	private TicketType ticketType;
	private TicketStatus ticketStatus;
	private double fare;
	
	public String getTicketId(){
		return ticketId;
	}
	
	public Passenger getPassenger(){
		return passenger;
	}
	
	public Station getSource(){
		return source;
	}
	
	public Station getDestination(){
		return destination;
	}
	
	public TicketType getTicketType(){                               
		return ticketType;
	}
	
	public TicketStatus getStatus(){
		return ticketStatus;
	}
	
	public double getFare(){
		return fare;
	}
	
	// Update the ticket status, an active ticket can only be changed to USED or CANCELLED
	public void setStatus(TicketStatus status){
		
		if(status == null) {
			throw new IllegalArgumentException("[ERROR]: Ticket status cannot be null.");
		}

		if(ticketStatus == TicketStatus.ACTIVE){
			if(status == TicketStatus.USED || status == TicketStatus.CANCELLED) {
				ticketStatus = status;
			} else {
				throw new IllegalArgumentException("[ERROR]: Ticket status can only become used or cancelled.");
			}
			
			if(ticketStatus == TicketStatus.USED) {
				throw new IllegalArgumentException("[ERROR]: Ticket status had been used.");
			}
			

			if(ticketStatus == TicketStatus.CANCELLED) {
				throw new IllegalArgumentException("[ERROR]: Ticket status had been cancelled.");
			}
		}
			
	}
	
	// Constructor for ticket, and validation for every information
	public Ticket(String ticketId, Passenger passenger,Station source, 
				  Station destination, TicketType ticketType, double fare){
		
		if(ticketId == null || ticketId.trim().isEmpty()){
			throw new IllegalArgumentException("[ERROR]: Ticket ID cannot be null or blank.");
			
		}
		
		if(passenger == null || ticketId.trim().isEmpty()){
			throw new IllegalArgumentException("[ERROR]: Passenger cannot be null.");
		}
		
		if(source == null || ticketId.trim().isEmpty()){ // Need compare Station Id when Station.java is build.
			throw new IllegalArgumentException("[ERROR]: Source station cannot be null.");
		}

		if(destination == null || ticketId.trim().isEmpty()){ // Need compare Station Id when Station.java is build.
			throw new IllegalArgumentException("[ERROR]: Destination station cannot be null.");
		}
		
		if(ticketType == null || ticketId.trim().isEmpty()){
			throw new IllegalArgumentException("[ERROR]: Ticket type cannot be null.");	
		}
		
		if(fare <= 0){
			throw new IllegalArgumentException("[ERROR]: Fare must be greater than RM0.00.");	
		}
		
		this.ticketId = ticketId;
		this.passenger = passenger;
		this.source = source;
		this.destination = destination;
		this.ticketType = ticketType;
		this.fare = fare;
		
		ticketStatus = TicketStatus.ACTIVE; 
	}
	
	// Print the ticket detail
	public void printTicket(){
		System.out.println("Ticket ID: " + ticketId);
		System.out.println("Passenger name: " + passenger.getName());
	//	System.out.println("Source station name: " + source.getName());
	//	System.out.println("Destination station name: " + destination.getName());
		System.out.println("Ticket type: " + ticketType);
		System.out.printf("Fare: RM %.2f%n", fare);
		System.out.println("Ticket status: " + ticketStatus);
		
	}
	
	// Function for cancel ticket
	public void cancelTicket(){
		setStatus(TicketStatus.CANCELLED);
	}
	
	// Function for used ticket
	public void useTicket(){
		setStatus(TicketStatus.USED);
	}

}