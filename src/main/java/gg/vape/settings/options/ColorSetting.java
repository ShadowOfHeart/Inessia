package gg.vape.settings.options;

import gg.vape.helpers.animation.Animation;
import gg.vape.helpers.animation.impl.EaseInOutQuad;
import gg.vape.settings.Setting;
import gg.vape.module.Module;

import java.util.function.Supplier;

public class ColorSetting extends Setting {

    public int color = -1;
    public boolean dragging;
    public Animation animation = new EaseInOutQuad(250, 1);
    public boolean slid;
    public boolean ifSlidingAlpha;
    public float alphaSlider;


    public ColorSetting(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public int get() {
        return color;
    }

    public ColorSetting setHidden(Supplier<Boolean> hidden) {
        this.hidden = hidden;
        return this;
    }

    public ColorSetting call(Module module) {
        module.addSettings(this);
        return this;
    }


}
