package gg.vape.module.impl.Movement;

import gg.vape.helpers.MovementUtil;
import gg.vape.helpers.math.MathHelper;
import gg.vape.settings.options.ModeSetting;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "Jesus", type = Category.Movement)
public class Jesus extends Module {

    public ModeSetting mode = new ModeSetting("���", "Matrix Solid", "Matrix Solid").call(this);

    @EventTarget
    public void onMotion(EventMotion e) {
    if (mode.is("Matrix Solid")) {
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.008, mc.player.posZ)).getBlock() == Blocks.WATER
                && !mc.player.onGround) {
            boolean isUp = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.03, mc.player.posZ)).getBlock() == Blocks.WATER;
            mc.player.jumpMovementFactor = 0;
            float yport = MovementUtil.getPlayerMotion() > 0.1 ? 0.02f : 0.032f;
            mc.player.setVelocity(mc.player.motionX, mc.player.fallDistance < 3.5 ? (isUp ? yport : -yport) : -0.1, mc.player.motionZ);
        }
        if (mc.player.posY > (int) mc.player.posY + 0.89 && mc.player.posY <= (int) mc.player.posY + 1 || mc.player.fallDistance > 3.5) {
            mc.player.posY = (int) mc.player.posY + 1 + 1E-45;
            if (!mc.player.isInWater() && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ)).getBlock() == Blocks.WATER) {

                if (mc.player.collidedHorizontally) {
                    mc.player.motionY = 0.2;
                    mc.player.motionX *= 0f;
                    mc.player.motionZ *= 0f;
                }
                MovementUtil.setSpeed(MathHelper.clamp((float) (MovementUtil.getPlayerMotion() + 0.2f), 0.5f, 1.14f));
            }

        }
        if ((mc.player.isInWater() || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.15, mc.player.posZ)).getBlock() == Blocks.WATER)) {
            mc.player.motionY = 0.16;
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)).getBlock() == Blocks.AIR) {
                mc.player.motionY = 0.12;
            }
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Blocks.AIR) {
                mc.player.motionY = 0.18;
            }
        }
    }
    }

}
