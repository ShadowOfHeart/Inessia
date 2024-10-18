package gg.vape.module.impl.Movement;

import gg.vape.event.events.impl.EventUpdate;
import gg.vape.helpers.TimerHelper;
import gg.vape.settings.options.ModeSetting;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;


@ModuleInfo(name = "NoClip", type = Category.Movement)
public class NoClip extends Module {

    //    public BooleanSetting destroyBlocks = new BooleanSetting("Destroy", true).call(this);
    public ModeSetting mode = new ModeSetting("Мод", "SunRise", "SunRise", "Destroy Block").call(this);
    TimerHelper timerHelper = new TimerHelper();

    @EventTarget
    public void onMotion(EventMotion e) {
        if (mode.is("SunRise")) {
            if (mc.player.collidedHorizontally) {
                mc.player.onGround = true;
                if (!mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.player.motionY = 0;
            }

        }

    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.is("ReallyWorld")) {
            if (mc.player.collidedHorizontally) {
                mc.player.onGround = true;
                if (!mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.player.motionY = 0;


            }
            float f = mc.player.rotationYaw * 0.017453292F;
            double speed = 0.7;
            double x = -(MathHelper.sin(f) * speed);
            double z = MathHelper.cos(f) * speed;
            if (timerHelper.hasReached(mc.player.getDigSpeed(mc.world.getBlockState(new BlockPos(NoClip.mc.player.posX + x, NoClip.mc.player.posY + 0.4, NoClip.mc.player.posZ + z))))) {//, mc.player.inventory.getCurrentItem()
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.onPlayerDamageBlock(new BlockPos(NoClip.mc.player.posX + x, NoClip.mc.player.posY + 0.4, NoClip.mc.player.posZ + z), NoClip.mc.player.getHorizontalFacing());
                timerHelper.reset();
            }


        }
        if (mode.is("Destroy Block")) {
            mc.player.noClip = true;
            mc.player.onGround = true;
            mc.player.motionY = 0.0F;
            mc.player.jumpTicks = 0;
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -0.2F;
            }


            float f = mc.player.rotationYaw * 0.017453292F;
            double speed = 0.7;
            double x = -(MathHelper.sin(f) * speed);
            double z = MathHelper.cos(f) * speed;
            if (timerHelper.hasReached(mc.player.getDigSpeed(mc.world.getBlockState(new BlockPos(NoClip.mc.player.posX + x, NoClip.mc.player.posY + 0.4, NoClip.mc.player.posZ + z))))) {//, mc.player.inventory.getCurrentItem()
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.onPlayerDamageBlock(new BlockPos(NoClip.mc.player.posX + x, NoClip.mc.player.posY + 0.4, NoClip.mc.player.posZ + z), NoClip.mc.player.getHorizontalFacing());
                timerHelper.reset();
            }
        }
    }

}
