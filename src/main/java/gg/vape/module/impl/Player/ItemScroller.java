package gg.vape.module.impl.Player;

import gg.vape.settings.options.SliderSetting;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "ItemScroller", type = Category.Player)
public class ItemScroller extends Module {
    public SliderSetting delay = new SliderSetting("Задержка", 10F, 0F, 500F, 1F).call(this);


}
