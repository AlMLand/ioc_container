package lightweight_ioc_container.ioc_container.customframework.annotation;

/**
 * Specifies the object of injection
 * Apply together with the annotation @Inject
 */
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
public @interface Named {
	
	String value() default "";

}
