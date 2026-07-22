//add or edit route infos
package service;
 
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
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
     * along a route, each stored Route is treated as a bidirectional edge.
     *
     * Uses breadth-first search over the station graph built from `routes`,
     * so the returned path is guaranteed to have the fewest possible legs.
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

        // Build an adjacency list: station -> list of (neighboring station, route used to get there)
        Map<Station, List<Edge>> graph = new HashMap<>();
        for (Route route : routes) {
        	graph.computeIfAbsent(route.getSource(), _ -> new ArrayList<>())
            .add(new Edge(route.getDestination(), route));
        	graph.computeIfAbsent(route.getDestination(), _ -> new ArrayList<>())
            .add(new Edge(route.getSource(), route));
        }

        // BFS from source, remembering how we reached each station
        Queue<Station> queue = new LinkedList<>();
        Map<Station, Station> cameFromStation = new HashMap<>();
        Map<Station, Route> cameFromRoute = new HashMap<>();
        Set<Station> visited = new HashSet<>();

        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            Station current = queue.poll();
            if (current.equals(destination)) break;

            for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(edge.station)) {
                    visited.add(edge.station);
                    cameFromStation.put(edge.station, current);
                    cameFromRoute.put(edge.station, edge.route);
                    queue.add(edge.station);
                }
            }
        }

        if (!visited.contains(destination)) {
            throw new IllegalArgumentException("[ERROR]: No route was found from "
                    + source.getName() + " to " + destination.getName() + ".");
        }

        // Walk backwards from destination to source to reconstruct the path in order
        LinkedList<Route> path = new LinkedList<>();
        Station step = destination;
        while (!step.equals(source)) {
            path.addFirst(cameFromRoute.get(step));
            step = cameFromStation.get(step);
        }

        return path;
    }

    private static class Edge {
        final Station station;
        final Route route;

        Edge(Station station, Route route) {
            this.station = station;
            this.route = route;
        }
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