package darian.saric.rasus.rest;

import darian.saric.rasus.model.Sensor;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("sensor")
public class SensorResource {
    // TODO: pospremi mejerenje

    /**
     * Registrira novi senzor kao aktivan. Kao parametar prima podatke o senzoru u JSON formatu.
     *
     * @return Vraća <tt>true</tt> ako je senzor uspješno registriran
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerNewSensor(final String jsonInput) {
        if (jsonInput == null) {
            //nije predan JSON
            return Response.status(200).entity(JSONObject.wrap(false)).build();
        }

        // TODO: pohrani senzor
        try {
            JSONObject object = new JSONObject(jsonInput);
            Sensor sensor = new Sensor(
                    object.getString("username"),
                    object.getDouble("latitude"),
                    object.getDouble("longitude"),
                    object.getString("ip"),
                    object.getInt("port"));

            System.out.println("Dodan senzor: " + sensor.getUsername());
        } catch (Exception e) {
            // predan neispravan JSON
            return Response.status(200).entity(JSONObject.wrap(false)).build();
        }

//        return Response.status(200).entity(JSONObject.wrap(true)).build();
        throw new UnsupportedOperationException();
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
        // TODO: implementiraj getClosestSensor
        throw new UnsupportedOperationException();
    }

    @Path("/{username}/measure")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postMeasurement(@PathParam("username") final String username, final String jsonInput) {
        // tODO: implementiraj postMeasurement
        throw new UnsupportedOperationException();
    }
}
