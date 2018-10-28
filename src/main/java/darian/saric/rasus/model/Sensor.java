package darian.saric.rasus.model;

import java.util.List;

/**
 * Ovaj razred predstavlja podatke o registriranim senzorima:
 *  - identifikator senzora
 *  - geolokacija senzora
 *  - sva zapisana mjerenja senzora
 */
public class Sensor {
    // TODO: dopuni model senzora
    //id
    //geolokacija
    private List<Measurement> measurements;

    public List<Measurement> getMeasurements() {
        return measurements;
    }
}
