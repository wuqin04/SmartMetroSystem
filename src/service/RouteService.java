//add or edit route infos
package service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Route;   
import model.Station;  

public final class RouteService {
    private static final String HEADER = "# routeId|routeName|type|stationIds(separated by ;)|segmentDistancesKm(separated by ;)";

    private final StationService stationService;
    private final Path routeFile;
    private final Map<String, Route> routes = new LinkedHashMap<String, Route>();

    public RouteService(Path dataDirectory) throws IOException {
        if (dataDirectory == null) {
            throw new IllegalArgumentException("[ERROR]: Data directory cannot be null.");
        }
        Files.createDirectories(dataDirectory);
        this.stationService = new StationService(dataDirectory);
        this.routeFile = dataDirectory.resolve("route.txt");
    }

    public StationService getStationService() {
        return stationService;
    }

    public Route getRoute(String routeId) {
        String key = requireText(routeId, "Route ID");
        Route route = routes.get(key);
        if (route == null) {
            throw new IllegalArgumentException("[ERROR]: Unknown route ID: " + key);
        }
        return route;
    }

    public List<Route> getAllRoutes() {
        return Collections.unmodifiableList(new ArrayList<Route>(routes.values()));
    }

    public Route addMajorRoute(String routeId, String routeName, List<Station> stations,
            List<Double> segmentDistancesKm) throws IOException {
        return addRoute(routeId, routeName, Route.Type.MAJOR, stations, segmentDistancesKm, true);
    }

    public Route addMinorRoute(String routeId, String routeName, List<Station> stations,
            List<Double> segmentDistancesKm) throws IOException {
        return addRoute(routeId, routeName, Route.Type.MINOR, stations, segmentDistancesKm, true);
    }

    /**
     * Changes the distance of one direct segment in a minor route and saves both files.
     * The total between non-adjacent stations is recalculated automatically by Route.
     */
    public Route editMinorRouteDistance(String routeId, String fromStationId,
            String toStationId, double newDistanceKm) throws IOException {
        Route minorRoute = requireMinorRoute(routeId);
        Route updatedRoute = minorRoute.withEditedSegmentDistance(
                fromStationId, toStationId, newDistanceKm);
        replaceRoute(minorRoute.getRouteId(), updatedRoute);
        saveAll();
        return updatedRoute;
    }

    /** Changes a minor route ID, retaining every station and distance, then saves both files. */
    public Route editMinorRouteId(String existingRouteId, String newRouteId) throws IOException {
        Route minorRoute = requireMinorRoute(existingRouteId);
        String replacementId = requireText(newRouteId, "New route ID");
        if (!minorRoute.getRouteId().equals(replacementId) && routes.containsKey(replacementId)) {
            throw new IllegalArgumentException("[ERROR]: Route ID already exists: " + replacementId);
        }

        Route renamedRoute = minorRoute.withRouteId(replacementId);
        replaceRoute(minorRoute.getRouteId(), renamedRoute);
        saveAll();
        return renamedRoute;
    }

    public double getDistanceBetween(String routeId, String firstStationId, String secondStationId) {
        return getRoute(routeId).getDistanceBetween(firstStationId, secondStationId);
    }

    public String getRouteInformation(String routeId) {
        return getRoute(routeId).getRouteInformation();
    }

    /** Loads both files. station.txt is always loaded before route.txt. */
    public void loadAll() throws IOException {
        stationService.loadStations();
        routes.clear();
        if (!Files.exists(routeFile)) {
            return;
        }

        List<String> records = Files.readAllLines(routeFile, StandardCharsets.UTF_8);
        for (String record : records) {
            if (record.trim().isEmpty() || record.startsWith("#")) {
                continue;
            }
            Route route = routeFromRecord(record);
            if (routes.containsKey(route.getRouteId())) {
                throw new IllegalArgumentException("Duplicate route ID in route.txt: " + route.getRouteId());
            }
            routes.put(route.getRouteId(), route);
        }
    }

    /** Writes station.txt first, then route.txt. */
    public void saveAll() throws IOException {
        stationService.saveStations();
        saveRoutes();
    }

