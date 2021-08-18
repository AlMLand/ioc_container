package lightweight_ioc_container.ioc_container.customframework.annotation;

/**
 * Mark class as 'bean'
 */
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Bean {

}
