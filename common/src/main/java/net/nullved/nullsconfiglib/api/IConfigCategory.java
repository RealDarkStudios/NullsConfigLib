package net.nullved.nullsconfiglib.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.impl.ConfigCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Supplier;

public interface IConfigCategory {
    @NotNull Component name();
    @NotNull ImmutableList<IOptionGroup> groups();
    @NotNull Component tooltip();

    static Builder createBuilder() {
        return new ConfigCategory.Builder();
    }

    interface Builder extends IOptionAddable {
        /**
         * Sets name of the category
         *
         * @see IConfigCategory#name()
         */
        Builder name(@NotNull Component name);

        /**
         * Adds an option to the root group of the category.
         * To add to another group, use {@link Builder#group(IOptionGroup)}.
         * To construct an option, use {@link IOption#createBuilder()}
         *
         * @see IConfigCategory#groups()
         * @see IOptionGroup#isRoot()
         */
        @Override
        Builder option(@NotNull IOption<?> option);

        /**
         * Adds an option to the root group of the category.
         * To add to another group, use {@link Builder#group(IOptionGroup)}.
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
         * Adds an option to the root group of the category if a condition is met.
         * To add to another group, use {@link Builder#group(IOptionGroup)}.
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
         * Adds an option to the root group of the category if a condition is met.
         * To add to another group, use {@link Builder#group(IOptionGroup)}.
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
         * Adds multiple options to the root group of the category.
         * To add to another group, use {@link Builder#groups(Collection)}.
         * To construct an option, use {@link IOption#createBuilder()}
         *
         * @see IConfigCategory#groups()
         * @see IOptionGroup#isRoot()
         */
        @Override
        Builder options(@NotNull Collection<? extends IOption<?>> options);

        /**
         * Adds an option group.
         * To add an option to the root group, use {@link Builder#option(IOption)}
         * To construct a group, use {@link IOptionGroup#createBuilder()}
         */
        Builder group(@NotNull IOptionGroup group);

        /**
         * Adds multiple option groups.
         * To add multiple options to the root group, use {@link Builder#options(Collection)}
         * To construct a group, use {@link IOptionGroup#createBuilder()}
         */
        Builder groups(@NotNull Collection<IOptionGroup> groups);

        /**
         * Fetches the builder for the root group of the category.
         * This is the group that has no header and options are added through {@link Builder#option(IOption)}.
         * In its default implementation, this builder is severely limited and a lot of methods are unsupported.
         */
        IOptionGroup.Builder rootGroupBuilder();

        /**
         * Sets the tooltip to be used by the category.
         * Can be invoked twice to append more lines.
         * No need to wrap the text yourself, the gui does this itself.
         *
         * @param tooltips text lines - merged with a new-line on {@link Builder#build()}.
         */
        Builder tooltip(@NotNull Component... tooltips);

        IConfigCategory build();
    }
}
