package gg.vape.notifications;

import gg.vape.helpers.StencilUtil;
import gg.vape.module.impl.Render.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import gg.vape.helpers.ScaleUtil;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.helpers.render.RoundedUtil;
import gg.vape.helpers.render.Translate;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

public class NotificationManager {

    public ArrayList<Notification> notifications = new ArrayList<>();

    public void call(String head, String text, NotificationType type) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Notification n = new Notification(text, type, head);
        n.translate = new Translate(ScaleUtil.calc(sr.getScaledWidth()) - 50, ScaleUtil.calc(sr.getScaledHeight()) - 10);
        notifications.add(n);

    }

    public void render() {
        ScaleUtil.scale_pre();
        int offset = 30;
        int x = 5;

        try {
            for (Notification n : notifications) {

                if ((System.currentTimeMillis() - n.startTime) > 2100) {
                    continue;
                }

                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                if ((System.currentTimeMillis() - n.startTime) > 2000) {
                    n.translate.interpolate(ScaleUtil.calc(sr.getScaledWidth()) + 100, ScaleUtil.calc(sr.getScaledHeight()) - offset, Minecraft.getMinecraft().deltaTime() * 10);
                } else
                    n.translate.interpolate(ScaleUtil.calc(sr.getScaledWidth()) - 115, ScaleUtil.calc(sr.getScaledHeight()) - offset, Minecraft.getMinecraft().deltaTime() * 10);



                StencilUtil.initStencilToWrite();
                RenderUtil.drawRectWH((float) n.translate.getX(), Math.round((float) n.translate.getY()), 110, 25, new Color(20, 20, 20, 150).getRGB());
                StencilUtil.readStencilBuffer(0);
                RenderUtil.drawBlurredShadow(Math.round((float) n.translate.getX()), Math.round((float) n.translate.getY()), 110, 25, 10, new Color(ClickGui.getColor()));
                StencilUtil.uninitStencilBuffer();
                RoundedUtil.drawRound(Math.round((float) n.translate.getX()), Math.round((float) n.translate.getY()), 110, 25, 3, new Color(26, 26, 26, 200));

//                RenderUtil.drawBlurredShadow(Math.round((float) n.translate.getX())+3, Math.round((float) n.translate.getY())+2, 104, 1, 15, new Color(ClickGui.getColor()));
//                RoundedUtil.drawRound(Math.round((float) n.translate.getX())+3, Math.round((float) n.translate.getY())+2, 104, 1, 0, new Color(ClickGui.getColor()));

                RenderUtil.drawTexture(new ResourceLocation("icons/exit.png"), Math.round((float) n.translate.getX() + x)-3, Math.round((float) n.translate.getY() + 5)-5, 24, 24, new Color(255, 255, 255,255));
                Fonts.BOLD18.drawString(n.header, Math.round((float) n.translate.getX() + x)+25, Math.round((float) n.translate.getY() + 5), new Color(255, 255, 255, 255).getRGB());
                Fonts.BOLD16.drawString(n.text, Math.round((float) n.translate.getX() + x)+25, Math.round((float) n.translate.getY() + 15), new Color(255, 255, 255, 255).getRGB());
                offset += 30;


            }
        } catch (Exception e) {

        }
        if (notifications.size() >= 20) {
            notifications.remove(0);
        }
        ScaleUtil.scale_post();
    }
}
