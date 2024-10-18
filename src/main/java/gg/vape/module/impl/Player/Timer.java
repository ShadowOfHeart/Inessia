package gg.vape.module.impl.Player;

import gg.vape.helpers.MovementUtil;
import gg.vape.settings.options.BooleanSetting;
import gg.vape.settings.options.ColorSetting;
import gg.vape.settings.options.SliderSetting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.event.events.impl.EventMotion;
import gg.vape.event.events.impl.EventPacket;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;


@ModuleInfo(name = "Timer", type = Category.Player)
public class Timer extends Module {

    public float ticks = 0;
    public boolean active;

    public float animWidth;


    public SliderSetting timer = new SliderSetting("Скорость", 1, 0.1f, 15, 0.1f).call(this);
    public BooleanSetting smart = new BooleanSetting("Smart", false);



    @EventTarget
    public void onSend(EventPacket eventSendPacket) {
        if (eventSendPacket.getPacket() instanceof CPacketPlayer.Position || eventSendPacket.getPacket() instanceof CPacketPlayer.PositionRotation) {
            if (ticks <= 25 && !active) {
                if (MovementUtil.isMoving()) {
                    ticks+=0.6;
                }
            }
        }
    }
    @EventTarget
    public void onPreUpdate(EventMotion e) {
        if (smart.get()) {
            if (!active)  mc.timer.timerSpeed = timer.get(); else  mc.timer.timerSpeed = 1;
        }
        else {
            mc.timer.timerSpeed = timer.get();
        }
        ticks = MathHelper.clamp(ticks, 0, 100);
    }
    @EventTarget
    public void onRender(EventDisplay e) {

    }
    public void onDisable() {
        super.onDisable();
        active = true;
        this.mc.timer.timerSpeed = 1.0f;
    }
}
