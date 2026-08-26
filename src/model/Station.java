package model;

public class Station {

    private String stationId;
    private String name;
    private String location;

	public Station(String stationId, String name, String location) {
        
        if (stationId == null || stationId.trim().isEmpty()) {
    		throw new IllegalArgumentException("[ERROR]: Station ID cannot be null or blank.");
    	}
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: Station name cannot be null or blank.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: Station location cannot be null or blank.");
        }
        
        this.stationId = stationId;
        this.name = name;
        this.location = location;
        
    }

    public String getStationId() {
    	return stationId;
    }

    public String getName() {
    	return name;
    }	

    public String getLocation() {
    	return location;
    }
    
    public void setStationId(String stationId) {
		this.stationId = stationId;
	}
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: Station name cannot be null or blank.");
        }
        this.name = name;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: Station location cannot be null or blank.");
        }
        this.location = location;
    }

    //Display station info
    public void displayInfo() {
        System.out.printf("%-14s %s%n", "Station ID:", stationId);
        System.out.printf("%-14s %s%n", "Station Name:", name);
        System.out.printf("%-14s %s%n", "Location:", location);
    }
}