package sample.util;

import org.jvnet.hk2.guice.bridge.api.HK2Inject;

import javax.ws.rs.core.UriInfo;

/**
 * Created by yoan on 2016/06/03.
 */
public class SampleUtil {
    @HK2Inject UriInfo uriInfo;

    public String getBaseUri() {
        return uriInfo.getBaseUri().toString();
    }
}
