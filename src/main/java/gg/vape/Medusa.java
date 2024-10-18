package gg.vape;

import gg.vape.altmanager.AltManager;
import gg.vape.altmanager.Session;
import gg.vape.command.CommandManager;
import gg.vape.editor.Drag;
import gg.vape.editor.DragManager;
import gg.vape.helpers.ChangeLog;
import gg.vape.helpers.FriendManager;
import gg.vape.profile.Profile;
import gg.vape.helpers.render.TPS;
import gg.vape.module.Manager;
import gg.vape.module.Module;
import gg.vape.module.impl.Render.FeatureList;
import gg.vape.notifications.NotificationManager;
import gg.vape.settings.config.ConfigManager;
import org.jsoup.nodes.Document;

import gg.vape.click.Screen;
import viamcp.ViaMCP;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;

public class Medusa {
    public static Manager m;
    public static Screen s;

    public static boolean protectedd = false;


    public static Robot imageRobot;
    public static NotificationManager notify;
    public static ConfigManager c;
    public static AltManager alt;
    public static FriendManager f;

    public static CommandManager d = new CommandManager();
    public static ChangeLog changeLog = new ChangeLog();
    public static int admin_ID = 1;

    public static String username;
    public static int uid;
    public static String version;
    public static String till;
    public static String title;
    public static int balance;
    public static boolean subscription;
    public static String password;

    public void load() {

//        new Discord().start();
        Document a = null;

        alt = new AltManager();
        try {
            imageRobot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        notify = new NotificationManager();


        Profile.load();

        m = new Manager();
        s = new Screen();
        c = new ConfigManager();
        DragManager.loadDragData();
        c.loadConfig("default");
        FeatureList.modules.addAll(Medusa.m.m);
        new TPS();
        f = new FriendManager();


        ViaMCP.getInstance().start();
        ViaMCP.getInstance().initAsyncSlider();
    }

    public void unload() {
        c.saveConfig("default");

        DragManager.saveDragData();
        try {
            if (AltManager.file.exists()) AltManager.file.delete();
            try (FileWriter fr = new FileWriter(AltManager.file)) {
                for (Session s : AltManager.sessions) {
                    fr.write(s.nick + "\n");
                }
            }

        } catch (Exception ex) {

        }
    }

    public static BufferedImage getScreenImage() {
        return imageRobot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }

    public static Drag createDrag(Module module, String name, float x, float y) {
        DragManager.draggables.put(name, new Drag(module, name, x, y));
        return DragManager.draggables.get(name);
    }


    public static void keyEvent(int key) {
        for (Module m : m.m) {
            if (m.bind == key) {
                m.toggle();
            }
        }
    }
}
