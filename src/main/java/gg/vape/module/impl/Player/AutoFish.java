package gg.vape.module.impl.Player;


import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventPacket;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

@ModuleInfo(name = "AutoFish", type = Category.Player)
public class AutoFish extends Module {


    @EventTarget
    public void onReceivePacket(EventPacket event) {
        SPacketSoundEffect packet;
        if (event.getPacket() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.getPacket()).getCategory() == SoundCategory.NEUTRAL && packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH && (AutoFish.mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod || AutoFish.mc.player.getHeldItemOffhand().getItem() instanceof ItemFishingRod)) {
            AutoFish.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
            AutoFish.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
}

