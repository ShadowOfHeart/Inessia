package gg.vape.module.impl.Player;

import gg.vape.settings.options.ListSetting;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "NoDelay", type = Category.Player)
public class NoDelay extends Module {
    public ListSetting delay = new ListSetting("Нет задержки", "Jump", "LeftClick", "Block", "RightClick").call(this);


    @EventTarget
    public void onMotion(EventUpdate e) {
        if (delay.selected.contains("Jump")) {
            mc.player.jumpTicks = 0;
        }
        if (delay.selected.contains("LeftClick")) {
            mc.leftClickCounter = 0;
        }
        if (delay.selected.contains("Block")) {
            mc.playerController.blockHitDelay = 0;
        }
        if (delay.selected.contains("RightClick")) {
            mc.rightClickDelayTimer = 0;
        }
    }
}