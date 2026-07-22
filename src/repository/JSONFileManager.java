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

	@Override
	public void saveData(Object data, String fileName) throws FileProcessingException {
	    try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

	        if (data instanceof List<?>) {
	            List<?> list = (List<?>) data;
	            JSONArray array = new JSONArray();

	            for (Object obj : list) {
	                array.put(obj.toString());  // temporary - see note below
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

	@Override
	public Object loadData(String fileName) throws FileProcessingException {
		// TODO: build this next
		return null;
	}

}
