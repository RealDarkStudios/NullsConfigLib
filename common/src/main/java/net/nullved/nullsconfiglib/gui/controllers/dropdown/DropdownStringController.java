package net.nullved.nullsconfiglib.gui.controllers.dropdown;

import net.nullved.nullsconfiglib.api.IOption;
import net.nullved.nullsconfiglib.api.utils.Dimension;
import net.nullved.nullsconfiglib.gui.AbstractWidget;
import net.nullved.nullsconfiglib.gui.NCLScreen;

import java.util.List;

public class DropdownStringController extends AbstractDropdownController<String> {
    public DropdownStringController(IOption<String> option, List<String> allowedValues, boolean allowEmptyValue, boolean allowAnyValue) {
        super(option, allowedValues, allowEmptyValue, allowAnyValue);
    }

    @Override
    public String getString() {
        return option().pendingValue();
    }

    @Override
    public void setFromString(String value) {
        option().requestSet(getValidValue(value));
    }

    @Override
    public AbstractWidget provideWidget(NCLScreen screen, Dimension<Integer> widgetDimension) {
        return new DropdownStringControllerElement(this, screen, widgetDimension);
    }
}
