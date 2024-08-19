package net.nullved.nullsconfiglib.api.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * An abstract interface for accessing properties of an instance of a field.
 *
 * @param <T> the type of the field
 */
public interface IReadOnlyFieldAccess<T> {
    /**
     * @return the current value of the field.
     */
    T get();

    /**
     * @return the name of the field.
     */
    String name();

    /**
     * @return the type of the field.
     */
    Type type();

    /**
     * @return the class of the field.
     */
    Class<T> typeClass();

    <A extends Annotation> Optional<A> getAnnotation(Class<A> annotationClass);
}