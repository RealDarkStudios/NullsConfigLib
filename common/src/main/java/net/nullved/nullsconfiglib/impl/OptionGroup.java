package net.nullved.nullsconfiglib.impl;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IListOption;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.IOptionDescription;
import net.nullved.nullsconfiglib.api.IOptionGroup;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApiStatus.Internal
public class OptionGroup implements IOptionGroup {
    private final @NotNull Component name;
    private final @NotNull IOptionDescription description;
    private final ImmutableList<? extends IOption<?>> options;
    private final boolean collapsed;
    private final boolean isRoot;

    public OptionGroup(@NotNull Component name, @NotNull IOptionDescription description,
                       ImmutableList<? extends IOption<?>> options, boolean collapsed, boolean isRoot) {
        this.name = name;
        this.description = description;
        this.options = options;
        this.collapsed = collapsed;
        this.isRoot = isRoot;
    }

    @Override
    public Component name() {
        return name;
    }

    @Override
    public IOptionDescription description() {
        return description;
    }

    @Override
    public Component tooltip() {
        return description.text();
    }

    @Override
    public @NotNull ImmutableList<? extends IOption<?>> options() {
        return options;
    }

    @Override
    public boolean collapsed() {
        return collapsed;
    }

    @Override
    public boolean isRoot() {
        return isRoot;
    }

    @ApiStatus.Internal
    public static final class Builder implements IOptionGroup.Builder {
        private Component name = Component.empty();
        private IOptionDescription description = IOptionDescription.EMPTY;
        private final List<IOption<?>> options = new ArrayList<>();
        private boolean collapsed = false;

        @Override
        public IOptionGroup.Builder name(@NotNull Component name) {
            Validate.notNull(name, "`name` must not be null");

            this.name = name;
            return this;
        }

        @Override
        public IOptionGroup.Builder description(@NotNull IOptionDescription description) {
            Validate.notNull(description, "`description` must not be null");

            this.description = description;
            return this;
        }

        @Override
        public IOptionGroup.Builder option(@NotNull IOption<?> option) {
            Validate.notNull(option, "`option` must not be null");

            if (option instanceof IListOption<?>)
                throw new UnsupportedOperationException("List options must not be added as an option but a group!");

            this.options.add(option);
            return this;
        }

        @Override
        public IOptionGroup.Builder options(@NotNull Collection<? extends IOption<?>> options) {
            Validate.notEmpty(options, "`options` must not be empty");

            if (options.stream().anyMatch(IListOption.class::isInstance))
                throw new UnsupportedOperationException("List options must not be added as an option but a group!");

            this.options.addAll(options);
            return this;
        }

        @Override
        public IOptionGroup.Builder collapsed(boolean collapsible) {
            this.collapsed = collapsible;
            return this;
        }

        @Override
        public OptionGroup build() {
            Validate.notEmpty(options, "`options` must not be empty to build `OptionGroup`");

            return new OptionGroup(name, description, ImmutableList.copyOf(options), collapsed, false);
        }
    }
}
