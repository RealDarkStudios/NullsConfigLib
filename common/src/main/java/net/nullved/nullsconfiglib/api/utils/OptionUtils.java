package net.nullved.nullsconfiglib.api.utils;

import net.nullved.nullsconfiglib.api.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class OptionUtils {
    public static Stream<IOption<?>> getFlatOptions(INCLGui gui) {
        return gui.categories().stream()
                .flatMap(category -> category.groups().stream())
                .flatMap(group -> group instanceof IListOption<?> list ? Stream.of(list) : group.options().stream());
    }

    public static void consumeOptions(INCLGui nclGui, Function<IOption<?>, Boolean> consumer) {
        for (IConfigCategory category : nclGui.categories()) {
            for (IOptionGroup group : category.groups()) {
                if (group instanceof IListOption<?> list) {
                    if (consumer.apply(list)) return;
                } else {
                    for (IOption<?> option : group.options()) {
                        if (consumer.apply(option)) return;
                    }
                }
            }
        }
    }

    /**
     * Consumes all options, ignoring groups and categories.
     *
     * @see OptionUtils#consumeOptions(INCLGui, Function)
     */
    public static void forEachOptions(INCLGui nclGui, Consumer<IOption<?>> consumer) {
        consumeOptions(nclGui, (opt) -> {
            consumer.accept(opt);
            return false;
        });
    }
}
