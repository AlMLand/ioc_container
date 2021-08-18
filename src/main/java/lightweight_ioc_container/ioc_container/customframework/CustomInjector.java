package lightweight_ioc_container.ioc_container.customframework;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.CacheableSearchConfig;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.ClassHunter.SearchResult;
import org.burningwave.core.classes.SearchConfig;

import lightweight_ioc_container.ioc_container.customframework.annotation.Bean;

/**
 * Responsible to create instances of all clients and inject instances for each service in client classes
 */
public class CustomInjector {

	/**
	 * Map all the client classes
	 */
	private Map<Class<?>, Class<?>> clientClassesMap;
	private Map<Class<?>, Object> applicationScope;
	private static CustomInjector customInjector;
	private static final Lock lock = new ReentrantLock(); 

	public CustomInjector() {
		super();
		clientClassesMap = new HashMap<>();
		applicationScope = new HashMap<>();
	}
	
	
	public static <T> T getService(Class<T> clientClass) {
		return customInjector.getBeanInstance(clientClass);
	}
	
	/**
	 * Start application
	 * @param applicationMainClass
	 */
	public static void startApplication(Class<?> applicationMainClass) {
		lock.lock();
		try {
			 customInjector = initializationCustomInjector(applicationMainClass);
			 customInjector.initializationFramework(applicationMainClass);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Hide from public usage
	 * @param applicationMainClass
	 * @return singleton from CustomerInjector
	 */
	private static CustomInjector initializationCustomInjector(Class<?> applicationMainClass) {
		if(customInjector == null) {
			return new CustomInjector();
		}
		return customInjector;
	}
	
	/**
	 * Initialization from CustomerInjector with null value
	 * End application
	 */
	public static void endApplicationInitCustomInjectorWithNull() {
		customInjector = null;
	}
	
	/**
	 * Initialize the CustomerInjector framework
	 * Hide from public usage
	 */
	private void initializationFramework(Class<?> applicationMainClass) {
		Class<?>[] classes = getAllClasses(applicationMainClass.getPackageName(), true);
		ComponentContainer componentContainer = ComponentContainer.getInstance();
		ClassHunter classHunter = componentContainer.getClassHunter();
		String packagePath = applicationMainClass.getPackageName().replace(".", "/");
		
		try(SearchResult searchResult = classHunter.findBy(SearchConfig
				.forResources(packagePath)
				.by(ClassCriteria.create().allThoseThatMatch(cls -> { return cls.getAnnotation(Bean.class) != null; } )))) {
			Collection<Class<?>> types = searchResult.getClasses();
			sortClassesByTypes(types);
			sortClassesByAnnotation(classes);
		}
	}
	
	
	private void sortClassesByTypes(Collection<Class<?>> types) {
		for(Class<?> implementedClass : types) {
			Class<?>[] interfaces = implementedClass.getInterfaces();
			if(interfaces.length == 0) {
				clientClassesMap.put(implementedClass, implementedClass);
			} else {
				for(Class<?> iface : interfaces) {
					clientClassesMap.put(implementedClass, iface);
				}
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private void sortClassesByAnnotation(Class<?>[] classes) {
		for(Class<?> eachClass : classes) {
			if(eachClass.isAnnotationPresent(Bean.class)) {
				Object classInstance = null;
				try {
					classInstance = eachClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				applicationScope.put(eachClass, classInstance);
				CustomInjectorUtil.inject(this, eachClass, classInstance);
			}
		}
	}

	/**
	 * Hide from public usage
	 * Get all the classes for the input package
	 */
	private Class<?>[] getAllClasses(String packageName, boolean recursive) {
		ComponentContainer componentContainer = ComponentContainer.getInstance();
		ClassHunter classHunter = componentContainer.getClassHunter();
		String packagePath = packageName.replace(".", "/");
		CacheableSearchConfig searchConfig = SearchConfig.forResources(packagePath);
		
		if(!recursive) {
			searchConfig.notRecursiveOnPath(packagePath, false);
		}
		
		try(SearchResult searchResult = classHunter.loadInCache(searchConfig).find()) {
			Collection<Class<?>> classes = searchResult.getClasses();
			return classes.toArray(new Class[classes.size()]);
		}
	}
	
	/**
	 * Create and get the Object instance of the implementation class for input
	 * interface service
	 * 
	 * Hide from public usage
	 */
	@SuppressWarnings("unchecked")
	private <T> T getBeanInstance(Class<T> interfaceClass) {
		return (T) getBeanInstance(interfaceClass, null, null);
	}

	/**
	 * Overload getBeanInstance to handle named search and injection by type
	 */
	@SuppressWarnings("deprecation")
	public <T> Object getBeanInstance(Class<T> interfaceClass, String fieldName, String beanName) {
		Class<?> implementationClass = getImplementationClass(interfaceClass, fieldName, beanName);
		if(applicationScope.containsKey(implementationClass)) {
			return applicationScope.get(implementationClass);
		}
		synchronized (applicationScope) {
			Object service = null;
			try {
				service = implementationClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			applicationScope.put(implementationClass, service);
			return service;
		}
	}

	/**
	 * Get the name of the implementation class for input interface service
	 * Hide from public usage
	 */
	private Class<?> getImplementationClass(Class<?> interfaceClass, final String fieldName, final String beanName) {
		Set<Entry<Class<?>, Class<?>>> implementationClasses = clientClassesMap.entrySet().stream()
				.filter(entry -> entry.getValue() == interfaceClass).collect(Collectors.toSet());
		String errorMessage = "";
		if(implementationClasses == null || implementationClasses.size() == 0) {
			errorMessage = "No implementation found for interface " + interfaceClass.getName();
		} else if (implementationClasses.size() == 1) {
			Optional<Entry<Class<?>, Class<?>>> optional = implementationClasses.stream().findFirst();
			if(optional.isPresent()) {
				return optional.get().getKey();
			}
		} else if (implementationClasses.size() > 1) {
			final String findBy = (beanName == null || beanName.trim().length() == 0) ? fieldName : beanName;
			Optional<Entry<Class<?>, Class<?>>> optional = implementationClasses.stream()
					.filter(entry -> entry.getKey().getSimpleName().equalsIgnoreCase(findBy)).findAny();
			if(optional.isPresent()) {
				return optional.get().getKey();
			}
		} else {
			errorMessage = "There are " + implementationClasses.size() + " of interface " + interfaceClass.getName() +
					" the conflict must be resolved";
		}
		throw new RuntimeErrorException(new Error(errorMessage));
	}
	
}
