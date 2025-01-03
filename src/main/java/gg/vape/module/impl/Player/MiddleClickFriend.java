package gg.vape.module.impl.Player;

import gg.vape.Medusa;
import gg.vape.notifications.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "MiddleClickFriend", type = Category.Player)
public class MiddleClickFriend extends Module {
    public boolean onFriend = true;


   @EventTarget
    public void onUpd(EventUpdate event) {
        for (Entity in : mc.world.loadedEntityList) {
            if (in == mc.player) continue;
            if (!(in instanceof EntityPlayer)) continue;
            if (!isLookingAtTarget(mc.player.rotationYaw, mc.player.rotationPitch, in, 500)) {
                if (Mouse.isButtonDown(2) && onFriend && ((KillAura) Medusa.m.getModule(KillAura.class)).target == null) {
                    this.onFriend = false;
                    Entity e = in;
                    if (!Medusa.f.isFriend(e.getName())) {
                        Medusa.notify.call("FriendManager", TextFormatting.getTextWithoutFormattingCodes(e.getName()) + " added", NotificationType.INFO);
                        Medusa.f.add(e.getName());
                    } else {
                        Medusa.notify.call("FriendManager", TextFormatting.getTextWithoutFormattingCodes(e.getName()) + " removed", NotificationType.INFO);
                        Medusa.f.remove(e.getName());
                    }
                }

                if (!Mouse.isButtonDown(2)) {
                    this.onFriend = true;
                }
            }
        }
    }

    public boolean isLookingAtTarget(float yaw, float pitch, Entity entity, double range) {
        Vec3d src = Minecraft.getMinecraft().player.getPositionEyes(1.0F);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.add(vectorForRotation.x * range, vectorForRotation.y * range, vectorForRotation.z * range);

        RayTraceResult rayTraceResult = Minecraft.getMinecraft().world.rayTraceBlocks(src, dest, false, false, true);

        if (rayTraceResult == null) {
            return true;
        }

        return (entity.getEntityBoundingBox().expand(0.06, 0.06, 0.06).calculateIntercept(src, dest) == null);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        onFriend = true;
    }
}
