package gg.vape.module.impl.Render;

import gg.vape.Medusa;
import gg.vape.editor.Drag;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.helpers.StencilUtil;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.helpers.render.RoundedUtil;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;



import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleInfo(name = "PotionHUD", type = Category.Hud)
public class PotionHUD extends Module {
    public Drag d = Medusa.createDrag(this, "PotionHUD", 60, 60);

    @EventTarget
    public void onRender(EventDisplay e) {

        float x = d.getX();
        float y = d.getY();
        d.setWidth(120);
        d.setHeight(60);


        List<PotionEffect> potions = new ArrayList<>(mc.player.getActivePotionEffects());
        potions.sort(Comparator.comparingDouble(effect -> -Fonts.BOLD16.getStringWidth(effect(effect))));


        int xOff = 21;
        int yOff = 14;
        int counter = 16;
        if (potions.size() == 0) return;
        d.setWidth(Fonts.BOLD16.getStringWidth(effect(potions.get(0))) + 26);
        d.setHeight((counter + Fonts.BOLD16.getFontHeight() - 2) * potions.size());
        GL11.glPushMatrix();

        for (PotionEffect potion : potions) {
            Potion effect = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
            String durationString = Potion.getDurationString(potion);
            StencilUtil.initStencilToWrite();
            RenderUtil.drawRectWH(x + xOff - 21, (y + counter) - yOff - 1, Fonts.BOLD16.getStringWidth(effect(potion)) + 4 + 26, 22, new Color(0,0,0,128).getRGB());
            StencilUtil.readStencilBuffer(2);
            RenderUtil.drawBlurredShadow(x + xOff - 21, (y + counter) - yOff - 1, Fonts.BOLD16.getStringWidth(effect(potion)) + 4 + 26, 22, 30, new Color(ClickGui.getColor()));
            StencilUtil.uninitStencilBuffer();

            int finalCounter = counter;
            RenderUtil.blur(() -> {
                RoundedUtil.drawRound(x + xOff - 21, (y + finalCounter) - yOff - 2, Fonts.BOLD16.getStringWidth(effect(potion)) + 4 + 26, 25, 1, new Color(255, 255, 255, 80));
            }, 5);


            RoundedUtil.drawRoundOutline(x + xOff - 21, (y + counter) - yOff - 2, Fonts.BOLD16.getStringWidth(effect(potion)) + 4 + 26, 25, 3, 0.4F, new Color(21, 21, 21, 150), new Color(ClickGui.getColor()));


            assert effect != null;
            if (effect.hasStatusIcon()) {
                Minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                int statusIconIndex = effect.getStatusIconIndex();

                new Gui().drawTexturedModalRect((float) ((x + xOff) - 18), (y + counter) - yOff + 1, statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
            }


            Fonts.BOLD16.drawString(effect(potion), x + xOff + 2, (y + counter) - yOff + 3, new Color(150, 150, 150).getRGB(), false);

            Fonts.BOLD16.drawString(durationString, x + xOff + 2, (y + counter + 10) - yOff, new Color(150, 150, 150).getRGB(), false);

            counter += 27;
        }
        GlStateManager.enableBlend();
        GL11.glPopMatrix();
    }


    public String effect(PotionEffect potion) {
        Potion effect = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
        assert effect != null;
        String level = I18n.format(effect.getName());

        if (potion.getAmplifier() == 1) level = level + " " + I18n.format("enchantment.level.2");
        else if (potion.getAmplifier() == 2) level = level + " " + I18n.format("enchantment.level.3");
        else if (potion.getAmplifier() == 3) level = level + " " + I18n.format("enchantment.level.4");

        return level;
    }
}



