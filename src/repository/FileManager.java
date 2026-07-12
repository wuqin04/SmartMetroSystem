package repository;

public interface FileManager {
	void saveData(Object data, String fileName);
	Object loadData(String FileName);
	

}
