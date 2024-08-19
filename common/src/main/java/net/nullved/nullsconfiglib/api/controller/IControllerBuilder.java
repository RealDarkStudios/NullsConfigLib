package net.nullved.nullsconfiglib.api.controller;

import net.nullved.nullsconfiglib.api.IController;
import org.jetbrains.annotations.ApiStatus;

@FunctionalInterface
public interface IControllerBuilder<T> {
    @ApiStatus.Internal
    IController<T> build();
}
