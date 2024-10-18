package gg.vape.module.impl.Player;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventPacket;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "NoRotationSet", type = Category.Player)
public class NoRotationSet extends Module {

    @EventTarget
    public void onRotate(EventPacket e) {
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) e.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
        }
    }

}
