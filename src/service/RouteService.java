package service;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import exception.FileProcessingException;
import repository.FileManager;

import model.Route;
import model.Station;

import util.JsonUtil;

public class RouteService {
    private ArrayList<Route> routes;
    private final FileManager jsonFileManager;
    private final String routeFile;
 
    public RouteService(ArrayList<Route> routes, FileManager jsonFileManager, String routeFile) {
        if (routes == null) {
            throw new IllegalArgumentException("[ERROR]: Routes list cannot be null.");
        }
 
        this.routes = routes;
        this.jsonFileManager = jsonFileManager;
        this.routeFile = routeFile;
    }
    
    public ArrayList<Route> getAllRoutes() {
    	return routes;
    }
 
    public List<Route> findRoutes(Station source, Station destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("[ERROR]: Source and destination stations cannot be null.");
        }
        if (source.equals(destination)) {
            throw new IllegalArgumentException("[ERROR]: Source and destination stations cannot be the same.");
        }

        Queue<Station> queue = new LinkedList<>();
        List<Station> visited = new ArrayList<>();
        
        Map<Station, Route> routeTracker = new HashMap<>(); 

        queue.add(source);
        visited.add(source);
        boolean destinationFound = false;

        while (!queue.isEmpty()) {
            Station current = queue.poll();

            if (current.equals(destination)) {
                destinationFound = true;
                break; // We found the destination, stop searching!
            }

            // Look at all routes to find the neighbors of the current station
            for (Route route : routes) {
                Station neighbor = null;
                
                // Check if this route connects to our current station
                if (route.getSource().equals(current)) {
                    neighbor = route.getDestination();
                } else if (route.getDestination().equals(current)) {
                    neighbor = route.getSource();
                }

                // If we found a valid, unvisited neighbor, add it to our queue
                if (neighbor != null && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    routeTracker.put(neighbor, route); // Remember how we got here
                }
            }
        }

        // 3. Handle Failure
        if (!destinationFound) {
            throw new IllegalArgumentException("[ERROR]: No route was found from " 
                    + source.getName() + " to " + destination.getName() + ".");
        }

        // 4. Reconstruct the Path Backwards
        List<Route> finalPath = new ArrayList<>();
        Station step = destination;
        
        while (!step.equals(source)) {
            Route takenRoute = routeTracker.get(step);
            
            // Add to the FRONT of the list so it reads logically from Source -> Destination
            finalPath.add(0, takenRoute); 
            
            // Move backward to the previous station
            if (takenRoute.getSource().equals(step)) {
                step = takenRoute.getDestination();
            } else {
                step = takenRoute.getSource();
            }
        }

