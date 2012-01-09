package de.zalando.storedprocedurewrapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author  jmussler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface SprocCall {
    String name() default "";

    String sql() default "";

    Class shardStrategy() default Void.class;
}
