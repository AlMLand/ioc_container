package lightweight_ioc_container.ioc_container.service;

import lightweight_ioc_container.ioc_container.customframework.annotation.Bean;
import lightweight_ioc_container.ioc_container.customframework.annotation.Inject;
import lightweight_ioc_container.ioc_container.customframework.annotation.Named;

@Bean
public class ComponentClient {

	@Inject
	private ComponentAlphaService componentAlphaService;
	@Inject
	@Named("componentBetaServiceImpl")
	private ComponentBetaService componentBetaService;
	
	public void showAllComponentNames() {
		String componentAlphaServiceName = componentAlphaService.getComponentAlphaName();
		String componentBetaServiceName = componentBetaService.getComponentBetaName();
		System.out.printf("Alpha name: %s\nBeta name: %s", componentAlphaServiceName, componentBetaServiceName);
	}
	
}
