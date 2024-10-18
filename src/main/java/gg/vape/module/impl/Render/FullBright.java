package gg.vape.module.impl.Render;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "FullBright", type = Category.Render)
public class FullBright extends Module {

    @EventTarget
    public void onUpdate(EventMotion e) {
        mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 20 * (780 + 37),1));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null)
            mc.player.removeActivePotionEffect(MobEffects.NIGHT_VISION);
    }


}
