package darian.saric.rasus.model;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.StrictMath.*;

/**
 * Ovaj razred čuva podatke o senzorima i mjerenjima.
 */
public class Storage {
    /**
     * {@link Set} koji čuva podatke o registriranim senzorima
     */
    private static Set<Sensor> sensorSet = new HashSet<>();
    /**
     * {@link Map} koja mapira korisničko ime senzora na listu pripradnih mjerenja
     */
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

    /**
     * Vraća senzor koji je lokacijski najbliži senzoru s primljenim korisničkim imenom. Ako ne postoji senzor
     * s predanim korisničkim imenom, metoda vraća <tt>null</tt>.
     *
     * @param username korisničko ime senzor
     *
     * @return najbliži senzor ili null
     */
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

    /**
     * Vraća mapu senzora i pripadnih geolokacijskih udaljenosti od predanog senzora.
     *
     * @param s1 promatrani senzor
     *
     * @return mapa neki drugi senzor -> udaljenost
     */
    private static Map<Sensor, Double> computeDistances(Sensor s1) {
        return sensorSet.stream().filter(s -> !s.equals(s1))
                .collect(Collectors.toMap(s -> s, s ->
                        calculateDistance(s1, s)
                ));
    }

    /**
     * Vraća geolokacijsku udaljenost izmežu predanih senzora.
     *
     * @param s1 neki {@link Sensor}
     * @param s  neki drugi {@link Sensor}
     *
     * @return udaljenost između dvaju senzora
     */
    private static double calculateDistance(Sensor s1, Sensor s) {
        final int R = 6371;
        double dlon = s.getLongitude() - s1.getLongitude();
        double dlat = s.getLatitude() - s1.getLatitude();

        double a = pow(sin(dlat / 2), 2) +
                cos(s1.getLatitude()) * cos(s.getLongitude()) * pow(sin(dlon / 2), 2);

        return R * 2 * atan2(sqrt(a), sqrt(1 - a));
    }

    /**
     * Vraća {@link Sensor} za predano korisničko ime. Vraća null ako takav senzor ne postoji.
     *
     * @param username neko korisničko ime
     *
     * @return podaci o senzoru s predanim korisničkim imenom
     */
    public static Sensor getSensorForName(String username) {
        for (Sensor s : sensorSet) {
            if (s.getUsername().equals(username)) {
                return s;
            }
        }

        return null;
    }

    /**
     * Briše {@link Sensor} iz {@link #sensorSet} i vraća status uspješnosti brisanja.
     *
     * @param s neki senzor
     *
     * @return status brisanja senzora iz kolekcije
     */
    public static boolean deregisterSensor(Sensor s) {
        return sensorSet.remove(s);
    }

    /**
     * Pohranjuje {@link Measurement} za zadano korisničko ime, ako {@link Sensor} s istim postoji,
     * te vraća status uspješnosti pohrane.
     *
     * @param username    korisničko ime senzora
     * @param measurement neko mjerenje
     *
     * @return status uspješnosti pohrane mjerenja
     */
    public static boolean storeMeasurement(String username, Measurement measurement) {
        List<Measurement> list = measurementMap.computeIfAbsent(username, k -> new LinkedList<>());
        list.add(measurement);

        return true;
    }
}
