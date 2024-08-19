package net.nullved.nullsconfiglib.api;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

public interface IListOptionEntry<T> extends IOption<T> {
    IListOption<T> parentGroup();

    @Override
    default @NotNull ImmutableSet<IOptionFlag> flags() {
        return parentGroup().flags();
    }

    @Override
    default boolean available() {
        return parentGroup().available();
    }
}
