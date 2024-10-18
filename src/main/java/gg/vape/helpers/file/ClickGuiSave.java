package gg.vape.helpers.file;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ClickGuiSave {

    public static File file = new File(Minecraft.getMinecraft().gameDir + "C:\\Vape Client\\clickgui.cfg");


    public static void save() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void load() {
        if (!file.exists()) {
            System.out.println("No clickgui data found");
            return;
        }
        String[] data;
        try {
            data = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).split(":");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to load clickgui");
            return;
        } catch (Exception ex) {
            System.out.println("Failed to set gui position");
        }
    }

}
