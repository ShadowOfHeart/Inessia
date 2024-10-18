package gg.vape.module.impl.Player;

import gg.vape.settings.options.ModeSetting;
import net.minecraft.network.play.client.CPacketPlayer;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "NoFall", type = Category.Player)
public class NoFall extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla").call(this);

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mode.get().equals("Vanilla")) {
            if (mc.player.fallDistance >= 3) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                mc.player.fallDistance = 0;
            }
        }

    }

}
