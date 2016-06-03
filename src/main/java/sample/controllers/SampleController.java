package sample.controllers;

import sample.util.SampleUtil;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by yoan on 2016/06/02.
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class SampleController {

    @Inject private SampleUtil sampleUtil;

    @Path("foo")
    @GET
    public String foo() {
        return "foo" +
                "<form action=baa method=POST><input type=submit name=btnName value=go></form>" +
                "<form action=hoge method=POST><input type=submit name=btnName value=gogogo></form>" +
                sampleUtil.getBaseUri();
    }

    @Path("baa")
    @POST
    public String baa(@FormParam("btnName") String btnName) {
        return "foo btnName = " + btnName + ", uri = " + sampleUtil.getBaseUri();
    }

    @Path("hoge")
    @POST
    public String hoge(@BeanParam Param param) {
        return "hoge btnName = " + param.getBtnName() + ", uri = " + sampleUtil.getBaseUri();
    }

    private static class Param {
        @FormParam("btnName")
        private String btnName;
        public void setBtnName(String btnName) {
            this.btnName = btnName;
        }

        public String getBtnName() {
            return btnName;
        }
    }

}
