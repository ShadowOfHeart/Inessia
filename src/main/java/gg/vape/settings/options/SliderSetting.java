package gg.vape.settings.options;

import gg.vape.helpers.animation.Animation;
import gg.vape.helpers.animation.impl.EaseInOutQuad;
import gg.vape.settings.Setting;
import gg.vape.module.Module;

import java.util.function.Supplier;

public class SliderSetting extends Setting {
public float current, minimum, maximum, increment;
public float sliderWidth;
public boolean sliding;
    public Animation animation = new EaseInOutQuad(250, 1);

public SliderSetting(String name, float current, float minimum, float maximum, float increment) {
    this.name = name;
    this.minimum = minimum;
    this.current = current;
    this.maximum = maximum;
    this.increment = increment;
}

public float get() {
    return current;
}

public SliderSetting setHidden(Supplier<Boolean> hidden) {
    this.hidden = hidden;
    return this;
}
public SliderSetting call(Module module) {
    module.addSettings(this);
    return this;
}
}
