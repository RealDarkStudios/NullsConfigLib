package net.nullved.nullsconfiglib.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.impl.OptionGroup;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Supplier;

public interface IOptionGroup {
    Component name();
    IOptionDescription description();
    @Deprecated
    Component tooltip();
    @NotNull ImmutableList<? extends IOption<?>> options();
    boolean collapsed();
    boolean isRoot();

    static Builder createBuilder() {
        return new OptionGroup.Builder();
    }

    interface Builder extends IOptionAddable {
        /**
         * Sets name of the group, can be {@link Component#empty()} to just separate options, like sodium.
         *
         * @see IOptionGroup#name()
         */
        Builder name(@NotNull Component name);

        Builder description(@NotNull IOptionDescription description);

        /**
         * Adds an option to group.
         * To construct an option, use {@link IOption#createBuilder()}
         */
        @Override
        Builder option(@NotNull IOption<?> option);

        /**
         * Adds an option to this group.
         * To construct an option, use {@link IOption#createBuilder()}
         *
         * @param optionSupplier to be called to initialise the option. called immediately
         * @return this
         */
        @Override
        default Builder option(@NotNull Supplier<@NotNull IOption<?>> optionSupplier) {
            IOptionAddable.super.option(optionSupplier);
            return this;
        }

        /**
         * Adds an option to this group if a condition is met.
         * To construct an option, use {@link IOption#createBuilder()}
         *
         * @param condition only if true is the option added
         * @return this
         */
        @Override
        default Builder optionIf(boolean condition, @NotNull IOption<?> option) {
            IOptionAddable.super.optionIf(condition, option);
            return this;
        }

        /**
         * Adds an option to this group if a condition is met.
         * To construct an option, use {@link IOption#createBuilder()}
         *
         * @param condition only if true is the option added
         * @param optionSupplier to be called to initialise the option. called immediately only if condition is true
         * @return this
         */
        @Override
        default Builder optionIf(boolean condition, @NotNull Supplier<@NotNull IOption<?>> optionSupplier) {
            IOptionAddable.super.optionIf(condition, optionSupplier);
            return this;
        }

        /**
         * Adds multiple options to group.
         * To construct an option, use {@link IOption#createBuilder()}
         */
        @Override
        Builder options(@NotNull Collection<? extends IOption<?>> options);

        /**
         * Dictates if the group should be collapsed by default
         *
         * @see IOptionGroup#collapsed()
         */
        Builder collapsed(boolean collapsible);

        IOptionGroup build();
    }
}
