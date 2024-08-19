package net.nullved.nullsconfiglib.impl;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.*;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.gui.controllers.ListEntryWidget;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

@ApiStatus.Internal
public final class ListOptionEntry<T> implements IListOptionEntry<T> {
    private final ListOption<T> group;

    private T value;

    private final IBinding<T> binding;
    private final IController<T> controller;

    ListOptionEntry(ListOption<T> group, T initialValue, @NotNull Function<IListOptionEntry<T>, IController<T>> controlGetter) {
        this.group = group;
        this.value = initialValue;
        this.binding = new EntryBinding();
        this.controller = new EntryController<>(controlGetter.apply(new HiddenNameListOptionEntry<>(this)), this);
    }

    @Override
    public @NotNull Component name() {
        return group.name();
    }

    @Override
    public @NotNull IOptionDescription description() {
        return group.description();
    }

    @Override
    public @NotNull Component tooltip() {
        return group.tooltip();
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
        return parentGroup().available();
    }

    @Override
    public void setAvailable(boolean available) {
    }

    @Override
    public IListOption<T> parentGroup() {
        return group;
    }

    @Override
    public boolean changed() {
        return false;
    }

    @Override
    public @NotNull T pendingValue() {
        return value;
    }

    @Override
    public void requestSet(@NotNull T value) {
        binding.setValue(value);
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
        return false;
    }

    @Override
    public boolean canResetToDefault() {
        return false;
    }

    @Override
    public void addListener(BiConsumer<IOption<T>, T> listener) {
    }

    @ApiStatus.Internal
    public record EntryController<T>(IController<T> controller, ListOptionEntry<T> entry) implements IController<T> {
        @Override
        public IOption<T> option() {
            return controller.option();
        }

        @Override
        public Component formatValue() {
            return controller.formatValue();
        }

        @Override
        public AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
            return new ListEntryWidget(screen, entry, controller.provideWidget(screen, widgetDimension));
        }
    }

    private class EntryBinding implements IBinding<T> {
        @Override
        public void setValue(T newValue) {
            value = newValue;
            group.callListeners(true);
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public T defaultValue() {
            throw new UnsupportedOperationException();
        }
    }
}
