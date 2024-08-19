package net.nullved.nullsconfiglib.api;

import net.minecraft.client.OptionInstance;
import net.nullved.nullsconfiglib.impl.GenericBinding;
import net.nullved.nullsconfiglib.mixin.OptionInstanceAccessor;
import org.apache.commons.lang3.Validate;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An interface for modifying the value, as well as providing the default value
 * @param <T> The Type for this binding
 */
public interface IBinding<T> {
    void setValue(T value);
    T getValue();
    T defaultValue();

    /**
     * Returns a new binding, converting to a type of {@link U}
     * @param to A converter from {@link T} to {@link U}, used for the default value / getter
     * @param from A converter from {@link U} to {@link T}, used for the setter
     * @return A new generic binding with type {@link U}
     * @param <U> The type to convert to
     */
    default <U> IBinding<U> xmap(Function<T, U> to, Function<U, T> from) {
        return IBinding.generic(to.apply(this.defaultValue()), () -> to.apply(getValue()), v -> this.setValue(from.apply(v)));
    }

    /**
     * Creates a generic binding
     * @param def The default value for this binding
     * @param getter The getter {@link Supplier<T>}, should return the current value
     * @param setter The setter {@link Consumer<T>}, should set to option to the value
     * @return A new generic binding of type {@link T}
     * @param <T> The type for this binding
     */
    static <T> IBinding<T> generic(T def, Supplier<T> getter, Consumer<T> setter) {
        Validate.notNull(def, "`def` must not be null");
        Validate.notNull(getter, "`getter` must not be null");
        Validate.notNull(setter, "`setter` must not be null");

        return new GenericBinding<>(def, getter, setter);
    }

    static <T> IBinding<T> minecraft(OptionInstance<T> minecraftOption) {
        Validate.notNull(minecraftOption, "`minecraftOption` must not be null");

        return new GenericBinding<>(
                ((OptionInstanceAccessor<T>) (Object) minecraftOption).getInitialValue(),
                minecraftOption::get,
                minecraftOption::set
        );
    }

    static <T> IBinding<T> immutable(T value) {
        Validate.notNull(value, "`value` must not be null");

        return new GenericBinding<>(
                value,
                () -> value,
                changed -> {}
        );
    }
}