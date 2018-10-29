package darian.saric.rasus.rest;

import darian.saric.rasus.model.Sensor;
import org.json.JSONObject;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class SensorWriter implements MessageBodyWriter<Sensor> {

    private static byte[] toData(Sensor s) {
        return s == null ? null :
                JSONObject.wrap(s).toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return type.equals(Sensor.class);
    }

    @Override
    public long getSize(Sensor sensor, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return toData(sensor).length;
    }

    @Override
    public void writeTo(Sensor sensor, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        outputStream.write(toData(sensor));
    }
}
