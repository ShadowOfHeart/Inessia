package gg.vape.module.impl.Render;

import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.event.events.impl.EventRender;
import gg.vape.helpers.BloomUtil;
import gg.vape.helpers.ColorUtility;
import gg.vape.helpers.Outline;
import gg.vape.helpers.ShaderUtility;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import gg.vape.settings.options.BooleanSetting;
import gg.vape.settings.options.ColorSetting;
import gg.vape.settings.options.ModeSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name = "ShaderESP", type = Category.Render)
public class ShaderESP extends Module {
    public Framebuffer framebuffer = new Framebuffer(1, 1, true);
    public ModeSetting colorMode = new ModeSetting("Мод", "Client", "Client", "Static").call(this);
    public BooleanSetting fill = new BooleanSetting("Заполнить", true).call(this);

    @EventTarget
    public void onRender(EventDisplay e) {

        BloomUtil.update(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.color(1, 1, 1, 1);
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player != mc.player) {
                mc.getRenderManager().renderEntityStaticNoShadow(player, e.ticks, false);
            }
        }
        GlStateManager.resetColor();
        GL11.glPopMatrix();
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);


        if (framebuffer != null) {
            GL11.glPushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0f);
            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            ShaderUtility.bindTexture(framebuffer.framebufferTexture);
            BloomUtil.renderBlur(ShaderUtility::drawQuads, 10, 2, colorMode.is("Client") ? ColorUtility.getColorStyle(1) : ClickGui.getColor(), true);
            if (fill.get()) {
                ShaderUtility.bindTexture(framebuffer.framebufferTexture);
                Outline.renderBlur(ShaderUtility::drawQuads, colorMode.is("Client") ? ColorUtility.getColorStyle(1) : ClickGui.getColor());
            }
            GlStateManager.disableAlpha();
            GL11.glPopMatrix();
        }

    }
}