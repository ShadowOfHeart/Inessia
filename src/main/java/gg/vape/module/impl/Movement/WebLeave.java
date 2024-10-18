package gg.vape.module.impl.Movement;

import gg.vape.settings.options.SliderSetting;
import net.minecraft.block.BlockWeb;
import net.minecraft.util.math.BlockPos;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "WebLeave", type = Category.Movement)
public class WebLeave extends Module {

    public SliderSetting motion = new SliderSetting("Движение", 1, 0, 10, 0.1f).call(this);

    @EventTarget
    public void onMotion(EventMotion e) {
        if (mc.player.isInWeb) {
            mc.player.motionY = 1;
        }
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ)).getBlock() instanceof BlockWeb) {
            mc.player.motionY = motion.get();
        }
    }

}
