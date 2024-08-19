package net.nullved.nullsconfiglib.config;

import net.nullved.nullsconfiglib.api.IBinding;
import net.nullved.nullsconfiglib.api.config.IFieldAccess;
import net.nullved.nullsconfiglib.api.config.IReadOnlyFieldAccess;

public record FieldBackedBinding<T>(IFieldAccess<T> field, IReadOnlyFieldAccess<T> defaultField) implements IBinding<T> {
    @Override
    public T getValue() {
        return field.get();
    }

    @Override
    public void setValue(T value) {
        field.set(value);
    }

    @Override
    public T defaultValue() {
        return defaultField.get();
    }
}
