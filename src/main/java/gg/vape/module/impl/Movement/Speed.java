package gg.vape.module.impl.Movement;

import gg.vape.helpers.MovementUtil;
import gg.vape.settings.options.ModeSetting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "Speed", type = Category.Movement)
public class Speed extends Module {

    public ModeSetting mode = new ModeSetting("Мод", "Matrix", "Matrix", "Sunrise", "Matrix Boost").call(this);

    @EventTarget
    public void onUpdate(EventMotion e) {
//        setSuffix(mode.get());

        if (mode.is("Matrix")) {
            if (MovementUtil.isMoving()) {
                if (mc.player.onGround)
                    mc.player.jump();
                if (mc.player.motionY == -0.4448259643949201 && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -1, 0).expand(0, 0.2, 0)).isEmpty()) {
                    mc.player.motionX *= 2;
                    mc.player.motionZ *= 2;
                }
            }
        } else if (mode.is("Sunrise")) {
            if (mc.player.onGround)
                MovementUtil.setSpeed((float) MovementUtil.getPlayerMotion() + 0.4f);
            if (!mc.player.onGround && mc.player.isInWater())
                MovementUtil.setSpeed((float) MovementUtil.getPlayerMotion() + 0.4f);
            MovementUtil.setSpeed((float) MovementUtil.getPlayerMotion());
        }
        else if (mode.is("Matrix Boost")) {
            if (MovementUtil.isMoving()) {
                if (mc.player.onGround) {
                    mc.player.jump();
                }
                if (mc.player.fallDistance > 0) {
                    MovementUtil.setSpeed(1);
                }
            }
        }
    }

    private void disabler() {

        int elytra = getSlotIDFromItem(Items.ELYTRA);

        if (mc.player.inventory.getItemStack().getItem() != Items.ELYTRA)
            mc.playerController.windowClick(0, elytra < 9 ? elytra + 36 : elytra, 1, ClickType.PICKUP, mc.player);

        mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, elytra < 9 ? elytra + 36 : elytra, 1, ClickType.PICKUP, mc.player);

    }

    public int getSlotIDFromItem(Item item) {
        for (ItemStack stack : mc.player.getArmorInventoryList()) {
            if (stack.getItem() == item) {
                return -2;
            }
        }
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }

}
