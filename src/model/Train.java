//view train infos
package model;

public class Train {
	
	private String trainId;
	private String trainName;
	private int capacity;
	
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
}
