package net.nullved.nullsconfiglib.api.config.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.nullved.nullsconfiglib.api.config.IConfigHandler;
import net.nullved.nullsconfiglib.api.config.IConfigSerializer;
import net.nullved.nullsconfiglib.config.serializer.GsonConfigSerializer;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

public interface IGsonConfigSerializerBuilder<T> {
    static <T> IGsonConfigSerializerBuilder<T> create(IConfigHandler<T> config) {
        return new GsonConfigSerializer.Builder<>(config);
    }

    IGsonConfigSerializerBuilder<T> setPath(Path path);

    IGsonConfigSerializerBuilder<T> overrideGsonBuilder(GsonBuilder gsonBuilder);

    IGsonConfigSerializerBuilder<T> overrideGsonBuilder(Gson gson);

    IGsonConfigSerializerBuilder<T> appendGsonBuilder(UnaryOperator<GsonBuilder> gsonBuilder);

    IGsonConfigSerializerBuilder<T> setJson5(boolean json5);

    IConfigSerializer<T> build();
}
