package gg.vape.module.impl.Misc;

import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.text.TextComponentString;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventPlayer;

@ModuleInfo(name = "PlayerLogger", type = Category.Misc)
public class PlayerLogger extends Module {

    @EventTarget
    public void onPacket(EventPlayer e) {
        if (e.getAction() == SPacketPlayerListItem.Action.UPDATE_GAME_MODE) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(("&8[&e/&8] &r" + mc.getConnection().getPlayerInfo(e.getPlayerData().getProfile().getId()).getDisplayName().getFormattedText() + "&8 → &e" + e.getPlayerData().gamemode.getName().toUpperCase()).replaceAll("&", "§")));
        }
    }

}
