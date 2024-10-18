package gg.vape.module.impl.Player;

import gg.vape.settings.options.BooleanSetting;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "Baritone", type = Category.Player)
public class Baritone extends Module {
    public BooleanSetting antirg = new BooleanSetting("AntiRG", true).call(this);
}
