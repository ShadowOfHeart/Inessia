package gg.vape.command.impl;

import gg.vape.command.Command;
import gg.vape.command.CommandInfo;
import gg.vape.helpers.ChatUtil;

@CommandInfo(name = "help")
public class HelpCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        ChatUtil.print("Список команд: ");
        ChatUtil.print(".vclip <дистанция> - вертикальная телепортация");
        ChatUtil.print(".hclip <дистанция> - горизонтальная телепортация");
        ChatUtil.print(".toggle <имя модуля> - включить/выключить модуль");
        ChatUtil.print(".crash - краш сервера с помощью скинов");
        ChatUtil.print(".help - показать список команд");
        ChatUtil.print(".config load <имя конфига> - загрузить конфиг");
        ChatUtil.print(".config save <имя конфига> - сохранить конфиг");
        ChatUtil.print(".config list - список конфигов");
        ChatUtil.print(".friend <имя друга> - добавить/удалить друга");
        ChatUtil.print(".bind <имя модуля> <клавиша> - назначить модуль на клавишу");
        ChatUtil.print(".set <имя модуля> <тип настройки> <имя настройки> <значение> - изменить настройку модуля");
        ChatUtil.print(".kick <ник> - кикает игрока через эксплойт");
    }
}
