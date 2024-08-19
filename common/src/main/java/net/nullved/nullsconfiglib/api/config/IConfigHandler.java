package net.nullved.nullsconfiglib.api.config;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.nullved.nullsconfiglib.api.INCLGui;
import net.nullved.nullsconfiglib.config.ConfigHandler;

import java.util.function.Function;

public interface IConfigHandler<T> {
    T instance();
    T defaults();
    StreamCodec<RegistryFriendlyByteBuf, T> codec();
    boolean hasCodec();
    Class<T> configClass();
    IConfigField<?>[] fields();
    ResourceLocation id();
    INCLGui generateGui();
    boolean supportsAutoGen();
    boolean load();
    void save();

    IConfigSerializer<T> serializer();

    static <T> Builder<T> createBuilder(Class<T> configClass) {
        return new ConfigHandler.Builder<>(configClass);
    }

    interface Builder<T> {
        /**
         * The unique identifier of this config handler.
         * The namespace should be your modid.
         *
         * @return this builder
         */
        Builder<T> id(ResourceLocation id);

        Builder<T> codec(StreamCodec<RegistryFriendlyByteBuf, T> codec);

        /**
         * The function to create the serializer for this config class.
         *
         * @return this builder
         */
        Builder<T> serializer(Function<IConfigHandler<T>, IConfigSerializer<T>> serializerFactory);

        IConfigHandler<T> build();
    }
}
