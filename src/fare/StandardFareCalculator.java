package fare;

import enums.TicketType;
import enums.DiscountType;
import model.Route;

public class StandardFareCalculator implements FareCalculator {
	
	private static final double BASE_FARE = 1.00;
	private static final double RATE_PER_KM = 0.60;
	private static final double DAILY_FARE = 10.00;
	private static final double MONTHLY_FARE = 50.00;

	@Override
	public double calculateFare(Route route, TicketType ticketType, DiscountType discountType) {
		
		if(route == null) {
			throw new IllegalArgumentException("[ERROR]: Route cannot be null.");
		}
		
		if(ticketType == null) {
			throw new IllegalArgumentException("[ERROR]: Ticket type cannot be null.");
		}
		
		if(discountType == null) {
			throw new IllegalArgumentException("[ERROR]: Discount type cannot be null.");
		}
		
		double distance = route.calculateDistance();
		
		if(distance <= 0) {
			throw new IllegalArgumentException("[ERROR]: Route distance must be greater than 0.0 km.");
		}
		
		double fare;
		
		if(ticketType == TicketType.SINGLE) {
			fare = BASE_FARE + (distance * RATE_PER_KM);
		}else if(ticketType == TicketType.DAILY){
			fare = DAILY_FARE;
		}else if(ticketType == TicketType.MONTHLY) {
			fare = MONTHLY_FARE; 
		}else {
			throw new IllegalArgumentException("[ERROR]: Invalid ticket type.");
		}
		
		if(ticketType == TicketType.SINGLE) {
			
			if(discountType == DiscountType.STUDENT || discountType == DiscountType.OKU|| discountType == DiscountType.SENIOR) {
				fare /= 2;
			}
		}
		
		double finalFare = Math.round(fare * 100.0) / 100.0;
		
		if(finalFare <= 0) {
			throw new IllegalArgumentException("[ERROR]: Calculated fare must be greater than RM0.00.");
		}
		
		return finalFare;
	}
}
