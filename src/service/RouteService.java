//add or edit route infos
package service;

import java.util.ArrayList;

import model.Route;
import model.Station;

public final class RouteService {
    private ArrayList<Route> routes;

    public RouteService() {
        routes = new ArrayList<Route>();
    }

    public void addRoute(Route route) {
        if (route == null) {
            throw new IllegalArgumentException("[ERROR]: Route cannot be null.");
        }
        routes.add(route);
    }

    /** Finds a stored route with the requested source and destination. */
    public Route findRoute(Station source, Station destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("[ERROR]: Source and destination stations cannot be null.");
        }

        for (Route route : routes) {
            if (route.getSource().equals(source)
                    && route.getDestination().equals(destination)) {
                return route;
            }
        }
        throw new IllegalArgumentException("[ERROR]: No route was found from "
                + source.getStationName() + " to " + destination.getStationName() + ".");
    }
}
