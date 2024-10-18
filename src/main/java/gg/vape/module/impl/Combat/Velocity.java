package gg.vape.module.impl.Combat;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventPacket;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "Velocity", type = Category.Combat)
public class Velocity extends Module {

    @EventTarget
    public void onReceive(EventPacket e) {
        if (e.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity packet = (SPacketEntityVelocity) e.getPacket();
            if (packet.getEntityID() == mc.player.getEntityId()) {
                e.cancel();
            }
        }
        if (e.getPacket() instanceof SPacketExplosion) {
            e.cancel();
        }
    }

}
