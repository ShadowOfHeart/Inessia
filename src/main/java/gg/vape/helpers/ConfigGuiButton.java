package gg.vape.helpers;


import java.awt.Color;

import gg.vape.helpers.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import org.lwjgl.input.Mouse;

import static gg.vape.helpers.Helper.mc;

public class ConfigGuiButton
        extends GuiButton {
    private int fade = 20;
    public static ScaledResolution sr = new ScaledResolution(mc);

    public ConfigGuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 35, buttonText);
    }

    public ConfigGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public static int getMouseX() {
        return Mouse.getX() * sr.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }

    public static int getMouseY() {
        return sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
        if (this.visible) {
            int height = this.height - 31;
            this.hovered = mouseX >= this.x + 93 && mouseY >= this.y - 41 && mouseX < this.x + this.width - 43 && mouseY < height + this.y - 30;
            Color text = new Color(155, 155, 155, 255);
            if (this.enabled) {
                if (this.hovered) {
                    if (this.fade < 100) {
                        this.fade += 8;
                    }
                    text = Color.white;
                } else if (this.fade > 20) {
                    this.fade -= 8;
                }
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            RectHelper.drawSkeetButton(this.x + 125, this.y + 2, this.width + this.x - 75, height + this.y);
            Fonts.BOLD16.drawCenteredString(this.displayString, (float)this.x + (float)this.width / 2.0f + 25.0f, (float)this.y + ((float)this.height - 72.5f), text.getRGB());
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        int height = this.height - 31;
        return this.enabled && this.visible && mouseX >= this.x + 93 && mouseY >= this.y - 41 && mouseX < this.x + this.width - 43 && mouseY < height + this.y - 30;
    }

    @Override
    public boolean isMouseOver() {
        return this.hovered;
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    @Override
    public int getButtonWidth() {
        return this.width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }
}

