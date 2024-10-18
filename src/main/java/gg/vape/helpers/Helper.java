package gg.vape.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;

import java.util.Random;

public interface Helper {

    Minecraft mc = Minecraft.getMinecraft();
    Gui gui = new Gui();
    Random random = new Random();
    TimerHelper timerHelper = new TimerHelper();
    ScaledResolution sr = new ScaledResolution(mc);


    static void sendPacket(Packet<?> packet) {
        mc.player.connection.sendPacket(packet);
    }

}