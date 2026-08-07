package service;

import java.util.ArrayList;

import model.Station;
import repository.FileManager;
import exception.FileProcessingException;
import util.JsonUtil;

public class StationService {
	
	private final ArrayList<Station> stations = new ArrayList<>();
	private final FileManager fileManager;
	private final String fileName;
	
	public StationService(FileManager fileManager, String fileName) {
		if (fileManager == null) {
			throw new IllegalArgumentException("[ERROR]: File manager cannot be null.");
		}
		
		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: File name cannot be null or blank.");
		}
		
		this.fileManager = fileManager;
		this.fileName = fileName.trim();
	}
	
	public ArrayList<Station> getAllStations() {
		return stations;
	}
	
	public void addStation(Station station) {
		if (station == null) {
			throw new IllegalArgumentException("[ERROR]: Station cannot be null.");
		}
		
		for (Station s : stations) {
			if (station.getName().equalsIgnoreCase(s.getName())) {
				throw new IllegalArgumentException("[ERROR]: Station name already exists.");
			}
		}
		
		if (!Character.isUpperCase(station.getName().trim().charAt(0))) {
			throw new IllegalArgumentException("[ERROR]: Station name must start with a capital letter.");
		}
		
		int stationNumber = stations.size() + 1;
		station.setStationId(String.format("STN%03d", stationNumber));

		stations.add(station);

		try {
			saveStations();
			System.out.println("[SUCCESS]: Information of the station has been added successfully.");
		} catch (FileProcessingException e) {
			stations.remove(stations.size() - 1);
			throw new IllegalStateException("[ERROR]: Station added but could not be saved to file.", e);
		}
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
		System.out.println("\n--- ALL REGISTERED STATIONS ---");
		
		if (stations.isEmpty()) {
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
		if (stationId == null || stationId.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Station ID cannot be null.");
		}
		
		String enteredId = stationId.trim();

		for (Station station : stations) {
			if (station.getStationId().equalsIgnoreCase(enteredId)) {
				return station;
			}
		}

		throw new IllegalArgumentException("[ERROR]: Station ID not found: " + enteredId);
	}
	
	public void saveStations() throws FileProcessingException {
		String jsonString = "[\n";

		for (int i = 0; i < stations.size(); i++) {
			Station station = stations.get(i);
			
			jsonString += """
				  {
				    "stationId": "%s",
				    "name": "%s",
				    "location": "%s"
				  }
			  """.formatted(
					  station.getStationId(),
					  station.getName(),
					  station.getLocation()
					  );

			if (i < stations.size() - 1) {
				jsonString += ",\n";
			} 
			else {
				jsonString += "\n";
			}
		}

		jsonString += "]";

		try {
			fileManager.saveData(jsonString, fileName);
		} catch (FileProcessingException e) {
			System.out.println("[ERROR]: Critical failure while saving stations to " + fileName);
			throw e;
		}
	}
	
	public void loadStations() {
		try {
			Object loadedData = fileManager.loadData(fileName);

			if (loadedData == null) return;
			
			String jsonString = String.valueOf(loadedData).trim();
			
        	if (jsonString.isEmpty() || jsonString.replaceAll("\\s+", "").equals("[]")) return;

			stations.clear();
			String[] blocks = jsonString.split("}");

			for (String block : blocks) {
				if (block.trim().isEmpty() || block.trim().equals("]")) {
					continue;
				}

				String stationId = JsonUtil.extractString(block, "stationId");
				String name = JsonUtil.extractString(block, "name");
				String location = JsonUtil.extractString(block, "location");
				
				Station station = new Station(stationId, name, location);
				
				stations.add(station);
			}
			
		} catch (FileProcessingException e) {
			System.out.println("[INFO]: No existing stations found. Starting fresh.");
		} catch (Exception e) {
			throw new IllegalStateException("[ERROR]: Unable to load station data from " + fileName + ".", e);
		}
	}
}
