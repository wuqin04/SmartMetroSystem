//view route infos
package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class Route {
    public enum Type {
        MAJOR, MINOR
    }

    private final String routeId;
    private final String routeName;
    private final Type type;
    private final List<Station> stations;
    private final List<Double> segmentDistancesKm;

    public Route(String routeId, String routeName, Type type,
            List<Station> stations, List<Double> segmentDistancesKm) {
        this.routeId = requireText(routeId, "Route ID");
        this.routeName = requireText(routeName, "Route name");
        if (type == null) {
            throw new IllegalArgumentException("[ERROR]: Route type cannot be null.");
        }
        this.type = type;
        this.stations = validateStations(stations);
        this.segmentDistancesKm = validateDistances(segmentDistancesKm, this.stations.size());
    }

    public String getRouteId() {
        return routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public Type getType() {
        return type;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Double> getSegmentDistancesKm() {
        return segmentDistancesKm;
    }

    public double getTotalDistanceKm() {
        double total = 0.0;
        for (Double distance : segmentDistancesKm) {
            total += distance.doubleValue();
        }
        return total;
    }

    /**
     * Returns the distance along this route between any two stations.
     * For example, A-B = 15 km and B-C = 6 km gives A-C = 21 km.
     */
    public double getDistanceBetween(String firstStationId, String secondStationId) {
        int first = indexOfStation(firstStationId);
        int second = indexOfStation(secondStationId);
        int start = Math.min(first, second);
        int end = Math.max(first, second);

        double total = 0.0;
        for (int i = start; i < end; i++) {
            total += segmentDistancesKm.get(i).doubleValue();
        }
        return total;
    }

    /** Returns formatted route information suitable for a UI, report, or console output. */
    public String getRouteInformation() {
        StringBuilder information = new StringBuilder();
        information.append("Route ID: ").append(routeId).append(System.lineSeparator());
        information.append("Route name: ").append(routeName).append(System.lineSeparator());
        information.append("Route type: ").append(type).append(System.lineSeparator());
        information.append("Stations:").append(System.lineSeparator());

        for (int i = 0; i < stations.size(); i++) {
            Station station = stations.get(i);
            information.append("  ").append(i + 1).append(". ")
                    .append(station.getStationName()).append(" (")
                    .append(station.getStationId()).append(")");
            if (i < segmentDistancesKm.size()) {
                information.append(" -> ")
                        .append(formatKm(segmentDistancesKm.get(i).doubleValue()));
            }
            information.append(System.lineSeparator());
        }
        information.append("Total distance: ").append(formatKm(getTotalDistanceKm()));
        return information.toString();
    }

    /** Convenience display method; it asks for no user input. */
    public void displayRouteInfo() {
        System.out.println(getRouteInformation());
    }

    Route withRouteId(String newRouteId) {
        return new Route(newRouteId, routeName, type, stations, segmentDistancesKm);
    }

    Route withEditedSegmentDistance(String fromStationId, String toStationId, double newDistanceKm) {
        int from = indexOfStation(fromStationId);
        int to = indexOfStation(toStationId);
        if (Math.abs(from - to) != 1) {
            throw new IllegalArgumentException("[ERROR]: Only directly connected station segments can be edited.");
        }
        if (Double.isNaN(newDistanceKm) || Double.isInfinite(newDistanceKm) || newDistanceKm <= 0.0) {
            throw new IllegalArgumentException("[ERROR]: Segment distance must be a positive finite number.");
        }

        List<Double> updatedDistances = new ArrayList<Double>(segmentDistancesKm);
        updatedDistances.set(Math.min(from, to), Double.valueOf(newDistanceKm));
        return new Route(routeId, routeName, type, stations, updatedDistances);
    }

    String toFileRecord() {
        StringBuilder stationIds = new StringBuilder();
        for (Station station : stations) {
            if (stationIds.length() > 0) {
                stationIds.append(';');
            }
            stationIds.append(station.getStationId());
        }

        StringBuilder distances = new StringBuilder();
        for (Double distance : segmentDistancesKm) {
            if (distances.length() > 0) {
                distances.append(';');
            }
            distances.append(distance.doubleValue());
        }
        return routeId + "|" + routeName + "|" + type + "|" + stationIds + "|" + distances;
    }

    private int indexOfStation(String stationId) {
        String requestedId = requireText(stationId, "Station ID");
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getStationId().equals(requestedId)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Station " + requestedId + " is not on route " + routeId + ".");
    }

    private static List<Station> validateStations(List<Station> stationValues) {
        if (stationValues == null || stationValues.isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: A route must contain at least one station.");
        }

        List<Station> checkedStations = new ArrayList<Station>();
        Set<String> stationIds = new HashSet<String>();
        for (Station station : stationValues) {
            if (station == null) {
                throw new IllegalArgumentException("[ERROR]: A route cannot contain a null station.");
            }
            if (!stationIds.add(station.getStationId())) {
                throw new IllegalArgumentException("[ERROR]: A route cannot contain the same station twice: "
                        + station.getStationId());
            }
            checkedStations.add(station);
        }
        return Collections.unmodifiableList(checkedStations);
    }

    private static List<Double> validateDistances(List<Double> distances, int stationCount) {
        if (distances == null || distances.size() != stationCount - 1) {
            throw new IllegalArgumentException("A route with " + stationCount + " station(s) needs "
                    + (stationCount - 1) + " segment distance(s).");
        }

        List<Double> checkedDistances = new ArrayList<Double>();
        for (Double distance : distances) {
            if (distance == null || Double.isNaN(distance.doubleValue())
                    || Double.isInfinite(distance.doubleValue()) || distance.doubleValue() <= 0.0) {
                throw new IllegalArgumentException("[ERROR]: Every segment distance must be a positive finite number.");
            }
            checkedDistances.add(distance);
        }
        return Collections.unmodifiableList(checkedDistances);
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + "[ERROR]: cannot be blank.");
        }
        String cleaned = value.trim();
        if (cleaned.indexOf('|') >= 0 || cleaned.indexOf(';') >= 0
                || cleaned.indexOf('\n') >= 0 || cleaned.indexOf('\r') >= 0) {
            throw new IllegalArgumentException(fieldName + "[ERROR]: cannot contain |, ;, or a line break.");
        }
        return cleaned;
    }

    private static String formatKm(double distance) {
        return String.format(Locale.ROOT, "%.1f km", distance);
    }
}
