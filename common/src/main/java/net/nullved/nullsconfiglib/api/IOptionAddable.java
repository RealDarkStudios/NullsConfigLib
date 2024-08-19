package net.nullved.nullsconfiglib.api;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Supplier;

public interface IOptionAddable {
    IOptionAddable option(@NotNull IOption<?> option);

    default IOptionAddable option(@NotNull Supplier<@NotNull IOption<?>> optionSupplier) {
        return option(optionSupplier.get());
    }

    default IOptionAddable optionIf(boolean condition, @NotNull IOption<?> option) {
        return condition ? option(option) : this;
    }

    /**
     * Adds an option to an abstract builder if a condition is met.
     * To construct an option, use {@link IOption#createBuilder()}
     * @param condition only if true is the option added
     * @param optionSupplier to be called to initialise the option. called immediately only if condition is true
     * @return this
     */
    default IOptionAddable optionIf(boolean condition, @NotNull Supplier<@NotNull IOption<?>> optionSupplier) {
        return condition ? option(optionSupplier) : this;
    }

    /**
     * Adds multiple options to an abstract builder.
     * To construct an option, use {@link IOption#createBuilder()}
     */
    IOptionAddable options(@NotNull Collection<? extends IOption<?>> options);
}
