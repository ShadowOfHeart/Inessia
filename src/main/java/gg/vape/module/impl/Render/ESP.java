package gg.vape.module.impl.Render;

import com.jhlabs.vecmath.Vector4f;
import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.math.MathHelper;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import gg.vape.settings.options.BooleanSetting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;


import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "ESP", type = Category.Render)
public class ESP extends Module {
    public final List<Entity> collectedEntities;
    private final IntBuffer viewport;
    private final FloatBuffer modelview;
    private final FloatBuffer projection;
    private final FloatBuffer vector;

    public BooleanSetting box = new BooleanSetting("Коробка", true);
    public BooleanSetting name = new BooleanSetting("Ник", false);
    public BooleanSetting item = new BooleanSetting("Предмет", true);
    public BooleanSetting health = new BooleanSetting("Здоровье", true);
    public BooleanSetting healthBar = new BooleanSetting("Полоска здоровья", true);

    public ESP() {
        this.collectedEntities = new ArrayList<>();
        this.viewport = GLAllocation.createDirectIntBuffer(16);
        this.modelview = GLAllocation.createDirectFloatBuffer(16);
        this.projection = GLAllocation.createDirectFloatBuffer(16);
        this.vector = GLAllocation.createDirectFloatBuffer(4);
    }

    @EventTarget
    public void onRender2D(final EventDisplay event) {
        GL11.glPushMatrix();
        this.collectEntities();
        final float partialTicks = event.ticks;
        final int scaleFactor = ScaledResolution.getScaleFactor();
        final double scaling = scaleFactor / Math.pow(scaleFactor, 2.0);
        GL11.glScaled(scaling, scaling, scaling);
        final RenderManager renderMng = mc.getRenderManager();
        final EntityRenderer entityRenderer = mc.entityRenderer;
        final List<Entity> collectedEntities = this.collectedEntities;
        for (final Entity entity : collectedEntities) {
            if (entity instanceof EntityPlayer && RenderUtil.isInViewFrustrum(entity)) {
                final double x = MathHelper.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
                final double y = MathHelper.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
                final double z = MathHelper.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
                final double width = entity.width / 1.5;
                final double n = entity.height + 0.2f - (entity.isSneaking() ? 0.2f : 0.0f);
                final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + n, z + width);
                final Vec3d[] vectors = {new Vec3d(aabb.minX, aabb.minY, aabb.minZ), new Vec3d(aabb.minX, aabb.maxY, aabb.minZ), new Vec3d(aabb.maxX, aabb.minY, aabb.minZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vec3d(aabb.minX, aabb.minY, aabb.maxZ), new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4f position = null;
                for (Vec3d vector : vectors) {
                    vector = this.project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4f((float) vector.x, (float) vector.y, (float) vector.z, 1.0f);
                        }
                        position.x = (float) Math.min(vector.x, position.x);
                        position.y = (float) Math.min(vector.y, position.y);
                        position.z = (float) Math.max(vector.x, position.z);
                        position.w = (float) Math.max(vector.y, position.w);
                    }
                }
                if (position != null) {
                    entityRenderer.setupOverlayRendering();
                    final double posX = position.x;
                    final double posY = position.y;
                    final double endPosX = position.z;
                    final double endPosY = position.w;
                    if (box.get()) {
                        RenderUtil.drawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, Color.black.getRGB());
                        RenderUtil.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, Color.black.getRGB());
                        RenderUtil.drawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, Color.black.getRGB());
                        RenderUtil.drawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, Color.black.getRGB());
                        RenderUtil.drawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, -1);
                        RenderUtil.drawRect(posX, endPosY - 0.5D, endPosX, endPosY, -1);
                        RenderUtil.drawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, -1);
                        RenderUtil.drawRect(endPosX - 0.5D, posY, endPosX, endPosY, -1);
                    }
                    final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                    final double hpPercentage = entityLivingBase.getHealth() / entityLivingBase.getMaxHealth();
                    final double hpHeight2 = (endPosY - posY) * hpPercentage;
                    final double hpHeight3 = (endPosY - posY);
                    double dif = (endPosX - posX) / 2.0D;
                    double textWidth = (Fonts.BOLD18.getStringWidth(entityLivingBase.getName()));
                    if (entityLivingBase.getHealth() > 0.0f) {
                        if (name.get())
                            Fonts.BOLD18.drawStringWithOutline(ChatFormatting.stripFormatting(entity.getName()), (float) ((posX + dif - textWidth / 2.0D)) - 1, (float) (posY) - 20 + 18, -1);
                        if (healthBar.get()) {

                            RenderUtil.drawRect((float) (posX - 3.5), (float) (endPosY + 0.5), (float) (posX - 1.5), (float) (endPosY - hpHeight3 - 0.5), Color.BLACK.getRGB());
                            RenderUtil.drawRect((float) (posX - 3.5), (float) (endPosY), (float) (posX - 1.5), (float) (endPosY - hpHeight3), ClickGui.getColor());
                            RenderUtil.drawRect(posX - 3.5F, posY, posX - 1.5, (endPosY - hpHeight2), new Color(0, 0, 0, 255).getRGB());
                        }
                    }
                    if (!entityLivingBase.getHeldItemMainhand().isEmpty()) {
                        if (item.get())
                            Fonts.BOLD18.drawCenteredStringWithOutline(ChatFormatting.stripFormatting(entityLivingBase.getHeldItemMainhand().getDisplayName()), (float) (posX + (endPosX - posX) / 2.0D), (float) (endPosY + 0.5D) + 4, -1);
                    }
                    if (health.get())
                        Fonts.BOLD14.drawStringWithOutline((int) entityLivingBase.getHealth() + "HP", (float) (posX - 10.5D) - Fonts.pix.getStringWidth((int) entityLivingBase.getHealth() + "HP"), (float) (endPosY) - hpHeight2 + 4, getHealthColor(entityLivingBase, ClickGui.getColor(), ClickGui.getColor()));
                }
            }
        }
        GL11.glPopMatrix();
    }
    public static int getHealthColor(final EntityLivingBase entity, final int c1, final int c2) {
        final float health = entity.getHealth();
        final float maxHealth = entity.getMaxHealth();
        final float hpPercentage = health / maxHealth;
        final int red = (int) ((c2 >> 16 & 0xFF) * hpPercentage + (c1 >> 16 & 0xFF) * (1.0f - hpPercentage));
        final int green = (int) ((c2 >> 8 & 0xFF) * hpPercentage + (c1 >> 8 & 0xFF) * (1.0f - hpPercentage));
        final int blue = (int) ((c2 & 0xFF) * hpPercentage + (c1 & 0xFF) * (1.0f - hpPercentage));
        return new Color(red, green, blue).getRGB();
    }


    private boolean isValid(final Entity entity) {
        if (entity == mc.player && mc.gameSettings.thirdPersonView == 0) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        return entity instanceof EntityPlayer;
    }

    private void collectEntities() {
        this.collectedEntities.clear();
        final List<Entity> playerEntities = mc.world.loadedEntityList;
        for (final Entity entity : playerEntities) {
            if (this.isValid(entity)) {
                this.collectedEntities.add(entity);
            }
        }
    }

    private Vec3d project2D(final int scaleFactor, final double x, final double y, final double z) {
        GL11.glGetFloat(2982, this.modelview);
        GL11.glGetFloat(2983, this.projection);
        GL11.glGetInteger(2978, this.viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.vector)) {
            return new Vec3d(this.vector.get(0) / scaleFactor, (Display.getHeight() - this.vector.get(1)) / scaleFactor, this.vector.get(2));
        }
        return null;
    }
}