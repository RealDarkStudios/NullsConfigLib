package net.nullved.nullsconfiglib.mixin;

import net.minecraft.client.OptionInstance;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@ApiStatus.Internal
@Mixin(OptionInstance.class)
public interface OptionInstanceAccessor<T> {
    /**
     * Accessor for {@link OptionInstance#initialValue}
     * @return The initial value of the option
     */
    @Accessor("initialValue")
    T getInitialValue();
}
