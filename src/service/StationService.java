package service;

import java.util.ArrayList;

import model.Station;

public class StationService {
	
	private ArrayList<Station> stations=new ArrayList<>();
	
	public ArrayList<Station> getAllStations() {
		return stations;
	}
	
	public void addStation(Station station) {
				
		//Check Station Name
		boolean nameExists=false;
		
		for(Station s: stations) {
			if(station.getName().equals(s.getName())) {
				nameExists=true;
				break;
			}
		}
		
		if(nameExists) {
			System.out.println("[ERROR]: Station name has already exists. ");
			return;
		}
		
		if(!Character.isUpperCase(station.getName().trim().charAt(0))) {
			System.out.println("[ERROR]: Each word of the station name does not start with capital letter. ");
			return;
		}
		
		//Generate Station ID
		int stationA= stations.size()+1;
		station.setStationId(String.format("Station%3d", stationA));

		//Save station info into the array
		stations.add(station); 

		System.out.println("[SUCCESS]: Information of the station has added successfully.");
	}
	
	
	public Station searchStation(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Name cannot be null.");
		}
		
		String searchName = name.trim();
		
		for (Station station : stations) {
			if (station.getName().equalsIgnoreCase(searchName)) {
				return station;
			}
		}
		return null;
	}
	
	public void viewStations() {
        System.out.println("\n[ALL REGISTERED STATIONS]");
        
        if (stations == null || stations.isEmpty()) {
            System.out.println("[INFO]: No stations currently exist in the system.");
            System.out.println("---------------------------------");
            return;
        }
        
        for (Station station : stations) {
            System.out.println("Station ID : " + station.getStationId());
            System.out.println("Name       : " + station.getName());
            System.out.println("Location   : " + station.getLocation());
            System.out.println("---------------------------------");
        }
    }
	
	public Station findStationById(String stationId) {
		
		if(stationId == null || stationId.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Source ID cannot be null.");
		}
		
		String enteredId = stationId.trim();

		for (Station station: stations) {

			if (station.getStationId().equalsIgnoreCase(enteredId)) {

				return station;
			}
		}

		throw new IllegalArgumentException("[ERROR]: Station ID not found: " + enteredId);
	}
}
