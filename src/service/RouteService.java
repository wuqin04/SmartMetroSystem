//add or edit route infos
package service;
 
import java.util.ArrayList;
import java.util.List;
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
     * Finds all stored routes that connect the requested source and destination.
     * A route is considered a match if it goes directly from source -> destination,
     * OR if it goes destination -> source, since trains can travel both ways
     * along the same route.
     *
     * @return a list of all matching routes (never empty; throws if none found)
     */
    public List<Route> findRoutes(Station source, Station destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("[ERROR]: Source and destination stations cannot be null.");
        }
 
        List<Route> matches = new ArrayList<Route>();
 
        for (Route route : routes) {
            boolean isForward = route.getSource().equals(source)
                    && route.getDestination().equals(destination);
            boolean isBackward = route.getSource().equals(destination)
                    && route.getDestination().equals(source);
 
            if (isForward || isBackward) {
                matches.add(route);
            }
        }
 
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: No route was found from "
                    + source.getName() + " to " + destination.getName() + ".");
        }
 
        return matches;
    }
 
    /**
     * Convenience overload: finds routes between two stations regardless of which
     * one is passed first, since direction no longer matters for matching.
     * Kept separate from findRoutes(source, destination) only for readability
     * at call sites where "either direction" is the intent.
     */
    public List<Route> findRoutesBothWays(Station stationA, Station stationB) {
        return findRoutes(stationA, stationB);
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