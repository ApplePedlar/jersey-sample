package sample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.jvnet.hk2.guice.bridge.api.HK2IntoGuiceBridge;

public class GuiceListener extends GuiceServletContextListener {

    private static Injector injector;

    public static Injector injector() {
        return injector;
    }
    
    @Override
    protected Injector getInjector() {
        if (injector == null) {
            init();
        }
        return injector;
    }
    
    public static void init(Module ... module) {
        injector = Guice.createInjector(module);
    }

    public static void init(ServiceLocator serviceLocator, Module ... applicationModules) {
        Module allModules[] = new Module[applicationModules.length + 1];

        allModules[0] = new HK2IntoGuiceBridge(serviceLocator);
        for (int lcv = 0; lcv < applicationModules.length; lcv++) {
            allModules[lcv + 1] = applicationModules[lcv];
        }

        injector = Guice.createInjector(allModules);

        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge g2h = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        g2h.bridgeGuiceInjector(injector);
    }
}
