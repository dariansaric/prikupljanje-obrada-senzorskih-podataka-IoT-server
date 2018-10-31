package darian.saric.rasus.rest;

import darian.saric.rasus.model.Measurement;
import darian.saric.rasus.model.Sensor;
import darian.saric.rasus.model.Storage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

import static darian.saric.rasus.model.Storage.*;

/**
 * Resurs koji poslužuje REST zahtjeve
 */
@Path("sensor")
public class SensorResource {
    /**
     * Objekt za bilježenje sistemskih zapisa (logova)
     */
    private static final Logger LOGGER = LogManager.getLogger(SensorResource.class);

    /**
     * Registrira novi senzor kao aktivan. Kao parametar prima podatke o senzoru u JSON formatu.
     *
     * @return Vraća <tt>true</tt> ako je senzor uspješno registriran
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerNewSensor(final String jsonInput) {
        boolean status = false;
        if (jsonInput == null) {
            //nije predan JSON
            LOGGER.info("POST /central/sensor - \"\"");
            return Response.status(200).entity(status).build();
        }

        LOGGER.info("POST /central/sensor - " + jsonInput);
        try {
            JSONObject object = new JSONObject(jsonInput);
            Sensor sensor = new Sensor(
                    object.getString("username"),
                    object.getDouble("latitude"),
                    object.getDouble("longitude"),
                    object.getString("ip"),
                    object.getInt("port"));

            status = registerSensor(sensor);
            LOGGER.info((status ? "U" : "Neu") + "spješno registriran " + sensor);

        } catch (Exception e) {
            // predan neispravan JSON
            LOGGER.info(String.format("Neispravni podaci senzora: %s", jsonInput));
            return Response.status(200).entity(status).build();
        }

        return Response.status(201).entity(status).build();
    }

    /**
     * Deregistrira senzor za zadani username. Vraća 200 (OK) ako je senzor uspješno deregistriran.
     *
     * @param username korisničko ime senzora
     *
     * @return status uspješnosti deregistracije
     */
    @Path("/{username}")
    @DELETE
    public Response deregisterRemoteSensor(@PathParam("username") final String username) {
        LOGGER.info("DELETE /central/sensor/" + username);
        Sensor s = getSensorForName(username);

        if (s == null) {
            LOGGER.info("Ne postoji registriran senzor imena '" + username + "'");
            return Response.status(404).build();
        }

        if (deregisterSensor(s)) {
            LOGGER.info(String.format("Uspješno deregistriran %s", s));
            return Response.status(200).build();
        }

        LOGGER.info(String.format("Neuspješno deregistriran %s", s));
        return Response.status(500).build();
    }

    /**
     * Vraća podatke o senzoru koji je geografski najbliže senzoru s predanim imenom.
     *
     * @param username korisničko ime senzora
     *
     * @return najbliži senzor u JSON formatu
     */
    @Path("/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClosestSensor(@PathParam("username") final String username) {
        LOGGER.info(String.format("GET /central/sensor/%s", username));
        Sensor s = Storage.getClosestSensor(username);
        LOGGER.info(String.format("Najbliži senzor senzoru %s je %s", username, s == null ? "<null>" : s));
        return Response.status(200).entity(Objects.requireNonNullElse(s, "null")).build();

    }

    @Path("/{username}/measure")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postMeasurement(@PathParam("username") final String username, final String jsonInput) {
        LOGGER.info(String.format("POST /central/sensor/%s/measure %s", username, jsonInput));
        boolean status = false;
        Sensor s = getSensorForName(username);
        if (s == null) {
            LOGGER.info("Ne postoji registriran senzor imena '" + username + "'");
            return Response.status(404).entity(status).build();
        }

        try {
            JSONObject o = new JSONObject(jsonInput);
            Measurement m = new Measurement(
                    o.has("temperature") ? o.getInt("temperature") : null,
                    o.has("pressure") ? o.getInt("pressure") : null,
                    o.has("humidity") ? o.getInt("humidity") : null,
                    o.has("co") ? o.getInt("co") : null,
                    o.has("no2") ? o.getInt("no2") : null,
                    o.has("so2") ? o.getInt("so2") : null
            );
            status = storeMeasurement(username, m);
            LOGGER.info(String.format("Uspješno pohranjeno mjerenje %s za senzor %s", m, username));
        } catch (Exception e) {
            //neispravan json mjerenja
            LOGGER.info(String.format("Neispravni podaci mjerenja: %s", jsonInput));
            return Response.status(400).entity(status).build();
        }

        return Response.status(201).entity(status).build();
    }
}
