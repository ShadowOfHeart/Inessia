package gg.vape.module.impl.Movement;

import gg.vape.helpers.MovementUtil;
import gg.vape.settings.options.ModeSetting;
import org.lwjgl.input.Keyboard;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "LongJump", type = Category.Movement)
public class LongJump extends Module {

    public ModeSetting mode = new ModeSetting("Мод", "Ванила", "Ванила").call(this);

    @EventTarget
    public void onUpdate(EventMotion e) {
        if (mode.is("Air Jump")) {
            if ((!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -1, 0).expand(-1f, 0, -1f).expand(1, 0, 1)).isEmpty())) {
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                    mc.player.jumpTicks = 0;
                    mc.player.fallDistance = 0;
                    e.setOnGround(true);
                    mc.player.onGround = true;
                }
            }
        }
        else if (mode.is("Ванила")) {
            if (!mc.player.onGround) {
                MovementUtil.setSpeed((float) (MovementUtil.getPlayerMotion() + 0.03f));
            }
        }
    }

}
