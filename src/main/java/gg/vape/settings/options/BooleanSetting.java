package gg.vape.settings.options;


import gg.vape.settings.Setting;
import gg.vape.helpers.animation.Animation;
import gg.vape.helpers.animation.impl.EaseInOutQuad;
import gg.vape.module.Module;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {

private boolean state;
public Animation animation = new EaseInOutQuad(250, 1);


public BooleanSetting(String name, boolean state) {
    this.name = name;
    this.state = state;
}


public boolean get() {
    return state;
}

public void set(boolean state) {
    this.state = state;
}

    public BooleanSetting setHidden(Supplier<Boolean> hidden) {
        this.hidden = hidden;
        return this;
    }
    public BooleanSetting call(Module module) {
        module.addSettings(this);
        return this;
    }

}
