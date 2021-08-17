package lightweight_ioc_container.ioc_container.service;

import lightweight_ioc_container.ioc_container.customframework.annotation.Bean;

@Bean
public class ComponentAlphaServiceImpl implements ComponentAlphaService {

	@Override
	public String getComponentAlphaName() {
		return "I am a Alpha";
	}

}
