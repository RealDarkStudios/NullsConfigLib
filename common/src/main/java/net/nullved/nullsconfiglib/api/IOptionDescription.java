package net.nullved.nullsconfiglib.api;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.impl.OptionDescription;

import java.util.Collection;

public interface IOptionDescription {
    IOptionDescription EMPTY = new OptionDescription(CommonComponents.EMPTY);
    Component text();

    static Builder createBuilder() {
        return new OptionDescription.Builder();
    }

    static IOptionDescription of(Component... description) {
        return createBuilder().text(description).build();
    }

    interface Builder {
        Builder text(Component... description);
        Builder text(Collection<? extends Component> text);

        IOptionDescription build();
    }
}
