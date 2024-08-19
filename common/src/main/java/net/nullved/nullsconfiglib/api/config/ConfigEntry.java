package net.nullved.nullsconfiglib.api.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigEntry {
    /**
     * The name of the config field.
     * Name translation will be at 'config.(modid).(config-name).(name)'
     */
    String name() default "";

    /**
     * If this field is required
     */
    boolean required() default true;

    /**
     * If this field can be null
     */
    boolean nullable() default false;
}
