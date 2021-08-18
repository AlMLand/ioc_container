package lightweight_ioc_container.ioc_container.customframework;

import static org.burningwave.core.assembler.StaticComponentContainer.Fields;

import java.lang.reflect.Field;
import java.util.Collection;

import org.burningwave.core.classes.FieldCriteria;

import lightweight_ioc_container.ioc_container.customframework.annotation.Inject;
import lightweight_ioc_container.ioc_container.customframework.annotation.Named;

public class CustomInjectorUtil {

	private CustomInjectorUtil() {
		super();
	}

	public static void inject(CustomInjector customInjector, Class<?> eachClass, Object classInstance) {
		Collection<Field> fields = Fields.findAllAndMakeThemAccessible(FieldCriteria
				.forEntireClassHierarchy().allThoseThatMatch(field -> field.isAnnotationPresent(Inject.class)), eachClass);
		collectionSearchForBeanWithAnnotationNamed(fields, customInjector, classInstance);
	}
	
	private static void collectionSearchForBeanWithAnnotationNamed(Collection<Field> fields,
			CustomInjector customInjector, Object classInstance) {
		for (Field field : fields) {
			String namedBean = field.isAnnotationPresent(Named.class) ? field.getAnnotation(Named.class).value() : null;
			Object fieldInstance = customInjector.getBeanInstance(field.getType(), field.getName(), namedBean);
			Fields.setDirect(classInstance, field, fieldInstance);
			inject(customInjector, fieldInstance.getClass(), fieldInstance);
		}
	}

}
