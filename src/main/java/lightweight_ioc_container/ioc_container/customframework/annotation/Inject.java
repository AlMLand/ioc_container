package lightweight_ioc_container.ioc_container.customframework.annotation;

/**
 * Mark a variable as a target for injection
 */
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

@Retention(RUNTIME)
@Target({ FIELD, METHOD, CONSTRUCTOR })
public @interface Inject {

}
