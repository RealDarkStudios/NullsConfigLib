package net.nullved.nullsconfiglib.api.config.autogen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntField {
    int min() default -Integer.MAX_VALUE;
    int max() default Integer.MAX_VALUE;
}
