package gg.vape.module.impl.Player;

import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "AutoRespawn", type = Category.Player)
public class AutoRespawn extends Module {

    @EventTarget
    public void update(EventUpdate e){
        if (mc.player != null && mc.world != null) {
            if (mc.player.deathTime > 0) {
                mc.player.respawnPlayer();
            }
        }
    }
}
