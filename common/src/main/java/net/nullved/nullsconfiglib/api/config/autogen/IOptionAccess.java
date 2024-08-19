package net.nullved.nullsconfiglib.api.config.autogen;

import net.nullved.nullsconfiglib.api.IOption;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface IOptionAccess {
    @Nullable IOption<?> getOption(String fieldName);

    void scheduleOptionOperation(String fieldName, Consumer<IOption<?>> optionConsumer);
}
