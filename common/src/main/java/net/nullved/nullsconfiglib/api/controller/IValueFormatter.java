package net.nullved.nullsconfiglib.api.controller;

import net.minecraft.network.chat.Component;

public interface IValueFormatter<T> {
    Component format(T value);
}
