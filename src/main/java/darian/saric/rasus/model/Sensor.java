package darian.saric.rasus.model;

/**
 * Ovaj razred predstavlja podatke o registriranim senzorima:
 * - identifikator senzora
 * - geolokacija senzora
 * - sva zapisana mjerenja senzora
 */
public class Sensor {
    // TODO: dopuni model senzora
    private String username;
    private double latitude;
    private double longitude;
    private String ip;
    private int port;

    public Sensor(String username, double latitude, double longitude, String ip, int port) {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ip = ip;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
