package gg.vape.module.impl.Render;

import gg.vape.settings.options.SliderSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventRenderHand;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "ViewModel", type = Category.Render)
public class ViewModel extends Module {

    public SliderSetting rightX = new SliderSetting("Правая X", 0, -180, 180, 1);
    public SliderSetting rightY = new SliderSetting("Правая Y", 0, -180, 180, 1);
    public SliderSetting rightZ = new SliderSetting("Правая Z", 0, -180, 180, 1);
    public SliderSetting leftX = new SliderSetting("Левая X", 0, -180, 180, 1);
    public SliderSetting leftY = new SliderSetting("Левая Y", 0, -180, 180, 1);
    public SliderSetting leftZ = new SliderSetting("Левая Z", 0, -180, 180, 1);

    public ViewModel() {
        addSettings(rightX, rightY, rightZ, leftX, leftY, leftZ);
    }

    @EventTarget
    public void onRender(EventRenderHand e) {
        if (e.e == EnumHandSide.RIGHT) {
            GlStateManager.translate(rightX.get() / 180, rightY.get() / 180, rightZ.get() / 180);
        }
        if (e.e == EnumHandSide.LEFT) {
            GlStateManager.translate(leftX.get() / 180, leftY.get() / 180, leftZ.get() / 180);
        }
    }

}
