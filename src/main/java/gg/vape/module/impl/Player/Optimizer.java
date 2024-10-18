package gg.vape.module.impl.Player;

import gg.vape.settings.options.BooleanSetting;
import net.minecraft.world.EnumSkyBlock;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventLight;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "Optimizer", type = Category.Player)
public class Optimizer extends Module {

    public BooleanSetting light = new BooleanSetting("—вет", true).call(this);
    public BooleanSetting entities = new BooleanSetting("Ёффекты", true).call(this);

    @EventTarget
    public void onWorldLight(EventLight event) {
        if (light.get()) {
            if (event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
                event.cancel();
            }
            if (event.getEnumSkyBlock() == EnumSkyBlock.BLOCK) {
                event.cancel();
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

    }

}
