package darian.saric.rasus.model;

/**
 * Ovaj razred predstavlja jedno mjerenje koje obavlja {@link Sensor}.
 */
public class Measurement {
    // TODO: opiši razred
    private String parameter;
    private double value;

    public Measurement(String parameter, double value) {
        this.parameter = parameter;
        this.value = value;
    }

    public String getParameter() {
        return parameter;
    }

    public double getValue() {
        return value;
    }
}