        return finalPath;
    }

    /** Prints every stored route to the console, separated by a divider line. */
    public void displayAllRoutes() {
        if (routes.isEmpty()) {
            System.out.println("No routes found in the system.");
            return;
        }
 
        for (Route route : routes) {
            route.displayRoute();
            System.out.println("-------------------------");
        }
    }
    
    public void saveRoutes() throws FileProcessingException {
    	String jsonString = "[\n";
    	
    	for (int i = 0; i < routes.size(); i++) {
    		Route route = routes.get(i);
    		
    		jsonString += """
	    				{
	    					"routeId": "%s",
			                "distanceKm": %s,
			                "sourceStationId": "%s",
			                "sourceName": "%s",
			                "sourceLocation": "%s",
			                "destinationStationId": "%s",
			                "destinationName": "%s",
			                "destinationLocation": "%s"
	    				}
    				""".formatted(
    						route.getRouteId(),
		                      route.getDistanceKm(),
		                      route.getSource().getStationId(),
		                      route.getSource().getName(),
		                      route.getSource().getLocation(),
		                      route.getDestination().getStationId(),
		                      route.getDestination().getName(),
		                      route.getDestination().getLocation()
    						);
    		
    		if (i < routes.size() - 1) {
    			jsonString += ",\n";
    		}
    		else {
    			jsonString += "\n";
    		}
    	}
    	
    	jsonString += "]";
    	
    	try {
            jsonFileManager.saveData(jsonString, routeFile);
        } catch (FileProcessingException e) {
            System.out.println("[ERROR]: Critical failure while saving routes to " + routeFile);
            throw e;
        }
    }

    public void loadRoutes() {
        try {
        	Object loadedObject = jsonFileManager.loadData(routeFile);
        	
        	if (loadedObject == null) return;
        	
        	String jsonString = String.valueOf(loadedObject).trim(); 
        	
        	if (jsonString.isEmpty() || jsonString.replaceAll("\\s+", "").equals("[]")) return;
            
            routes.clear();
            
            String[] routeBlocks = jsonString.split("}");
            
            for (String block : routeBlocks) {
                if (block.trim().isEmpty() || block.trim().equals("]")) {
                    continue;
                }
                
                String routeId = JsonUtil.extractString(block, "routeId");
                double distanceKm = JsonUtil.extractNumber(block, "distanceKm");
                
                String sourceStationId = JsonUtil.extractString(block, "sourceStationId");
                String sourceName = JsonUtil.extractString(block, "sourceName");
                String sourceLocation = JsonUtil.extractString(block, "sourceLocation");
                
                String destinationStationId = JsonUtil.extractString(block, "destinationStationId");
                String destinationName = JsonUtil.extractString(block, "destinationName");
                String destinationLocation = JsonUtil.extractString(block, "destinationLocation");
                
                Station source = new Station(sourceStationId, sourceName, sourceLocation);
                Station destination = new Station(destinationStationId, destinationName, destinationLocation);
                
                Route route = new Route(routeId, source, destination, distanceKm);
                
                routes.add(route);
            }
       
        } catch (FileProcessingException e) {
        	System.out.println("[INFO]: Creating new data.");
        } catch (Exception e) {
        	throw new IllegalStateException("[ERROR]: Unable to load route data from " + routeFile + ".", e);
        }
    }
    
    public void addRoute(Route route) {
        if (route == null) {
            throw new IllegalArgumentException("[ERROR]: Route cannot be null.");
        }

        routes.add(route);

        try {
            saveRoutes();
        } catch (FileProcessingException e) {
            throw new IllegalStateException("[ERROR]: Route added but could not be saved.");
        }
    }
    
    public void updateRouteStatus(String routeId, enums.OperationalStatus newStatus) {
        if (routeId == null || routeId.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: Route ID cannot be empty.");
        }
        
        if (newStatus == null) {
            throw new IllegalArgumentException("[ERROR]: New status cannot be null.");
        }
        
        Route route = findRouteById(routeId.trim());
        
        if (route == null) {
            throw new IllegalArgumentException("[ERROR]: Route ID " + routeId + " not found.");
        }
        
        route.setStatus(newStatus);
        
        try {
            saveRoutes(); 
        } catch (Exception e) {
            throw new IllegalStateException("[ERROR]: Route status updated in memory, but failed to save to file.", e);
        }
    }
    
    public Route findRouteById(String routeId) {
        if (routeId == null || routeId.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: Route ID cannot be empty.");
        }
        
        for (Route route : routes) {
            if (route.getRouteId().equalsIgnoreCase(routeId.trim())) {
                return route;
            }
        }
        throw new IllegalArgumentException("[ERROR]: Route ID '" + routeId + "' not found.");
    }
    
    public void updateRouteDistance(String routeId, double newDistance) {
        if (routeId == null || routeId.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: Route ID cannot be empty.");
        }
        
        if (newDistance <= 0) {
            throw new IllegalArgumentException("[ERROR]: Distance must be greater than 0 km.");
        }
        
        Route route = findRouteById(routeId.trim());
        
        if (route == null) {
            throw new IllegalArgumentException("[ERROR]: Route ID " + routeId + " not found.");
        }
        
        // Update the distance
        route.setDistance(newDistance);
        
        try {
            saveRoutes(); 
        } catch (Exception e) {
            throw new IllegalStateException("[ERROR]: Route distance updated in memory, but failed to save to file.", e);
        }
    }
}