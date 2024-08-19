package net.nullved.nullsconfiglib.impl;

import com.google.common.collect.ImmutableSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.nullved.nullsconfiglib.api.*;
import net.nullved.nullsconfiglib.gui.controllers.LabelController;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

public class LabelOption implements ILabelOption {
    private final Component label;
    private final Component name = Component.literal("Label Option");
    private final IOptionDescription description;
    private final LabelController labelController;
    private final IBinding<Component> binding;

    public LabelOption(Component label) {
        Validate.notNull(label, "`label` must not be null");

        this.label = label;
        this.labelController = new LabelController(this);
        this.binding = IBinding.immutable(label);
        this.description = IOptionDescription.createBuilder()
                .text(this.label)
                .build();
    }

    @Override
    public @NotNull Component label() {
        return label;
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
    public @NotNull IController<Component> controller() {
        return labelController;
    }

    @Override
    public @NotNull IBinding<Component> binding() {
        return binding;
    }

    @Override
    public boolean available() {
        return true;
    }

    @Override
    public void setAvailable(boolean available) {
        throw new UnsupportedOperationException("Label options cannot be disabled.");
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
    public @NotNull Component pendingValue() {
        return label;
    }

    @Override
    public void requestSet(@NotNull Component value) {
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
        return true;
    }

    @Override
    public boolean canResetToDefault() {
        return false;
    }

    @Override
    public void addListener(BiConsumer<IOption<Component>, Component> changedListener) {
    }

    @ApiStatus.Internal
    public static final class Builder implements ILabelOption.Builder {
        private final List<Component> lines = new ArrayList<>();

        @Override
        public ILabelOption.Builder line(@NotNull Component line) {
            Validate.notNull(line, "`line` must not be null");

            this.lines.add(line);
            return this;
        }

        @Override
        public ILabelOption.Builder lines(@NotNull Collection<? extends Component> lines) {
            this.lines.addAll(lines);
            return this;
        }

        @Override
        public ILabelOption build() {
            MutableComponent text = Component.empty();
            Iterator<Component> iterator = lines.iterator();
            while (iterator.hasNext()) {
                text.append(iterator.next());

                if (iterator.hasNext()) {
                    text.append("\n");
                }
            }

            return new LabelOption(text);
        }
    }
}
