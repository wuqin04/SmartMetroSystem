package repository;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TXTFileManager implements FileManager {

	@Override
	public void saveData(Object data, String fileName) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(fileName));
			
			writer.println("S001,KL Sentral, KL");
			writer.println("S002,Bangsar, KL");
			
			writer
		}
		
	}

	@Override
	public Object loadData(String FileName) {
		// TODO Auto-generated method stub
		return null;
	}

}
