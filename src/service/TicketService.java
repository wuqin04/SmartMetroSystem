package service;

import java.util.ArrayList;
import enums.TicketType;
import enums.TicketStatus;
import enums.UserRole;
import enums.DiscountType;
import repository.FileManager;
import fare.FareCalculator;
import model.Ticket;
import model.Train;
import model.Passenger;
import model.Route;
import model.Station;
import model.User;
import exception.FileProcessingException;
import exception.TicketNotFoundException;
import util.JsonUtil;

public class TicketService {

	private final ArrayList<Ticket> tickets;
	private final FareCalculator fareCalculator;
	private final DiscountService discountEligibilityService;
	private final FileManager fileManager;
	private final String fileName;
	private final UserService userService;
	private final StationService stationService;
	private final TrainService trainService;
	
	public TicketService(ArrayList<Ticket> tickets, FileManager fileManager, FareCalculator fareCalculator, 
						DiscountService discountEligibilityService, String fileName, UserService userService, StationService stationService, 
						TrainService trainService) {
		
		if (tickets == null) {
			throw new IllegalArgumentException("[ERROR]: Tickets cannot be null or blank.");
		}
		
		if (fareCalculator == null) {
			throw new IllegalArgumentException("[ERROR]: Fare calculator cannot be null or blank.");
		}
		
		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: File name cannot be null or blank.");
		}
		
		if (discountEligibilityService == null) {
			throw new IllegalArgumentException("[ERROR]: Discount eligibility service cannot be null.");
		}
		
		if (userService == null) {
			throw new IllegalArgumentException("[ERROR]: User service cannot be null or blank.");
		}
		
		if (fileManager == null) {
			throw new IllegalArgumentException("[ERROR]: File manager cannot be null or blank.");
		}
		
		if (stationService == null) {
			throw new IllegalArgumentException("[ERROR]: Station service cannot be null or blank.");
		}
		
