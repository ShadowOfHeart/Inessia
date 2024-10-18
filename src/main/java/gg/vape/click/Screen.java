package gg.vape.click;


import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.Medusa;
import gg.vape.helpers.*;
import gg.vape.helpers.animation.Animation;
import gg.vape.helpers.animation.Direction;
import gg.vape.helpers.animation.impl.EaseBackIn;
import gg.vape.helpers.animation.impl.EaseInOutQuad;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.math.MathHelper;
import gg.vape.helpers.render.*;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.impl.Render.ClickGui;
import gg.vape.settings.Setting;
import gg.vape.settings.config.Config;
import gg.vape.settings.config.ConfigManager;
import gg.vape.settings.options.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;



import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static gg.vape.helpers.render.RenderUtil.isHovered;
import static gg.vape.helpers.render.RoundedUtil.drawRound;
import static gg.vape.helpers.render.RoundedUtil.drawRoundOutline;

public class Screen extends GuiScreen {

    public int x, y;
    public static GuiTextField search;
    public static Config selectedConfig;
    public static Config lastConfig;
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    private int width;
    private int height;
    private float scrollOffset;

    public boolean dragging = false;
    public boolean categoryOpen = false;
    public Category selectedType = Category.Combat;
    public Category[] types;
    public Module selMod = Medusa.m.getModule(KillAura.class);
    public Module selMod2 = Medusa.m.getModule(ClickGui.class);
    private float translateAnim;
    private float animScroll;

    public Screen() {
        types = Category.values();
        this.x = 200;
        this.y = 100;
        this.translateAnim = -500.0F;
        this.animScroll = 0.0F;
    }

    public Animation openAnimation = new EaseBackIn(250, 1, 1);
    public Animation animation1 = new EaseInOutQuad(500, 1);
    public Animation animation2 = new EaseInOutQuad(500, 1);
    public Animation animation3 = new EaseInOutQuad(250, 1);
    public Animation animation4 = new EaseInOutQuad(500, 1);
    public Animation animation5 = new EaseInOutQuad(250, 1);
    public double scrollX, scrollXA;
    public double scrollXQ, scrollXAQ;
    public boolean bue;
    public double prevX, prevY;
    public boolean drag;
    public static boolean friends = false;
    public static boolean configs = false;
    public static boolean oClient = true;

    public int prevMouseX, prevMouseY;

    public boolean binding;
    public int categoryQ = 50;


    @Override
    public void initGui() {

        ScaledResolution sr = new ScaledResolution(this.mc);


        super.initGui();
//        openAnimation = new EaseBackIn(500, 1, 1);
//        openAnimation.setDirection(Direction.FORWARDS);
        this.translateAnim = -500.0F;
        this.animScroll = 0.0F;
    }

    public String text = "";

