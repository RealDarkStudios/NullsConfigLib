package net.nullved.nullsconfiglib.impl;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.IConfigCategory;
import net.nullved.nullsconfiglib.api.INCLGui;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@ApiStatus.Internal
public final class NCLGui implements INCLGui {
    private final Component title;
    private final ImmutableList<IConfigCategory> categories;
    private final Runnable saveFunction;
    private final Consumer<NCLScreen> initConsumer;

    private boolean generated = false;

    public NCLGui(Component title, ImmutableList<IConfigCategory> categories, Runnable saveFunction, Consumer<NCLScreen> initConsumer) {
        this.title = title;
        this.categories = categories;
        this.saveFunction = saveFunction;
        this.initConsumer = initConsumer;
    }

    @Override
    public Screen generateScreen(@Nullable Screen parent) {

        if (generated)
            throw new UnsupportedOperationException("To prevent memory leaks, you should only generate a Screen once per instance. Please re-build the instance to generate another GUI.");

        NullsConfigLib.LOGGER.info("Generating NCL Screen");
        generated = true;
        return new NCLScreen(this, parent);
    }

    @Override
    public Component title() {
        return title;
    }

    @Override
    public ImmutableList<IConfigCategory> categories() {
        return categories;
    }

    @Override
    public Runnable saveFunction() {
        return saveFunction;
    }

    @Override
    public Consumer<NCLScreen> initConsumer() {
        return initConsumer;
    }

    @ApiStatus.Internal
    public static final class Builder implements INCLGui.Builder {
        private Component title;
        private final List<IConfigCategory> categories = new ArrayList<>();
        private Runnable saveFunction = () -> {};
        private Consumer<NCLScreen> initConsumer = screen -> {};

        @Override
        public Builder title(@NotNull Component title) {
            Validate.notNull(title, "`title` cannot be null");

            this.title = title;
            return this;
        }

        @Override
        public Builder category(@NotNull IConfigCategory category) {
            Validate.notNull(category, "`category` cannot be null");

            this.categories.add(category);
            return this;
        }

        @Override
        public Builder categories(@NotNull Collection<? extends IConfigCategory> categories) {
            Validate.notNull(categories, "`categories` cannot be null");

            this.categories.addAll(categories);
            return this;
        }

        @Override
        public Builder save(@NotNull Runnable saveFunction) {
            Validate.notNull(saveFunction, "`saveFunction` cannot be null");

            this.saveFunction = saveFunction;
            return this;
        }

        @Override
        public Builder screenInit(@NotNull Consumer<NCLScreen> initConsumer) {
            Validate.notNull(initConsumer, "`initConsumer` cannot be null");

            this.initConsumer = initConsumer;
            return this;
        }

        @Override
        public INCLGui build() {
            Validate.notNull(title, "`title must not be null to build `NCLGui`");
            Validate.notEmpty(categories, "`categories` must not be empty to build `NCLGui`");

            return new NCLGui(title, ImmutableList.copyOf(categories), saveFunction, initConsumer);
        }
    }
}
