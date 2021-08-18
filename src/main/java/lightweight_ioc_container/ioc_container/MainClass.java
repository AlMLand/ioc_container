package lightweight_ioc_container.ioc_container;

import lightweight_ioc_container.ioc_container.customframework.CustomInjector;
import lightweight_ioc_container.ioc_container.service.ComponentClient;

public class MainClass {

	public static void main(String[] args) {
		
		CustomInjector.startApplication(MainClass.class);
		ComponentClient componentClient = CustomInjector.getService(ComponentClient.class);
		componentClient.showAllComponentNames();
		CustomInjector.endApplicationInitCustomInjectorWithNull();
		
	}

}
