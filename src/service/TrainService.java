package service;

import java.util.ArrayList;

import exception.FileProcessingException;
import repository.FileManager;

import model.Train;
import util.JsonUtil;

public class TrainService {
	
	private final ArrayList<Train> trains = new ArrayList<>();
	private final FileManager fileManager;
	private final String fileName;
	
	public ArrayList<Train> getTrains() {
		return trains;
	}

	public TrainService(FileManager fileManager, String fileName) {
		this.fileManager = fileManager;
		this.fileName = fileName;
	}
	
	public void addTrain(Train train) {
		if (train == null) {
			throw new IllegalArgumentException("[ERROR]: Train cannot be null.");
		}
		
		for (Train t : trains) {
			if (t.getTrainId().equalsIgnoreCase(train.getTrainId())) {
				throw new IllegalArgumentException("[ERROR]: Train ID already exists.");
			}
			if (t.getTrainName().equalsIgnoreCase(train.getTrainName())) {
				throw new IllegalArgumentException("[ERROR]: Train name already exists.");
			}
		}
		
		trains.add(train);
		
		try {
			saveTrains();
			System.out.println("[SUCCESS]: Train added successfully.");
		} catch (FileProcessingException e) {
			trains.remove(trains.size() - 1);
			throw new IllegalStateException("[ERROR]: Train added but could not be saved.", e);
		}
	}
	
	public void updateTrain(Train updatedTrain) {
		if (updatedTrain == null) {
			throw new IllegalArgumentException("[ERROR]: Updated train cannot be null.");
		}
		
		int indexToUpdate = -1;
		Train oldTrain = null;
		
		for (int i = 0; i < trains.size(); i++) {
			if (trains.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId())) {
				indexToUpdate = i;
				oldTrain = trains.get(i);
				break;
			}
		}
		
		if (indexToUpdate == -1) {
			throw new IllegalArgumentException("[ERROR]: Train ID not found.");
		}
		
		for (Train t : trains) {
			if (!t.getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()) && 
			     t.getTrainName().equalsIgnoreCase(updatedTrain.getTrainName())) {
				throw new IllegalArgumentException("[ERROR]: Train name already exists on another train.");
			}
		}
		
		trains.set(indexToUpdate, updatedTrain);
		
		try {
			saveTrains();
			System.out.println("[SUCCESS]: Train updated successfully.");
		} catch (FileProcessingException e) {
			trains.set(indexToUpdate, oldTrain); // Rollback to the old train data
			throw new IllegalStateException("[ERROR]: Train updated but could not be saved.", e);
		}
	}
	
	public void viewTrains() {
		System.out.println("\n--- ALL AVAILABLE TRAINS ---");
		if (trains.isEmpty()) {
			System.out.println("No trains available.");
			System.out.println("----------------------------");
			return;
		}
		for (Train t : trains) {
			t.displayTrain();
			System.out.println("----------------------------");
		}
	}
	
	public void loadTrains() {
		try {
			Object loadedObject = fileManager.loadData(fileName);
			
			if (loadedObject == null) return;
			
			String jsonString = String.valueOf(loadedObject).trim(); 
			
			if (jsonString.isEmpty() || jsonString.replaceAll("\\s+", "").equals("[]")) return;
			
			trains.clear();
			
			String[] trainBlocks = jsonString.split("}");
			
			for (String block : trainBlocks) {
				if (block.trim().isEmpty() || block.trim().equals("]")) {
					continue;
				}
				
				String trainId = JsonUtil.extractString(block, "trainId");
				String trainName = JsonUtil.extractString(block, "trainName");
				int trainCapacity = (int) JsonUtil.extractNumber(block, "capacity");
				
				Train train = new Train(trainId, trainName, trainCapacity);
				
				trains.add(train);
			}
			
		} catch (FileProcessingException e) {
			System.out.println("[INFO]: Creating new data.");
		} catch (Exception e) {
			throw new IllegalStateException("[ERROR]: Unable to load train data from " + fileName + ".", e);
		}
	}
	
	public void saveTrains() throws FileProcessingException {
		String jsonString = "[\n";
		
		for (int i = 0; i < trains.size(); i++) {
			Train t = trains.get(i);
			
			jsonString += """
						{
							"trainId": "%s",
							"trainName": "%s",
							"capacity": %s
						}
					""".formatted(
							t.getTrainId(), 
							t.getTrainName(),
							t.getCapacity()
					);
			
			if (i < trains.size() - 1) {
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
			System.out.println("[ERROR]: Critical failure while saving trains to " + fileName);
			throw e;
		}
	}
	
	public String generateTrainId() {
		if (trains.isEmpty()) {
			return "TRN001";
		}
		
		int maxNum = 0;
		for (Train t : trains) {
			String currentId = t.getTrainId();
			if (currentId.toUpperCase().startsWith("TRN")) {
				try {
					int num = Integer.parseInt(currentId.substring(3));
					if (num > maxNum) {
						maxNum = num;
					}
				} catch (NumberFormatException e) {
					// Ignore safely if there is a malformed ID in the file
				}
			}
		}
		
		return String.format("TRN%03d", maxNum + 1);
	}
	
	public Train findTrainById(String trainId) {
		if (trainId == null || trainId.trim().isEmpty()) {
			throw new IllegalArgumentException("[ERROR]: Train ID cannot be null or blank.");
		}
		
		for (Train train : trains) {
			if (train.getTrainId().equals(trainId)) {
				return train;
			}
		}
		
		return null;
	}
}