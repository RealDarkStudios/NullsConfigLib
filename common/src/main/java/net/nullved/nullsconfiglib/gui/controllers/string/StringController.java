package net.nullved.nullsconfiglib.gui.controllers.string;

import net.nullved.nullsconfiglib.api.IOption;

public class StringController implements IStringController<String> {
    private final IOption<String> option;

    public StringController(IOption<String> option) {
        this.option = option;
    }

    @Override
    public IOption<String> option() {
        return option;
    }

    @Override
    public String getString() {
        return option().pendingValue();
    }

    @Override
    public void setFromString(String value) {
        option().requestSet(value);
    }
}
