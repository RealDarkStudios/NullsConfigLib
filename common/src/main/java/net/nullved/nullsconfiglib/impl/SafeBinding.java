package net.nullved.nullsconfiglib.impl;

import net.nullved.nullsconfiglib.api.IBinding;

import java.util.Objects;

public class SafeBinding<T> implements IBinding<T> {
    private final IBinding<T> binding;

    public SafeBinding(IBinding<T> binding) {
        this.binding = binding;
    }

    @Override
    public T getValue() {
        return Objects.requireNonNull(binding.getValue());
    }

    @Override
    public void setValue(T value) {
        binding.setValue(Objects.requireNonNull(value));
    }

    @Override
    public T defaultValue() {
        return Objects.requireNonNull(binding.defaultValue());
    }
}
