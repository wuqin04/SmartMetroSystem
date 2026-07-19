//display info at ui
package model;
import java.util.Scanner;
import service.TicketService;
import service.StationService;
//import model.Admin; 
public class Station {
	
	private String stationId;
	private String name;
	private String location;
	StationService stationService=new StationService();
	
	Scanner sc= new Scanner(System.in);		
	
	public void setStationId(String stationId) {
		this.stationId=stationId;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getStationId() {
		return stationId;
	}
	
	public void setLocation(String location) {
		this.location=location;
	}
	
	public void displayInfo() {
		int choice;
		
		System.out.println("[STATION PORTAL]\n(Enter 1, 2 or 3 for station's options or 0 to return back to previous page.");
		System.out.println("1. Add station");
		System.out.println("2. Search station");
		System.out.println("3. View station");
		System.out.println("0. Return to previous page");
		System.out.println("Enter your choice:");
		
		
		choice=sc.nextInt();
		
		while (choice!=1&&choice!=2&&choice!=3&&choice!=0) {
			System.out.println("[ERROR]: Invalid option. Please try again.");
			choice=sc.nextInt();

		}
		switch (choice) {
		case 1:
			System.out.println("[ADD STATION]");
			
			System.out.println("Insert new station ID: ");
			String stationId=sc.nextLine();
			
			System.out.println("Insert the new station's name: ");
			String name=sc.nextLine();
			
			System.out.println("Insert the new station's location: ");
			String location=sc.nextLine();
			
			Station newStation=new Station();
			newStation.setStationId(stationId);
	        newStation.setName(name);
	        newStation.setLocation(location);
			stationService.addStation(newStation);
			break;
		case 2:
			stationService.searchStation();
			break;
		case 3:
			//stationService.viewStations();
			break;
		default:
			// If return to admin page:
			/*
			Admin admin = new Admin();
        	admin.displayInfo(); // or whatever the menu method in Admin.java is called
        	*/
        	break;
			 
			
		}
		
	}
	

}
