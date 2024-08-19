package net.nullved.nullsconfiglib.api.config;

/**
 * A writable field instance access.
 *
 * @param <T> the type of the field
 */
public interface IFieldAccess<T> extends IReadOnlyFieldAccess<T> {
    void set(T value);
}
