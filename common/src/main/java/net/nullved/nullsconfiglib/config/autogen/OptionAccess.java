package net.nullved.nullsconfiglib.config.autogen;

import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class OptionAccess implements IOptionAccess {
    private final Map<String, IOption<?>> storage = new HashMap<>();
    private final Map<String, Consumer<IOption<?>>> scheduledOperations = new HashMap<>();

    @Override
    public @Nullable IOption<?> getOption(String fieldName) {
        return storage.get(fieldName);
    }

    @Override
    public void scheduleOptionOperation(String fieldName, Consumer<IOption<?>> optionConsumer) {
        if (storage.containsKey(fieldName)) {
            optionConsumer.accept(storage.get(fieldName));
        } else {
            scheduledOperations.merge(fieldName, optionConsumer, Consumer::andThen);
        }
    }

    public void putOption(String fieldName, IOption<?> option) {
        storage.put(fieldName, option);

        Consumer<IOption<?>> consumer = scheduledOperations.remove(fieldName);
        if (consumer != null) {
            consumer.accept(option);
        }
    }

    public void checkBadOperations() {
        if (!scheduledOperations.isEmpty()) {
            NullsConfigLib.LOGGER.warn("There are scheduled operations on the `OptionAccess` that tried to reference fields that do not exist. The following have been referenced that do not exist: " + String.join(", ", scheduledOperations.keySet()));
        }
    }
}
