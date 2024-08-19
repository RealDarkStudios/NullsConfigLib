/*
 */

package net.nullved.nullsconfiglib.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.*;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApiStatus.Internal
public class ListOption<T> implements IListOption<T> {
    private final Component name;
    private final IOptionDescription description;
    private final IBinding<List<T>> binding;
    private final Supplier<T> initialValue;
    private final List<IListOptionEntry<T>> entries;
    private final boolean collapsed;
    private boolean available;
    private final int minNumberOfEntries;
    private final int maxNumberOfEntries;
    private final boolean insertEntriesAtEnd;
    private final ImmutableSet<IOptionFlag> flags;
    private final EntryFactory entryFactory;

    private final List<BiConsumer<IOption<List<T>>, List<T>>> listeners;
    private final List<Runnable> refreshListeners;
    private int listenerTriggerDepth = 0;

    public ListOption(@NotNull Component name, @NotNull IOptionDescription description, @NotNull IBinding<List<T>> binding,
                      @NotNull Supplier<T> initialValue, @NotNull Function<IListOptionEntry<T>, IController<T>> controllerFunction,
                      ImmutableSet<IOptionFlag> flags, boolean collapsed, boolean available, int minNumberOfEntries,
                      int maxNumberOfEntries, boolean insertEntriesAtEnd, Collection<BiConsumer<IOption<List<T>>, List<T>>> listeners) {
        this.name = name;
        this.description = description;
        this.binding = new SafeBinding<>(binding);
        this.initialValue = initialValue;
        this.entryFactory = new EntryFactory(controllerFunction);
        this.entries = createEntries(binding().getValue());
        this.collapsed = collapsed;
        this.flags = flags;
        this.available = available;
        this.minNumberOfEntries = minNumberOfEntries;
        this.maxNumberOfEntries = maxNumberOfEntries;
        this.insertEntriesAtEnd = insertEntriesAtEnd;
        this.listeners = new ArrayList<>();
        this.listeners.addAll(listeners);
        this.refreshListeners = new ArrayList<>();
        callListeners(true);
    }

    @Override
    public Component name() {
        return name;
    }

    @Override
    public IOptionDescription description() {
        return description;
    }

    @Override
    public Component tooltip() {
        return description.text();
    }

    @Override
    public @NotNull ImmutableList<IListOptionEntry<?>> options() {
        return ImmutableList.copyOf(entries);
    }

    @Override
    public @NotNull IController<List<T>> controller() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull IBinding<List<T>> binding() {
        return binding;
    }

    @Override
    public boolean collapsed() {
        return collapsed;
    }

    @Override
    public @NotNull ImmutableSet<IOptionFlag> flags() {
        return flags;
    }

    @Override
    public @NotNull List<T> pendingValue() {
        return ImmutableList.copyOf(entries.stream().map(IOption::pendingValue).toList());
    }

    @Override
    public void insertEntry(int index, IListOptionEntry<?> entry) {
        entries.add(index, (IListOptionEntry<T>) entry);
        onRefresh();
    }

    @Override
    public IListOptionEntry<T> insertNewEntry() {
        IListOptionEntry<T> newEntry = entryFactory.create(initialValue.get());
        if (insertEntriesAtEnd) {
            entries.add(newEntry);
        } else {
            entries.add(0, newEntry);
        }
        onRefresh();
        return newEntry;
    }

    @Override
    public void removeEntry(IListOptionEntry<?> entry) {
        if (entries.remove(entry))
            onRefresh();
    }

    @Override
    public int indexOf(IListOptionEntry<?> entry) {
        return entries.indexOf(entry);
    }

    @Override
    public void requestSet(@NotNull List<T> value) {
        entries.clear();
        entries.addAll(createEntries(value));
        onRefresh();
    }

    @Override
    public boolean changed() {
        return !binding().getValue().equals(pendingValue());
    }

    @Override
    public boolean applyValue() {
        if (changed()) {
            binding().setValue(pendingValue());
            return true;
        }
        return false;
    }

    @Override
    public void forgetPendingValue() {
        requestSet(binding().getValue());
    }

    @Override
    public void requestSetDefault() {
        requestSet(binding().defaultValue());
    }

    @Override
    public boolean isPendingValueDefault() {
        return binding().defaultValue().equals(pendingValue());
    }

    @Override
    public boolean available() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        boolean changed = this.available != available;

        this.available = available;

