package net.nullved.nullsconfiglib.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.IOptionDescription;
import net.nullved.nullsconfiglib.api.IOptionFlag;

import java.util.Set;

public record OptionInfo(Component name, IOptionDescription description, Set<IOptionFlag> flags) {
    public static OptionInfo of(Component name, IOptionDescription description, Set<IOptionFlag> flags) {
        return new OptionInfo(name.copy().withStyle(ChatFormatting.BOLD), description, flags);
    }
}
