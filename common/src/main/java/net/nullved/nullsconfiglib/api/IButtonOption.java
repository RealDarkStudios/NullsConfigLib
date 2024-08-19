package net.nullved.nullsconfiglib.api;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.gui.NCLScreen;
import net.nullved.nullsconfiglib.impl.ButtonOption;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface IButtonOption extends IOption<BiConsumer<NCLScreen, IButtonOption>> {
    BiConsumer<NCLScreen, IButtonOption> action();

    static Builder createBuilder() {
        return new ButtonOption.Builder();
    }

    interface Builder {
        Builder name(@NotNull Component name);
        Builder text(@NotNull Component text);
        Builder description(@NotNull IOptionDescription description);
        Builder action(@NotNull BiConsumer<NCLScreen, IButtonOption> action);
        Builder available(boolean available);

        IButtonOption build();
    }
}