		this.tickets = tickets;
		this.fareCalculator = fareCalculator;
		this.discountEligibilityService = discountEligibilityService;
		this.fileName = fileName.trim();
		this.userService = userService;
		this.fileManager = fileManager;
		this.stationService = stationService;
		this.trainService = trainService;
		
	}

	public boolean hasActivePass(Passenger passenger) {
		for (Ticket t : tickets) {
			if (t.getPassenger().getUserId().equals(passenger.getUserId())) {
				if ((t.getTicketType() == TicketType.DAILY || t.getTicketType() == TicketType.MONTHLY) 
						&& t.getStatus() == TicketStatus.ACTIVE) {
					return true;
				}
			}
		}
		return false;
	}

	public Ticket buyTicket(Passenger passenger, Route route, TicketType type) throws FileProcessingException {
		if (passenger == null) {
			throw new IllegalArgumentException("[ERROR]: Passenger cannot be null.");
		}
		
		if (route == null) {
			throw new IllegalArgumentException("[ERROR]: Route cannot be null.");
		}
		
		if (type == null) {
			throw new IllegalArgumentException("[ERROR]: Ticket type cannot be null.");
		}
		
		Train assignedTrain = null;
		
		if (type == TicketType.SINGLE) {
			assignedTrain = getAvailableTrain();
			
			if (assignedTrain == null) {
				throw new IllegalStateException("[ERROR]: All trains are currently at maximum capacity. Please try again later.");
			}
		}
		
		int ticketNumber = tickets.size() + 1;
		String generatedId = String.format("T%03d", ticketNumber);
		
		Station source = route.getSource();
		Station destination = route.getDestination();
		
		DiscountType discountType = discountEligibilityService.determineDiscountType(passenger);
		double ticketFare = fareCalculator.calculateFare(route, type, discountType);
		
		Ticket ticket = new Ticket(generatedId, passenger, source, destination, assignedTrain, type, ticketFare);
		
		tickets.add(ticket);
		
		try {
			saveTickets();
			userService.saveUsers();
			return ticket;
		} catch (FileProcessingException e){
			tickets.remove(ticket);
			throw new IllegalStateException("[ERROR]: System error. Ticket could not be saved to file.", e);
		}
	}
	
	public void cancelTicket(String ticketId, Passenger passenger) throws TicketNotFoundException, FileProcessingException {
		if (ticketId == null || ticketId.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Ticket ID cannot be null or empty.");
		}
		
		if (passenger == null) {
			throw new IllegalArgumentException("[ERROR]: Passenger cannot be null.");
		}
		
		String enteredId = ticketId.trim();
		
		for (Ticket ticket : tickets) {
			if (ticket.getTicketId().equalsIgnoreCase(enteredId)) {
				
				if (ticket.getPassenger().getUserId().equals(passenger.getUserId())) {
					ticket.cancelTicket(); 
					
					try {
						saveTickets();
						return;
					} catch (FileProcessingException e) {
						throw new IllegalStateException("[ERROR]: Ticket cancelled in memory, but failed to save to file.", e);
					}
				}
				throw new IllegalArgumentException("[ERROR]: Ticket does not belong to passenger " + passenger.getUserId() + ".");
			}
		}
		throw new TicketNotFoundException("[ERROR]: Ticket ID not found.");
	}
	
	public void viewTickets(User user) {
	    if (user == null) {
	        throw new IllegalArgumentException("[ERROR]: User cannot be null.");
	    }
	    
	    System.out.println("\n----------------- TICKET DETAILS -----------------");
	    
	    if (user.getRole() == UserRole.ADMIN) {
	        if (tickets.isEmpty()) {
	            System.out.println("[INFO]: No tickets found in the system.");
	            System.out.println("--------------------------------------------------");
	            return;
	        }
	        
	        for (Ticket ticket : tickets) {
	            ticket.printTicket();
	        }
	        
	    } else if (user.getRole() == UserRole.PASSENGER) {
	        boolean ticketFound = false;

	        for (Ticket ticket : tickets) {
	            if (ticket.getPassenger().getUserId().equals(user.getUserId())) {
	                ticket.printTicket();
	                ticketFound = true;
	            }
	        }
	        
	        if (!ticketFound) {
	            System.out.println("[INFO]: You have no purchased tickets.");
	            System.out.println("--------------------------------------------------");
	        } 
	        
	    } else {
	        throw new IllegalArgumentException("[ERROR]: Invalid user role.");
	    }
	}
	
	public void saveTickets() throws FileProcessingException {
		String jsonString = "[\n";

		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			
			String sourceIdStr = (ticket.getSource() != null) ? ticket.getSource().getStationId() : "null";
			String destIdStr = (ticket.getDestination() != null) ? ticket.getDestination().getStationId() : "null";
			String trainIdStr = (ticket.getTrain() != null) ? ticket.getTrain().getTrainId() : "null";
			
			jsonString += """
					  {
					    "ticketId": "%s",
					    "passengerId": "%s",
					    "sourceId": "%s",
					    "destinationId": "%s",
					    "trainId": "%s",
					    "ticketType": "%s",
					    "ticketStatus": "%s",
					    "fare": %s
					  }
				  """.formatted(
						  ticket.getTicketId(),
						  ticket.getPassenger().getUserId(),
						  sourceIdStr,
						  destIdStr,
						  trainIdStr,
						  ticket.getTicketType().name(),
						  ticket.getStatus().name(),
						  ticket.getFare()
						  );

			if (i < tickets.size() - 1) {
				jsonString += ",\n";
			} else {
				jsonString += "\n";
			}
		}

		jsonString += "]";

		try {
			fileManager.saveData(jsonString, fileName);
		} catch (FileProcessingException e) {
			System.out.println("[ERROR]: Critical failure while saving tickets to " + fileName);
			throw e;
		}
	}
	
	public void loadTickets() {
	    try {
	        Object loadedData = fileManager.loadData(fileName);

	        if (loadedData == null) return;
	        
	        String jsonString = String.valueOf(loadedData).trim();
	        
	        if (jsonString.isEmpty() || jsonString.replaceAll("\\s+", "").equals("[]")) return;

	        tickets.clear();
	        String[] blocks = jsonString.split("}");

	        for (String block : blocks) {
	            if (block.trim().isEmpty() || block.trim().equals("]")) {
	                continue;
	            }

	            String ticketId = JsonUtil.extractString(block, "ticketId");
	            String passengerId = JsonUtil.extractString(block, "passengerId");
	            String typeText = JsonUtil.extractString(block, "ticketType");
	            TicketType ticketType = TicketType.valueOf(typeText);
	            String statusText = JsonUtil.extractString(block, "ticketStatus");
	            TicketStatus status = TicketStatus.valueOf(statusText);
	            double fare = JsonUtil.extractNumber(block, "fare");

	            Passenger passenger = userService.findPassengerById(passengerId);

	            Station source = null;
	            Station destination = null;

	            if (ticketType == TicketType.DAILY || ticketType == TicketType.MONTHLY) {
	                source = new Station("D1", "SYS", "SYS");
	                destination = new Station("D2", "SYS", "SYS");
	            } else {
	                String sourceId = JsonUtil.extractString(block, "sourceId");
	                if (sourceId != null && !sourceId.trim().isEmpty() && !sourceId.equals("null")) {
	                    source = stationService.findStationById(sourceId);
	                }

	                String destinationId = JsonUtil.extractString(block, "destinationId");
	                if (destinationId != null && !destinationId.trim().isEmpty() && !destinationId.equals("null")) {
	                    destination = stationService.findStationById(destinationId);
	                }
	            }
	            
	            String trainId = JsonUtil.extractString(block, "trainId");
	            Train train = null;
	            if (trainId != null && !trainId.trim().isEmpty() && !trainId.equals("null")) {
	                train = trainService.findTrainById(trainId); 
	            }

	            Ticket ticket = new Ticket(ticketId, passenger, source, destination, train, ticketType, fare);

	            if (status == TicketStatus.CANCELLED) {
	                ticket.cancelTicket();
	            } 
	            else if (status == TicketStatus.USED) {
	                ticket.useTicket();
	            }
	            
	            tickets.add(ticket);
	        }
	        
	    } catch (FileProcessingException e) {
	        System.out.println("[INFO]: No existing tickets found. Starting fresh.");
	    } catch (Exception e) {
	        throw new IllegalStateException("[ERROR]: Unable to load ticket data from " + fileName + ".", e);
	    }
	}
	
	private Train getAvailableTrain() {
		ArrayList<Train> allTrains = trainService.getTrains(); 
		
		for (Train train : allTrains) {
			int currentPassengerCount = 0;
			
			for (Ticket ticket : tickets) {
				if (ticket.getTicketType() == enums.TicketType.SINGLE 
					&& ticket.getStatus() == enums.TicketStatus.ACTIVE 
					&& ticket.getTrain() != null 
					&& ticket.getTrain().getTrainId().equals(train.getTrainId())) {
					
					currentPassengerCount++;
				}
			}
			
			if (currentPassengerCount < train.getCapacity()) {
				return train;
			}
		}
		
		return null;
	}
}