        if (changed)
            callListeners(false);
    }

    @Override
    public int numberOfEntries() {
        return this.entries.size();
    }
    @Override
    public int maxNumberOfEntries() {
        return this.maxNumberOfEntries;
    }
    @Override
    public int minNumberOfEntries() {
        return this.minNumberOfEntries;
    }

    @Override
    public void addListener(BiConsumer<IOption<List<T>>, List<T>> changedListener) {
        this.listeners.add(changedListener);
    }

    @Override
    public void addRefreshListener(Runnable changedListener) {
        this.refreshListeners.add(changedListener);
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    private List<IListOptionEntry<T>> createEntries(Collection<T> values) {
        return values.stream().map(entryFactory::create).collect(Collectors.toList());
    }

    void callListeners(boolean bypass) {
        List<T> pendingValue = pendingValue();
        if (bypass || listenerTriggerDepth == 0) {
            if (listenerTriggerDepth > 10) {
                throw new IllegalStateException("Listener trigger depth exceeded 10! This means a listener triggered a listener etc etc 10 times deep. This is likely a bug in the mod using YACL!");
            }

            this.listenerTriggerDepth++;

            for (BiConsumer<IOption<List<T>>, List<T>> listener : listeners) {
                try {
                    listener.accept(this, pendingValue);
                } catch (Exception e) {
                    NullsConfigLib.LOGGER.error("Exception whilst triggering listener for option '%s'".formatted(name.getString()), e);
                }
            }

            this.listenerTriggerDepth--;
        }
    }

    private void onRefresh() {
        refreshListeners.forEach(Runnable::run);
        callListeners(true);
    }

    private class EntryFactory {
        private final Function<IListOptionEntry<T>, IController<T>> controllerFunction;

        private EntryFactory(Function<IListOptionEntry<T>, IController<T>> controllerFunction) {
            this.controllerFunction = controllerFunction;
        }

        public IListOptionEntry<T> create(T initialValue) {
            return new ListOptionEntry<>(ListOption.this, initialValue, controllerFunction);
        }
    }

    @ApiStatus.Internal
    public static final class Builder<T> implements IListOption.Builder<T> {
        private Component name;
        private IOptionDescription description;
        private Function<IListOptionEntry<T>, IController<T>> controllerFunction;
        private IBinding<List<T>> binding = null;
        private final Set<IOptionFlag> flags = new HashSet<>();
        private Supplier<T> initialValue;
        private boolean collapsed = false;
        private boolean available = true;
        private int minNumberOfEntries = 0;
        private int maxNumberOfEntries = Integer.MAX_VALUE;
        private boolean insertEntriesAtEnd = false;
        private final List<BiConsumer<IOption<List<T>>, List<T>>> listeners = new ArrayList<>();

        @Override
        public IListOption.Builder<T> name(@NotNull Component name) {
            Validate.notNull(name, "`name` must not be null");

            this.name = name;
            return this;
        }

        @Override
        public IListOption.Builder<T> description(@NotNull IOptionDescription description) {
            Validate.notNull(description, "`description` must not be null");

            this.description = description;
            return this;
        }

        @Override
        public IListOption.Builder<T> initial(@NotNull Supplier<T> initialValue) {
            Validate.notNull(initialValue, "`initialValue` cannot be empty");

            this.initialValue = initialValue;
            return this;
        }

        @Override
        public IListOption.Builder<T> initial(@NotNull T initialValue) {
            Validate.notNull(initialValue, "`initialValue` cannot be empty");

            this.initialValue = () -> initialValue;
            return this;
        }

        @Override
        public IListOption.Builder<T> controller(@NotNull Function<IOption<T>, IControllerBuilder<T>> controller) {
            Validate.notNull(controller, "`controller` cannot be null");

            this.controllerFunction = opt -> controller.apply(opt).build();
            return this;
        }

        @Override
        public IListOption.Builder<T> customController(@NotNull Function<IListOptionEntry<T>, IController<T>> control) {
            Validate.notNull(control, "`control` cannot be null");

            this.controllerFunction = control;
            return this;
        }

        @Override
        public IListOption.Builder<T> binding(@NotNull IBinding<List<T>> binding) {
            Validate.notNull(binding, "`binding` cannot be null");

            this.binding = binding;
            return this;
        }

        @Override
        public IListOption.Builder<T> binding(@NotNull List<T> def, @NotNull Supplier<@NotNull List<T>> getter, @NotNull Consumer<@NotNull List<T>> setter) {
            Validate.notNull(def, "`def` must not be null");
            Validate.notNull(getter, "`getter` must not be null");
            Validate.notNull(setter, "`setter` must not be null");

            this.binding = IBinding.generic(def, getter, setter);
            return this;
        }

        @Override
        public IListOption.Builder<T> available(boolean available) {
            this.available = available;
            return this;
        }

        @Override
        public IListOption.Builder<T> minNumberOfEntries(int number) {
            this.minNumberOfEntries = number;
            return this;
        }

        @Override
        public IListOption.Builder<T> maxNumberOfEntries(int number) {
            this.maxNumberOfEntries = number;
            return this;
        }

        @Override
        public IListOption.Builder<T> insertEntriesAtEnd(boolean insertAtEnd) {
            this.insertEntriesAtEnd = insertAtEnd;
            return this;
        }

        @Override
        public IListOption.Builder<T> flag(@NotNull IOptionFlag... flag) {
            Validate.notNull(flag, "`flag` must not be null");

            this.flags.addAll(Arrays.asList(flag));
            return this;
        }

        @Override
        public IListOption.Builder<T> flags(@NotNull Collection<IOptionFlag> flags) {
            Validate.notNull(flags, "`flags` must not be null");

            this.flags.addAll(flags);
            return this;
        }

        @Override
        public IListOption.Builder<T> collapsed(boolean collapsible) {
            this.collapsed = collapsible;
            return this;
        }

        @Override
        public IListOption.Builder<T> listener(@NotNull BiConsumer<IOption<List<T>>, List<T>> listener) {
            this.listeners.add(listener);
            return this;
        }

        @Override
        public IListOption.Builder<T> listeners(@NotNull Collection<BiConsumer<IOption<List<T>>, List<T>>> listeners) {
            this.listeners.addAll(listeners);
            return this;
        }

        @Override
        public ListOption<T> build() {
            Validate.notNull(controllerFunction, "`controller` must not be null");
            Validate.notNull(binding, "`binding` must not be null");
            Validate.notNull(initialValue, "`initialValue` must not be null");

            return new ListOption<>(name, description, binding, initialValue, controllerFunction, ImmutableSet.copyOf(flags),
                    collapsed, available, minNumberOfEntries, maxNumberOfEntries, insertEntriesAtEnd, listeners);
        }
    }
}
