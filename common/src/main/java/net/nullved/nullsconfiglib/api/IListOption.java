package net.nullved.nullsconfiglib.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.impl.ListOption;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IListOption<T> extends IOptionGroup, IOption<List<T>> {
    @Override
    @NotNull ImmutableList<IListOptionEntry<?>> options();

    @ApiStatus.Internal
    int numberOfEntries();

    @ApiStatus.Internal
    int maxNumberOfEntries();

    @ApiStatus.Internal
    int minNumberOfEntries();

    @ApiStatus.Internal
    IListOptionEntry<T> insertNewEntry();

    @ApiStatus.Internal
    void insertEntry(int index, IListOptionEntry<?> entry);

    @ApiStatus.Internal
    int indexOf(IListOptionEntry<?> entry);

    @ApiStatus.Internal
    void removeEntry(IListOptionEntry<?> entry);

    @ApiStatus.Internal
    void addRefreshListener(Runnable changedListener);

    static <T> Builder<T> createBuilder() {
        return new ListOption.Builder<>();
    }

    interface Builder<T> {
        /**
         * Sets name of the list, for UX purposes, a name should always be given,
         * but isn't enforced.
         *
         * @see IListOption#name()
         */
        Builder<T> name(@NotNull Component name);

        Builder<T> description(@NotNull IOptionDescription description);

        /**
         * Sets the value that is used when creating new entries
         */
        Builder<T> initial(@NotNull Supplier<T> initialValue);

        /**
         * Sets the value that is used when creating new entries
         */
        Builder<T> initial(@NotNull T initialValue);

        Builder<T> controller(@NotNull Function<IOption<T>, IControllerBuilder<T>> controller);

        /**
         * Sets the controller for the option.
         * This is how you interact and change the options.
         */
        Builder<T> customController(@NotNull Function<IListOptionEntry<T>, IController<T>> control);

        /**
         * Sets the binding for the option.
         * Used for default, getter and setter.
         *
         * @see IBinding
         */
        Builder<T> binding(@NotNull IBinding<List<T>> binding);

        /**
         * Sets the binding for the option.
         * Shorthand of {@link IBinding#generic(Object, Supplier, Consumer)}
         *
         * @param def default value of the option, used to reset
         * @param getter should return the current value of the option
         * @param setter should set the option to the supplied value
         * @see IBinding
         */
        Builder<T> binding(@NotNull List<T> def, @NotNull Supplier<@NotNull List<T>> getter, @NotNull Consumer<@NotNull List<T>> setter);

        /**
         * Sets if the option can be configured
         *
         * @see IOption#available()
         */
        Builder<T> available(boolean available);

        /**
         * Sets a minimum size for the list. Once this size is reached,
         * no further entries may be removed.
         */
        Builder<T> minNumberOfEntries(int number);

        /**
         * Sets a maximum size for the list. Once this size is reached,
         * no further entries may be added.
         */
        Builder<T> maxNumberOfEntries(int number);

        /**
         * Dictates if new entries should be added to the end of the list
         * rather than the top.
         */
        Builder<T> insertEntriesAtEnd(boolean insertAtEnd);

        /**
         * Adds a flag to the option.
         * Upon applying changes, all flags are executed.
         * {@link IOption#flags()}
         */
        Builder<T> flag(@NotNull IOptionFlag... flag);

        /**
         * Adds a flag to the option.
         * Upon applying changes, all flags are executed.
         * {@link IOption#flags()}
         */
        Builder<T> flags(@NotNull Collection<IOptionFlag> flags);

        /**
         * Dictates if the group should be collapsed by default.
         * If not set, it will not be collapsed by default.
         *
         * @see IOptionGroup#collapsed()
         */
        Builder<T> collapsed(boolean collapsible);

        /**
         * Adds a listener to the option. Invoked upon changing any of the list's entries.
         *
         * @see IOption#addListener(BiConsumer)
         */
        IListOption.Builder<T> listener(@NotNull BiConsumer<IOption<List<T>>, List<T>> listener);

        /**
         * Adds multiple listeners to the option. Invoked upon changing of any of the list's entries.
         *
         * @see IOption#addListener(BiConsumer)
         */
        IListOption.Builder<T> listeners(@NotNull Collection<BiConsumer<IOption<List<T>>, List<T>>> listeners);

        IListOption<T> build();
    }
}
