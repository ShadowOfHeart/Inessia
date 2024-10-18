package gg.vape.module.impl.Movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "GuiWalk", type = Category.Movement)
public class GuiWalk extends Module {
    @EventTarget
    public void update(EventUpdate e) {
        KeyBinding[] keys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump,
                mc.gameSettings.keyBindSprint};

        if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditSign)
            return;

        for (KeyBinding keyBinding : keys) {
            keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
    }
}
