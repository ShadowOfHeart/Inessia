package gg.vape.module.impl.Movement;

import gg.vape.helpers.MovementUtil;
import gg.vape.settings.options.BooleanSetting;
import net.minecraft.network.play.client.CPacketEntityAction;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.event.events.impl.EventPacket;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "Sprint", type = Category.Movement)
public class Sprint extends Module {

    public BooleanSetting allmode = new BooleanSetting("Во все сороны", false).call(this);

@EventTarget
public void onUpdate(EventMotion e) {
    if (allmode.get())
        mc.player.setSprinting(MovementUtil.isMoving());
    else
        mc.player.setSprinting(mc.player.moveForward > 0);
}

@EventTarget
public void onUpdate(EventPacket e) {
    if (e.getPacket() instanceof CPacketEntityAction) {
        CPacketEntityAction packet = (CPacketEntityAction) e.getPacket();
        if (packet.getAction() == CPacketEntityAction.Action.START_SPRINTING) {
            e.cancel();
        }
        if (packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
            e.cancel();
        }
       
    }
}

}
