package gg.vape.module.impl.Render;

import gg.vape.helpers.render.AntiAliasing;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventRender;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.awt.*;

import static net.minecraft.client.renderer.RenderGlobal.drawBoundingBox;

@ModuleInfo(name = "Hitbox", type = Category.Render)
public class HitBox extends Module {

    @EventTarget
    public void onRender(EventRender e) {

        renderDebugBoundingBox(mc.player, e.pt, new Color(255, 0, 255, 255), 1, false, true);
    }

    private void renderDebugBoundingBox(Entity entityIn, float partialTicks, Color color, float line, boolean outline, boolean smooth) {
        float x = (float) ((float) ((float) (entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks)));
        float y = (float) (entityIn.lastTickPosY + (mc.player.posY - entityIn.lastTickPosY) * partialTicks);
        float z = (float) ((float) ((float) (entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks)));

        double renderPosX = mc.getRenderManager().renderPosX;
        double renderPosY = mc.getRenderManager().renderPosY;
        double renderPosZ = mc.getRenderManager().renderPosZ;
        x -= renderPosX;
        y -= renderPosY;
        z -= renderPosZ;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_CULL_FACE);
        AntiAliasing.hook(smooth, false, false);
        AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
        {
            if (outline) {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                GL11.glLineWidth(line + 1);
                bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
                drawBoundingBox(bufferbuilder, axisalignedbb.minX - entityIn.posX + x, axisalignedbb.minY - entityIn.posY + y, axisalignedbb.minZ - entityIn.posZ + z, axisalignedbb.maxX - entityIn.posX + x, axisalignedbb.maxY - entityIn.posY + y, axisalignedbb.maxZ - entityIn.posZ + z, 0, 0, 0, color.getAlpha() / 255f);
                tessellator.draw();
            }
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GL11.glLineWidth(line);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        drawBoundingBox(bufferbuilder, axisalignedbb.minX - entityIn.posX + x, axisalignedbb.minY - entityIn.posY + y, axisalignedbb.minZ - entityIn.posZ + z, axisalignedbb.maxX - entityIn.posX + x, axisalignedbb.maxY - entityIn.posY + y, axisalignedbb.maxZ - entityIn.posZ + z, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        tessellator.draw();
        AntiAliasing.unhook(smooth, false, false);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GlStateManager.resetColor();

        GL11.glPopMatrix();
    }


}
