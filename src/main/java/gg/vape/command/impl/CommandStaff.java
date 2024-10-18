package gg.vape.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.command.Command;
import gg.vape.command.CommandInfo;
import gg.vape.helpers.ChatUtil;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(name = "staff")
public class CommandStaff extends Command {

    public static List<String> staffNames = new ArrayList<>();

    @Override
    public void onError() {
        ChatUtil.print(ChatFormatting.GRAY + "?????? ? ?????????????" + ChatFormatting.WHITE + ":");
        ChatUtil.print(ChatFormatting.WHITE + ".staff " + ChatFormatting.GRAY + "<"
                + ChatFormatting.RED + "add; remove; clear; list." + ChatFormatting.GRAY + ">");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("add")) {
                if (staffNames.contains(args[2])) {
//                    ChatUtil.print(ChatFormatting.RED + "???? ????? ??? ? Staff List!");
                } else {
                    staffNames.add(args[2]);
//                    ChatUtil.print(ChatFormatting.GREEN + "??? " + ChatFormatting.WHITE + args[2] + ChatFormatting.GREEN + " ???????? ? Staff List");
                }
            }
            if (args[1].equalsIgnoreCase("remove")) {
                if (staffNames.contains(args[2])) {
                    staffNames.remove(args[2]);
//                    ChatUtil.print(ChatFormatting.GREEN + "??? " + ChatFormatting.WHITE + args[2] + ChatFormatting.GREEN + " ?????? ?? Staff List");
                } else {
//                    ChatUtil.print(ChatFormatting.RED + "????? ?????? ??? ? Staff List!");
                }
            }
            if (args[1].equalsIgnoreCase("clear")) {
                if (staffNames.isEmpty()) {
//                    ChatUtil.print(ChatFormatting.RED + "Staff List ????!");
                } else {
                    staffNames.clear();
//                    ChatUtil.print(ChatFormatting.GREEN + "Staff List ??????");
                }
            }
            if (args[1].equalsIgnoreCase("list")) {
//                ChatUtil.print(ChatFormatting.GRAY + "?????? Staff:");
                for (String name : staffNames) {
                    ChatUtil.print(ChatFormatting.WHITE + name);
                }
            }

        } else {
            onError();
        }
    }
}