    /**
     * Creates requested Malaysian major routes plus the Bukit Dukung-to-TRX minor route.
     * It fails if routes are already loaded so existing data cannot be overwritten accidentally.
     */
    public void createMalaysianSampleRoutes() throws IOException {
        if (!routes.isEmpty()) {
            throw new IllegalArgumentException("[ERROR]: Routes already exist. Load, add, or edit them instead of reseeding.");
        }

        addRoute("MRT-KAJANG", "Kajang Line", Route.Type.MAJOR,
                stations(new Station("MRT-KG35", "Kajang"),
                        new Station("MRT-KG29", "Bukit Dukung"),
                        new Station("MRT-TRX", "TRX")),
                distances(10.6, 9.8), false);

        addRoute("MRT-PUTRAJAYA", "Putrajaya Line", Route.Type.MAJOR,
                stations(new Station("MRT-PY01", "Kwasa Damansara"),
                        new Station("MRT-PY15", "Kampung Batu"),
                        new Station("MRT-PY36", "Putrajaya Sentral")),
                distances(16.5, 35.0), false);

        addRoute("LRT-KELANA-JAYA", "Kelana Jaya Line", Route.Type.MAJOR,
                stations(new Station("LRT-KJ01", "Gombak"),
                        new Station("LRT-KJ10", "KLCC"),
                        new Station("LRT-KJ37", "Putra Heights")),
                distances(15.1, 28.0), false);

        addRoute("LRT-AMPANG", "Ampang Line", Route.Type.MAJOR,
                stations(new Station("LRT-AG01", "Ampang"),
                        new Station("LRT-AG10", "Masjid Jamek"),
                        new Station("LRT-AG18", "Sentul Timur")),
                distances(12.0, 5.0), false);

        addRoute("LRT-SRI-PETALING", "Sri Petaling Line", Route.Type.MAJOR,
                stations(new Station("LRT-SP01", "Sri Petaling"),
                        new Station("LRT-SP10", "Bukit Jalil"),
                        new Station("LRT-AG18", "Sentul Timur")),
                distances(4.0, 16.0), false);

        addRoute("MONORAIL-KL", "KL Monorail", Route.Type.MAJOR,
                stations(new Station("MON-01", "KL Sentral"),
                        new Station("MON-10", "Bukit Bintang"),
                        new Station("MON-11", "Titiwangsa")),
                distances(3.2, 5.4), false);

        addRoute("MIN-BD-TRX", "Bukit Dukung to TRX", Route.Type.MINOR,
                stations(new Station("MRT-KG29", "Bukit Dukung"),
                        new Station("MRT-TRX", "TRX")),
                distances(9.8), false);

        saveAll();
    }

    private Route addRoute(String routeId, String routeName, Route.Type type,
            List<Station> suppliedStations, List<Double> suppliedDistances, boolean saveAfterAdd)
            throws IOException {
        String cleanedId = requireText(routeId, "Route ID");
        if (routes.containsKey(cleanedId)) {
            throw new IllegalArgumentException("[ERROR]: Route ID already exists: " + cleanedId);
        }

        // Validate all supplied data before registering or changing any station.
        new Route(cleanedId, routeName, type, suppliedStations, suppliedDistances);

        List<Station> canonicalStations = new ArrayList<Station>();
        for (Station station : suppliedStations) {
            canonicalStations.add(stationService.registerOrMerge(station, routeName));
        }
        Route route = new Route(cleanedId, routeName, type, canonicalStations, suppliedDistances);
        routes.put(route.getRouteId(), route);

        if (saveAfterAdd) {
            saveAll();
        }
        return route;
    }

    private Route requireMinorRoute(String routeId) {
        Route route = getRoute(routeId);
        if (route.getType() != Route.Type.MINOR) {
            throw new IllegalArgumentException("[ERROR]: Only a minor route can be edited: " + route.getRouteId());
        }
        return route;
    }

    private void replaceRoute(String existingRouteId, Route replacement) {
        Map<String, Route> rebuilt = new LinkedHashMap<String, Route>();
        boolean replaced = false;
        for (Route existingRoute : routes.values()) {
            if (existingRoute.getRouteId().equals(existingRouteId)) {
                rebuilt.put(replacement.getRouteId(), replacement);
                replaced = true;
            } else {
                rebuilt.put(existingRoute.getRouteId(), existingRoute);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("[ERROR]: Route to replace was not found: " + existingRouteId);
        }
        routes.clear();
        routes.putAll(rebuilt);
    }

    private Route routeFromRecord(String record) {
        String[] fields = record.split("\\|", -1);
        if (fields.length != 5) {
            throw new IllegalArgumentException("[ERROR]: Invalid route record: " + record);
        }

        Route.Type type;
        try {
            type = Route.Type.valueOf(fields[2]);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("[ERROR]: Invalid route type in route.txt: " + fields[2], exception);
        }

        List<Station> routeStations = new ArrayList<Station>();
        String[] stationIds = fields[3].split(";", -1);
        for (String stationId : stationIds) {
            routeStations.add(stationService.getStation(stationId));
        }

        List<Double> distances = new ArrayList<Double>();
        if (!fields[4].trim().isEmpty()) {
            String[] distanceValues = fields[4].split(";", -1);
            for (String distanceValue : distanceValues) {
                try {
                    distances.add(Double.valueOf(distanceValue));
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException("[ERROR]: Invalid distance in route.txt: " + distanceValue,
                            exception);
                }
            }
        }
        return new Route(fields[0], fields[1], type, routeStations, distances);
    }

    private void saveRoutes() throws IOException {
        List<String> records = new ArrayList<String>();
        records.add(HEADER);
        for (Route route : routes.values()) {
            records.add(route.toFileRecord());
        }
        Files.write(routeFile, records, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
    }

    private static List<Station> stations(Station... stations) {
        return Arrays.asList(stations);
    }

    private static List<Double> distances(Double... distances) {
        return Arrays.asList(distances);
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
}