package net.nullved.nullsconfiglib.gui.controllers.dropdown;

import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.NCLScreen;

import java.util.List;

public class DropdownStringControllerElement extends AbstractDropdownControllerElement<String, String> {
    private final DropdownStringController controller;

    public DropdownStringControllerElement(DropdownStringController controller, NCLScreen screen, Dimension<Integer> widgetDimension) {
        super(controller, screen, widgetDimension);
        this.controller = controller;
    }

    @Override
    public List<String> computeMatchingValues() {
        return controller.getAllowedValues(inputField).stream()
                .filter(this::matchingValue)
                .sorted((s1, s2) -> {
                    if (s1.startsWith(inputField) && !s2.startsWith(inputField)) return -1;
                    if (!s1.startsWith(inputField) && s2.startsWith(inputField)) return 1;
                    return s1.compareTo(s2);
                })
                .toList();
    }

    public String getString(String object) {
        return object;
    }
}
