package gg.vape.module.impl.Render;

import gg.vape.helpers.math.MathHelper;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventCustomModel;
import gg.vape.model.models.TessellatorModel;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "CustomModel", type = Category.Render)
public class CustomModel extends Module {

    private TessellatorModel hitlerHead;
    private TessellatorModel hitlerBody;


    @Override
    public void onEnable() {
        super.onEnable();
        this.hitlerHead = new TessellatorModel("/assets/minecraft/vapegg/obj/head.obj");
        this.hitlerBody = new TessellatorModel("/assets/minecraft/vapegg/obj/body.obj");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.hitlerHead = null;
        this.hitlerBody = null;
    }

    @EventTarget
    private void on(final EventCustomModel event) {
        GlStateManager.pushMatrix();

        AbstractClientPlayer entity = mc.player;
        RenderManager manager = mc.getRenderManager();
        double x = MathHelper.interpolate(entity.posX, entity.lastTickPosX, mc.getRenderPartialTicks()) - manager.renderPosX;
        double y = MathHelper.interpolate(entity.posY, entity.lastTickPosY, mc.getRenderPartialTicks()) - manager.renderPosY;
        double z =  MathHelper.interpolate(entity.posZ, entity.lastTickPosZ, mc.getRenderPartialTicks()) - manager.renderPosZ;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        boolean sneak = mc.player.isSneaking();
        GL11.glTranslated(x, y, z);
        if (!(mc.currentScreen instanceof GuiContainer))
            GL11.glRotatef(-yaw, 0.0F, mc.player.height, 0.0F);
        GlStateManager.scale(0.03, sneak ? 0.027 : 0.029, 0.03);

        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1, 1.0F);
        this.hitlerHead.render();
        this.hitlerBody.render();
        GlStateManager.enableLighting();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();

    }

}
