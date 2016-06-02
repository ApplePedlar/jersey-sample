package sample;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.rules.ExternalResource;

import java.net.URI;

public class EmbeddedGrizzly extends ExternalResource {

    private final URI baseUri;
    private final Class<? extends ResourceConfig> clazz;
    private HttpServer server;

    public EmbeddedGrizzly(Class<? extends ResourceConfig> clazz) {
        this(String.format("http://localhost:1%04d/", (int) (Math.random() * 10000)), clazz);
    }

    public EmbeddedGrizzly(String baseUri, Class<? extends ResourceConfig> clazz) {
        this(URI.create(baseUri), clazz);
    }

    public EmbeddedGrizzly(URI baseUri, Class<? extends ResourceConfig> clazz) {
        this.baseUri = baseUri;
        this.clazz = clazz;
    }

    @Override
    protected void before() throws Throwable {
        this.server = GrizzlyHttpServerFactory.createHttpServer(baseUri, false);

        final WebappContext context = new WebappContext("webapp", "");

        ServletRegistration servletRegistration = context.addServlet("ServletContainer", ServletContainer.class);
        servletRegistration.addMapping("/*");
        servletRegistration.setInitParameter("javax.ws.rs.Application", clazz.getCanonicalName());

        // これを設定しないとエラーが起きた時のレスポンスがHTMLになってisErrorを使っているテストが全部こける
        servletRegistration.setInitParameter("jersey.config.server.response.setStatusOverSendError", "true");

        context.deploy(server);

        server.start();
    }

    @Override
    protected void after() {
        server.shutdownNow();
    }

    public URI getBaseUri() {
        return baseUri;
    }
}
