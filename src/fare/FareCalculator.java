package fare;

import model.Route;
import model.Passenger;
import enums.TicketType;
import enums.DiscountType;

public interface FareCalculator {
	
	double calculateFare(Route route, TicketType ticketType, DiscountType discountType);	
}