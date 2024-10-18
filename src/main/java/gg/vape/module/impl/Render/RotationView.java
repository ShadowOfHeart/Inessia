package gg.vape.module.impl.Render;

import gg.vape.Medusa;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.event.events.impl.EventRender;
import gg.vape.helpers.Path;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "RotationView", type = Category.Render)
public class RotationView extends Module {
    public Path path;
    public static EntityLivingBase entity;
    public Vec2f getRotationToEntity(Entity entity) {
        Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
        Vec3d entityPos = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        double diffX = entityPos.x - eyesPos.x;
        double diffY = entityPos.y - eyesPos.y;
        double diffZ = entityPos.z - eyesPos.z;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, dist)));
        return new Vec2f(yaw, pitch);
    }
    @EventTarget
    public void onPreMotion(EventMotion e) {
//        entity = (EntityLivingBase) getNearestEntity();
//
//        if (entity == null) {
//            path = null;
//            return;
//        }
//        path = new Path(mc.player.getPositionVector(), entity.getPositionVector());
//        path.calculatePath(1);
//
//        Vec2f rotations = getRotationToEntity(entity);
//        e.setYaw(rotations.x);
//        e.setPitch(rotations.y);




        if(Medusa.m.getModule(KillAura.class).state) {
            mc.player.rotationPitchHead = mc.player.rotationPitch;
            mc.player.renderYawOffset = mc.player.rotationYaw;
            mc.player.rotationYawHead = mc.player.rotationYaw;
        }
    }

    public Entity getNearestEntity() {
        Entity nearest = null;
        double distance = 6;

        for (EntityPlayer entity : mc.world.playerEntities) {
            if (entity == mc.player) continue;


            if (nearest == null) {
                nearest = entity;

            }
        }

        return nearest;
    }
    @EventTarget
    public void onRender(EventRender e) {
//        if (path == null) return;
//        for (Vec3d entity : path.getPath()) {
//
//            if (mc.player.getPositionVector().distanceTo(entity) < 2) continue;
//
//            double x = (entity.x) - mc.getRenderManager().renderPosX;
//            double y = (entity.y) - mc.getRenderManager().renderPosY;
//            double z = (entity.z) - mc.getRenderManager().renderPosZ;
//            GL11.glPushMatrix();
//            mc.entityRenderer.setupCameraTransform(e.pt, 2);
//            GL11.glDisable(GL11.GL_ALPHA_TEST);
//            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//            GL11.glEnable(GL11.GL_BLEND);
//            GL11.glDisable(GL11.GL_TEXTURE_2D);
//            GL11.glDepthMask(false);
//            GlStateManager.color(1, 1, 1, 0.3f);
//            Render<Entity> render = null;
//            try {
//                render = mc.getRenderManager().getEntityRenderObject(mc.player);
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//            if (render != null) {
//                (RenderLivingBase.class.cast(render)).doRender((EntityLivingBase) mc.player, x, y, z, 1, mc.getRenderPartialTicks());
//            }
//            GL11.glEnable(GL11.GL_TEXTURE_2D);
//            GL11.glDepthMask(true);
//            GL11.glDisable(GL11.GL_BLEND);
//            GL11.glEnable(GL11.GL_ALPHA_TEST);
//
//
//            mc.entityRenderer.setupCameraTransform(e.pt, 0);
//            GlStateManager.resetColor();
//            GL11.glPopMatrix();
//        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        entity = null;
    }
}
