package gg.vape.module.impl.Render;

import gg.vape.settings.options.ColorSetting;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.awt.*;
@ModuleInfo(name = "EnchantmentColor", type = Category.Render)
public class EnchantmentColor extends Module {
    public ColorSetting enchantColor = new ColorSetting("Color", new Color(120, 210, 210).getRGB()).call(this);
}
