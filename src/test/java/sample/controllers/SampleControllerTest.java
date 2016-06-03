package sample.controllers;

import org.junit.ClassRule;
import org.junit.Test;
import sample.EmbeddedGrizzly;
import sample.MyApplication;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * Created by yoan on 2016/06/02.
 */
public class SampleControllerTest {
    @ClassRule
    public static EmbeddedGrizzly embeddedGrizzly = new EmbeddedGrizzly(MyApplication.class);

    @Test
    public void foo() throws Exception {
        String res = get("foo");
        assertThat(res, startsWith("foo"));
        assertThat(res, containsString("http://localhost:8090/"));
    }

    @Test
    public void baa() throws Exception {
        MultivaluedMap<String, String> form = new MultivaluedHashMap<String, String>() {{
            add("btnName", "abc");
        }};
        String res = post("baa", form);
        assertThat(res, containsString("btnName = abc"));
        assertThat(res, containsString("http://localhost:8090/"));
    }

    @Test
    public void hoge() throws Exception {
        MultivaluedMap<String, String> form = new MultivaluedHashMap<String, String>() {{
            add("btnName", "fuga");
        }};
        String res = post("hoge", form);
        assertThat(res, containsString("btnName = fuga"));
        assertThat(res, containsString("http://localhost:8090/"));
    }

    private String get(String path) {
        return ClientBuilder.newClient()
                .target(embeddedGrizzly.getBaseUri())
                .path(path)
                .request()
                .get(String.class);
    }

    private String post(String path, MultivaluedMap<String, String> form) {
        return ClientBuilder.newClient()
                .target(embeddedGrizzly.getBaseUri())
                .path(path)
                .request()
                .post(Entity.form(form), String.class);
    }

}