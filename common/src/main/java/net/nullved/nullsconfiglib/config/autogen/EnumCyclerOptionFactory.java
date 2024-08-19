package net.nullved.nullsconfiglib.config.autogen;

import net.minecraft.network.chat.Component;
import net.nullved.nullsconfiglib.api.INameableEnum;
import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.config.IConfigField;
import net.nullved.nullsconfiglib.api.config.autogen.EnumCycler;
import net.nullved.nullsconfiglib.api.config.autogen.IOptionAccess;
import net.nullved.nullsconfiglib.api.config.autogen.SimpleOptionFactory;
import net.nullved.nullsconfiglib.api.controller.IControllerBuilder;
import net.nullved.nullsconfiglib.api.controller.ICyclingListControllerBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class EnumCyclerOptionFactory extends SimpleOptionFactory<EnumCycler, Enum<?>> {
    @Override
    protected IControllerBuilder<Enum<?>> createController(EnumCycler annotation, IConfigField<Enum<?>> field, IOptionAccess storage, IOption<Enum<?>> option) {
        List<? extends Enum<?>> values;

        if (option.pendingValue() instanceof EnumCycler.CyclableEnum<?> cyclableEnum) {
            values = Arrays.asList(cyclableEnum.allowedValues());
        } else {
            Enum<?>[] constants = field.access().typeClass().getEnumConstants();
            values = IntStream.range(0, constants.length)
                    .filter(ordinal -> annotation.allowedOrdinals().length == 0 || Arrays.stream(annotation.allowedOrdinals()).noneMatch(allowed -> allowed == ordinal))
                    .mapToObj(ordinal -> constants[ordinal])
                    .toList();
        }

        // EnumController doesn't support filtering
        var builder = ICyclingListControllerBuilder.create(option)
                .values(values);
        if (INameableEnum.class.isAssignableFrom(field.access().typeClass())) {
            builder.formatValue(v -> ((INameableEnum) v).getDisplayName());
        } else {
            builder.formatValue(v -> Component.translatable("ncl.config.enum.%s.%s".formatted(field.access().typeClass().getSimpleName(), v.name().toLowerCase())));
        }
        return builder;
    }
}
