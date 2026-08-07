package service;
 
import java.util.ArrayList;
import java.util.List;

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
 
    /**
     * Finds a sequence of routes connecting the requested source and destination,
     * traversing through intermediate stations if there is no direct route
     * (e.g. Kajang -> Bukit Bintang -> TRX). Since trains can travel both ways
     * along a route, each stored Route is treated as usable in either direction.
     *
     * Builds the path step by step: starting from source, repeatedly scans
     * the routes list to find a route connecting the current station to any
     * unvisited station, until destination is reached.
     *
     * @return an ordered list of routes forming a path from source to destination
     *         (never empty; throws if no path exists)
     */
    public List<Route> findRoutes(Station source, Station destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("[ERROR]: Source and destination stations cannot be null.");
        }
        if (source.equals(destination)) {
            throw new IllegalArgumentException("[ERROR]: Source and destination stations cannot be the same.");
        }

        List<Route> path = new ArrayList<>();
        List<Station> visited = new ArrayList<>();

        Station current = source;
        visited.add(current);

        while (!current.equals(destination)) {
            Route nextRoute = null;
            Station nextStation = null;

            for (Route route : routes) {
                boolean forward = route.getSource().equals(current) && !visited.contains(route.getDestination());
                boolean backward = route.getDestination().equals(current) && !visited.contains(route.getSource());

                if (forward) {
                    nextRoute = route;
                    nextStation = route.getDestination();
                    break;
                } else if (backward) {
                    nextRoute = route;
                    nextStation = route.getSource();
                    break;
                }
            }

            if (nextRoute == null) {
                throw new IllegalArgumentException("[ERROR]: No route was found from "
                        + source.getName() + " to " + destination.getName() + ".");
            }

            path.add(nextRoute);
            visited.add(nextStation);
            current = nextStation;
        }

        return path;
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
}