package gg.vape.module.impl.Render;

import gg.vape.settings.options.ColorSetting;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.awt.*;

@ModuleInfo(name = "FogColor", type = Category.Render)
public class FogColor extends Module {

    public ColorSetting fogColor = new ColorSetting("Color", new Color(255, 255, 255).getRGB()).call(this);

}
