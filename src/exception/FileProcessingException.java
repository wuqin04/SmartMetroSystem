package exception;

public class FileProcessingException extends Exception {
    public FileProcessingException(String message) {
        super("Failed to save/load data: " + message);
    }
}