package sample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.jvnet.hk2.guice.bridge.api.HK2IntoGuiceBridge;
import sample.util.SampleUtil;

import javax.inject.Inject;

/**
 * Created by yoan on 2016/06/02.
 */
public class MyApplication extends ResourceConfig {
    @Inject
    public MyApplication(ServiceLocator serviceLocator) {
        packages("sample.controllers");
        register(SampleUtil.class);

        Injector injector = Guice.createInjector(new HK2IntoGuiceBridge(serviceLocator));
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge g2h = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        g2h.bridgeGuiceInjector(injector);
    }

}
