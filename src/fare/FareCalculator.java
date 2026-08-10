package fare;

import model.Route;
import enums.TicketType;
import enums.DiscountType;

public interface FareCalculator {
	
	double calculateFare(Route route, TicketType ticketType, DiscountType discountType);	
}