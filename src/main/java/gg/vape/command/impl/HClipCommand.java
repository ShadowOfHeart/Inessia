package gg.vape.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.command.Command;
import gg.vape.command.CommandInfo;
import gg.vape.helpers.ChatUtil;

@CommandInfo(name = "hclip")
public class HClipCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        mc.player.setPosition(mc.player.posX - Math.sin(Math.toRadians(mc.player.rotationYaw)) * Double.parseDouble(args[1]), mc.player.posY, mc.player.posZ + Math.cos(Math.toRadians(mc.player.rotationYaw)) * Double.parseDouble(args[1]));
        ChatUtil.print("Clipped to " + args[1] + " blocks.");
    }

    @Override
    public void onError() {
        super.onError();
        ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> hclip <значение>");
    }
}
