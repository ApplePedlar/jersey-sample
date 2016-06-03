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

}
