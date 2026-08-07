//Add or modify train infos
package service;

import java.util.ArrayList;
import java.util.List;
import model.Train;
import org.json.JSONObject;
import repository.FileManager;
import repository.JSONFileManager;
import exception.FileProcessingException;

public class TrainService {
	
	private ArrayList<Train> trains = new ArrayList<>();
	
	public void addTrain(Train train) {
		
		boolean nameExists = false;
		
		for(Train s: trains) {
			if(train.getTrainName().equals(s.getTrainName())) {
				nameExists = true;
				break;
			}
		}
		
		for (Train t : trains) {
			if (t.getTrainId().equalsIgnoreCase(train.getTrainId())) {
				System.out.println("[ERROR]: Train ID already exists.");
				return;
			}
		}
		
		if(nameExists) {
			System.out.println("[ERROR]: Train name has already exists.");
			return;
		}
		trains.add(train);
		System.out.println("[SUCCESS]: Train added successfully.");
		
	}
	
	public void viewTrains() {
		if (trains.isEmpty()) {
			System.out.println("[ERROR]: No trains available.");
			return;
		}
		for(Train t:trains) {
			t.displayTrain();
		}
	}
	
	public void loadTrains(FileManager fileManager,String fileName) {
		try {
			Object loadedObject = fileManager.loadData(fileName);
			
			if(loadedObject == null) {
				return;
			}
			
			if(!(loadedObject instanceof List<?>)) {
				System.out.println("[ERROR]: Unexpected data format when loading the trains");
				return;
			}
			
			List<?> rawList = (List<?>) loadedObject;
			for(Object obj : rawList) {
				JSONObject jsonObj = (JSONObject) obj;
				Train train = Train.fromJSON(jsonObj);
				trains.add(train);
			}
			
		}catch (FileProcessingException e) {
	        System.out.println(e.getMessage());
		}
	}	
	public void saveTrains(FileManager fileManager,String fileName) {
		try {
			fileManager.saveData(trains, fileName);
			System.out.println("[SUCCESS]: Train saved successfully");
		}catch (FileProcessingException e) {
			System.out.println(e.getMessage());
		}
		
	}
}
