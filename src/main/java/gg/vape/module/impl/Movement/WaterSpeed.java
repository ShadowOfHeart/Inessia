package gg.vape.module.impl.Movement;

import gg.vape.Medusa;
import gg.vape.editor.Drag;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.helpers.Helper;
import gg.vape.helpers.MoveUtility;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import gg.vape.settings.options.BooleanSetting;
import gg.vape.settings.options.SliderSetting;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.block.BlockAir;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;


import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "WaterSpeed", type = Category.Movement)
public class WaterSpeed extends Module {

    public BooleanSetting speedCheck = new BooleanSetting("Проверка на бафф", false).call(this);
    public BooleanSetting smart = new BooleanSetting("Смарт старый", false).call(this);
    public BooleanSetting smartnew = new BooleanSetting("Смарт новый", false).call(this);
    public SliderSetting speed = new SliderSetting("Скорость", 0.4f, 0.1f, 1f, 0.01f).call(this);
    public Drag dWat = Medusa.createDrag(this, "WaterSpeed", 20, 30);
    public BooleanSetting miniJump = new BooleanSetting("Мини прыжок", true).call(this);
    public static float tick = 0;
    public static int smTick = 100;


    @EventTarget
    public void onRender(EventDisplay event){
//        if(smartnew.get()) {
//            int xx = (int) dWat.getX();
//            int yy = (int) dWat.getY();
//            dWat.setWidth(110);
//            dWat.setHeight(20);
//
//            RenderUtil.drawBlurredShadow(xx, yy, 110, 20, 15, new Color(20, 20, 20, 255));
//
//            RoundedUtil.drawRound(xx, yy, 110, 20, 2, new Color(0, 0, 0, 255));
//
//            Fonts.BOLD18.drawStringWithShadow("WaterSpeed", xx + 5, yy + 1.5f, new Color(255, 255, 255, 255).getRGB());
//
//            RenderUtil.drawBlurredShadow(xx + 5, yy + 12, smTick, 5, 10, new Color(255, 0, 0, 255));
//            RoundedUtil.drawRound(xx + 5, yy + 12, smTick, 5, 1, new Color(255, 0, 0, 255));
//            Fonts.BOLD16.drawCenteredString(smTick + "%", xx + 55, yy + 12, new Color(255, 255, 255, 255).getRGB());
//        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(smartnew.get()) {

            if ((smTick <= 100 && smTick >= 1) && mc.player.isInWater()) {
                if (Helper.timerHelper.hasReached(10)) {
                    smTick-=4;
                    Helper.timerHelper.reset();
                }
            }
            if(!mc.player.isInWater() && smTick <= 99){
                if (Helper.timerHelper.hasReached(10)) {
                    smTick+=2;
                    Helper.timerHelper.reset();
                }
            }

            if(smTick > 100){
                smTick--;
            }

            if(smTick<=0){
                toggle();
            }

        }
        if(smartnew.get()) {
            if (smTick <= 0) {
                return;
//                toggle();
            }
        }
        if (!smart.get()) {
            if (mc.player.collidedHorizontally || !mc.player.isInWater()
                    || speedCheck.get() && !mc.player.isPotionActive(MobEffects.SPEED)) {

                return;
            }
            MoveUtility.setMotion(speed.get());
        } else {
            List<ItemStack> stacks = new ArrayList<>();
            mc.player.getArmorInventoryList().forEach(stacks::add);
            stacks.removeIf(w -> w.getItem() instanceof ItemAir);
            float motion = MoveUtility.getMotion();
            boolean hasEnchantments = false;
            for (ItemStack stack : stacks) {

                int enchantmentLevel = 0;

                if (buildEnchantments(stack, 1)) {
                    enchantmentLevel = 1;
                }

                if (enchantmentLevel > 0) {
                    motion = 0.5f;
                    hasEnchantments = true;
                }
            }

            if (mc.player.collidedHorizontally) {
                tick = 0;
                return;
            }
            if (!mc.player.isInWater()) return;
            if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isSneaking()
                    && !(mc.world.getBlockState(mc.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir)) {
                mc.player.motionY = 0.12f;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -0.35f;
            }
            if (speedCheck.get() && !mc.player.isPotionActive(MobEffects.SPEED)) {
                tick = 0;
                return;
            }


            if (miniJump.get() && hasEnchantments && mc.world.getBlockState(mc.player.getPosition().add(0, 1,
                    0)).getBlock() instanceof BlockAir && mc.gameSettings.keyBindJump.isKeyDown()) {
                tick++;
                mc.player.motionY = .12f;
            }
            if (hasEnchantments) {
                tick++;
                MoveUtility.setMotion(0.4f);
                Strafe.oldSpeed = 0.4f;
            }

        }

    };

    @Override
    public void onDisable() {
        tick = 0;
        if(mc.player != null)
            if(!mc.player.isInWater())
                smTick=80;
        super.onDisable();
    }

    public boolean buildEnchantments(ItemStack stack, float strenght) {
        if (stack != null) {
            if (stack.getItem() instanceof ItemArmor) {
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack) > 0;
            }
        } else {
            return false;
        }

        return false;
    }
}
