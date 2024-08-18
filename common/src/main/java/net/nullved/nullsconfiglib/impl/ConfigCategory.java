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

package net.nullved.nullsconfiglib.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.*;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApiStatus.Internal
public final class ConfigCategory implements IConfigCategory {
    private final Component name;
    private final ImmutableList<IOptionGroup> groups;
    private final Component tooltip;

    public ConfigCategory(Component name, ImmutableList<IOptionGroup> groups, Component tooltip) {
        this.name = name;
        this.groups = groups;
        this.tooltip = tooltip;
    }

    @Override
    public @NotNull Component name() {
        return name;
    }

    @Override
    public @NotNull ImmutableList<IOptionGroup> groups() {
        return groups;
    }

    @Override
    public @NotNull Component tooltip() {
        return tooltip;
    }

    @ApiStatus.Internal
    public static final class Builder implements IConfigCategory.Builder {
        private Component name;

        private final List<IOption<?>> rootOptions = new ArrayList<>();
        private final RootGroupBuilder rootGroupBuilder = new RootGroupBuilder();

        private final List<IOptionGroup> groups = new ArrayList<>();

        private final List<Component> tooltipLines = new ArrayList<>();

        @Override
        public Builder name(@NotNull Component name) {
            Validate.notNull(name, "`name` cannot be null");

            this.name = name;
            return this;
        }

        @Override
        public Builder option(@NotNull IOption<?> option) {
            Validate.notNull(option, "`option` must not be null");

            if (option instanceof IListOption<?> listOption) {
                NullsConfigLib.LOGGER.warn("Adding list option as an option is not supported! Rerouting to group!");
                return group(listOption);
            }

            this.rootOptions.add(option);
            return this;
        }

        @Override
        public Builder options(@NotNull Collection<? extends IOption<?>> options) {
            Validate.notNull(options, "`options` must not be null");

            if (options.stream().anyMatch(IListOption.class::isInstance))
                throw new UnsupportedOperationException("List options must not be added as an option but a group!");

            this.rootOptions.addAll(options);
            return this;
        }

        @Override
        public Builder group(@NotNull IOptionGroup group) {
            Validate.notNull(group, "`group` must not be null");

            this.groups.add(group);
            return this;
        }

        @Override
        public Builder groups(@NotNull Collection<IOptionGroup> groups) {
            Validate.notEmpty(groups, "`groups` must not be empty");

            this.groups.addAll(groups);
            return this;
        }

        @Override
        public Builder tooltip(@NotNull Component... tooltips) {
            Validate.notEmpty(tooltips, "`tooltips` cannot be empty");

            tooltipLines.addAll(List.of(tooltips));
            return this;
        }

        @Override
        public IOptionGroup.Builder rootGroupBuilder() {
            return rootGroupBuilder;
        }

        @Override
        public ConfigCategory build() {
            Validate.notNull(name, "`name` must not be null to build `ConfigCategory`");

            List<IOptionGroup> combinedGroups = new ArrayList<>();
            combinedGroups.add(new OptionGroup(CommonComponents.EMPTY, OptionDescription.EMPTY, ImmutableList.copyOf(rootOptions), false, true));
            combinedGroups.addAll(groups);

            Validate.notEmpty(combinedGroups, "at least one option must be added to build `ConfigCategory`");

            MutableComponent concatenatedTooltip = Component.empty();
            boolean first = true;
            for (Component line : tooltipLines) {
                if (line.getContents() == CommonComponents.EMPTY.getContents())
                    continue;

                if (!first) concatenatedTooltip.append("\n");
                first = false;

                concatenatedTooltip.append(line);
            }

            return new ConfigCategory(name, ImmutableList.copyOf(combinedGroups), concatenatedTooltip);
        }

        private class RootGroupBuilder implements IOptionGroup.Builder {
            @Override
            public IOptionGroup.Builder name(@NotNull Component name) {
                throw new UnsupportedOperationException("Cannot set name of root group!");
            }

            @Override
            public IOptionGroup.Builder description(@NotNull IOptionDescription description) {
                throw new UnsupportedOperationException("Cannot set name of root group!");
            }

            @Override
            public IOptionGroup.Builder option(@NotNull IOption<?> option) {
                ConfigCategory.Builder.this.option(option);
                return this;
            }

            @Override
            public IOptionGroup.Builder options(@NotNull Collection<? extends IOption<?>> options) {
                ConfigCategory.Builder.this.options(options);
                return this;
            }

            @Override
            public IOptionGroup.Builder collapsed(boolean collapsible) {
                throw new UnsupportedOperationException("Cannot set collapsible of root group!");
            }

            @Override
            public IOptionGroup build() {
                throw new UnsupportedOperationException("Cannot build root group!");
            }
        }
    }
}
