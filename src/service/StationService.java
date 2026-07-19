package service;
import java.util.Scanner;
import model.Station;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class StationService {
	
	private ArrayList<Station> stations=new ArrayList<>();
	Scanner sc= new Scanner(System.in);
	
	public void addStation(Station station) {
	    stations.add(station); // keep the in-memory list too, if you still need it

	    try {
	        FileWriter fw = new FileWriter("stations.txt", true); // true = append mode
	        PrintWriter pw = new PrintWriter(fw);

	        pw.println(station.getStationId() + "," + station.getName() + "," + station.getLocation());

	        pw.close();
	        System.out.println("[SUCCESS]: Station \"" + station.getName() + "\" has been added and saved to file.");

	    } catch (IOException e) {
	        System.out.println("[ERROR]: Could not save station to file.");
	        e.printStackTrace();
	    }
	}
	
	
	public Station searchStation() {
		return null ;
	}
	/*
	public void viewStations() {
		
	}
	*/
}
