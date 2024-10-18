package gg.vape.module.impl.Player;

import net.minecraft.world.EnumSkyBlock;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventLight;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "AntiLagMachine", type = Category.Player)
public class AntiLagMachine extends Module {

    @EventTarget
    public void onWorldLight(EventLight event) {
            if (event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
                event.cancel();
            }
    }

}
