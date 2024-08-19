package net.nullved.nullsconfiglib.api.config;

import java.util.Map;

public abstract class IConfigSerializer<T> {
    protected final IConfigHandler<T> config;

    public IConfigSerializer(IConfigHandler<T> config) {
        this.config = config;
    }

    public abstract void save();

    public LoadResult load(Map<IConfigField<?>, IFieldAccess<?>> bufferAccessMap) {
        return LoadResult.NO_CHANGE;
    }

    public enum LoadResult {
        /**
         * Indicates that the config was loaded successfully and the temporary object should be applied.
         */
        SUCCESS,
        /**
         * Indicates that the config was not loaded successfully and the load should be abandoned.
         */
        FAILURE,
        /**
         * Indicates that the config has not changed after a load and the temporary object should be ignored.
         */
        NO_CHANGE,
        /**
         * Indicates the config was loaded successfully, but the config should be re-saved straight away.
         */
        DIRTY
    }
}
