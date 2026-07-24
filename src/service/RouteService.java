//add or edit route infos
package service;
 
import java.util.List;
import java.util.ArrayList;
import model.Route;
import model.Station;

public final class RouteService {
    private ArrayList<Route> routes;
 
    public RouteService(ArrayList<Route> routes) {
        if (routes == null) {
            throw new IllegalArgumentException("[ERROR]: Routes list cannot be null.");
        }
        this.routes = routes;
    }
 
    public void addRoute(Route route) {
        if (route == null) {
            throw new IllegalArgumentException("[ERROR]: Route cannot be null.");
        }
        routes.add(route);
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
}