package net.nullved.nullsconfiglib.api.config.autogen;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ListGroup {
    Class<? extends IValueFactory<?>> valueFactory();
    Class<? extends IControllerFactory<?>> controllerFactory();

    int maxEntries() default 0;
    int minEntries() default 0;

    boolean addEntriesToBottom() default false;

    interface IValueFactory<T> {
        T provideNewValue();
    }

    interface IControllerFactory<T> {
        IControllerBuilder<T> createController(ListGroup annotation, IConfigField<List<T>> field, IOptionAccess storage, IOption<T> option);
    }
}
