package service;
import model.Passenger;
import enums.DiscountType;
import java.time.LocalDate;
import java.time.Period;
public class DiscountEligibilityService {
	private final int CHILD_MAX_AGE = 12;
	private final int SENIOR_MAX_AGE = 60;
	
	public DiscountType determineDiscountType(Passenger passenger) {
			
		if(passenger == null) {
			throw new IllegalArgumentException("[ERROR]: Passenger cannot be null or blank.");
		}
		
		int age = calculateAge(passenger.getDateOfBirth());
		
		if(age <= CHILD_MAX_AGE) {
			return DiscountType.CHILD;
		}
		
		if(age >= SENIOR_MAX_AGE) {
			return DiscountType.SENIOR;
		}
		
		return DiscountType.NONE;
	}
	
	private int calculateAge(LocalDate dateOfBirth) {
		
		if (dateOfBirth == null) {
			throw new IllegalArgumentException("[ERROR]: Date of birth cannot be null.");
		}
		if (dateOfBirth.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("[ERROR]: Date of birth cannot be in the future.");
		}
		LocalDate currentDate = LocalDate.now();
		Period agePeriod = Period.between(dateOfBirth, currentDate);
		
		return agePeriod.getYears();
	}
}

