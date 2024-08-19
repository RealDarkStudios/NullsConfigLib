package net.nullved.nullsconfiglib.impl;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.nullved.nullsconfiglib.api.IOptionDescription;

import java.util.*;

public record OptionDescription(Component text) implements IOptionDescription {
    public static class Builder implements IOptionDescription.Builder {
        private final List<Component> descriptionLines = new ArrayList<>();

        @Override
        public IOptionDescription.Builder text(Component... description) {
            this.descriptionLines.addAll(Arrays.asList(description));
            return this;
        }

        @Override
        public IOptionDescription.Builder text(Collection<? extends Component> text) {
            this.descriptionLines.addAll(text);
            return this;
        }

        @Override
        public IOptionDescription build() {
            MutableComponent concatenatedDescription = Component.empty();
            Iterator<Component> iter = descriptionLines.iterator();
            while (iter.hasNext()) {
                concatenatedDescription.append(iter.next());
                if (iter.hasNext()) concatenatedDescription.append("\n");
            }

            return new OptionDescription(concatenatedDescription);
        }
    }
}
