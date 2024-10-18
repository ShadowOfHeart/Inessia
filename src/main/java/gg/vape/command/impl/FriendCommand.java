package gg.vape.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.Medusa;
import gg.vape.command.Command;
import gg.vape.command.CommandInfo;
import gg.vape.helpers.ChatUtil;

@CommandInfo(name = "friend")
public class FriendCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        if (Medusa.f.isFriend(args[1])) {
            Medusa.f.remove(args[1]);
            ChatUtil.print(args[1] + " Удален из друзей.");
        } else {
            Medusa.f.add(args[1]);
            ChatUtil.print(args[1] + " Добавлен в друзья.");
        }
    }

    @Override
    public void onError() {
        super.onError();
        ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> friend <имя игрока>");
    }
}
