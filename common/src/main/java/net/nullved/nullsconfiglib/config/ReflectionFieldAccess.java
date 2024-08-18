/*
 * NullsConfigLib - A Config Library for Null's Mods
 * Copyright (C) 2024 NullVed
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
