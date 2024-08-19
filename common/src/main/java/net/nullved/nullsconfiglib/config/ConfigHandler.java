package net.nullved.nullsconfiglib.config;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.nullved.nullsconfiglib.NullsConfigLib;
import net.nullved.nullsconfiglib.api.*;
import net.nullved.nullsconfiglib.api.config.*;
import net.nullved.nullsconfiglib.api.config.autogen.CategorySettings;
import net.nullved.nullsconfiglib.api.config.autogen.DisableOption;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.config.autogen.NCLAutoGenException;
import net.nullved.nullsconfiglib.config.autogen.OptionAccess;
import net.nullved.nullsconfiglib.config.autogen.OptionFactoryRegistry;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigHandler<T> implements IConfigHandler<T> {
    private final Class<T> configClass;
    private final ResourceLocation id;
    private final StreamCodec<RegistryFriendlyByteBuf, T> codec;
    private final boolean supportsAutoGen;
    private final IConfigSerializer<T> serializer;
    private final ConfigField<?>[] fields;

    private T instance;
    private final T defaults;
    private final Constructor<T> noArgsConstructor;

    public ConfigHandler(Class<T> configClass, ResourceLocation id, StreamCodec<RegistryFriendlyByteBuf, T> codec, Function<IConfigHandler<T>, IConfigSerializer<T>> serializerFactory) {
        this.configClass = configClass;
        this.id = id;
        this.codec = codec;
        this.supportsAutoGen = id != null && Platform.getEnvironment().equals(Env.CLIENT);

        try {
            noArgsConstructor = configClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new NCLAutoGenException("Failed to find no-args constructor for config class '%s'!".formatted(configClass.getName()), e);
        }

        this.instance = createNewObject();
        this.defaults = createNewObject();

        this.fields = discoverFields();
        this.serializer = serializerFactory.apply(this);
    }

    @Override
    public ResourceLocation id() {
        return id;
    }

    private ConfigField<?>[] discoverFields() {
        return Arrays.stream(configClass.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> field.isAnnotationPresent(ConfigEntry.class) || field.isAnnotationPresent(CategorySettings.class))
                .map(field -> new ConfigField<>(
                        new ReflectionFieldAccess<>(field, instance),
                        new ReflectionFieldAccess<>(field, defaults),
                        this,
                        field.getAnnotation(ConfigEntry.class),
                        field.getAnnotation(CategorySettings.class)
                ))
                .toArray(ConfigField[]::new);
    }

    @Override
    public T instance() {
        return instance;
    }

    @Override
    public T defaults() {
        return defaults;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> codec() {
        return codec;
    }

    @Override
    public boolean hasCodec() {
        return codec != null;
    }

    @Override
    public Class<T> configClass() {
        return configClass;
    }

    @Override
    public ConfigField<?>[] fields() {
        return fields;
    }

    @Override
    public boolean supportsAutoGen() {
        return supportsAutoGen;
    }

    @Override
    public INCLGui generateGui() {
        if (!supportsAutoGen()) {
            throw new NCLAutoGenException("Auto GUI generation is not support for this config. You either need to enable it in the builder or you are attempting to create a GUI in a dedicated server environment");
        }

        boolean hasAutoGenFields = Arrays.stream(fields).anyMatch(field -> field.categorySettings().isPresent());

        if (!hasAutoGenFields) {
            throw new NCLAutoGenException("No fields in this config class are annotated with @CategorySettings. You must annotate at least one field with @CategorySettings to generate a GUI");
        }

        OptionAccess storage = new OptionAccess();
        Map<String, CategoryAndGroups> categories = new LinkedHashMap<>();
        for (ConfigField<?> field : fields) {
            field.categorySettings().ifPresent(autoGen -> {
                CategoryAndGroups groups = categories.computeIfAbsent(
                        autoGen.category(),
                        k -> new CategoryAndGroups(
                                IConfigCategory.createBuilder()
                                        .name(Component.translatable("config.%s.%s.category_%s".formatted(id().getNamespace(), id().getPath(), k))),
                                new LinkedHashMap<>()
                        )
                );

                IOptionAddable group = groups.groups().computeIfAbsent(autoGen.group().orElse(""), k -> {
                    if (k.isEmpty())
                        return groups.category();
                    return IOptionGroup.createBuilder()
                            .name(Component.translatable("config.%s.%s.category_%s.group_%s".formatted(id().getNamespace(), id().getPath(), autoGen.category(), k)));
                });

                IOption<?> option;
                try {
                    option = createOption(field, storage);
                } catch (Exception e) {
                    throw new NCLAutoGenException("Failed to create option for field '%s'".formatted(field.access().name()), e);
                }

                if (field.access().getAnnotation(DisableOption.class).isPresent()) {
                    option.setAvailable(false);
                }

                storage.putOption(field.access().name(), option);
                group.option(option);
            });
        }

        storage.checkBadOperations();
        categories.values().forEach(CategoryAndGroups::finalizeGroups);

        INCLGui.Builder nclGuiBuilder = INCLGui.createBuilder()
                .save(this.serializer::save)
                .title(Component.translatable("config.%s.%s.title".formatted(id().getNamespace(), id().getPath())));

        categories.values().forEach(category -> nclGuiBuilder.category(category.category().build()));

        return nclGuiBuilder.build();
    }

    private <U> IOption<U> createOption(IConfigField<U> configField, IOptionAccess storage) {
        return OptionFactoryRegistry.createOption(((ReflectionFieldAccess<?>) configField.access()).field(), configField, storage)
                .orElseThrow(() -> new NCLAutoGenException("Failed to create option for field '%s'".formatted(configField.access().name())));
    }

    @Override
    public IConfigSerializer<T> serializer() {
        return serializer;
    }

    @Override
    public boolean load() {
        T newInstance = createNewObject();

        Map<ConfigField<?>, ReflectionFieldAccess<?>> accessBufferImpl = Arrays.stream(fields())
                .map(field -> new AbstractMap.SimpleImmutableEntry<>(
                        field, new ReflectionFieldAccess<>(field.access().field(), newInstance)
                ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<IConfigField<?>, IFieldAccess<?>> accessBuffer = accessBufferImpl.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        IConfigSerializer.LoadResult loadResult = IConfigSerializer.LoadResult.FAILURE;
        Throwable error = null;
        try {
            loadResult = this.serializer().load(accessBuffer);
        } catch (Throwable t) {
            error = t;
        }

        switch (loadResult) {
            case DIRTY:
            case SUCCESS:
                this.instance = newInstance;
                for (ConfigField<?> field : fields()) {
                    ((ConfigField<Object>) field).setFieldAccess((ReflectionFieldAccess<Object>) accessBufferImpl.get(field));
                }

                if (loadResult == IConfigSerializer.LoadResult.DIRTY) {
                    this.save();
                }
            case NO_CHANGE:
                return true;
            case FAILURE:
                NullsConfigLib.LOGGER.error(
                        "Unsuccessfuel load of config class '{}'. The load will be abandoned and remains unchanged.",
                        configClass.getSimpleName(), error
                );
        }

        return false;
    }

    @Override
    public void save() {
        serializer().save();
    }

    private T createNewObject() {
        try {
            return noArgsConstructor.newInstance();
        } catch (Exception e) {
            throw new NCLAutoGenException("Failed to create instance of config class '%s'".formatted(configClass.getName()), e);
        }
    }

    public static class Builder<T> implements IConfigHandler.Builder<T> {
        private final Class<T> configClass;
        private ResourceLocation id;
        private StreamCodec<RegistryFriendlyByteBuf, T> codec;
        private Function<IConfigHandler<T>, IConfigSerializer<T>> serializerFactory;

        public Builder(Class<T> configClass) {
            this.configClass = configClass;
        }

        @Override
        public Builder<T> id(ResourceLocation id) {
            this.id = id;
            return this;
        }

        @Override
        public IConfigHandler.Builder<T> codec(StreamCodec<RegistryFriendlyByteBuf, T> codec) {
            this.codec = codec;
            return this;
        }

        @Override
        public Builder<T> serializer(Function<IConfigHandler<T>, IConfigSerializer<T>> serializerFactory) {
            this.serializerFactory = serializerFactory;
            return this;
        }

        @Override
        public IConfigHandler<T> build() {
            Validate.notNull(serializerFactory, "serializerFactory must not be null");
            Validate.notNull(configClass, "configClass must not be null");

            return new ConfigHandler<>(configClass, id, codec, serializerFactory);
        }
    }

    private record CategoryAndGroups(IConfigCategory.Builder category, Map<String, IOptionAddable> groups) {
        private void finalizeGroups() {
            groups.forEach((name, group) -> {
                if (group instanceof IOptionGroup.Builder groupBuilder) {
                    category.group(groupBuilder.build());
                }
            });
        }
    }
}
