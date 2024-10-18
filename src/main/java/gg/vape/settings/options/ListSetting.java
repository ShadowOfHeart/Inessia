package gg.vape.settings.options;

import gg.vape.helpers.animation.Animation;
import gg.vape.helpers.animation.impl.EaseInOutQuad;
import gg.vape.settings.Setting;
import gg.vape.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ListSetting extends Setting {

    public List<String> list;
    public boolean opened;
    public List<String> selected = new ArrayList<>();
    public Animation animation = new EaseInOutQuad(100, 1);

    public ListSetting(String name, String... settings) {
        this.name = name;
        this.list = Arrays.asList(settings);
    }

    public ListSetting call(Module module) {
        module.addSettings(this);
        return this;
    }

    public ListSetting setHidden(Supplier<Boolean> hidden) {
        this.hidden = hidden;
        return this;
    }

}
