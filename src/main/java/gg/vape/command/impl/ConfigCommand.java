package gg.vape.command.impl;

import gg.vape.Medusa;
import gg.vape.command.Command;
import gg.vape.command.CommandInfo;
import gg.vape.helpers.ChatUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(name = "config")
public class ConfigCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        if (args[1].startsWith("load")) {
            Medusa.c.loadConfig(args[2]);
            ChatUtil.print("Конфигурация загружена.");
        }
        if (args[1].startsWith("save")) {
            Medusa.c.saveConfig(args[2]);
            ChatUtil.print("Конфигурация сохранена.");
        }
        if (args[1].startsWith("list")) {
            File file = new File(mc.gameDir + "C:\\Vape Client\\configs");
            for (File s : file.listFiles()) {
                ChatUtil.print(s.getName());
            }
        }
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        try {
            if (args[1].startsWith("load")) {
                File file = new File(mc.gameDir + "C:\\Vape Client\\configs");
                return Arrays.asList(file.listFiles()).stream().map(File::getName).collect(Collectors.toList());
            }
            if (args[1].startsWith("save")) {
                File file = new File(mc.gameDir + "C:\\Vape Client\\configs");
                return Arrays.asList(file.listFiles()).stream().map(File::getName).collect(Collectors.toList());
            }
            return Arrays.asList("load", "save", "list");
        } catch (Exception ex) {
            return null;
        }
    }
}
