package viamcp.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;

public class GuiCredits extends GuiScreen
{
    private GuiScreen parent;

    public GuiCredits(GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.add(new GuiButton(1, width / 2 - 100, height - 25, 200, 20, "Back"));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException
    {
        if (guiButton.id == 1)
        {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();

        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0, 2.0, 2.0);
        String title = TextFormatting.BOLD + "Credits";
        drawString(this.fontRenderer, title, (this.width - (this.fontRenderer.getStringWidth(title) * 2)) / 4, 5, -1);
        GlStateManager.popMatrix();

        int fixedHeight = ((5 + this.fontRenderer.FONT_HEIGHT) * 2) + 2;

        String viaVerTeam = TextFormatting.GRAY + (TextFormatting.BOLD + "ViaVersion Team");
        String florMich = TextFormatting.GRAY + (TextFormatting.BOLD + "FlorianMichael");
        String laVache = TextFormatting.GRAY + (TextFormatting.BOLD + "LaVache-FR");
        String hideri = TextFormatting.GRAY + (TextFormatting.BOLD + "Hiderichan / Foreheadchan");
        String contactInfo = TextFormatting.GRAY + (TextFormatting.BOLD + "Contact Info");

        drawString(this.fontRenderer, viaVerTeam, (this.width - this.fontRenderer.getStringWidth(viaVerTeam)) / 2, fixedHeight, -1);
        drawString(this.fontRenderer, "ViaVersion", (this.width - this.fontRenderer.getStringWidth("ViaVersion")) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT, -1);
        drawString(this.fontRenderer, "ViaBackwards", (this.width - this.fontRenderer.getStringWidth("ViaBackwards")) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 2, -1);
        drawString(this.fontRenderer, "ViaRewind", (this.width - this.fontRenderer.getStringWidth("ViaRewind")) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 3, -1);
        drawString(this.fontRenderer, florMich, (this.width - this.fontRenderer.getStringWidth(florMich)) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 5, -1);
        drawString(this.fontRenderer, "ViaForge", (this.width - this.fontRenderer.getStringWidth("ViaForge")) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 6, -1);
        drawString(this.fontRenderer, laVache, (this.width - this.fontRenderer.getStringWidth(laVache)) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 8, -1);
        drawString(this.fontRenderer, "Original ViaMCP", (this.width - this.fontRenderer.getStringWidth("Original ViaMCP")) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 9, -1);
        drawString(this.fontRenderer, hideri, (this.width - this.fontRenderer.getStringWidth(hideri)) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 11, -1);
        drawString(this.fontRenderer, "ViaMCP Reborn", (this.width - this.fontRenderer.getStringWidth("ViaMCP Reborn")) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 12, -1);
        drawString(this.fontRenderer, contactInfo, (this.width - this.fontRenderer.getStringWidth(contactInfo)) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 14, -1);
        drawString(this.fontRenderer, "Discord: Geh"+"ake#"+"7381", (this.width - this.fontRenderer.getStringWidth("Discord: Geh"+"ake#"+"7381")) / 2, fixedHeight + this.fontRenderer.FONT_HEIGHT * 15, -1);

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        drawString(this.fontRenderer, TextFormatting.GRAY + "(https://github.com/ViaVersion/ViaVersion)", (this.width + this.fontRenderer.getStringWidth("ViaVersion ")), (fixedHeight + this.fontRenderer.FONT_HEIGHT) * 2 + this.fontRenderer.FONT_HEIGHT / 2, -1);
        drawString(this.fontRenderer, TextFormatting.GRAY + "(https://github.com/ViaVersion/ViaBackward)", (this.width + this.fontRenderer.getStringWidth("ViaBackwards ")), (fixedHeight + this.fontRenderer.FONT_HEIGHT * 2) * 2 + this.fontRenderer.FONT_HEIGHT / 2, -1);
        drawString(this.fontRenderer, TextFormatting.GRAY + "(https://github.com/ViaVersion/ViaRewind)", (this.width + this.fontRenderer.getStringWidth("ViaRewind ")), (fixedHeight + this.fontRenderer.FONT_HEIGHT * 3) * 2 + this.fontRenderer.FONT_HEIGHT / 2, -1);
        drawString(this.fontRenderer, TextFormatting.GRAY + "(https://github.com/FlorianMichael/ViaForge)", (this.width + this.fontRenderer.getStringWidth("ViaForge ")), (fixedHeight + this.fontRenderer.FONT_HEIGHT * 6) * 2 + this.fontRenderer.FONT_HEIGHT / 2, -1);
        drawString(this.fontRenderer, TextFormatting.GRAY + "(https://github.com/LaVache-FR/ViaMCP)", (this.width + this.fontRenderer.getStringWidth("Original ViaMCP ")), (fixedHeight + this.fontRenderer.FONT_HEIGHT * 9) * 2 + this.fontRenderer.FONT_HEIGHT / 2, -1);
        drawString(this.fontRenderer, TextFormatting.GRAY + "(https://github.com/Foreheadchann/ViaMCP-Reborn)", (this.width + this.fontRenderer.getStringWidth("ViaMCP Reborn ")), (fixedHeight + this.fontRenderer.FONT_HEIGHT * 12) * 2 + this.fontRenderer.FONT_HEIGHT / 2, -1);
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
