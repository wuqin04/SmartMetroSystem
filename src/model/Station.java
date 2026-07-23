//display info at ui
package model;
import java.util.Scanner;
import ui.AdminUI;
import service.TicketService;
import service.StationService;

public class Station {
	
	private int choiceS;
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
		choiceS= stationPortal.choice;
			 
			
		
		
	}
	

}
