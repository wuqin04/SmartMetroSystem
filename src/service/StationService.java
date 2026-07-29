package service;

import model.Passenger;
import model.Station;
import model.User;

import java.util.ArrayList;

import java.io.IOException;

public class StationService {
	
	private ArrayList<Station> stations=new ArrayList<>();
	
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
		
		return null ;
	}
	
	/*
	public void viewStations() {
		
	}
	*/
	
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
