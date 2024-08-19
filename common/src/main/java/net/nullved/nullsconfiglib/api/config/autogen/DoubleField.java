package net.nullved.nullsconfiglib.api.config.autogen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DoubleField {
    double min() default -Double.MAX_VALUE;
    double max() default Double.MAX_VALUE;

    String format() default "%.2f";
}
