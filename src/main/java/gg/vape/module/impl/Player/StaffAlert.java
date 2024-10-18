package gg.vape.module.impl.Player;

import gg.vape.Medusa;
import gg.vape.helpers.render.Translate;
import gg.vape.notifications.NotificationType;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import gg.vape.editor.Drag;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventPlayer;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@ModuleInfo(name = "StaffAlert", type = Category.Player)
public class StaffAlert extends Module {

    public ArrayList<NetworkPlayerInfo> staff = new ArrayList<>();

    public Translate translate = new Translate(0,0);
    public Drag drag = Medusa.createDrag(this, "staff", 20, 60);

    @Override
    public void onDisable() {
        super.onDisable();
        staff.clear();
    }
    @EventTarget
    public void onPower(EventPlayer eventPlayer) {
        if (eventPlayer.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER && check(eventPlayer.getPlayerData().getDisplayName().getUnformattedText().toLowerCase())) {
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (check(mc.player.connection.getPlayerInfo(eventPlayer.getPlayerData().getProfile().getId()).getDisplayName().getUnformattedText().toLowerCase()))
                    Medusa.notify.call("Staff Alert", eventPlayer.getPlayerData().getDisplayName().getFormattedText() + " joined!", NotificationType.INFO);
            }).start();
        }

        if (eventPlayer.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
            for (NetworkPlayerInfo info : staff) {
                if (info.getGameProfile().getId().equals(eventPlayer.getPlayerData().getProfile().getId())) {
                    if (mc.player.connection.getPlayerInfo(eventPlayer.getPlayerData().getProfile().getId()).getGameProfile().getName() == null) {
                        staff.remove(info);
                        Medusa.notify.call("Staff Alert", eventPlayer.getPlayerData().getDisplayName().getFormattedText() + " leaved!", NotificationType.INFO);
                    }
                    else {
                        Medusa.notify.call("Staff Alert", eventPlayer.getPlayerData().getDisplayName().getFormattedText() + " spectator!", NotificationType.INFO);
                    }
                    break;
                }
            }
        }

    }


    public boolean check(String name) {
        return name.contains("helper") || name.contains("moder") || name.contains("admin") || name.contains("owner") || name.contains("curator") || name.contains("хелпер") || name.contains("модер") || name.contains("админ") || name.contains("куратор");
    }
}
