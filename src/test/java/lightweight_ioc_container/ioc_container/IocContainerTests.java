package lightweight_ioc_container.ioc_container;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lightweight_ioc_container.ioc_container.customframework.CustomInjector;
import lightweight_ioc_container.ioc_container.service.ComponentClient;

public class IocContainerTests {
	
	private ComponentClient componentClient;
	
	@Before
	public void setUp() {
		CustomInjector.startApplication(MainClass.class);
		componentClient = CustomInjector.getService(ComponentClient.class);
	}
	
	@After
	public void tearDown() {
		CustomInjector.endApplicationInitCustomInjectorWithNull();
	}
	
	@Test
    public void shouldAnswerWithTrueWhenComponentAlphaCallMethodGetComponentAlphaName() {
		assertEquals("I am a Alpha", componentClient.getComponentAlphaService().getComponentAlphaName());
    }
	
	@Test
    public void shouldAnswerWithTrueWhenComponentBetaCallMethodGetComponentBetaName() {
		assertEquals("I am a Beta", componentClient.getComponentBetaService().getComponentBetaName());
    }
	
}
