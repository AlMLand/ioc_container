package lightweight_ioc_container.ioc_container;

import static org.junit.Assert.*;

import org.junit.*;

import lightweight_ioc_container.ioc_container.customframework.CustomInjector;
import lightweight_ioc_container.ioc_container.service.*;

public class IocContainerTests {
	
	@Before
	public void setUp() {
		CustomInjector.startApplication(MainClass.class);
	}
	
	@After
	public void tearDown() {
		CustomInjector.endApplicationInitCustomInjectorWithNull();
	}
	
	@Test
    public void shouldAnswerWithTrueWhenComponentAlphaCallMethodGetComponentAlphaName() {
		ComponentClient componentClient = CustomInjector.getService(ComponentClient.class);
		assertEquals("I am a Alpha", componentClient.getComponentAlphaService().getComponentAlphaName());
    }
	
	@Test
    public void shouldAnswerWithTrueWhenComponentBetaCallMethodGetComponentBetaName() {
		ComponentClient componentClient = CustomInjector.getService(ComponentClient.class);
		assertEquals("I am a Beta", componentClient.getComponentBetaService().getComponentBetaName());
    }
	
	@Test
	public void shouldReturnTrueAlphaServiceNotNullAndAlphaServiceCallGetComponentAlphaName() {
		ComponentAlphaService componentAlphaService = CustomInjector.getService(ComponentAlphaService.class);
		assertNotNull(componentAlphaService);
		assertEquals("I am a Alpha", componentAlphaService.getComponentAlphaName());
	}
	
	@Test
	public void shouldReturnTrueBetaServiceNotNullAndBetaServiceCallGetComponentBetaName() {
		ComponentBetaService componentBetaService = CustomInjector.getService(ComponentBetaService.class);
		assertNotNull(componentBetaService);
		assertEquals("I am a Beta", componentBetaService.getComponentBetaName());
	}
	
	@Test
	public void shouldReturnTrueClient_1NotNullAndClient_2NotNullAndClient_1EqualsClient_2() {
		ComponentClient client_1 = CustomInjector.getService(ComponentClient.class);
		assertNotNull(client_1);
		ComponentClient client_2 = CustomInjector.getService(ComponentClient.class);
		assertNotNull(client_2);
		assertEquals(client_1, client_2);
	}
	
}
