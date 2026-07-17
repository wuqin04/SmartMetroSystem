//display info at ui
package model;
import java.util.Scanner;
import service.TicketService;

public class Station {
	
	private String stationId;
	private String name;
	private String location;
	
	public void displayInfo() {
		int choice;
		Scanner sc= new Scanner(System.in);		
		System.out.println("This is station portal. What do you want to do? (Enter 1, 2 or 3 for station's options or 99 to exit.");
		System.out.println("1. Add station");
		System.out.println("2. Search station");
		System.out.println("3. View station");
		
		choice=sc.nextInt();
		switch (choice) {
		case 1:
			
		}
		
	}
	

}
