package model;

import org.json.JSONObject;
import repository.JSONSerializable;

//Add JSONSerializable to Station to make sure JSONFileManager can save or load
public class Station implements JSONSerializable{

    private String stationId;
    private String name;
    private String location;

    //Check the availability of data
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

    //Display station info
    public void displayInfo() {
    	System.out.println("Station ID:		" + stationId);
    	System.out.println("Station Name:	" + name);
    	System.out.println("Location:		" + location);
    }

    //Convert station in JSON
	@Override
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("stationId", stationId);
		obj.put("name", name);
		obj.put("location", location);
		return obj;
	}
	
	//change the input data in to JSON format
	public static Station fromJSON(JSONObject obj) {
		if(obj == null) {
			throw new IllegalArgumentException("[ERROR]: Cannot build station from null JSON object");
		}
		try {
			String stationId = obj.getString("stationId");
			String name = obj.getString("name");
			String location = obj.getString("location");
			return new Station(stationId, name, location);
		} catch (org.json.JSONException e) {
			throw new IllegalArgumentException("[ERROR]: Malformed station data in JSON file");
		}
	}
}