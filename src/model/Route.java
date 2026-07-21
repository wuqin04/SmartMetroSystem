//view route infos
package model;

import java.util.Objects;

public final class Route {
    private String routeId;
    private final Station source;
    private final Station destination;
    private double distanceKm;

    public Route(String routeId, Station source, Station destination, double distanceKm) {
        this.source = Objects.requireNonNull(source, "[ERROR]: Source station cannot be null.");
        this.destination = Objects.requireNonNull(destination, "[ERROR]: Destination station cannot be null.");

        if (source.getStationId().equals(destination.getStationId())) {
            throw new IllegalArgumentException("[ERROR]: Source and destination must be different stations.");
        }

        setRouteId(routeId);
        setDistanceKm(distanceKm);
    }

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

    /** Returns the direct distance of this route in kilometres. */
    public double calculateDistance() {
        return distanceKm;
    }

    /** Prints route information only. */
    public void displayRoute() {
        System.out.printf(
                "%s: %s -> %s (%.2f km)%n",
                routeId,
                source.getName(),
                destination.getName(),
                distanceKm);
    }

    void setRouteId(String routeId) {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("[ERROR]: Route ID cannot be blank.");
        }
        this.routeId = routeId.trim();
    }

    void setDistanceKm(double distanceKm) {
        if (!Double.isFinite(distanceKm) || distanceKm <= 0) {
            throw new IllegalArgumentException("[ERROR]: Route distance must be a positive finite value.");
        }
        this.distanceKm = distanceKm;
    }
}
