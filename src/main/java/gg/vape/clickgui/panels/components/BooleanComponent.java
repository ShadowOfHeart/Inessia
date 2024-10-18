package gg.vape.clickgui.panels.components;

import gg.vape.settings.options.BooleanSetting;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.module.Module;

import java.awt.*;

public class BooleanComponent extends Component{

    public float x,y, width, height;
    public BooleanSetting setting;
    public Module module;

    public BooleanComponent(Module module, BooleanSetting s, float x, float y, float width, float height) {
        super(module, x, y, width, height);
        this.module = module;
        this.setting = s;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        RenderUtil.drawRectWH(x, y, width, height, new Color(5, 5, 5, 150).getRGB());
    }
}
