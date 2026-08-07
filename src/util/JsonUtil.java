package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {
	/**
     * Extracts a String value from a JSON block based on the key.
     * Uses Regex to safely handle varying amounts of whitespace.
     */
    public static String extractString(String textBlock, String key) {
        // Regex pattern looks for: "key" : "value"
        String regex = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textBlock);
        
        if (matcher.find()) {
            return matcher.group(1).trim(); // Returns just the value inside the quotes
        }
        return "";
    }

    /**
     * Extracts a double or integer value from a JSON block based on the key.
     */
    public static double extractNumber(String textBlock, String key) {
        // Regex pattern looks for: "key" : 123.45 (ignoring quotes around the number)
        String regex = "\"" + key + "\"\\s*:\\s*([0-9.]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textBlock);
        
        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1).trim());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }
    
    /**
     * Extracts a boolean value from a JSON block (true/false)
     */
    public static boolean extractBoolean(String textBlock, String key) {
        String regex = "\"" + key + "\"\\s*:\\s*(true|false)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textBlock);
        
        if (matcher.find()) {
            return Boolean.parseBoolean(matcher.group(1).trim());
        }
        return false;
    }
}
