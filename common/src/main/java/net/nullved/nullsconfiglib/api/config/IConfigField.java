package net.nullved.nullsconfiglib.api.config;

import net.nullved.nullsconfiglib.api.config.autogen.ICategorySettings;

import java.util.Optional;

public interface IConfigField<T> {
    IFieldAccess<T> access();
    IReadOnlyFieldAccess<T> defaultAccess();
    IConfigHandler<?> parent();
    Optional<ISerialConfigField> serial();
    Optional<ICategorySettings> categorySettings();
}
