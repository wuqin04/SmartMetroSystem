package service;

import java.util.ArrayList;
import java.util.List;

import model.Train;
import repository.FileManager;
import exception.FileProcessingException;
import util.JsonUtil;

public class TrainService {
	
	private final ArrayList<Train> trains = new ArrayList<>();
	private final FileManager fileManager;
	private final String fileName;
	
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
			Object loadedData = fileManager.loadData(fileName);
			
			if (loadedData == null) return;
			
			String jsonString = String.valueOf(loadedData).trim();
			
        	if (jsonString.isEmpty() || jsonString.replaceAll("\\s+", "").equals("[]")) return;
			
			trains.clear();
			String[] blocks = jsonString.split("}");
			
			for (String block : blocks) {
				if (block.trim().isEmpty() || block.trim().equals("]")) {
					continue;
				}
				
				String trainId = JsonUtil.extractString(block, "trainId");
				String trainName = JsonUtil.extractString(block, "trainName");
				int trainCapacity = (int)JsonUtil.extractNumber(block, "capacity");
				
				Train train = new Train(trainId, trainName, trainCapacity);
				trains.add(train);
			}
			
		} catch (FileProcessingException e) {
			System.out.println("[INFO]: No existing trains found. Starting fresh.");
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
				    "capacity": "%s",
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
}
