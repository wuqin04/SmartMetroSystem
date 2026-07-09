package model;

public class Ticket {
	
	private String ticketID;
	private String passenger;
	private String source;
	private String destination;
	private String ticketType;
	private String status;
	private double fare;
	
	public String getTicketID()
	{
		return ticketID;
	}
	
	public String getPassenger()
	{
		return passenger;
	}
	
	public String getSourse()
	{
		return source;
	}
	
	public String getDestination()
	{
		return destination;
	}
	
	public String getTicketType()
	{                               
		return ticketType;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public double getFare()
	{
		return fare;
	}
	
	public Ticket(String aTicketID, String aPassenger,String aSource, 
				  String aDestination, String aTicketType, String aStatus, double aFare)
	{
		ticketID = aTicketID;
		passenger = aPassenger;
		source = aSource;
		destination = aDestination;
		ticketType = aTicketType;
		status = aStatus;
		fare = aFare;
	}

}

