package net.nullved.nullsconfiglib.gui;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.controller.IValueFormatter;

public final class ValueFormatters {
    public static IValueFormatter<Float> percent(int decimalPlaces) {
        return new PercentFormatter(decimalPlaces);
    }

    public record PercentFormatter(int decimalPlaces) implements IValueFormatter<Float> {
        public PercentFormatter() {
            this(1);
        }

        @Override
        public Component format(Float value) {
            return Component.literal(String.format(".%d%f%%", decimalPlaces, value * 100));
        }
    }
}
