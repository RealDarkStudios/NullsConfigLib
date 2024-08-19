package net.nullved.nullsconfiglib.config;

import net.nullved.nullsconfiglib.api.config.IFieldAccess;
import net.nullved.nullsconfiglib.config.autogen.NCLAutoGenException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Optional;

public record ReflectionFieldAccess<T>(Field field, Object instance) implements IFieldAccess<T> {
    @Override
    public T get() {
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new NCLAutoGenException("Failed to access field '%s'".formatted(name()), e);
        }
    }

    @Override
    public void set(T value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new NCLAutoGenException("Failed to set field '%s'".formatted(name()), e);
        }
    }

    @Override
    public String name() {
        return field.getName();
    }

    @Override
    public Type type() {
        return field.getGenericType();
    }

    @Override
    public Class<T> typeClass() {
        return (Class<T>) field.getType();
    }

    @Override
    public <A extends Annotation> Optional<A> getAnnotation(Class<A> annotationClass) {
        return Optional.ofNullable(field.getAnnotation(annotationClass));
    }
}
