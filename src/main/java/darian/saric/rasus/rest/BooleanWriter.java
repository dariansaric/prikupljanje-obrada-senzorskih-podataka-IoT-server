package darian.saric.rasus.rest;

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
public class BooleanWriter implements MessageBodyWriter<Boolean> {

    private static byte[] toData(Boolean b) {
        return b == null ? null :
                new JSONObject().putOnce("status", Boolean.valueOf(b ? "true" : "false"))
                        .toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return type.equals(Boolean.class);
    }


    @Override
    public void writeTo(Boolean aBoolean, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        outputStream.write(toData(aBoolean));
    }

    @Override
    public long getSize(Boolean aBoolean, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return toData(aBoolean).length;
    }
}
