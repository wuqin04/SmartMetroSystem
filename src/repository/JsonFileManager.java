package repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import exception.FileProcessingException;

public class JsonFileManager implements FileManager {
	
	@Override
	public void saveData(Object data, String fileName) throws FileProcessingException {
		try {
			Path path = Paths.get(fileName);
			
			String json = String.valueOf(data);
			
			Files.writeString(path, json, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new FileProcessingException("[ERROR]: " + e.getMessage());
		}
		
	}
	
	@Override
    public Object loadData(String fileName) throws FileProcessingException {
		Path path = Paths.get(fileName);
		
		if (Files.notExists(path)) {
			throw new FileProcessingException("[ERROR]: File does not exist: " + fileName);
		}
		
		try {
			String json = Files.readString(path, StandardCharsets.UTF_8);
			return json;
		} catch (Exception e) {
			throw new FileProcessingException("[ERROR]: " + e.getMessage());
		}
	}
	
}


