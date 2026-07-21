package model;

import enums.UserRole;
import service.StationService;
import service.TrainService;
import service.ReportService;

public class Admin extends User {

	private StationService stationService;
	private TrainService trainService;
	private ReportService reportService;
	
	public Admin(String userId, String name, String email, String password, StationService stationService, TrainService trainService, ReportService reportService) {
		 super(userId, name, email, password, UserRole.ADMIN);
		 
		 if(stationService == null) {
			 throw new IllegalArgumentException("[ERROR]: Station service cannot be null.");
		 }
		 
		 if(trainService == null) {
			 throw new IllegalArgumentException("[ERROR]: Train service cannot be null.");
		 }
		 
		 if(reportService == null) {
			 throw new IllegalArgumentException("[ERROR]: Report service cannot be null.");
		 }
		 
		 this.stationService = stationService;
		 this.trainService = trainService;
		 this.reportService = reportService;
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