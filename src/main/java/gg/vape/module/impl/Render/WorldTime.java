package gg.vape.module.impl.Render;

import gg.vape.settings.options.SliderSetting;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventPacket;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

@ModuleInfo(name = "WorldTime", type = Category.Render)
public class WorldTime extends Module {

    public SliderSetting time = new SliderSetting("Время", 1000, 0, 24000, 100).call(this);

    @EventTarget
    public void onUpdate(EventUpdate e) {
//        setSuffix(String.valueOf(time.get()));
        mc.world.setWorldTime((long) time.get());
    }

    @EventTarget
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof SPacketTimeUpdate) {
            e.cancel();;
        }
    }

}
