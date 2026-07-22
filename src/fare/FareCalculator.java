package fare;

import model.Route;
import enums.TicketType;

public interface FareCalculator {
	
	double calculateFare(Route route, TicketType ticketType);	
}