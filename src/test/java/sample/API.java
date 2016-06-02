package sample;

import com.fasterxml.jackson.databind.JsonNode;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.GZipEncoder;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author yamakoshi
 *
 */
public class API {

    private URI baseUri;

    private String path;

    private MultivaluedMap<String, Object> queryParams = new MultivaluedHashMap<>();

    private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

    private MultivaluedMap<String, String> form = new MultivaluedHashMap<>();

    private List<Cookie> cookies = new ArrayList<>();

    private Entity<?> entity;

    private Entity<?> requestingEntity;

    private HttpAuthenticationFeature basic;

    public API(URI baseUri) {
        this.baseUri = baseUri;
    }

    public API pathAndQuery(String pathAndQuery) {
        if (pathAndQuery.contains("?")) {
            path = pathAndQuery.substring(0, pathAndQuery.indexOf("?"));

            String queryStr = pathAndQuery.substring(path.length() + 1);

            for (String param : queryStr.split("&")) {
                String[] kv = param.split("=");
                query(kv[0], kv[1]);
            }
        } else {
            path = pathAndQuery;
        }

        return this;
    }

    public API path(String path) {
        this.path = path;
        return this;
    }

    public API query(String key, Object ... values) {
        for (Object value : values) {
            queryParams.add(key, value);
        }
        return this;
    }

    public API header(String key, Object ... values) {
        headers.put(key, Arrays.asList(values));
        return this;
    }

    public API removeHeader(String key) {
        headers.remove(key);
        return this;
    }

    public API user(long userId) {
        return this;
    }

    public API user(Optional<Long> userIdOpt) {
        userIdOpt.ifPresent(userId -> {
        });
        return this;
    }

    public API form(String param, Object ... values) {
        for (Object value : values) {
            if (value != null) {
                form.add(param, value.toString());
            }
        }
        return this;
    }
    
    public API entity(Entity<?> entity) {
        this.entity = entity;
        return this;
    }

    public API entity(MultivaluedMap<String, String> form) {
        entity = Entity.form(form);
        return this;
    }

    public API entity(FormDataMultiPart formData) {
        entity = Entity.entity(formData, formData.getMediaType());
        return this;
    }

    public API cookie(String name, String value) {
        cookies.add(new Cookie(name, value));
        return this;
    }

    public API cookie(Cookie cookie) {
        cookies.add(cookie);
        return this;
    }

    public API basicAuth(String username, String password) {
        this.basic = HttpAuthenticationFeature.basic(username, password);
        return this;
    }

    public Response get() {
        return builder().get();
    }

    public <T> T get(Class<T> klass) {
        return builder().get(klass);
    }

    public <T> T get(GenericType<T> genericType) {
        return builder().get(genericType);
    }

    public Response post() {
        return builder().post(requestingEntity);
    }

    public <T> T post(Class<T> klass) {
        return builder().post(requestingEntity, klass);
    }

    public <T> T post(GenericType<T> genericType) {
        return builder().post(requestingEntity, genericType);
    }

    public Response put() {
        return builder().put(requestingEntity);
    }

    public <T> T put(Class<T> klass) {
        return builder().put(requestingEntity, klass);
    }

    public <T> T put(GenericType<T> genericType) {
        return builder().put(requestingEntity, genericType);
    }

    public Response delete() {
        return builder().delete();
    }

    public <T> T delete(Class<T> klass) {
        return builder().delete(klass);
    }

    public <T> T delete(GenericType<T> genericType) {
        return builder().delete(genericType);
    }

    private Builder builder() {
        WebTarget target = ClientBuilder.newClient()
                .register(MultiPartFeature.class)
                .register(JacksonFeature.class)
                .register(GZipEncoder.class)
                .property(ClientProperties.FOLLOW_REDIRECTS, false)
                .target(baseUri)
                .path(path);

        if (basic != null) {
            target.register(basic);
        }

        for (Entry<String, List<Object>> entry : queryParams.entrySet()) {
            target = target.queryParam(entry.getKey(), entry.getValue().toArray());
        }

        Builder builder = target
                .request(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML)
                .headers(headers);

        for (Cookie cookie : cookies) {
            builder = builder.cookie(cookie);
        }
        
        if (entity != null) {
            requestingEntity = entity;
        } else if (!form.isEmpty()) {
            requestingEntity = Entity.form(form);
        }

        return builder;
    }

}
