package net.nullved.nullsconfiglib.api.config.autogen;

import net.nullved.nullsconfiglib.api.controller.IValueFormatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomFormat {
    Class<? extends IValueFormatter<?>> value();
}
