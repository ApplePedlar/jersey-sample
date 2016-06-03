package sample;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.HttpMethodOverrideFilter;
import org.jvnet.hk2.guice.bridge.api.HK2IntoGuiceBridge;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoan on 2016/06/02.
 */
public class MyApplication extends ResourceConfig {

    @Inject
    public MyApplication(ServiceLocator serviceLocator) {
        System.out.println("MyApplication constructor");
        packages(this.getClass().getPackage().getName() + ".controllers");
        GuiceListener.init(new HK2IntoGuiceBridge(serviceLocator));
    }

}
