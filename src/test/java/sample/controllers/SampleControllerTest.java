package sample.controllers;

import org.junit.ClassRule;
import org.junit.Test;
import sample.API;
import sample.EmbeddedGrizzly;
import sample.MyApplication;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

/**
 * Created by yoan on 2016/06/02.
 */
public class SampleControllerTest {
    @ClassRule
    public static EmbeddedGrizzly embeddedGrizzly = new EmbeddedGrizzly(MyApplication.class);

    @Test
    public void foo() throws Exception {
        assertThat(api("foo").get(String.class), startsWith("foo"));
    }

    @Test
    public void baa() throws Exception {
        String res = api("baa").form("btnName", "abc").post(String.class);
        assertThat(res, containsString("btnName = abc"));
    }

    @Test
    public void hoge() throws Exception {
        String res = api("hoge").form("btnName", "fuga").post(String.class);
        assertThat(res, containsString("btnName = fuga"));
    }

    protected API api(String pathAndQuery) {
        return new API(embeddedGrizzly.getBaseUri())
                .pathAndQuery(pathAndQuery);
    }

}