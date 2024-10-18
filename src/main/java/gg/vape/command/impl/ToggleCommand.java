package gg.vape.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.Medusa;
import gg.vape.command.Command;
import gg.vape.command.CommandInfo;
import gg.vape.helpers.ChatUtil;
import gg.vape.module.Module;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(name = "toggle")
public class ToggleCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        Medusa.m.getModule(args[1]).toggle();
    }

    @Override
    public void onError() {
        super.onError();
        ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> toggle <имя модуля>");
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        List<String> modules = new ArrayList<>();
        for (Module m : Medusa.m.m) {
            modules.add(m.name);
        }
        return modules;
    }
}
