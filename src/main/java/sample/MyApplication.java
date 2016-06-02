package sample;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.HttpMethodOverrideFilter;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.jvnet.hk2.guice.bridge.api.HK2IntoGuiceBridge;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoan on 2016/06/02.
 */
public class MyApplication extends ResourceConfig {
    private final String ROOT_PACKAGE = this.getClass().getPackage().getName();
    private final String CONTROLLERS = ROOT_PACKAGE + ".controllers";

    @Inject
    public MyApplication(ServiceLocator serviceLocator) {
        this(serviceLocator, getModule(serviceLocator));
    }

    public MyApplication(ServiceLocator serviceLocator, Module... modules) {
        packages(CONTROLLERS);

        register(JacksonFeature.class);
        register(JacksonJaxbJsonProvider.class);
        register(MultiPartFeature.class);
        register(ValidationFeature.class);
        register(HttpMethodOverrideFilter.class);

        GuiceListener.init(serviceLocator, modules);
    }

    private static Module getModule(ServiceLocator serviceLocator) {
        List<Module> result = new ArrayList<>();
        if (serviceLocator != null) {
            result.add(new HK2IntoGuiceBridge(serviceLocator));
        }
        return Modules.combine(result);
    }
}