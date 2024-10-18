package gg.vape.command;

import gg.vape.command.impl.*;


import java.util.ArrayList;

public class CommandManager {

    public ArrayList<Command> cmds = new ArrayList<>();

    public CommandManager() {
        cmds.add(new VClipCommand());
        cmds.add(new HClipCommand());
        cmds.add(new HelpCommand());
        cmds.add(new ToggleCommand());
        cmds.add(new ConfigCommand());
        cmds.add(new FriendCommand());
        cmds.add(new BindCommand());
        cmds.add(new SettingCommand());
        cmds.add(new KickCommand());
        cmds.add(new CrashCommand());
    }

}
