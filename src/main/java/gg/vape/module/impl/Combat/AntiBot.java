package gg.vape.module.impl.Combat;

import gg.vape.event.events.impl.EventUpdate;
import gg.vape.settings.options.BooleanSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@ModuleInfo(name = "AntiBot", type = Category.Combat)
public class AntiBot extends Module {

    public static ArrayList<Entity> isBot = new ArrayList<>();
    public static BooleanSetting invisible = new BooleanSetting("Игнор невидимых", true);

    public AntiBot(){
        addSettings(invisible);
    }
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        try {
            for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                if (entityPlayer.isInvisible() && entityPlayer != mc.player) {
                    mc.world.removeEntity(entityPlayer);
                }
            }
        } catch (Exception ex) {
//            ChatUtil.print("Module: [AntiBot.class] ->" + ChatFormatting.GREEN + " Bot Remove!");

        }
    }
    public static List<Entity> isBotPlayer = new ArrayList<>();

    @EventTarget
    public void onEvent(EventMotion event) {
        if (event instanceof EventMotion) {
            for (Entity entity : mc.world.playerEntities) {
                if (!entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8))) && entity instanceof EntityOtherPlayerMP) {
                    isBotPlayer.add(entity);
                }
                if (!entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8))) && entity.isInvisible() && entity instanceof EntityOtherPlayerMP) {
                    mc.world.removeEntity(entity);
                }
            }
        }
    }
    @EventTarget
    public void onMotion(EventMotion e) {
        for (Entity entity : mc.world.loadedEntityList) {

            if (!entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8))) && entity instanceof EntityOtherPlayerMP) {
                if (!isBot.contains(entity)) {
                    isBot.add(entity);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        isBot.clear();
    }

}
