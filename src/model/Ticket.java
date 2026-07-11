package model;
import enums.TicketStatus;
import enums.TicketType;

public class Ticket {
	
	private String ticketId;
	private Passenger passenger;
	private Station source;
	private Station destination;
	private TicketType ticketType;
	private TicketStatus ticketStatus;
	private double fare;
	
	
	public static void print(String text)
	{
		System.out.println(text);
	}
	
	public String getTicketId()
	{
		return ticketId;
	}
	
	public Passenger getPassenger()
	{
		return passenger;
	}
	
	public Station getSource()
	{
		return source;
	}
	
	public Station getDestination()
	{
		return destination;
	}
	
	public TicketType getTicketType()
	{                               
		return ticketType;
	}
	
	public TicketStatus getStatus()
	{
		return ticketStatus;
	}
	
	public void setStatus(TicketStatus aStatus)
	{
		ticketStatus = aStatus;
	}
	
	public double getFare()
	{
		return fare;
	}
	
	public Ticket(String aTicketId, Passenger aPassenger,Station aSource, 
				  Station aDestination, TicketType aTicketType, double aFare)
	{
		ticketId = aTicketId;
		passenger = aPassenger;
		source = aSource;
		destination = aDestination;
		ticketType = aTicketType;
		fare = aFare;
		//ticketStatus = TicketStatus.ACTIVE; 
		
		
	}
	
	// Function for cancel ticket
	public void cancelTicket()
	{
		//ticketStatus = TicketStatus.CANCELLED;
	}
	
	// Print the ticket detail
	public void printTicket()
	{
		print("Ticket ID: " + ticketId);
	//  print("Passenger name: " + passenger.getName());
	//	print("Source station name: " + source.getName());
	//	print("Destination station name: " + destination.getName());
		print("Ticket type: " + ticketType);
		print("Fare: RM " + fare);
		print("Ticket status: " + ticketStatus);
		
	}
	
	// Function for used ticket
	public void useTicket()
	{
		//ticketStatus = TicketStatus.USED;
	}

}

