package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
import exception.FileProcessingException;

public class JSONFileManager implements FileManager {

	//Save sample data into JSON file
	@Override
	public void saveData(Object data, String fileName) throws FileProcessingException {
	    try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

	        if (data instanceof List<?>) {
	            List<?> list = (List<?>) data;
	            JSONArray array = new JSONArray();

	            for (Object obj : list) {
	                array.put(obj.toString());
	            }

	            writer.println(array.toString());

	        } else {
	            throw new FileProcessingException("[ERROR]: Unsupported data type for " + fileName);
	        }

	        System.out.println("[SUCCESS]: Data saved successfully");
	    } catch (IOException e) {
	        throw new FileProcessingException("[ERROR]: Failed to save data to " + fileName);
	    }
	}

	//Load all lines from the JSON file and return in to ArrayList
	@Override
	public Object loadData(String fileName) throws FileProcessingException {
		ArrayList<String> lines = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) !=null) {
				sb.append(line);
			}
			
			JSONArray array = new JSONArray(sb.toString());
			for(int i = 0; i < array.length(); i++) {
				lines.add(array.getString(i));
			}
			
			System.out.println("[SUCCESS]: Data loaded successfully");
		} catch (Exception e) {
	        throw new FileProcessingException("[ERROR]: Failed to load data from " + fileName);
	    }

	    return lines;
	}

}


