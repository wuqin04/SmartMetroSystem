package service;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import enums.TicketType;
import enums.TicketStatus;
import enums.UserRole;
import repository.JSONFileManager;
import fare.FareCalculator;
import model.Ticket;
import model.Passenger;
import model.Route;
import model.Station;
import model.User;
import exception.FileProcessingException;
import exception.TicketNotFoundException;

public class TicketService {

	private ArrayList<Ticket> tickets;
	private FareCalculator fareCalculator;
	private JSONFileManager fileManager;
	private String fileName;
	private UserService userService;
	private StationService stationService;
	
	public TicketService(ArrayList<Ticket> tickets, JSONFileManager fileManager, FareCalculator fareCalculator, String fileName, UserService userService,StationService stationService){
		
		if(tickets == null) {
			throw new IllegalArgumentException("[ERROR]: Tickets cannot be null or blank.");
		}
		
		if(fareCalculator == null) {
			throw new IllegalArgumentException("[ERROR]: Fare calculator cannot be null or blank.");
		}
		
		if(fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: File name cannot be null or blank.");
		}
		
		if(userService == null) {
			throw new IllegalArgumentException("[ERROR]: User service cannot be null or blank.");
		}
		
		if(fileManager == null) {
			throw new IllegalArgumentException("[ERROR]: Json file cannot be null or blank.");
		}
		
		if(stationService == null) {
			throw new IllegalArgumentException("[ERROR]: Station service cannot be null or blank.");
		}
		
		this.tickets = tickets;
		this.fareCalculator =  fareCalculator;
		this.fileName = fileName.trim();
		this.userService = userService;
		this.fileManager = fileManager;
		this.stationService = stationService;
		
		loadTickets();
	}
	
	// Buy ticket function
	public Ticket buyTicket(Passenger passenger, Route route, TicketType type) throws FileProcessingException{
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
		String generatedId = String.format("T%03d", ticketNumber);
		
		Station source = route.getSource();
		Station destination = route.getDestination();

		// fare calculator not create yet.
		double ticketFare = fareCalculator.calculateFare(route, type);
				
		// Check passenger balance
		if(passenger.getBalance() < ticketFare) {
			throw new IllegalArgumentException("[ERROR]: Passenger has insufficient balance.");
		}
		
		// Create ticket 
		Ticket ticket = new Ticket(generatedId, passenger, source, destination, type, ticketFare);
		passenger.buyTicket(ticket);
		
		// Add the ticket into the ArrayList
		tickets.add(ticket);
		try {
			saveTickets();
		}catch(FileProcessingException e){
			tickets.remove(ticket);
			throw e;
		}
		
		System.out.printf("[SUCCESS]: Ticket purchase successful! Remaining Balance: RM%.2f\n", passenger.getBalance());
		
		return ticket;
	}
	
	public void cancelTicket(String ticketId, Passenger passenger) throws TicketNotFoundException, FileProcessingException {
		
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
					saveTickets();
					return;
				}
				throw new IllegalArgumentException("[ERROR]: Ticket does not belong to passenger" + passenger.getUserId() + ".");
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
				System.out.println("[ERROR]: Passenger has no tickets.");
			} 
			
		} else {
			throw new IllegalArgumentException("[ERROR]: Invalid user role.");
		}
	}
	
	private void saveTickets() throws FileProcessingException {

	    List<JSONObject> ticketData = new ArrayList<>();

	    for (Ticket ticket : tickets) {

	        JSONObject jsonTicket = new JSONObject();

	        jsonTicket.put("ticket Id", ticket.getTicketId());
	        jsonTicket.put("passenger Id",ticket.getPassenger().getUserId());
	        jsonTicket.put("source Id",ticket.getSource().getStationId());
	        jsonTicket.put("destination Id",ticket.getDestination().getStationId());
	        jsonTicket.put("ticket type",ticket.getTicketType().name());
	        jsonTicket.put("ticket status",ticket.getStatus().name());
	        jsonTicket.put("fare", ticket.getFare());

	        ticketData.add(jsonTicket);
	    }

	    fileManager.saveData(ticketData, fileName);
	}
	
	private void loadTickets(){
		Object loadedObject = fileManager.loadData(fileName);

			if (loadedObject == null) {
				return;
			}

			if (!(loadedObject instanceof List<?>)) {
				throw new IllegalStateException("[ERROR]: Invalid ticket file format.");
			}

			List<?> loadedData = (List<?>) loadedObject;

			tickets.clear();

			for (Object obj : loadedData) {

				JSONObject jsonTicket;

				if (obj instanceof JSONObject) {
					jsonTicket = (JSONObject) obj;
				} else {
					jsonTicket = new JSONObject(obj);
				}

				String ticketId = jsonTicket.getString("ticket Id");

				String passengerId = jsonTicket.getString("passenger Id");

				String sourceId = jsonTicket.getString("source Id");

				String destinationId = jsonTicket.getString("destination Id");

				TicketType ticketType = TicketType.valueOf(jsonTicket.getString("ticket type"));

				TicketStatus status = TicketStatus.valueOf(jsonTicket.getString("ticket status"));

				double fare = jsonTicket.getDouble("fare");

				Passenger passenger = userService.findPassengerById(passengerId);

				Station source = stationService.findStationById(sourceId);

				Station destination = stationService.findStationById(destinationId);

				Ticket ticket = new Ticket(ticketId, passenger, source, destination, ticketType, fare);

				if (status == TicketStatus.CANCELLED) {
					ticket.cancelTicket();

				} else if (status == TicketStatus.USED) {
					ticket.useTicket();
				}
				tickets.add(ticket);
			}
	}
}