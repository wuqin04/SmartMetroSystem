//view route infos
package model;

public class Route {

    private String routeId;
    private Station source;
    private Station destination;
    private double distanceKm;

    public Route(String routeId, Station source, Station destination, double distanceKm) {
        if (routeId == null || routeId.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: Route ID cannot be null or empty.");
        }
        if (source == null) {
            throw new IllegalArgumentException("[ERROR]: Source station cannot be null.");
        }
        if (destination == null) {
            throw new IllegalArgumentException("[ERROR]: Destination station cannot be null.");
        }
        if (source.getStationId().equals(destination.getStationId())) {
            throw new IllegalArgumentException("[ERROR]: Source and destination station cannot be the same.");
        }
        if (distanceKm <= 0) {
            throw new IllegalArgumentException("[ERROR]: Distance must be greater than 0 km.");
        }

        this.routeId = routeId;
        this.source = source;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }

    // Accessors - needed so RouteService can read a Route's private fields
    public String getRouteId() {
        return routeId;
    }

    public Station getSource() {
        return source;
    }

    public Station getDestination() {
        return destination;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double calculateDistance() {
        return distanceKm;
    }

    public void displayRoute() {
        System.out.println("Route ID     : " + routeId);
        System.out.println("Source       : " + source.getName() + " (" + source.getStationId() + ")");
        System.out.println("Destination  : " + destination.getName() + " (" + destination.getStationId() + ")");
        System.out.println("Distance     : " + distanceKm + " km");
    }
}