package sample;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.message.GZipEncoder;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;

/**
 * @author yamakoshi
 *
 */
public class API {

    private URI baseUri;

    private String path;

    private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

    private MultivaluedMap<String, String> form = new MultivaluedHashMap<>();

    private Entity<?> requestingEntity;

    public API(URI baseUri) {
        this.baseUri = baseUri;
    }

    public API form(String param, Object ... values) {
        for (Object value : values) {
            if (value != null) {
                form.add(param, value.toString());
            }
        }
        return this;
    }

    public API path(String path) {
        this.path = path;
        return this;
    }


    public <T> T get(Class<T> klass) {
        return builder().get(klass);
    }
    public <T> T post(Class<T> klass) {
        return builder().post(requestingEntity, klass);
    }

    private Builder builder() {
        WebTarget target = ClientBuilder.newClient()
                .register(GZipEncoder.class)
                .property(ClientProperties.FOLLOW_REDIRECTS, false)
                .target(baseUri)
                .path(path);

        Builder builder = target
                .request(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML)
                .headers(headers);

        requestingEntity = Entity.form(form);

        return builder;
    }

}
