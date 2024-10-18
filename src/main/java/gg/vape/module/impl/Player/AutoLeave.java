package gg.vape.module.impl.Player;

import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.Medusa;
import gg.vape.helpers.ChatUtil;
import gg.vape.helpers.math.MathHelper;
import gg.vape.settings.options.BooleanSetting;
import gg.vape.settings.options.ModeSetting;
import gg.vape.settings.options.SliderSetting;
import net.minecraft.entity.player.EntityPlayer;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "AutoLeave", type = Category.Player)
public class AutoLeave extends Module {

    private final ModeSetting mode =
            new ModeSetting("Мод", "/spawn", "/spawn", "/home", "/hub", "/lobby").call(this);
    private final SliderSetting radius = new SliderSetting("Радиус", 30, 1, 100, 5).call(this);

    private final BooleanSetting ignoreFriends = new BooleanSetting("Игнорить друзей", true);

    @EventTarget
    public void onUpdate(EventMotion e) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == mc.player || ignoreFriends.get() && Medusa.f.friends.contains(player.getName()))
                continue;

            double distance = mc.player.getDistance(player);
            if (distance <= radius.get()) {
                ChatUtil.print(ChatFormatting.YELLOW + "[AutoLeave]: " + ChatFormatting.WHITE + "замечен игрок " +
                        ChatFormatting.GREEN + player.getName() + ChatFormatting.WHITE + " в радиусе " +
                        ChatFormatting.GREEN + MathHelper.round(distance, 1));
                mc.player.sendChatMessage(mode.get());
                toggle();
            }
        }
    }

}