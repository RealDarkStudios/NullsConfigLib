package net.nullved.nullsconfiglib.gui.controllers.dropdown;

import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.NCLScreen;

import java.util.List;

public class EnumDropdownControllerElement<E extends Enum<E>> extends AbstractDropdownControllerElement<E, String> {
    private final EnumDropdownController<E> controller;

    public EnumDropdownControllerElement(EnumDropdownController<E> controller, NCLScreen screen, Dimension<Integer> dim) {
        super(controller, screen, dim);
        this.controller = controller;
    }

    @Override
    public List<String> computeMatchingValues() {
        return controller.getValidEnumConstants(inputField).toList();
    }

    @Override
    public String getString(String object) {
        return object;
    }
}
