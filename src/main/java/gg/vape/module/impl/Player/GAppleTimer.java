package gg.vape.module.impl.Player;

import gg.vape.settings.options.SliderSetting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import org.lwjgl.input.Mouse;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.event.events.impl.GAppleEatEvent;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "GAppleTimer", type = Category.Player)
public class GAppleTimer extends Module {

    private final SliderSetting cooldown = new SliderSetting("Задержка", 55, 0, 100, 10).call(this);
    private boolean isEated;

    @Override
    public void onDisable() {
        isEated = false;
    }

    @EventTarget
    public void onEat(GAppleEatEvent event) {
        isEated = true;
        if( mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE)
                && mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
            event.cancel();
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (isEated) {
            mc.player.getCooldownTracker().setCooldown(Items.GOLDEN_APPLE, (int) cooldown.get());
            isEated = false;
        }
        if (mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE)
                && mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
            mc.gameSettings.keyBindUseItem.pressed = (false);
        } else if (Mouse.isButtonDown(1) && !(mc.currentScreen instanceof GuiContainer)) {
            mc.gameSettings.keyBindUseItem.pressed = (true);
        }
    }

}
