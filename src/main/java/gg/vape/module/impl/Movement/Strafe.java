package gg.vape.module.impl.Movement;

import gg.vape.event.events.impl.EventPacket;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.helpers.*;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import gg.vape.settings.options.BooleanSetting;
import gg.vape.settings.options.ModeSetting;
import gg.vape.settings.options.SliderSetting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;

import java.util.Random;

@ModuleInfo(name = "Strafe", type = Category.Movement)
public class Strafe extends Module {
    public ModeSetting mode = new ModeSetting("Strafe Mode", "Matrix", "Matrix", "Hard");
    public double boost;
    TimerUtility timerUtility = new TimerUtility();
    public BooleanSetting elytra = new BooleanSetting("Elytra Boost", false);
    public SliderSetting setSpeed = new SliderSetting("Speed", 1.5F, 0.5F, 2.5F, 0.1F).setHidden(() -> elytra.get());
    public static double oldSpeed, contextFriction;
    public static boolean needSwap, prevSprint, needSprintState;
    public static int counter, noSlowTicks;
    private int ticks;
    private float waterTicks = 0;
    @EventTarget
    public void onUpdate(EventMotion e) {
//        if(mode.is("hard")) {
//            if (!mc.player.onGround && MovementUtil.getPlayerMotion() <= 0.22f) {
//                MovementUtil.setSpeed(mc.player.isSneaking() ? (float) MovementUtil.getPlayerMotion() : 0.22f);
//            }
//
//            if (!mc.player.onGround && mc.player.motionY == -0.4448259643949201) {
//                MovementUtil.setSpeed((float) MovementUtil.getPlayerMotion());
//            }
//        }
//        else if (mode.is("Boost")) {
//            mc.player.setSprinting(MovementUtil.isMoving());
//        }
        if (mode.is("Matrix")) {
            if (MovementUtils.isMoving()) {
                Helper.mc.player.setSprinting(true);
                if (MovementUtils.getSpeed() < 0.2177f) {
                    Helper.mc.player.jumpMovementFactor = 0.033f;
                    MovementUtils.strafe();
                    if (Math.abs(Helper.mc.player.movementInput.moveStrafe) < 0.1 && Helper.mc.gameSettings.keyBindForward.pressed) {
                        MovementUtils.strafe();
                    }
                    if (Helper.mc.player.onGround) {
                        MovementUtils.strafe();
                    }
                }
            }
        }
        if (mode.is("hard")) {
            if (mc.player.isInWater() || mc.player.isInLava()) {
                waterTicks = 10;
            } else {
                waterTicks--;
            }
            if (waterTicks > 0) return;
            if (MoveUtility.isMoving() && MoveUtility.getMotion() <= 0.289385188) {
                if (!e.isOnGround()) {
                    MoveUtility.setStrafe(MoveUtility.reason(false) || mc.player.isHandActive()
                            ? MoveUtility.getMotion() - 0.00001f : 0.245f - (new Random().nextFloat() * 0.000001f));
                }
            }
        }

//        int elytraSlot = ElytraFly.getHotbarSlotOfItem();


//        if (elytra.get() && elytraSlot != -1) {
//
//            if (MoveUtility.isMoving() && !mc.player.onGround && mc.player.fallDistance >= 0.15 && e.isOnGround()) {
//                MoveUtility.setMotion(setSpeed.get());
//                Strafe.oldSpeed = (setSpeed.get() / 1.06);
//            }
//        }

        if (mc.player.isInWater()) {
            waterTicks = 10;
        } else {
            waterTicks--;
        }
    }



    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
//        if (!isEnabled()) {
//            return;
//        }
        if (!elytra.get()) return;
//        int elytra = ElytraFly.getHotbarSlotOfItem();
//
//        if (mc.player.isInWater() || mc.player.isInLava() || waterTicks > 0 || elytra == -1 || mc.player.isInWeb)
//            return;
//        if (mc.player.fallDistance != 0 && mc.player.fallDistance < 0.1 && mc.player.motionY < -0.1) {
//            if (elytra != -2) {
//                mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
//                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
//            }
//
//            mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
//            mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
//
//            if (elytra != -2) {
//                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
//                mc.playerController.windowClick(0, elytra, 1, ClickType.PICKUP, mc.player);
//            }
//        }
        if (mode.is("Matrix")) {
            if (MovementUtils.isMoving()) {
                Helper.mc.player.setSprinting(true);
                if (MovementUtils.getSpeed() < 0.2177f) {
                    Helper.mc.player.jumpMovementFactor = 0.033f;
                    MovementUtils.strafe();
                    if (Math.abs(Helper.mc.player.movementInput.moveStrafe) < 0.1 && Helper.mc.gameSettings.keyBindForward.pressed) {
                        MovementUtils.strafe();
                    }
                    if (Helper.mc.player.onGround) {
                        MovementUtils.strafe();
                    }
                }
            }
        }

//         if (mode.is("Boost")) {
//            MovementUtils.strafe();
//            mc.player.setSprinting(MovementUtil.isMoving());
//        }
    }

    private void disabler() {

        int elytra = 10;

        if (Helper.mc.player.inventory.getItemStack().getItem() != Items.ELYTRA)
            Helper.mc.playerController.windowClick(0, elytra < 9 ? elytra + 36 : elytra, 1, ClickType.PICKUP, Helper.mc.player);

        Helper.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Helper.mc.player);
        Helper.mc.player.connection.sendPacket(new CPacketEntityAction(Helper.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        Helper.mc.player.connection.sendPacket(new CPacketEntityAction(Helper.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        Helper.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Helper.mc.player);
        Helper.mc.playerController.windowClick(0, elytra < 9 ? elytra + 36 : elytra, 1, ClickType.PICKUP, Helper.mc.player);

    }

    @EventTarget
    public void on(EventPacket event) {
//        if (strafeMode.currentMode.equals("Elytra")) {
//            if (Helper.mc.player.isOnLadder() || Helper.mc.player.isInLiquid() || InventoryUtil.getSlotWithElytra() == -1) {
//                return;
//
//            }
//        }
    }

}
