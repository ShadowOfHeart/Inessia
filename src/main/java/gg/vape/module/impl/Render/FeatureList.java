package gg.vape.module.impl.Render;

import gg.vape.Medusa;
import gg.vape.editor.Drag;
import gg.vape.helpers.ScaleUtil;
import gg.vape.helpers.font.FontRenderer;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.render.ColorUtil;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.helpers.render.RoundedUtil;
import gg.vape.helpers.render.Translate;
import gg.vape.settings.options.BooleanSetting;
import gg.vape.settings.options.ColorSetting;
import gg.vape.settings.options.ModeSetting;
import gg.vape.settings.options.SliderSetting;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;


import java.awt.*;
import java.util.ArrayList;

@ModuleInfo(name = "ArrayList", type = Category.Hud)
public class FeatureList extends Module {


    //setSuffix(String.valueOf(time.get()));
    public static ArrayList<Module> modules = new ArrayList<>();

    public static ModeSetting mode = new ModeSetting("Мод цвета", "Fade", "Fade", "Rainbow", "Astolfo", "Static");
    public static ModeSetting font = new ModeSetting("Шрифт", "Tenacity", "Rubik", "Tenacity");

    public static SliderSetting speed = new SliderSetting("Color Speed", 5, 1, 9.9f, 1).setHidden(() -> mode.get().equalsIgnoreCase("Static"));
    public static SliderSetting animationSpeed = new SliderSetting("Animation Speed", 50, 0, 100, 1);

    public BooleanSetting hide = new BooleanSetting("Убрать визуалы", true);
    public BooleanSetting glow = new BooleanSetting("Глов", true);
    public SliderSetting glowRadius = new SliderSetting("Glow Radius", 15, 5, 80, 5).setHidden(() -> !glow.get());
    public SliderSetting glowAlpha = new SliderSetting("Glow Alpha", 150, 0, 255, 1).setHidden(() -> !glow.get());
    public BooleanSetting shadow = new BooleanSetting("Тень", true);
    public BooleanSetting rightLine = new BooleanSetting("Линия с права", false);
    public BooleanSetting rightLineGlow = new BooleanSetting("Right Line Glow", true).setHidden(() -> !rightLine.get());
    public SliderSetting rightLineRadius = new SliderSetting("Right Line Radius", 15, 0, 25, 1).setHidden(() -> !rightLine.get() && !rightLineGlow.get());
    public SliderSetting rightLineAlpha = new SliderSetting("Right Line Alpha", 150, 0, 255, 1).setHidden(() -> !rightLine.get() && !rightLineGlow.get());
    public BooleanSetting lower = new BooleanSetting("to Lower Case", false);
    public ColorSetting s = new ColorSetting("Color", new Color(123, 157, 245).getRGB()).setHidden(() -> !(mode.is("Static") || mode.is("Fade")));
    public Drag d = Medusa.createDrag(this, "ArrayList", 4, 4);
    public FeatureList() {
        addSettings(hide, glow, shadow, mode);
    }
    


    public FontRenderer getFont() {
        if (font.is("Rubik")) {
            return Fonts.RUB14;
        }
        if (font.is("Tenacity")) {
            return Fonts.BOLD14;
        }
        return null;
    }

    @EventTarget
    public void onDisplay(EventDisplay e) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        ScaleUtil.scale_pre();
        modules.sort((f1, f2) -> getFont().getStringWidth(lower.get() ? f1.getDisplayName().toLowerCase() : f1.getDisplayName()) > getFont().getStringWidth(lower.get() ? f2.getDisplayName().toLowerCase() : f2.getDisplayName()) ? -1 : 1);
        int count = mc.player.getActivePotionEffects().size() > 0 ? ScaledResolution.getScaleFactor() > 2 ? 6 : 3 : 0;
//        int x = -500;
//        int y = 0;
        int x = (int) d.getX() - sr.getScaledWidth() + 5;
        int y = (int) d.getY() + 2;
        d.setWidth(100);
        d.setHeight(100);


