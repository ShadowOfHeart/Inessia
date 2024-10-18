package gg.vape.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.Medusa;
import gg.vape.command.Command;
import gg.vape.command.CommandInfo;
import gg.vape.helpers.ChatUtil;
import gg.vape.settings.Setting;
import gg.vape.settings.options.BooleanSetting;
import gg.vape.settings.options.ModeSetting;
import gg.vape.settings.options.SliderSetting;
import gg.vape.module.Module;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(name = "set")
public class SettingCommand extends Command {
    @Override
    public void execute(String[] args) {
        super.execute(args);
        Setting s = null;
        for (Setting set : Medusa.m.getModule(args[1]).getSettings()) {
            if (set.name.contains(args[3])) {
                s = set;
            }
        }
        if (s == null) {
            ChatUtil.print(ChatFormatting.RED + "Нет такой настройки.");
            return;
        }
        if (args[2].contains("boolean")) {
            ((BooleanSetting) s).set(Boolean.parseBoolean(args[4]));
        }
        else if (args[2].contains("slider")) {
            ((SliderSetting) s).current = (Float.parseFloat(args[4]));
        }
        else if (args[2].contains("mode")) {
            if ( ((ModeSetting) s).modes.contains(args[4])) {
                ((ModeSetting) s).currentMode = (args[4]);
            }
            else {
                ChatUtil.print(ChatFormatting.RED + "Нет такого мода.");
                return;
            }
        }

        ChatUtil.print("Установлено значение " + args[4] + " на " + args[3] + " для модуля " + args[1]);

    }

    @Override
    public void onError() {
        super.onError();
        ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> set <имя модуля> <тип настройки> <имя настройки> <значение>");
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        if (args.length <= 1) {
            List<String> modules = new ArrayList<>();
            for (Module m : Medusa.m.m) {
                modules.add(m.name);
            }
            return modules;
        }
        else if (args.length <= 2) {
            List<String> types = new ArrayList<>();
            types.add("boolean");
            types.add("slider");
            types.add("mode");
            return types;
        }
        else if (args.length <= 3) {
            List<String> types = new ArrayList<>();
            for (Setting s : Medusa.m.getModule(args[1]).getSettings()) {
                if (s.name.contains(" ")) continue;
                types.add(s.name);
            }
            return types;
        }

        return null;
    }

}
