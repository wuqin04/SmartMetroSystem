package model;
import enums.TicketStatus;
import enums.TicketType;

public class Ticket {
	
	public String ticketId;
	private Passenger passenger;
	private Station source;
	private Station destination;
	private Train train;
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
	
	public Train getTrain() {
		return train;
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

		if(this.ticketStatus == TicketStatus.USED) {
			throw new IllegalStateException("[ERROR]: Ticket has already been used.");
		}
		
		if(this.ticketStatus == TicketStatus.CANCELLED) {
			throw new IllegalStateException("[ERROR]: Ticket has already been cancelled.");
		}
		
		if (status == TicketStatus.USED || status == TicketStatus.CANCELLED) {
			this.ticketStatus = status;
		} else {
			throw new IllegalArgumentException("[ERROR]: Active tickets can only be set to USED or CANCELLED.");
		}
			
	}
	
	// Constructor for ticket, and validation for every information
	public Ticket(String ticketId, Passenger passenger, Station source, 
				  Station destination, Train train, TicketType ticketType, double fare){
		
		if(ticketId == null || ticketId.trim().isEmpty()){
			throw new IllegalArgumentException("[ERROR]: Ticket ID cannot be null or blank.");
		}
		
		if(passenger == null){ 
			throw new IllegalArgumentException("[ERROR]: Passenger cannot be null.");
		}
		
		if(ticketType == null){
			throw new IllegalArgumentException("[ERROR]: Ticket type cannot be null.");	
		}
		
		// Specific validations for Single Trip tickets
		if (ticketType == TicketType.SINGLE) {
			if(source == null){ 
				throw new IllegalArgumentException("[ERROR]: Source station cannot be null for single trips.");
			}
	
			if(destination == null){ 
				throw new IllegalArgumentException("[ERROR]: Destination station cannot be null for single trips.");
			}
			
			if(train == null) {
				throw new IllegalArgumentException("[ERROR]: Train cannot be null for single trips.");
			}
		}
		
		if(fare <= 0){
			throw new IllegalArgumentException("[ERROR]: Fare must be greater than RM0.00.");	
		}
		
		this.ticketId = ticketId;
		this.passenger = passenger;
		this.source = source;
		this.destination = destination;
		this.train = train;
		this.ticketType = ticketType;
		this.fare = fare;
		
		this.ticketStatus = TicketStatus.ACTIVE; 
	}
	
	public void printTicket(){
		System.out.println("\n--- TICKET DETAILS ---");
		System.out.println("Ticket ID: " + ticketId);
		System.out.println("Passenger name: " + passenger.getName());
		
		if (ticketType == TicketType.SINGLE && source != null && destination != null) {
			System.out.println("Source station name: " + source.getName());
			System.out.println("Destination station name: " + destination.getName());
			
			if (train != null) {
				System.out.println("Train: " + train.getTrainName());
			}
			
		} else if (ticketType == TicketType.DAILY) {
			System.out.println("Route: Daily Pass (All Stations)");
		} else if (ticketType == TicketType.MONTHLY) {
			System.out.println("Route: Monthly Pass (All Stations)");
		}
		
		System.out.println("Ticket type: " + ticketType);
		System.out.printf("Fare: RM %.2f%n", fare);
		System.out.println("Ticket status: " + ticketStatus);
		System.out.println("----------------------");
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