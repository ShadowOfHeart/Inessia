package gg.vape.module.impl.Movement;

import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventNoSlow;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "NoSlow", type = Category.Movement)
public class NoSlow extends Module {

    @EventTarget
    public void onEating(EventNoSlow e) {
        if(mc.player.onGround) {
            if (mc.player.ticksExisted % 2 == 0) {
                mc.player.motionX *= 0.4;
                mc.player.motionZ *= 0.4;
            }
            if (mc.player.ticksExisted % 4 == 0) {
                mc.player.motionX *= 1.2;
                mc.player.motionZ *= 1.2;
            }
        } else {
            mc.player.motionX *= 0.97;
            mc.player.motionZ *= 0.97;
        }
        e.cancel();
    }
}
