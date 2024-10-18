package gg.vape.module.impl.Misc;

import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventPacket;

@ModuleInfo(name = "ItemSwapFix", type = Category.Misc)
public class ItemSwapFix extends Module {

    @EventTarget
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof SPacketHeldItemChange) {
            e.cancel();
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        }
    }

}
