package gg.vape.module.impl.Render;

import gg.vape.settings.options.BooleanSetting;
import gg.vape.settings.options.ColorSetting;
import org.lwjgl.input.Keyboard;
import gg.vape.Medusa;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.awt.*;

@ModuleInfo(name = "ClickGui", type = Category.Hud)
public class ClickGui extends Module {

    public static ColorSetting color = new ColorSetting("Цвет Клиента", new Color(70, 255, 100, 255).getRGB());
    public static BooleanSetting blur = new BooleanSetting("Размытие BG", false);

    public ClickGui() {
        bind = Keyboard.KEY_RSHIFT;
        addSettings(color, blur);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(Medusa.s);
//        ClickGuiSave.save();
        toggle();
    }

    public static int getColor() {
        return color.get();
    }

}

