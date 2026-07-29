package model;

import enums.UserRole;
import service.StationService;
import service.TrainService;
import service.ReportService;

public class Admin extends User {

	private StationService stationService;
	private TrainService trainService;
	private ReportService reportService;
	
	public Admin(String userId, String name, String email, String password) {
		 super(userId, name, email, password, UserRole.ADMIN);
	}
	
	public void addStation(Station station) {
		
		if(station == null) {
			throw new IllegalArgumentException("[ERROR]: Station cannot be null.");
		} 
		
		stationService.addStation(station);
	}
	
	public void addTrain(Train train) {
		
		if(train == null) {
			throw new IllegalArgumentException("[ERROR]: Train cannot be null.");
		}
		
		trainService.addTrain(train);
	
	}
	
	public void viewReports() {
	
		reportService.showTotalSales();
		reportService.showTotalRevenue();
		reportService.showCancelledTickets();
	}
}