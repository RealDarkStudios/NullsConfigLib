package net.nullved.nullsconfiglib.impl;

import net.nullved.nullsconfiglib.api.IBinding;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GenericBinding<T> implements IBinding<T> {
    private final T def;
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public GenericBinding(T def, Supplier<T> getter, Consumer<T> setter) {
        this.def = def;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void setValue(T value) {
        setter.accept(value);
    }

    @Override
    public T getValue() {
        return getter.get();
    }

    @Override
    public T defaultValue() {
        return def;
    }
}
