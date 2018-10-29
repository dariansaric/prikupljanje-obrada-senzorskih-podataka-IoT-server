package darian.saric.rasus.model;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.StrictMath.*;

/**
 * Ovaj razred čuva podatke o senzorima i mjerenjima.
 */
public class Storage {
    private static Set<Sensor> sensorSet = new HashSet<>();
    private static Map<String, List<Measurement>> measurementMap = new HashMap<>();

    /**
     * Vraća <tt>true</tt> ako je senzor uspješno registriran.
     *
     * @param sensor potencijalni novi senzor
     *
     * @return true ako je uspješno registriran senzor
     */
    public static boolean registerSensor(Sensor sensor) {
        return sensorSet.add(sensor);
    }

    public static Sensor getClosestSensor(String username) {
        if (sensorSet.size() == 1) {
            return null;
        }

        Sensor s1 = getSensorForName(username);
        if (s1 == null) {
            return null;
        }

        return computeDistances(s1).entrySet().stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static Map<Sensor, Double> computeDistances(Sensor s1) {
        return sensorSet.stream().filter(s -> !s.equals(s1))
                .collect(Collectors.toMap(s -> s, s ->
                        calculateDistance(s1, s)
                ));
    }

    private static double calculateDistance(Sensor s1, Sensor s) {
        final int R = 6371;
        double dlon = s.getLongitude() - s1.getLongitude();
        double dlat = s.getLatitude() - s1.getLatitude();

        double a = pow(sin(dlat / 2), 2) +
                cos(s1.getLatitude()) * cos(s.getLongitude()) * pow(sin(dlon / 2), 2);

        return R * 2 * atan2(sqrt(a), sqrt(1 - a));
    }

    public static Sensor getSensorForName(String username) {
        for (Sensor s : sensorSet) {
            if (s.getUsername().equals(username)) {
                return s;
            }
        }

        return null;
    }

    public static boolean deregisterSensor(String username) {
        //trebat ce za kasnije
        return sensorSet.remove(getSensorForName(username));
    }

    public static boolean storeMeasurement(String username, Measurement measurement) {
        List<Measurement> list = measurementMap.get(username);
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(measurement);
        return true;
    }
}
