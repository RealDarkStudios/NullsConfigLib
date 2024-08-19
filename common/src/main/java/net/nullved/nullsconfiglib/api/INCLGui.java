package net.nullved.nullsconfiglib.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.config.IConfigHandler;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.impl.NCLGui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public interface INCLGui {
    Component title();
    ImmutableList<IConfigCategory> categories();
    Runnable saveFunction();
    Consumer<NCLScreen> initConsumer();
    Screen generateScreen(@Nullable Screen parent);
    static Builder createBuilder() {
        return new NCLGui.Builder();
    }

    static <T> INCLGui create(IConfigHandler<T> configHandler, ConfigBackedBuilder<T> builder) {
        return builder.build(configHandler.defaults(), configHandler.instance(), createBuilder().save(configHandler::save)).build();
    }

    interface Builder {
        Builder title(@NotNull Component title);
        Builder category(@NotNull IConfigCategory category);
        Builder categories(@NotNull Collection<? extends IConfigCategory> categories);
        Builder save(@NotNull Runnable saveFunction);
        Builder screenInit(@NotNull Consumer<NCLScreen> initConsumer);
        INCLGui build();
    }

    @FunctionalInterface
    interface ConfigBackedBuilder<T> {
        INCLGui.Builder build(T defaults, T config, INCLGui.Builder builder);
    }
}