        for (Module m : modules) {
            if (hide.get() && m.category == Category.Render) continue;
//            float width = ScaleUtil.calc(e.sr.getScaledWidth()) - getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) - 3;

            float width = sr.getScaledWidth();

            Translate translate = m.a;
            final int offset = (count * (getFont().getFontHeight() + 4));
            translate.interpolate(m.state ? width : ScaleUtil.calc(e.sr.getScaledWidth()) + 3d, offset, (animationSpeed.get() / 5) * mc.deltaTime());


            if (m.state && translate.getX() + x <= ScaleUtil.calc(e.sr.getScaledWidth() + x + 3)) {
                m.isRender = true;
            }
            if (translate.getX() + x >= ScaleUtil.calc(e.sr.getScaledWidth() + x) + 3) {
                m.isRender = false;
            }

            if (m.isRender) {
                GL11.glPushMatrix();
                GL11.glTranslated(-2 - (rightLine.get() ? 1 : 0), 2, 0);
                RoundedUtil.drawRound((float) translate.getX() + x, (float) translate.getY() + y, (float) (width - ScaleUtil.calc(e.sr.getScaledWidth()) + getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) + 3),  9, 0, new Color(10, 10, 10, 255));
                if (shadow.get())
                    RenderUtil.drawBlurredShadow((float) translate.getX() + x, (float) translate.getY() + y, (float) (width - ScaleUtil.calc(e.sr.getScaledWidth()) + getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) + 3),  9, 15, new Color(10, 10, 10, 255));
                GL11.glPopMatrix();
                count++;
            }
        }
        count = mc.player.getActivePotionEffects().size() > 0 ? ScaledResolution.getScaleFactor() > 2 ? 6 : 3 : 0;
        for (Module m : modules) {
            if (hide.get() && m.category == Category.Render) continue;
            float width = sr.getScaledWidth();
            Translate translate = m.a;
            if (m.isRender) {
                int color = getColor(count);
                GL11.glPushMatrix();
                GL11.glTranslated(-2 - (rightLine.get() ? 1 : 0), 2, 0);
                RenderUtil.drawRectWH((float) translate.getX() + x, (float) translate.getY() + y, (float) (width - ScaleUtil.calc(e.sr.getScaledWidth()) +getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName())+ 3), 9, new Color(10, 10, 10, 150).getRGB());
                if (rightLine.get())
                    RenderUtil.drawRectWH((float) translate.getX() + x - width + ScaleUtil.calc(e.sr.getScaledWidth()), (float) translate.getY() + y, 1, 9, color);
                getFont().drawString(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName(), (float) translate.getX() + x + 1.5f, (float) translate.getY() + y + 3 - (getFont() == Fonts.REG14 ? 1 : 0), color);
                GL11.glPopMatrix();

                count++;
            }
        }

        count = mc.player.getActivePotionEffects().size() > 0 ? ScaledResolution.getScaleFactor() > 2 ? 6 : 3 : 0;

        for (Module m : modules) {
            if (hide.get() && m.category == Category.Render) continue;
            float width = sr.getScaledWidth();
            Translate translate = m.a;
            if (m.isRender) {
                int color = getColor(count);
                GL11.glPushMatrix();
                GL11.glTranslated(-2 - (rightLine.get() ? 1 : 0), 2, 0);
                if (rightLine.get() && rightLineGlow.get())
                    RenderUtil.drawBlurredShadow(Math.round((float) translate.getX() + x - width + ScaleUtil.calc(e.sr.getScaledWidth())), Math.round((float) translate.getY() + y), 3F, 9, (int) rightLineRadius.get(), new Color(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), (int) rightLineAlpha.get()));
                if (glow.get()) {
                    RenderUtil.drawBlurredShadow(Math.round((float) translate.getX() + x), Math.round((float) translate.getY() + y) - 0.5f, (float) (width - ScaleUtil.calc(e.sr.getScaledWidth()) + getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) +3), 9, (int) glowRadius.get(), new Color(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), (int) glowAlpha.get()));
                }
                GL11.glPopMatrix();
                count++;
            }
        }
        ScaleUtil.scale_post();
    }

    public int getColor(int index) {
        if (mode.get().equalsIgnoreCase("Fade"))
            return ColorUtil.fade(10 - (int) speed.get(), index * 50, new Color(ClickGui.color.get()), 1).getRGB();
        if (mode.get().equalsIgnoreCase("Rainbow"))
            return ColorUtil.rainbow(10 - (int) speed.get(), index * 40, 0.7f, 1, 1).getRGB();
        if (mode.get().equalsIgnoreCase("Astolfo"))
            return astolfo(1, index * 25, 0.5f, 10 - speed.get()).getRGB();
        if (mode.get().equalsIgnoreCase("Static"))
            return ClickGui.color.get();
        return -1;
    }

    public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, saturation, 1F);
    }

    //get de

}
