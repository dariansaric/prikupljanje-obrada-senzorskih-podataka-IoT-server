package darian.saric.rasus.rest;

import darian.saric.rasus.model.Measurement;
import darian.saric.rasus.model.Sensor;
import darian.saric.rasus.model.Storage;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

import static darian.saric.rasus.model.Storage.*;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("sensor")
public class SensorResource {
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
            return Response.status(200).entity(JSONObject.wrap(status)).build();
        }

        try {
            JSONObject object = new JSONObject(jsonInput);
            Sensor sensor = new Sensor(
                    object.getString("username"),
                    object.getDouble("latitude"),
                    object.getDouble("longitude"),
                    object.getString("ip"),
                    object.getInt("port"));

            status = registerSensor(sensor);

        } catch (Exception e) {
            // predan neispravan JSON
            return Response.status(200).entity(JSONObject.wrap(status)).build();
        }

        return Response.status(200).entity(JSONObject.wrap(status)).build();
//        throw new UnsupportedOperationException();
    }

    @Path("/{username}")
    @DELETE
    public Response deregisterNewSensor(@PathParam("username") final String username) {
        Sensor s = getSensorForName(username);

        if (s == null) {
            return Response.status(404).build();
        }

        deregisterSensor(s);
        return Response.status(200).build();
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
        Sensor s = Storage.getClosestSensor(username);
        return Response.status(200).entity(Objects.requireNonNullElse(s, null)).build();

    }

    @Path("/{username}/measure")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postMeasurement(@PathParam("username") final String username, final String jsonInput) {
        boolean status = false;
        Sensor s = getSensorForName(username);
        if (s == null) {
            return Response.status(200).entity(JSONObject.wrap(status)).build();
        }

        try {
            JSONObject object = new JSONObject(jsonInput);
            Measurement m = new Measurement(
                    object.getString("parameter"),
                    object.getDouble("value"));

            status = storeMeasurement(username, m);
        } catch (Exception e) {
            //neispravan json mjerenja
            return Response.status(200).entity(JSONObject.wrap(status)).build();
        }

        return Response.status(200).entity(JSONObject.wrap(status)).build();
    }
}
