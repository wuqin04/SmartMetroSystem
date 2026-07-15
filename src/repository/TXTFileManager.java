package repository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class TXTFileManager implements FileManager {

	//Save sample data into TXT file
	@Override
	public void saveData(Object data, String fileName) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(fileName));
			
			//Sample data
			writer.println("S001|KL Sentral|KL");
			writer.println("S002|Bangsar|KL");
			
			writer.close();
			System.out.println("[SUCCESS] Data saved succesfully");
		}catch(IOException e) {
			System.out.println("[ERROR] Failed to save data");	
		}
		
	}

	//Load all lines from a text file and return as ArrayList<String>
	@Override
	public Object loadData(String fileName) {
		ArrayList<String> lines = new ArrayList<>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			String line;
			
			while((line = reader.readLine()) !=null) {
				lines.add(line);
			}
				reader.close();
				
				System.out.println("[SUCCESS] Data loaded successfully");
			}catch (IOException e) {
				System.out.println("[ERROR] Failed to load data");
			}
			
			return lines;

	}

}
