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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.*;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class Option<T> implements IOption<T> {
    private final Component name;
    private IOptionDescription description;
    private final IController<T> controller;
    private final IBinding<T> binding;
    private boolean available;

    private final ImmutableSet<IOptionFlag> flags;

    private T pendingValue;

    private final List<BiConsumer<IOption<T>, T>> listeners;
    private int listenerTriggerDepth = 0;

    public Option(@NotNull Component name, @NotNull Function<T, IOptionDescription> descriptionFunction,
                  @NotNull Function<IOption<T>, IController<T>> controlGetter, IBinding<T> binding, boolean available,
                  ImmutableSet<IOptionFlag> flags, @NotNull Collection<BiConsumer<IOption<T>, T>> listeners) {
        this.name = name;
        this.binding = new SafeBinding<>(binding);
        this.available = available;
        this.flags = flags;
        this.listeners = new ArrayList<>(listeners);

        this.pendingValue = binding.getValue();
        this.controller = controlGetter.apply(this);

        addListener((opt, pending) -> description = descriptionFunction.apply(pending));
        triggerListeners(true);
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
    public @NotNull IController<T> controller() {
        return controller;
    }

    @Override
    public @NotNull IBinding<T> binding() {
        return binding;
    }

    @Override
    public boolean available() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        boolean changed = this.available != available;

        this.available = available;

        if (changed) {
            if (!available) {
                this.pendingValue = binding.getValue();
            }
            this.triggerListeners(!available);
        }
    }

    @Override
    public @NotNull ImmutableSet<IOptionFlag> flags() {
        return flags;
    }

    @Override
    public boolean changed() {
        return !binding.getValue().equals(pendingValue);
    }

    @Override
    public @NotNull T pendingValue() {
        return pendingValue;
    }

    @Override
    public void requestSet(@NotNull T value) {
        Validate.notNull(value, "`value` cannot be null");

        pendingValue = value;
        this.triggerListeners(true);
    }

    @Override
    public boolean applyValue() {
        if (changed()) {
            binding.setValue(pendingValue);
            return true;
        }
        return false;
    }

    @Override
    public void forgetPendingValue() {
        requestSet(binding.getValue());
    }

    @Override
    public void requestSetDefault() {
        requestSet(binding.defaultValue());
    }

    @Override
    public boolean isPendingValueDefault() {
        return binding.defaultValue().equals(pendingValue);
    }

    @Override
    public void addListener(BiConsumer<IOption<T>, T> changedListener) {
        listeners.add(changedListener);
    }

    private void triggerListeners(boolean bypass) {
        if (bypass || listenerTriggerDepth == 0) {
            if (listenerTriggerDepth > 10) {
                throw new IllegalStateException("Listener trigger depth exceeded 10! This means a listener triggered a listener etc etc 10 times deep. This is likely a bug in the mod using NCL!");
            }

            this.listenerTriggerDepth++;

            for (BiConsumer<IOption<T>, T> listener : listeners) {
                try {
                    listener.accept(this, pendingValue);
                } catch (Exception e) {
                    NullsConfigLib.LOGGER.error("Exception whilst triggering listener for option '%s'".formatted(name.getString()), e);
                }
            }

            this.listenerTriggerDepth--;
        }
    }

    @ApiStatus.Internal
    public static class Builder<T> implements IOption.Builder<T> {
        private Component name = Component.literal("Name not specified!").withStyle(ChatFormatting.RED);

        private Function<T, IOptionDescription> descriptionFunction = pending -> IOptionDescription.EMPTY;

        private Function<IOption<T>, IController<T>> controlGetter;

        private IBinding<T> binding;

        private boolean available = true;

        private boolean instant = false;

        private final Set<IOptionFlag> flags = new HashSet<>();

        private final List<BiConsumer<IOption<T>, T>> listeners = new ArrayList<>();

        @Override
        public IOption.Builder<T> name(@NotNull Component name) {
            Validate.notNull(name, "`name` cannot be null");

            this.name = name;
            return this;
        }

        @Override
        public IOption.Builder<T> description(@NotNull IOptionDescription description) {
            return description(opt -> description);
        }

        @Override
        public IOption.Builder<T> description(@NotNull Function<T, IOptionDescription> descriptionFunction) {
            this.descriptionFunction = descriptionFunction;
            return this;
        }

        @Override
        public IOption.Builder<T> controller(@NotNull Function<IOption<T>, IControllerBuilder<T>> controllerBuilder) {
            Validate.notNull(controllerBuilder, "`controllerBuilder` cannot be null");

            return customController(opt -> controllerBuilder.apply(opt).build());
        }

        @Override
        public IOption.Builder<T> customController(@NotNull Function<IOption<T>, IController<T>> control) {
            Validate.notNull(control, "`control` cannot be null");

            this.controlGetter = control;
            return this;
        }

        @Override
        public IOption.Builder<T> binding(@NotNull IBinding<T> binding) {
            Validate.notNull(binding, "`binding` cannot be null");

            this.binding = binding;
            return this;
        }

        @Override
        public IOption.Builder<T> binding(@NotNull T def, @NotNull Supplier<@NotNull T> getter, @NotNull Consumer<@NotNull T> setter) {
            Validate.notNull(def, "`def` must not be null");
            Validate.notNull(getter, "`getter` must not be null");
            Validate.notNull(setter, "`setter` must not be null");

            this.binding = IBinding.generic(def, getter, setter);
            return this;
        }

        @Override
        public IOption.Builder<T> available(boolean available) {
            this.available = available;
            return this;
        }

        @Override
        public IOption.Builder<T> flag(@NotNull IOptionFlag... flag) {
            Validate.notNull(flag, "`flag` must not be null");

            this.flags.addAll(Arrays.asList(flag));
            return this;
        }

        @Override
        public IOption.Builder<T> flags(@NotNull Collection<? extends IOptionFlag> flags) {
            Validate.notNull(flags, "`flags` must not be null");

            this.flags.addAll(flags);
            return this;
        }

        @Override
        public IOption.Builder<T> instant(boolean instant) {
            this.instant = instant;
            return this;
        }

        @Override
        public IOption.Builder<T> listener(@NotNull BiConsumer<IOption<T>, T> listener) {
            this.listeners.add(listener);
            return this;
        }

        @Override
        public IOption.Builder<T> listeners(@NotNull Collection<BiConsumer<IOption<T>, T>> listeners) {
            this.listeners.addAll(listeners);
            return this;
        }

        @Override
        public IOption<T> build() {
            Validate.notNull(controlGetter, "`control` must not be null when building `Option`");
            Validate.notNull(binding, "`binding` must not be null when building `Option`");
            Validate.isTrue(!instant || flags.isEmpty(), "instant application does not support option flags");

            if (instant) {
                listeners.add((opt, pendingValue) -> opt.applyValue());
            }

            return new Option<>(name, descriptionFunction, controlGetter, binding, available, ImmutableSet.copyOf(flags), listeners);
        }
    }
}
