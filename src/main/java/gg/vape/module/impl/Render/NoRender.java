package gg.vape.module.impl.Render;

import gg.vape.settings.options.ListSetting;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "NoRender", type = Category.Render)
public class NoRender extends Module {

    public ListSetting listSetting = new ListSetting("Моды", "Fire", "Block").call(this);

}
