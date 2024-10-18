package gg.vape.module.impl.Player;

import net.minecraft.network.play.client.CPacketCloseWindow;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventPacket;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "XCarry", type = Category.Player)
public class XCarry extends Module {

    @EventTarget
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof CPacketCloseWindow) {
            e.cancel();
        }
    }

}