    @Override
    public void drawScreen(int mouseX1, int mouseY1, float partialTicks) {
        super.drawScreen(mouseX1, mouseX1, partialTicks);
        int mouseX = (int) ScaleUtil.calc(mouseX1, mouseY1)[0];
        int mouseY = (int) ScaleUtil.calc(mouseX1, mouseY1)[1];

//        if (openAnimation.getOutput() == 0) {
//            mc.player.closeScreen();
//        }
        if (scrollXAQ >= 0) {
            scrollXAQ = 0;
        }
        if (scrollXQ >= 25) {
            scrollXQ = 25;
        }

        dragging(mouseX, mouseY);
        ScaledResolution sr = new ScaledResolution(mc);
        x = (int) MathHelper.clamp(x, 0, ScaleUtil.calc(sr.getScaledWidth()) - 350);
        y = (int) MathHelper.clamp(y, 0, ScaleUtil.calc(sr.getScaledHeight()) - 200);
        if (ClickGui.blur.get()) {
            GaussianBlur.renderBlur(15);
        } else {

        }
        drawDefaultBackground();
        RenderUtil.drawRect(0, 0, mc.displayWidth, mc.displayHeight, new Color(new Color(ClickGui.getColor()).getRed(), new Color(ClickGui.getColor()).getGreen(), new Color(ClickGui.getColor()).getBlue(), (int) (openAnimation.getOutput() * 30)).darker().darker().darker().getRGB());
        ScaleUtil.scale_pre();

        categoryOpen = isHovered(mouseX, mouseY, x - 2, y, (float) (25f + categoryQ), 200);

//        this.animScroll = (float) AnimationUtil.animation(this.animScroll, this.scrollXAQ, 5.5E-6F);
        animScroll = (float) AnimationUtil.animation(animScroll, scrollXQ, 0.0000055f);
        this.translateAnim = AnimationUtil.animation(this.translateAnim, 0.0F, 0.1F);
        AnimationUtil.translateAnimation(this.translateAnim, 0.0F, () -> {

            int alpha = 255;
            RenderUtil.drawBlurredShadow(x, y, 350, 200, 15, Color.BLACK);
            StencilUtil.initStencilToWrite();

            drawRound(x, y, 350, 200, 3, new Color(21, 21, 21, alpha));
            StencilUtil.readStencilBuffer(1);
            drawRound(x, y, 350, 200, 3, new Color(21, 21, 21, alpha));
            // grid
            if (animating) {
                selectedType = preSelected;
                preSelected = null;
                animating = false;
                scrollX = 0;
            }
//        for (int i = 0; i < 15; i++) {
//            RenderUtil.drawRectWH((float) (x + categoryQ + 5 + i * (25)), y, 0.5f, 200, new Color(25, 25, 25, 255).getRGB());
//            RenderUtil.drawRectWH(x + 5, y + i * (25), 300, 0.5f, new Color(25, 25, 25, 255).getRGB());
//        }
            float y1;
            RenderUtil.drawTexture(new ResourceLocation("icons/guisetting.png"), this.x + 323, this.y + 10, 12, 12, new Color(255, 255, 255, 255));
            RenderUtil.drawTexture(new ResourceLocation("icons/player.png"), this.x + 323, this.y + 30, 12, 12, new Color(255, 255, 255, 255));
            RenderUtil.drawTexture(new ResourceLocation("icons/folder.png"), this.x + 323, this.y + 50, 12, 12, new Color(255, 255, 255, 255));
            RenderUtil.drawTexture(new ResourceLocation("icons/bb/template.png"), this.x + 323, this.y + 70, 12, 12, new Color(255, 255, 255, 255));

            {

                List<Module> list = Medusa.m.getModulesFromCategory(selectedType);
                List<Module> modules = Medusa.m.getModulesFromCategory(selectedType);
                int xSett = 145;
                int x = (int) (this.x + 35 + categoryQ);
                float y = (float) (this.y + 10 + scrollXA);


                for (Module m : modules) {
                    float heightBoost = 0;
                    for (Setting s : m.getSettingsForGUI()) {
                        if (m.opened) {
                            if (s instanceof ModeSetting) {
                                ModeSetting b = (ModeSetting) s;
                                if (b.hidden.get()) continue;
                                heightBoost += (b.opened ? b.modes.size() * 10 + 5 : 0);
                            }
                            if (s instanceof ListSetting) {
                                ListSetting b = (ListSetting) s;
                                if (b.hidden.get()) continue;
                                heightBoost += (b.opened ? b.list.size() * 10 + 5 : 0);
                            }

                            if (s instanceof ColorSetting) {
                                if (s.hidden.get()) continue;
                                heightBoost += 20;
                            }
                        }
                    }

                    m.animation.setDirection((m.state ? Direction.FORWARDS : Direction.BACKWARDS));
//              (m.animation.getOutput() * 15);
                    RenderUtil.drawBlurredShadow(x, y, 220, 28, 15, m.state ? new Color(36, 36, 36, (int) (m.animation.getOutput() * alpha)) : new Color(27, 27, 27, alpha));
                    drawRound(x, y, 220, 28, 4, m.state ? new Color(36, 36, 36, (int) (m.animation.getOutput() * alpha)) : new Color(27, 27, 27, alpha));
                    drawRound(x, y, 20, 28, 4, m.state ? new Color(ClickGui.getColor()) : new Color(36, 36, 36, alpha));
                    drawRound(x + 3, y, 17, 28, 0, m.state ? new Color(ClickGui.getColor()) : new Color(36, 36, 36, alpha));
                    drawRound(x + 209, y, 11, 28, 4, m.state ? new Color(46, 46, 46, alpha) : new Color(32, 32, 32, alpha));
                    drawRound(x + 209, y, 8, 28, 0, m.state ? new Color(46, 46, 46, alpha) : new Color(32, 32, 32, alpha));
                    int yds = 0;
                    for (int i = 0; i < 3; i++) {
                        Fonts.BOLD18.drawString(".", x + 214, y + 4 + yds, m.state ? new Color(255, 255, 255, alpha).getRGB() : new Color(255, 255, 255, 130).getRGB());
                        yds += 5;
                    }

                    RenderUtil.drawBlurredShadow(x + 185, y + 10, 17, 8, 5, m.state ? new Color(ClickGui.getColor()) : new Color(36, 36, 36, alpha));
                    drawRound(x + 185, y + 10, 17, 8, 3, m.state ? new Color(ClickGui.getColor()) : new Color(36, 36, 36, alpha));
                    RenderUtil.drawBlurredShadow((float) (x + 186 + (m.animation.getOutput() * 9)), y + 11, 6, 6, 5, m.state ? new Color(255, 255, 255, alpha) : new Color(255, 255, 255, 130));
                    drawRound((float) (x + 186 + (m.animation.getOutput() * 9)), y + 11, 6, 6, 2.5f, m.state ? new Color(255, 255, 255, alpha) : new Color(255, 255, 255, 130));

                    RenderUtil.drawTexture(new ResourceLocation("icons/" + selectedType + ".png"), x + 4.5f, y + 9, 12, 12, new Color(255, 255, 255, (int) net.minecraft.util.math.MathHelper.clamp((m.animation.getOutput() * 255), 30, 255)));


                    Fonts.BOLD18.drawString(m.name, x + 25, y + 4, m.state ? new Color(255, 255, 255, alpha).getRGB() : new Color(255, 255, 255, 130).getRGB());

                    if (binding && bindingModule == m) {
                        Fonts.BOLD16.drawString("[binding...]", x + 25, y + 17, new Color(255, 255, 255, 130).getRGB());
                    } else if (m.bind > 1) {
                        Fonts.BOLD16.drawString("[" + Keyboard.getKeyName(m.bind) + "]", x + 25, y + 17, new Color(255, 255, 255, 130).getRGB());
                    } else if (m.bind <= 0) {
                        Fonts.BOLD16.drawString("[.....]", x + 25, y + 17, new Color(255, 255, 255, 130).getRGB());
                    }

                    y += 35;
                }

                for (Module m : modules) {
                    float xQ = (int) (this.x + 35 + categoryQ + 135);
                    float yQ = (float) (this.y - 5 + scrollXAQ);
                    m.animation1.setDirection((m.opened ? Direction.FORWARDS : Direction.BACKWARDS));
//              (m.animation1.getOutput() * 15);

// + 135 + (m.animation1.getOutput() * -135)

                    drawRound(this.x, this.y - 1, 350, 202, 3, new Color(0, 0, 0, (int) (m.animation1.getOutput() * 130)));
                    drawRound((float) (xQ - 5 + 145 + (m.animation1.getOutput() * -145)), this.y - 1, 135, 202, 3, new Color(26, 26, 26, 255));
//                if (m.opened) {
                    Fonts.BOLD18.drawString("Настройка: " + m.name, xQ + 5 + 135 + (m.animation1.getOutput() * -135), yQ + 8, new Color(255, 255, 255, alpha).getRGB());

                    for (Setting s : m.getSettingsForGUI()) {
                        if (s instanceof ListSetting) {
                            ListSetting b = (ListSetting) s;
                            if (b.hidden.get()) continue;
                            yQ += 20;
                            b.animation.setDirection((b.opened ? Direction.FORWARDS : Direction.BACKWARDS));

                            drawRound((float) (xQ + 2 + 135 + (m.animation1.getOutput() * -135)), yQ + 4, 121F, (float) (12 + b.list.size() * (b.animation.getOutput() * 10)), 3, new Color(36, 36, 36, alpha));
                            Fonts.BOLD16.drawString(b.name, xQ + 5 + 135 + (m.animation1.getOutput() * -135), yQ - 3, new Color(73, 73, 73, alpha).getRGB());
                            Fonts.BOLD16.drawString("Включено  " + b.selected.size() + " из " + b.list.size(), xQ + 5 + 135 + (m.animation1.getOutput() * -135), yQ + 7, new Color(255, 255, 255, alpha).getRGB());

                            if (b.opened) {

//                                drawRound((float) (xQ + 5 + 135 + (m.animation1.getOutput() * -135)), yQ + 17, 115, 2, 0.5f, new Color(50, 50, 50, alpha));
                                for (int i = 0; i < b.list.size(); i++) {
                                    Fonts.BOLD16.drawString(b.list.get(i), xQ + 5 + 135 + (m.animation1.getOutput() * -135), yQ + 18 + (i * 10), b.selected.contains(b.list.get(i)) ? ClickGui.getColor() : new Color(255, 255, 255, 30).getRGB());
                                }

                            }
                            yQ += b.list.size() * net.minecraft.util.math.MathHelper.clamp((b.animation.getOutput() * 10), 0, 10) + 5;
                        }
                        if (s instanceof BooleanSetting) {
                            BooleanSetting b = (BooleanSetting) s;
                            if (b.hidden.get()) continue;
                            yQ += 20;
                            b.animation.setDirection((b.get() ? Direction.FORWARDS : Direction.BACKWARDS));


                            drawRound((float) (xQ + 5 + 135 + (m.animation1.getOutput() * -135)), yQ + 4, 17, 8, 3, new Color(45, 45, 45, alpha));
                            RenderUtil.drawBlurredShadow((float) ((float) (xQ + 6 + (b.animation.getOutput() * 9)) + 135 + (m.animation1.getOutput() * -135)), yQ + 5, 6, 6, 15, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));
                            drawRound((float) ((float) (xQ + 6 + b.animation.getOutput() * 9) + 135 + (m.animation1.getOutput() * -135)), yQ + 5, 6, 6, 2.5f, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));

                            Fonts.BOLD18.drawString(b.name.toLowerCase(), xQ + 25 + 135 + (m.animation1.getOutput() * -135), yQ + 4, new Color(255, 255, 255, (int) net.minecraft.util.math.MathHelper.clamp((b.animation.getOutput() * 255), 30, 255)).getRGB());

                        }
                        if (s instanceof SliderSetting) {
                            SliderSetting b = (SliderSetting) s;
                            if (b.hidden.get()) continue;
                            yQ += 20;
//                            drawRound(xQ + 2, yQ, 121, 15, 3, new Color(36, 36, 36, alpha));
                            if (b.sliding)
                                b.current = (float) MathHelper.round(net.minecraft.util.math.MathHelper.clamp((float) ((double) (mouseX - xQ - 120) * (b.maximum - b.minimum) / (double) 115 + b.maximum), b.minimum, b.maximum), b.increment);
                            b.sliderWidth = MathHelper.interpolate((((b.current) - b.minimum) / (b.maximum - b.minimum)) * 115, b.sliderWidth, 0.3);
                            float amountWidth = ((b.current) - b.minimum) / (b.maximum - b.minimum);
                            drawRound((float) (xQ + 5 + 135 + (m.animation1.getOutput() * -135)), yQ + 9, 115, 2, 1, new Color(56, 56, 56, alpha));
                            RenderUtil.drawBlurredShadow((float) ((float) (xQ + 5) + 135 + (m.animation1.getOutput() * -135)), yQ + 9, b.sliderWidth, 2, 15, new Color(ClickGui.getColor()));
                            drawRound((float) ((float) (xQ + 5) + 135 + (m.animation1.getOutput() * -135)), yQ + 9, b.sliderWidth, 2, 1, new Color(ClickGui.getColor()));
                            RenderUtil.drawBlurredShadow((float) ((float) (xQ + 3) + b.sliderWidth + 135 + (m.animation1.getOutput() * -135)), yQ + 8, 4, 4, 15, new Color(ClickGui.getColor()));
                            drawRound((float) ((float) (xQ + 3) + b.sliderWidth + 135 + (m.animation1.getOutput() * -135)), yQ + 8, 4, 4, 1.5f, new Color(ClickGui.getColor()));
                            Fonts.BOLD12.drawString(String.valueOf(b.current), xQ + 120 - Fonts.BOLD16.getStringWidth(String.valueOf(b.current)) + 135 + (m.animation1.getOutput() * -135), yQ + 2, new Color(100, 100, 100, alpha).getRGB());
                            Fonts.BOLD16.drawString(b.name, xQ + 5 + 135 + (m.animation1.getOutput() * -135), yQ + 0, new Color(100, 100, 100, alpha).getRGB());
                        }
                        if (s instanceof ModeSetting) {
                            ModeSetting b = (ModeSetting) s;
                            if (b.hidden.get()) continue;
                            yQ += 20;
                            b.animation.setDirection((b.opened ? Direction.FORWARDS : Direction.BACKWARDS));
                            drawRound((float) (xQ + 2 + 135 + (m.animation1.getOutput() * -135)), yQ + 5, 121, (float) (12 + (b.modes.size() * (b.animation.getOutput() * 10))), 3, new Color(36, 36, 36, alpha));
                            Fonts.BOLD16.drawString(b.name, xQ + 3 + 135 + (m.animation1.getOutput() * -135), yQ - 3, new Color(73, 73, 73, alpha).getRGB());
                            Fonts.BOLD16.drawString("Мод: " + b.currentMode, xQ + 5 + 135 + (m.animation1.getOutput() * -135), yQ + 8, new Color(255, 255, 255, alpha).getRGB());

                            if (b.opened) {

//                                drawRound((float) (xQ + 5 + 135 + (m.animation1.getOutput() * -135)), yQ + 17, 115, 2, 0.5f, new Color(50, 50, 50, alpha));
                                for (int i = 0; i < b.modes.size(); i++) {
                                    Fonts.BOLD16.drawString(b.modes.get(i), xQ + 5 + 135 + (m.animation1.getOutput() * -135), yQ + 18 + (i * 10), b.currentMode.contains(b.modes.get(i)) ? ClickGui.getColor() : new Color(255, 255, 255, 30).getRGB());
                                }

                            }
                            yQ += b.modes.size() * net.minecraft.util.math.MathHelper.clamp((b.animation.getOutput() * 10), 0, 10) + 5;
                        }
                        if (s instanceof ColorSetting) {
                            ColorSetting b = (ColorSetting) s;
                            if (b.hidden.get()) continue;
                            yQ += 20;

                            double soX = mouseX - xQ;
                            double soY = mouseY - yQ;
                            soX -= 20;
                            soY -= 17.5;
                            double dst = Math.sqrt(soX * soX + soY * soY);
                            double dst1 = Math.sqrt(soX * soX);
                            final float[] hsb = Color.RGBtoHSB(new Color(b.get()).getRed(), new Color(b.get()).getGreen(), new Color(b.get()).getBlue(), null);

                            double poX =
                                    hsb[1] * 15 * (Math.sin(Math.toRadians(hsb[0] * 360)) / Math
                                            .sin(Math.toRadians(90)));
                            double poY =
                                    hsb[1] * 15 * (Math.sin(Math.toRadians(90 - (hsb[0] * 360))) / Math
                                            .sin(Math.toRadians(90)));
                            if (dst > 15) {
                                b.slid = false;
                            }
                            if (b.slid) {
                                b.color = Color.HSBtoRGB((float) (Math.atan2(soX, soY) / (Math.PI * 2) - 1), (float) (dst / 15), 1);
                            }
                            drawRound((float) (xQ + 2 + 135 + (m.animation1.getOutput() * -135)), yQ, 121, 35, 3, new Color(36, 36, 36, alpha));
                            Fonts.BOLD18.drawString(b.name, xQ + 40 + 135 + (m.animation1.getOutput() * -135), yQ + 6, new Color(255, 255, 255).getRGB());
//                            RenderUtil.drawBlurredShadow((float) (xQ + 40 + 135 + (m.animation1.getOutput() * -135)), yQ + 25, 20, 5, 10, new Color(b.get()));
//                            RenderUtil.drawRectWH((float) (xQ + 40 + 135 + (m.animation1.getOutput() * -135)), yQ + 25, 20, 5, b.get());
                            RenderUtil.drawColoredCircle(xQ + 20 + 135 + (m.animation1.getOutput() * -135), yQ + 17.5, 15, 1);
                            drawRoundCircle((float) (xQ + 20 + 135 + (m.animation1.getOutput() * -135)), yQ + 17.5f, 32, new Color(36, 36, 36, alpha));
                            RoundedUtil.drawRoundCircle((float) (xQ + (float) poX + 20.5f + 135 + (m.animation1.getOutput() * -135)), yQ + (float) poY + 18, 3f, new Color(0, 0, 0, 255));
                            RoundedUtil.drawRoundCircle((float) (xQ + (float) poX + 20.5f + 135 + (m.animation1.getOutput() * -135)), yQ + (float) poY + 18, 2, new Color(b.get()));

                            yQ += 20;
                        }
//                    }
                    }


                }

                {
                    float xQ = (int) (this.x + 35 + categoryQ);
                    float yQ = (float) (this.y - 5 + scrollXQ);
                    animation1.setDirection((friends ? Direction.FORWARDS : Direction.BACKWARDS));


//              (animation1.getOutput() * 15)

// + 290 + (animation1.getOutput() * -300)

                    drawRound(this.x, this.y - 1, 350, 202, 3, new Color(0, 0, 0, (int) (animation1.getOutput() * 255)));
                    drawRound((float) (xQ - 5 + 290 + (animation1.getOutput() * -300)), this.y - 1, 320, 202, 3, new Color(26, 26, 26, 255));


                    for (int i = 0; i < Medusa.f.friends.size(); i++) {


                        drawRound((float) (xQ + 10 + 290 + (animation1.getOutput() * -300)), yQ + 15, 230, 25, 3, new Color(60, 60, 60, 60));
                        RenderUtil.drawTexture(new ResourceLocation("icons/bb/delete.png"), xQ + 210 + 300 + (animation1.getOutput() * -300), yQ + 20, 15, 15, new Color(255, 255, 255, 255));
                        RenderUtil.drawTexture(new ResourceLocation("icons/bb/altmanager/mojang.png"), xQ + 180 + 300 + (animation1.getOutput() * -300), yQ + 15, 25, 25, new Color(255, 255, 255, 255));

                        RenderUtil.downloadImage("https://minotar.net/bust/" + Medusa.f.friends.get(i) + "/100.png", (float) (xQ + 15 + 290 + (animation1.getOutput() * -300)), yQ + 17.5f, 20, 20);
                        Fonts.BOLD18.drawString(Medusa.f.friends.get(i), xQ + 42 + 290 + (animation1.getOutput() * -300), yQ + 24, new Color(255, 255, 255, 255).getRGB());

                        yQ += 35;
                    }
                    drawRound((float) (xQ - 5 + 290 + (animation1.getOutput() * -300)), this.y - 1, 320, 27, 3, new Color(30, 30, 30, 255));
                    RenderUtil.drawTexture(new ResourceLocation("icons/bb/close.png"), this.x + 325 + 300 + (animation1.getOutput() * -300), this.y + 8, 10, 10, new Color(255, 255, 255, 255));
                    Fonts.BOLD18.drawString("Тут сисок твоих друзей)", xQ + 10 + 290 + (animation1.getOutput() * -300), this.y + 5, new Color(255, 255, 255, 255).getRGB());
                    Fonts.BOLD18.drawString("Число друзей: " + Medusa.f.friends.size(), xQ + 10 + 290 + (animation1.getOutput() * -300), this.y + 15, new Color(255, 255, 255, 255).getRGB());
                    if(Medusa.f.friends.size()==0){
                        Fonts.BOLD18.drawString("Ты DeadInside у тебя нет друзей(", xQ + 300 + (animation1.getOutput() * -300),  this.y + 40, Color.WHITE.getRGB());
                    }

                }



                {

                    float xQ = (int) (this.x + 35 + categoryQ);
                    float yQ = (float) (this.y - 5);
                    animation4.setDirection((oClient ? Direction.FORWARDS : Direction.BACKWARDS));

                    drawRound(this.x, this.y - 1, 350, 202, 3, new Color(0, 0, 0, (int) (animation4.getOutput() * 255)));
                    drawRound((float) (xQ - 5 + 290 + (animation4.getOutput() * -300)), this.y - 1, 320, 202, 3, new Color(26, 26, 26, 255));

                    Fonts.BOLD16.drawString("Vape Client - позволяет вам сделать ваших врагов",xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 50, -1);
                    Fonts.BOLD16.drawString("инвалидами! Один из лучших Rage читов который позволяет",xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 65, -1);
                    Fonts.BOLD16.drawString("вам убивать ваших врагов, а не вас! А невероятно ахуенные и",xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 80, -1);
                    Fonts.BOLD16.drawString("красивые визуалы придают вашей игре новые краски!",xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 95, -1);
                    Fonts.BOLD16.drawString("Обходы этого софта дают противникам понят, что им до",xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 110, -1);
                    Fonts.BOLD16.drawString("вас еще очень далеко!",xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 125, -1);

                    Fonts.BOLD18.drawString("Username ->  "+ Medusa.username,xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 150, -1);
                    Fonts.BOLD18.drawString("UID ->  "+ Medusa.uid,xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 160, -1);
                    Fonts.BOLD18.drawString("Title ->  "+ Medusa.title,xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 170, -1);
                    Fonts.BOLD18.drawString("Version ->  "+ Medusa.version,xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 180, -1);
                    Fonts.BOLD18.drawString("Till ->  "+ Medusa.till,xQ + 0 + 300 + (animation4.getOutput() * -300), yQ + 190, -1);


                    drawRound((float) (xQ - 5 + 290 + (animation4.getOutput() * -300)), this.y - 1, 320, 27, 3, new Color(30, 30, 30, 255));
                    RenderUtil.drawTexture(new ResourceLocation("icons/bb/close.png"), this.x + 325 + 300 + (animation4.getOutput() * -300), this.y + 8, 10, 10, new Color(255, 255, 255, 255));
                    Fonts.BOLD18.drawString("О клиенте!",xQ + 10 + 290 + (animation4.getOutput() * -300), this.y + 6, -1);
                    Fonts.BOLD18.drawString("Тут написана инфа о нас",xQ + 10 + 290 + (animation4.getOutput() * -300), this.y + 16, -1);

                }


                {
                    float xQ = (int) (this.x + 35 + categoryQ);
                    float yQ = (float) (this.y - 5 + scrollXQ);
                    animation2.setDirection((configs ? Direction.FORWARDS : Direction.BACKWARDS));


//              (animation2.getOutput() * 15)

// + 300 + (animation2.getOutput() * -300)

                    drawRound(this.x, this.y - 1, 350, 202, 3, new Color(0, 0, 0, (int) (animation2.getOutput() * 255)));
                    drawRound((float) (xQ - 5 + 290 + (animation2.getOutput() * -300)), this.y - 1, 320, 202, 3, new Color(26, 26, 26, 255));
                    search = new GuiTextField(228, this.mc.fontRenderer, (int) (xQ + 3 + 300 + (animation2.getOutput() * -300)), (int) (this.y + 20 + 160), 85, 15);


                        search.drawTextBox();
                        if (search.getText().isEmpty() && !search.isFocused()) {
                            Fonts.BOLD16.drawString("Create config...", xQ + 10 + 300 + (animation2.getOutput() * -300), this.y - 5 + 190, -1);
                        }
                        for (ImageButton imageButton : this.imageButtons) {
                            imageButton.draw(mouseX, mouseY, Color.WHITE);
//                            if (!Mouse.isButtonDown(0)) continue;
                            imageButton.onClick(mouseX, mouseY);
                        }
                        int ii = 20;
                        for (int i = 0; i < 5; i++) {
                            drawRound((float) (xQ + 210 + 300 + (animation2.getOutput() * -300)), this.y - 5 + 15 + ii, 50, 15, 2, new Color(36, 36, 36, 255));
                            ii+=20;
                        }
                        Fonts.BOLD14.drawString("Создать", xQ + 215 + 300 + (animation2.getOutput() * -300), this.y - 5 + 120, -1);
                        Fonts.BOLD14.drawString("Загрузить", xQ + 215 + 300 + (animation2.getOutput() * -300), this.y - 5 + 40, -1);
                        Fonts.BOLD14.drawString("Сохранить", xQ + 215 + 300 + (animation2.getOutput() * -300), this.y - 5 + 60, -1);
                        Fonts.BOLD14.drawString("Удалить", xQ + 215 + 300 + (animation2.getOutput() * -300), this.y - 5 + 80, -1);
                        Fonts.BOLD14.drawString("Папка", xQ + 215 + 300 + (animation2.getOutput() * -300), this.y - 5 + 100, -1);

                        int yDist = 0;

                        for (Config config : Medusa.c.getContents()) {
                            int color;
                            if (isHovered( mouseX, mouseY, (float) (xQ + 300 + (animation2.getOutput() * -300)), yQ + 15 + yDist, 200, 15)) {
                                color = -1;
                            } else {
                                color = PaletteHelper.getColor(150);
                            }
                            if (selectedConfig != null && config.getName().equals(selectedConfig.getName())) {
                                drawRound((float) (xQ + 300 + (animation2.getOutput() * -300)), yQ + 15 + yDist, 200, 15, 3, new Color(60, 60, 60, 120));
                            } else {
                                drawRound((float) (xQ + 300 + (animation2.getOutput() * -300)), yQ + 15 + yDist, 200, 15, 3, new Color(60, 60, 60, 60));
                            }
                            Fonts.BOLD18.drawString(config.getName(), xQ + 5 + 300 + (animation2.getOutput() * -300), yQ + 19 + yDist, color);
                            yDist += 20;
                        }


                    drawRound((float) (xQ - 5 + 290 + (animation2.getOutput() * -300)), this.y - 1, 320, 27, 3, new Color(30, 30, 30, 255));
                    RenderUtil.drawTexture(new ResourceLocation("icons/bb/close.png"), this.x + 325 + 300 + (animation2.getOutput() * -300), this.y + 8, 10, 10, new Color(255, 255, 255, 255));
                    Fonts.BOLD18.drawString("Конфиг-Менеджер",xQ + 10 + 290 + (animation2.getOutput() * -300), this.y + 6, -1);
                    Fonts.BOLD18.drawString("Тут твои конфиги, у тебя их: " + Medusa.c.getContents().size(),xQ + 10 + 290 + (animation2.getOutput() * -300), this.y + 16, -1);

                }

                int x1 = (int) (this.x + 165 + categoryQ);
                y1 = (float) (this.y + 10 + scrollXA);


                scrollX = MathHelper.clamp((float) scrollX, (float) (-Math.max(y - this.y, y1 - this.y) - Math.max(y - this.y, y1 - this.y) + scrollX) + 170, (float) (0));


            }


            RenderUtil.drawRectWH((float) (x - 2), y - 2, (float) (25f + categoryQ), 204, new Color(30, 30, 30, alpha).getRGB());
                {
                    StencilUtil.initStencilToWrite();
                    RenderUtil.drawRectWH((float) (x - 2), y, (float) (25f + categoryQ), 200, new Color(25, 25, 25, alpha).getRGB());
                    StencilUtil.readStencilBuffer(1);
                    int i = 23;

                    for (Category type : types) {
                        type.animation.setDirection((selectedType == type ? Direction.FORWARDS : Direction.BACKWARDS));

//              (type.animation.getOutput() * 15);
                        drawRound(x + 2, (float) (y + (type.animation.getOutput() * i) + 9), 1, 14, 0.5f, selectedType == type ? new Color(ClickGui.getColor()) : new Color(0, 0, 0, 0));


                        Fonts.BOLD18.drawString(type.name(), x + 22, y + 13 + i, new Color(255, 255, 255, (int) net.minecraft.util.math.MathHelper.clamp((type.animation.getOutput() * 255), 30, 255)).getRGB());
                        RenderUtil.drawTexture(new ResourceLocation("icons/" + type.name().toLowerCase() + ".png"), (float) (x + 5), (float) (y + 10) + i, 12, 12, new Color(255, 255, 255, (int) net.minecraft.util.math.MathHelper.clamp((type.animation.getOutput() * 255), 30, 255)));
                        i += 20;
                    }

                    StencilUtil.uninitStencilBuffer();

            }
            RenderUtil.drawTexture(new ResourceLocation("vapegg/vape.png"), this.x + 3, this.y + 5, 65, 20, new Color(255, 255, 255, 255));
            animation3.setDirection((HoverUtility.isHovered(mouseX, mouseY, this.x - 1, this.y + 165, 75, 35) ? Direction.FORWARDS : Direction.BACKWARDS));


            drawRound((float) (this.x - 1), (float) (this.y + 175.5f - (animation3.getOutput() * 15)), 73.5f, 25.2f - 0.5f - 10, 0, new Color(16, 16, 16, alpha));
            Fonts.BOLD14.drawString(mc.player.getName(), this.x - 1 + 2, this.y + 162 + 20 - (animation3.getOutput() * 15), new Color(255, 255, 255, 255).getRGB());
            drawRound((float) (this.x - 1 + 2), (float) (this.y + 175.5f + 2 - (animation3.getOutput() * 15)), 73.5f - 4, 1, 0.5f, new Color(ClickGui.getColor()));

            drawRound(this.x - 1, this.y + 175.5f, 73.5f, 25.2f - 0.5f, 0, new Color(16, 16, 16, alpha));

            drawRound(this.x - 1 + 27, this.y + 175.5f + 2, 73.5f - 29, 1, 0.5f, new Color(ClickGui.getColor()));

            Fonts.BOLD16.drawString("Title: " + Medusa.title, this.x - 1 + 27, this.y + 182, new Color(255, 255, 255, alpha).getRGB());
            Fonts.BOLD16.drawString("UID: " + Medusa.uid, this.x - 1 + 27, this.y + 192, new Color(255, 255, 255, alpha).getRGB());


            RenderUtil.drawFace(this.x - 1, this.y + 175, 8, 8, 8, 8, 25, 25, 64, 64, (AbstractClientPlayer) mc.player);






        });


        StencilUtil.uninitStencilBuffer();
        prevMouseX = mouseX;
        prevMouseY = mouseY;
        scrollXA = MathHelper.interpolate(scrollX, scrollXA, 0.1);
        scrollX += ((Mouse.getDWheel() / 120f) * 30);

        ScaleUtil.scale_post();

    }

    @Override
    public void handleMouseInput() throws IOException {

        if(!friends && !configs && !oClient) {
            if (Mouse.hasWheel() && isHovered(prevMouseX, prevMouseY, this.x + 35 + categoryQ + 135, this.y - 10, 135, 200)) {
                int mouse = Mouse.getDWheel();
                if (mouse > 0) {
                    scrollXAQ += 30;
                } else {
                    if (mouse < 0) {
                        scrollXAQ -= 30;
                    }
                }
            }
        }
        if (friends || configs) {
            int mouse = Mouse.getDWheel();
            if (mouse > 0) {
                scrollXQ += 15;
            } else {
                if (mouse < 0) {
                    scrollXQ -= 15;
                }
            }
        }
        super.handleMouseInput();


    }
    public static void drawRoundCircle(float x, float y, float radius, Color color) {
        drawRoundOutline(x - (radius / 2), y - (radius / 2), radius, radius, (radius / 2) - 0.5f, 0.1f, new Color(0, 0, 0, 0), color);
    }

    public int getColor() {
        final ByteBuffer rgb = BufferUtils.createByteBuffer(100);
        GL11.glReadPixels(Mouse.getX(), Mouse.getY(), 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, rgb);
        return new Color(rgb.get(0) & 0xFF, rgb.get(1) & 0xFF, rgb.get(2) & 0xFF).getRGB();
    }

    public void dragging(int mouseX, int mouseY) {
        if (drag) {
            x = (int) (mouseX + prevX);
            y = (int) (mouseY + prevY);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        drag = false;
        selectedConfig = null;

        for (Module s : Medusa.m.m) {
            for (Setting setting : s.getSettingsForGUI()) {
                if (setting instanceof SliderSetting) {
                    SliderSetting sliderSetting = (SliderSetting) setting;
                    sliderSetting.sliding = false;
                }
                if (setting instanceof ColorSetting) {
                    ColorSetting b = (ColorSetting) setting;
                    b.slid = false;
                }
            }

        }
    }


    @Override
    protected void mouseReleased(int mouseX1, int mouseY1, int state) {
        super.mouseReleased(mouseX1, mouseY1, state);
        int mouseX = (int) ScaleUtil.calc(mouseX1, mouseY1)[0];
        int mouseY = (int) ScaleUtil.calc(mouseX1, mouseY1)[1];
        drag = false;
        Category selected2 = Category.Hud;
        for (Module m : Medusa.m.getModulesFromCategory(selected2)) {
            if (m == selMod2) {

                for (Setting s : m.getSettingsForGUI()) {
                    if (s instanceof ColorSetting) {
                        ColorSetting colorw = (ColorSetting) s;
                        colorw.slid = false;
                    }
                }

            }
        }
        for (Module s : Medusa.m.m) {
            for (Setting setting : s.getSettingsForGUI()) {
                if (setting instanceof SliderSetting) {
                    SliderSetting sliderSetting = (SliderSetting) setting;
                    sliderSetting.sliding = false;
                }
                if (setting instanceof ColorSetting) {
                    ColorSetting b = (ColorSetting) setting;
                    b.slid = false;
                }
            }

        }
    }

    public Module bindingModule;
    public Category preSelected;
    public boolean animating;

    public ArrayList<String> texts = new ArrayList<>();

    @Override
    protected void mouseClicked(int mouseX1, int mouseY1, int mouseButton) throws IOException {
        super.mouseClicked(mouseX1, mouseY1, mouseButton);
        int mouseX = (int) ScaleUtil.calc(mouseX1, mouseY1)[0];
        int mouseY = (int) ScaleUtil.calc(mouseX1, mouseY1)[1];
        if (isHovered(mouseX, mouseY, x, y, 75, 200)) {
            drag = true;
            prevX = x - mouseX;
            prevY = y - mouseY;
        }
        if(!friends && !configs && !oClient) {
            if (isHovered(mouseX, mouseY, this.x + 323, this.y + 50, 12, 12)) {
                try {
                    Desktop.getDesktop().browse(new File("C:\\Vape Client\\").toURI());
//                File file = new File(this.mc.gameDir + "C:\\Vape Client", "\\");
//                Sys.openURL(file.getAbsolutePath());
                } catch (Exception e) {
                    ChatUtil.print("Не удалось открыть папку!");
                }
            }
        }

        if(!friends && !configs && !oClient) {
            if (isHovered(mouseX, mouseY, this.x + 323, this.y + 70, 12, 12)) {
                configs = true;
                scrollXQ += 1000;
            }
        }
        if(!friends && !configs && !oClient) {
            if (isHovered(mouseX, mouseY, this.x + 323, this.y + 30, 12, 12)) {
                friends = true;
                scrollXQ += 1000;
            }
        }
        if(!friends && !configs && !oClient) {
            if (isHovered(mouseX, mouseY, this.x + 323, this.y + 10, 12, 12)) {
                oClient = true;
            }
        }
        if(configs) {
            float xQ = (int) (this.x + 35 + categoryQ);
            float yQ = (float) (this.y - 5 + scrollXQ);
            animation2.setDirection((configs ? Direction.FORWARDS : Direction.BACKWARDS));
            if (isHovered(mouseX, mouseY, this.x + 325, this.y + 8, 10, 10)) {
                configs = false;
            }
            search = new GuiTextField(228, this.mc.fontRenderer, (int) (xQ + 3 + 300 + (animation2.getOutput() * -300)), (int) (this.y + 20 + 160), 85, 15);

            search.mouseClicked(mouseX, mouseY, mouseButton);
            if (this.scrollOffset < 0.0f) {
                this.scrollOffset = 0.0f;
            }
            int yDist = 0;
            for (Config config : Medusa.c.getContents()) {
                if (isHovered(mouseX, mouseY, xQ, yQ + 15 + yDist, 200, 15)) {
                    selectedConfig = new Config(config.getName());
                }
                yDist+=20;
            }


        }
        if(oClient) {
            if (isHovered(mouseX, mouseY, this.x + 325, this.y + 8, 10, 10)) {
                oClient = false;
            }


        }
        if(friends) {
            float xQ = (int) (this.x + 35 + categoryQ);
            float yQ = (float) (this.y - 5 + scrollXQ);
            if (isHovered(mouseX, mouseY, this.x + 325, this.y + 8, 10, 10)) {
                friends = false;
            }
            for (int i = 0; i < Medusa.f.friends.size(); i++) {

                if (isHovered(mouseX, mouseY, xQ + 210, yQ + 20, 15, 15)) {
                    Medusa.f.friends.remove(i);
                }

                yQ += 35;
            }
        }
        int i = 23;
        for (Category type : types) {
            if (isHovered(mouseX, mouseY, (float) (x - 1), y + i + 6, (float) (25f + categoryQ), 20)) {
                preSelected = type;
                animating = true;
            }
            i += 20;
        }
        if (categoryOpen) return;
        List<Module> list = Medusa.m.getModulesFromCategory(selectedType);
        List<Module> modules = Medusa.m.getModulesFromCategory(selectedType);

        int x = this.x + 35 + categoryQ;
        float y = (float) (this.y + 10 + scrollXA);

        if(!friends && !configs && !oClient) {
            for (Module m : modules) {
                if (!selMod.opened) {
                    if (isHovered(mouseX, mouseY, x, y, 220, 28)) {
                        if (mouseButton == 0) {
                            scrollXAQ += 1000;
                            m.toggle();
                        }
                        if (mouseButton == 2) {
                            scrollXAQ += 1000;
                            bindingModule = m;
                            binding = true;
                        }
                        if (mouseButton == 1) {
                            scrollXAQ += 1000;
                            selMod = m;
                            m.opened = !m.opened;

                        }

                    }
                }
                y += 35;

            }

            for (Module m : modules) {

                float xQ = (int) (this.x + 35 + categoryQ + 135);
                float yQ = (float) (this.y - 5 + scrollXAQ);
                if (m.opened) {

                    if (isHovered(mouseX, mouseY, this.x, this.y, 210, 200)) {

                        if (mouseButton == 0) {

                            m.opened = !m.opened;


                        }

                    }

                    for (Setting s : m.getSettingsForGUI()) {
                        if (s instanceof ListSetting) {
                            ListSetting b = (ListSetting) s;
                            yQ += 20;
                            if (isHovered(mouseX, mouseY, xQ, yQ + 5, 125, 10)) {
                                b.opened = !b.opened;
                            }
                            if (b.opened) {
                                RenderUtil.drawRectWH(xQ, yQ + 14, 125, 0.5f, new Color(73, 73, 73, 255).getRGB());
                                for (int i1 = 0; i1 < b.list.size(); i1++) {
                                    if (isHovered(mouseX, mouseY, xQ, yQ + 18 + (i1 * 10), 125, 9f)) {
                                        if (b.selected.contains(b.list.get(i1))) {
                                            b.selected.remove(b.list.get(i1));
                                        } else {
                                            b.selected.add(b.list.get(i1));
                                        }
                                    }
                                }
                                yQ += b.list.size() * 10 + 5;
                            }
                            yQ += 5;
                        }
                        if (s instanceof SliderSetting) {

                            SliderSetting sl = (SliderSetting) s;
                            if (sl.hidden.get()) continue;
                            yQ += 20;
                            if (isHovered(mouseX, mouseY, xQ, yQ, 125, 15))
                                sl.sliding = true;
                        }
                        if (s instanceof BooleanSetting) {
                            BooleanSetting b = (BooleanSetting) s;
                            if (b.hidden.get()) continue;
                            yQ += 20;
                            if (isHovered(mouseX, mouseY, xQ, yQ, 125, 15)) {
                                b.set(!b.get());
                            }
                        }
                        if (s instanceof ModeSetting) {
                            ModeSetting b = (ModeSetting) s;
                            if (b.hidden.get()) continue;
                            yQ += 20;
                            if (isHovered(mouseX, mouseY, xQ, yQ + 5, 125, 10)) {
                                b.opened = !b.opened;
                            }
                            if (b.opened) {
                                RenderUtil.drawRectWH(xQ, yQ + 14, 125, 0.5f, new Color(73, 73, 73, 255).getRGB());
                                for (int i1 = 0; i1 < b.modes.size(); i1++) {
                                    if (isHovered(mouseX, mouseY, xQ, yQ + 18 + (i1 * 10), 125, 9f))
                                        b.currentMode = b.modes.get(i1);
                                }
                                yQ += b.modes.size() * 10 + 5;
                            }
                            yQ += 5;
                        }
                        if (s instanceof ColorSetting) {
                            ColorSetting b = (ColorSetting) s;
                            if (b.hidden.get()) continue;
                            yQ += 20;
                            double soX = mouseX - xQ;
                            double soY = mouseY - yQ;
                            soX -= 20;
                            soY -= 17.5;
                            double dst = Math.sqrt(soX * soX + soY * soY);
                            if (dst <= 15) {
                                b.slid = true;
                            }
                            yQ += 20;
                        }
                    }


                }
            }

        }

        if (configs) {
            float xQ = (int) (this.x + 35 + categoryQ);
            float yQ = (float) (this.y - 5 + scrollXQ);
            animation2.setDirection((configs ? Direction.FORWARDS : Direction.BACKWARDS));
            search = new GuiTextField(228, this.mc.fontRenderer, (int) (xQ + 3 + 300 + (animation2.getOutput() * -300)), (int) (this.y + 20 + 160), 85, 15);

            if (isHovered(mouseX, mouseY, (float) (xQ + 210 + 300 + (animation2.getOutput() * -300)), this.y - 5 + 15 + 100, 50, 15)) {
                if (!search.getText().isEmpty()) {
                    Medusa.c.saveConfig(search.getText());
                    ChatUtil.print((Object) ((Object) ChatFormatting.GREEN) + "Successfully " + (Object) ((Object) ChatFormatting.WHITE) + "saved config: " + (Object) ((Object) ChatFormatting.RED) + "\"" + search.getText() + "\"");
                    ConfigManager.getLoadedConfigs().clear();
                    Medusa.c.load();
                    search.setFocused(false);
                    search.setText("");
                } else {
                    ChatUtil.print((Object) ((Object) ChatFormatting.RED) + "Failed " + (Object) ((Object) ChatFormatting.WHITE) + "to create config! Please write your config name!");
                }
            }
            if (selectedConfig != null) {
                if (isHovered(mouseX, mouseY, (float) (xQ + 210 + 300 + (animation2.getOutput() * -300)), this.y - 5 + 15 + 20, 50, 15)) {
                    if (Medusa.c.loadConfig(selectedConfig.getName())) {
                        ChatUtil.print((Object) ((Object) ChatFormatting.GREEN) + "Successfully " + (Object) ((Object) ChatFormatting.WHITE) + "loaded config: " + (Object) ((Object) ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                        lastConfig = selectedConfig;
                    } else {
                        ChatUtil.print((Object) ((Object) ChatFormatting.RED) + "Failed " + (Object) ((Object) ChatFormatting.WHITE) + "load config: " + (Object) ((Object) ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                    }
                } else if (isHovered(mouseX, mouseY, (float) (xQ + 210 + 300 + (animation2.getOutput() * -300)), this.y - 5 + 15 + 40, 50, 15)) {
                    if (Medusa.c.saveConfig(selectedConfig.getName())) {
                        ChatUtil.print((Object) ((Object) ChatFormatting.GREEN) + "Successfully " + (Object) ((Object) ChatFormatting.WHITE) + "saved config: " + (Object) ((Object) ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                        lastConfig = selectedConfig;
                        ConfigManager.getLoadedConfigs().clear();
                        Medusa.c.load();
                    } else {
                        ChatUtil.print((Object) ((Object) ChatFormatting.RED) + "Failed " + (Object) ((Object) ChatFormatting.WHITE) + "to save config: " + (Object) ((Object) ChatFormatting.RED) + "\"" + search.getText() + "\"");
                    }
                } else if (isHovered(mouseX, mouseY, (float) (xQ + 210 + 300 + (animation2.getOutput() * -300)), this.y - 5 + 15 + 60, 50, 15)) {
                    if (Medusa.c.deleteConfig(selectedConfig.getName())) {
                        ChatUtil.print((Object) ((Object) ChatFormatting.GREEN) + "Successfully " + (Object) ((Object) ChatFormatting.WHITE) + "deleted config: " + (Object) ((Object) ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                    } else {
                        ChatUtil.print((Object) ((Object) ChatFormatting.RED) + "Failed " + (Object) ((Object) ChatFormatting.WHITE) + "to delete config: " + (Object) ((Object) ChatFormatting.RED) + "\"" + selectedConfig.getName() + "\"");
                    }
                }
            }
            if (isHovered(mouseX, mouseY, (float) (xQ + 210 + 300 + (animation2.getOutput() * -300)), this.y - 5 + 15 + 80, 50, 15)) {
                try {
                    Desktop.getDesktop().browse(new File("C:\\Vape Client\\configs").toURI());
                } catch (Exception e) {
                    ChatUtil.print("Не удалось открыть папку!");
                }
            }
        }

        int x1 = this.x + 165;
        float y1 = (float) (this.y + 10 + scrollXA);


    }
    public void renderPlayer(final int offset) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        float width1 = this.width - 2;
        float height = this.height - 2;
        float x = sr.getScaledWidth() / 2f - width1 / 2f;
        float y = sr.getScaledHeight() / 2f - height / 2f;
        int width = 400;
        int offset_y = 30;
        RenderUtil.drawBlurredShadow((float)((int)(x + width + 10.0) + offset), (float)(y + 10.0) + offset_y, 110.0f, 171.0f, 15, new Color(255,255,255,0));
        RoundedUtil.drawRound((int)(x + width + 10.0) + offset, (float)(y + 10.0) + offset_y, (int)(x + width + 120.0) + offset, (float)(y + 180.0), 1.5f, new Color(255,255,255,0));
        new Color(255, 255, 255, 255);
        GuiInventory.drawEntityOnScreen((int)(x + width + 64.0) + offset, (int)y + 165 + offset_y, 75, 0.0f, 0.0f, this.mc.player);
//        Gui.drawRect((int)(this.x + width + 28.0) + offset, (int)(this.y + 20.0) + offset_y, (int)(this.x + width + 29.0) + offset, (int)(this.y + 170.0) + offset_y, EntityESP.colorEsp.getRGB());
//        Gui.drawRect((int)(this.x + width + 100.0) + offset, (int)(this.y + 20.0) + offset_y, (int)(this.x + width + 101.0) + offset, (int)(this.y + 170.0) + offset_y, EntityESP.colorEsp.getRGB());
//        Gui.drawRect((int)(this.x + width + 28.0) + offset, (int)(this.y + 20.0) + offset_y, (int)(this.x + width + 101.0) + offset, (int)(this.y + 21.0) + offset_y, EntityESP.colorEsp.getRGB());
//        Gui.drawRect((int)(this.x + width + 28.0) + offset, (int)(this.y + 169.0) + offset_y, (int)(this.x + width + 101.0) + offset, (int)(this.y + 170.0) + offset_y, EntityESP.colorEsp.getRGB());
    }

    public static void drawEntityOnScreen(final float x, final float y, final float scale, final EntityLivingBase entityLivingBase) {
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableColorMaterial();
        GlStateManager.translate(x, y, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        new Color(255, 255, 255, 255);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(entityLivingBase.rotationPitch / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(entityLivingBase, 0.0, 0.0, 0.0, entityLivingBase.rotationYaw, 1.0f, false);
        rendermanager.setRenderShadow(true);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.popMatrix();
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);
    }

    private boolean isHoveredConfig(int x, int y, int width, int height, int mouseX, int mouseY) {
        return HoverUtility.isHovered(x, y, x + width, y + height, mouseX, mouseY);
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if (binding) {
            if (keyCode == 1) {
                bindingModule.bind = 0;
                binding = false;
                bindingModule = null;
                return;
            }
            bindingModule.bind = keyCode;
            binding = false;
            bindingModule = null;
        }
        if(configs) {
            float xQ = (int) (this.x + 35 + categoryQ);
            float yQ = (float) (this.y - 5 + scrollXQ);
            animation2.setDirection((configs ? Direction.FORWARDS : Direction.BACKWARDS));
            search = new GuiTextField(228, this.mc.fontRenderer, (int) (xQ + 3 + 300 + (animation2.getOutput() * -300)), (int) (this.y + 20 + 160), 85, 15);

            search.textboxKeyTyped(typedChar, keyCode);
            search.setText(search.getText().replace(" ", ""));
            if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
                search.setFocused(!search.isFocused());
            }
            try {
                super.keyTyped(typedChar, keyCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.keyTyped(typedChar, keyCode);
    }


    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
