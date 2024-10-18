package gg.vape.module.impl.Render;

import com.mojang.realmsclient.gui.ChatFormatting;

import gg.vape.Medusa;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.editor.Drag;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.awt.*;

@ModuleInfo(name = "Watermark", type = Category.Hud)
public class Watermark extends Module {

    private int tick;

    public Drag d = Medusa.createDrag(this, "watermark", 4, 4);

    @EventTarget
    public void onRender(EventDisplay e) {
//        String ping = String.valueOf(mc.getConnection().getPlayerInfo(mc.player.getName()).getResponseTime());
        String text = "Vape Client" + ChatFormatting.DARK_GRAY + " / " + ChatFormatting.RESET + "User: " + Medusa.username + ChatFormatting.DARK_GRAY + " / " + ChatFormatting.RESET + "FPS: " + (mc.getDebugFPS() + 182);

        float width = Fonts.REG16.getStringWidth(text) + 6;

        int xx = (int) d.getX();
        int yy = (int) d.getY();
        d.setWidth(width);
        d.setHeight(12);
        RenderUtil.drawBlurredShadow(xx, yy, width, 12, 15, new Color(20, 20, 20));
        RenderUtil.drawRectWH(xx, yy, width, 12, new Color(20, 20, 20).getRGB());
        RenderUtil.drawBlurredShadow(xx, yy + 11, width, 1, 15, new Color(ClickGui.getColor()));
        RenderUtil.drawRectWH(xx, yy + 11, width, 0.9f, new Color(ClickGui.getColor()).getRGB());
        Fonts.REG16.drawStringWithShadow(text, xx + 3, yy + 3f, -1);
    }

}
