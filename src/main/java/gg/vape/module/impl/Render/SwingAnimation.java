package gg.vape.module.impl.Render;

import gg.vape.Medusa;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.settings.options.BooleanSetting;
import gg.vape.settings.options.ModeSetting;
import gg.vape.settings.options.SliderSetting;
import net.minecraft.util.EnumHandSide;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventRenderHand;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "SwingAnimation", type = Category.Render)
public class SwingAnimation extends Module {
    public static boolean blocking;
    public static ModeSetting swordAnim = new ModeSetting("Animation Mode", "Block",  "Block");
    public static BooleanSetting auraOnly = new BooleanSetting("Only Aura", false);
    public static SliderSetting strength = new SliderSetting("Slap Speed",8.0f, 1.0f, 20.0f, 1.0f);
    public static SliderSetting swingSpeed = new SliderSetting("Spin Speed",4.0f, 1.0f, 10.0f, 1.0f);
    public static SliderSetting spinSpeed = new SliderSetting("Wrap strength",4.0f, 1.0f, 10.0f, 1.0f);
    public SwingAnimation(){
        addSettings();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String anim = swordAnim.get();
        blocking = Medusa.m.getModule(KillAura.class).state && KillAura.target != null;
//        this.setSuffix(anim);
    }

//    public ModeSetting mode = new ModeSetting("Mode", "Bonk", "Bonk", "Knife", "Block").call(this);
//    public SliderSetting mult = new SliderSetting("Hand Speed", 14, 1, 20, 1).call(this);

    @EventTarget
    public void onRender(EventRenderHand e) {
        if ( !mc.player.getHeldItemMainhand().isEmpty() && e.e == EnumHandSide.RIGHT) {
        }
    }

}
