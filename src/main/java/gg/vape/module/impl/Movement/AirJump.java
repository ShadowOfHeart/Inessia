package gg.vape.module.impl.Movement;

import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.helpers.Helper;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "AirJump", type = Category.Movement)
public class AirJump extends Module {


    @EventTarget
    public void onUpdate(EventMotion e) {
        if ((!Helper.mc.world.getCollisionBoxes(Helper.mc.player, Helper.mc.player.getEntityBoundingBox().offset(0, -1, 0).expand(0.5, 0, 0.5)).isEmpty() && Helper.mc.player.ticksExisted % 2 == 0)) {
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                Helper.mc.player.jumpTicks = 0;
                Helper.mc.player.fallDistance = 0;
                e.setOnGround(true);
                Helper.mc.player.onGround = true;
            }
        }
    }
}
