package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import exception.FileProcessingException;

public class TXTFileManager implements FileManager {

	//Save sample data into TXT file
	@Override
	public void saveData(Object data, String fileName) throws FileProcessingException {
		try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

	        if (data instanceof List<?>) {
	        	List<?> list = (List<?>) data;
	        	for (Object obj : list) {
	        		writer.println(obj.toString());
	        	}
	        } else if (data instanceof Map<?, ?>) {
	        	Map<?, ?> map = (Map<?, ?>) data;
	        	for (Object value : map.values()) {
	        		writer.println(value.toString());
	        	}
	        } else {
	        	throw new FileProcessingException("[ERROR]: Unsupported data type for " + fileName);
	        }

			System.out.println("[SUCCESS]: Data saved successfully");
		}catch(IOException e) {
			throw new FileProcessingException("[ERROR]: Failed to save data to " + fileName);
		}

	}

	//Load all lines from a text file and return as ArrayList<String>
	@Override
	public Object loadData(String fileName) throws FileProcessingException {
		
		ArrayList<String> lines = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

				String line;

				while((line = reader.readLine()) !=null) {
					lines.add(line);
				}
				
				System.out.println("[SUCCESS]: Data loaded successfully");
			}catch (Exception e) {
				throw new FileProcessingException("[ERROR]: Failed to load data from " + fileName);
			}
			
			return lines;
	}
}
