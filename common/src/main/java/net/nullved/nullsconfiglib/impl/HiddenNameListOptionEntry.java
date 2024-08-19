package net.nullved.nullsconfiglib.impl;

import com.google.common.collect.ImmutableSet;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class HiddenNameListOptionEntry<T> implements IListOptionEntry<T> {
    private final IListOptionEntry<T> option;

    public HiddenNameListOptionEntry(IListOptionEntry<T> option) {
        this.option = option;
    }

    @Override
    public @NotNull Component name() {
        return Component.empty();
    }

    @Override
    public @NotNull IOptionDescription description() {
        return option.description();
    }

    @Override
    public @NotNull Component tooltip() {
        return option.tooltip();
    }

    @Override
    public @NotNull IController<T> controller() {
        return option.controller();
    }

    @Override
    public @NotNull IBinding<T> binding() {
        return option.binding();
    }

    @Override
    public boolean available() {
        return option.available();
    }

    @Override
    public void setAvailable(boolean available) {
        option.setAvailable(available);
    }

    @Override
    public IListOption<T> parentGroup() {
        return option.parentGroup();
    }

    @Override
    public @NotNull ImmutableSet<IOptionFlag> flags() {
        return option.flags();
    }

    @Override
    public boolean changed() {
        return option.changed();
    }

    @Override
    public @NotNull T pendingValue() {
        return option.pendingValue();
    }

    @Override
    public void requestSet(@NotNull T value) {
        option.requestSet(value);
    }

    @Override
    public boolean applyValue() {
        return option.applyValue();
    }

    @Override
    public void forgetPendingValue() {
        option.forgetPendingValue();
    }

    @Override
    public void requestSetDefault() {
        option.requestSetDefault();
    }

    @Override
    public boolean isPendingValueDefault() {
        return option.isPendingValueDefault();
    }

    @Override
    public boolean canResetToDefault() {
        return option.canResetToDefault();
    }

    @Override
    public void addListener(BiConsumer<IOption<T>, T> changedListener) {
        option.addListener(changedListener);
    }
}
