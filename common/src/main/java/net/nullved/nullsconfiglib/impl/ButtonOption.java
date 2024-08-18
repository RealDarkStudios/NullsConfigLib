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

import com.google.common.collect.ImmutableSet;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.*;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.gui.controllers.ActionController;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

@ApiStatus.Internal
public class ButtonOption implements IButtonOption {
    private final Component name;
    private final IOptionDescription description;
    private final BiConsumer<NCLScreen, IButtonOption> action;
    private boolean available;
    private final IController<BiConsumer<NCLScreen, IButtonOption>> controller;
    private final IBinding<BiConsumer<NCLScreen, IButtonOption>> binding;

    public ButtonOption(@NotNull Component name, @Nullable IOptionDescription description,
                        @NotNull BiConsumer<NCLScreen, IButtonOption> action, @Nullable Component text, boolean available) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.available = available;
        this.controller = text != null ? new ActionController(this, text) : new ActionController(this);
        this.binding = new EmptyBinder();
    }

    @Override
    public @NotNull Component name() {
        return name;
    }

    @Override
    public @NotNull IOptionDescription description() {
        return description;
    }

    @Override
    public @NotNull Component tooltip() {
        return description.text();
    }

    @Override
    public BiConsumer<NCLScreen, IButtonOption> action() {
        return action;
    }

    @Override
    public boolean available() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public @NotNull IController<BiConsumer<NCLScreen, IButtonOption>> controller() {
        return controller;
    }

    @Override
    public @NotNull IBinding<BiConsumer<NCLScreen, IButtonOption>> binding() {
        return binding;
    }

    @Override
    public @NotNull ImmutableSet<IOptionFlag> flags() {
        return ImmutableSet.of();
    }

    @Override
    public boolean changed() {
        return false;
    }

    @Override
    public @NotNull BiConsumer<NCLScreen, IButtonOption> pendingValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestSet(@NotNull BiConsumer<NCLScreen, IButtonOption> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean applyValue() {
        return false;
    }

    @Override
    public void forgetPendingValue() {
    }

    @Override
    public void requestSetDefault() {
    }

    @Override
    public boolean isPendingValueDefault() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addListener(BiConsumer<IOption<BiConsumer<NCLScreen, IButtonOption>>, BiConsumer<NCLScreen, IButtonOption>> changedListener) {
    }

    private static class EmptyBinder implements IBinding<BiConsumer<NCLScreen, IButtonOption>> {
        @Override
        public void setValue(BiConsumer<NCLScreen, IButtonOption> value) {

        }

        @Override
        public BiConsumer<NCLScreen, IButtonOption> getValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public BiConsumer<NCLScreen, IButtonOption> defaultValue() {
            throw new UnsupportedOperationException();
        }
    }

    @ApiStatus.Internal
    public static final class Builder implements IButtonOption.Builder {
        private Component name;
        private Component text = null;
        private IOptionDescription description = IOptionDescription.EMPTY;
        private boolean available = true;
        private BiConsumer<NCLScreen, IButtonOption> action;

        @Override
        public Builder name(@NotNull Component name) {
            Validate.notNull(name, "`name` cannot be null");

            this.name = name;
            return this;
        }

        @Override
        public Builder text(@NotNull Component text) {
            Validate.notNull(text, "`text` cannot be null");

            this.text = text;
            return this;
        }

        @Override
        public Builder description(@NotNull IOptionDescription description) {
            Validate.notNull(description, "`description` cannot be null");

            this.description = description;
            return this;
        }

        @Override
        public Builder action(@NotNull BiConsumer<NCLScreen, IButtonOption> action) {
            Validate.notNull(action, "`action` cannot be null");

            this.action = action;
            return this;
        }

        @Override
        public Builder available(boolean available) {
            this.available = available;
            return this;
        }

        @Override
        public ButtonOption build() {
            Validate.notNull(name, "`name` must not be null when building `ButtonOption`");
            Validate.notNull(action, "`action` must not be null when building `ButtonOption`");

            return new ButtonOption(name, description, action, text, available);
        }
    }
}
