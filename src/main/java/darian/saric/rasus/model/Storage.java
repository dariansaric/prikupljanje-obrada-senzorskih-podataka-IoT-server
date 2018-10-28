package darian.saric.rasus.model;

import java.util.*;

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

    public static Sensor getSensorForName(String username) {
        for (Sensor s : sensorSet) {
            if (s.getUsername().equals(username)) {
                return s;
            }
        }

        return null;
    }

    public static boolean deregisterSensor(String username) {
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
