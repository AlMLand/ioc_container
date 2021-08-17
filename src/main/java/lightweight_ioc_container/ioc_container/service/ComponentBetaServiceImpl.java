package lightweight_ioc_container.ioc_container.service;

import lightweight_ioc_container.ioc_container.customframework.annotation.Bean;

@Bean
public class ComponentBetaServiceImpl implements ComponentBetaService {

	@Override
	public String getComponentBetaName() {
		return "I am a Beta";
	}

}
