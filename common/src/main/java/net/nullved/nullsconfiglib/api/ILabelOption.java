package net.nullved.nullsconfiglib.api;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.impl.LabelOption;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ILabelOption extends IOption<Component> {
    @NotNull Component label();

    static ILabelOption label(@NotNull Component label) {
        return new LabelOption(label);
    }

    static Builder createBuilder() {
        return new LabelOption.Builder();
    }

    interface Builder {
        Builder line(@NotNull Component line);

        Builder lines(@NotNull Collection<? extends Component> lines);

        ILabelOption build();
    }
}
