package service;
import java.util.Scanner;
import model.Station;
import java.util.ArrayList;


public class StationService {
	
	private ArrayList<Station> stations=new ArrayList<>();
	Scanner sc= new Scanner(System.in);
	
	public void addStation(Station station) {     
        stations.add(station);
        System.out.println("[SUCCESS]: Station \"" + station.getName() + "\" has been added.");
	}
	
	/*
	public Station searchStation() {
		return null ;
	}
	
	public void viewStations() {
		
	}
	*/
}
