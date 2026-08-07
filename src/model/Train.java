//view train infos
package model;

import org.json.JSONObject;
import repository.JSONSerializable;

public class Train implements JSONSerializable {
	
	private String trainId;
	private String trainName;
	private int capacity;
	
	//Check the availability of data
	public Train(String trainId, String trainName, int capacity) {
		if(trainId == null || trainId.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Train ID cannot be null or blank.");
		}
		if(trainName == null || trainName.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Train name cannot be null or blank");
		}
		if(capacity <= 0) {
			throw new IllegalArgumentException("[ERROR]: Train capacity must be greater than 0");
		}
		
		this.trainId = trainId;
		this.trainName = trainName;
		this.capacity = capacity;
	}
	
	public String getTrainId() {
		return trainId;
	}
	
	public String getTrainName() {
		return trainName;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	//Display train info
	public void displayTrain() {
		System.out.println("Train ID       :" + trainId);
		System.out.println("Train Name     :" + trainName);
		System.out.println("Train Capacity :" + capacity);
		
	}

	//Convert train into JSON
	@Override
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("trainId", trainId);
		obj.put("trainName", trainName);
		obj.put("capacity", capacity);
		return obj;
	}
	
	//Change the input data in to JSON format
	public static Train fromJSON(JSONObject obj) {
		if(obj == null) {
			throw new IllegalArgumentException("[ERROR]: Cannot build train from null JSON object");
		}
		try {
			String trainId = obj.getString("trainId");
			String trainName = obj.getString("trainName");
			int capacity = obj.getInt("capacity");
			return new Train(trainId, trainName, capacity);
		} catch (org.json.JSONException e) {
			throw new IllegalArgumentException("[ERROR]: Malformed train data in JSON file");
		}
		
	}

}
