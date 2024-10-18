package gg.vape.event.events.impl;

import gg.vape.event.events.Event;
import net.minecraft.network.play.server.SPacketPlayerListItem;

public class EventPlayer implements Event {

    private final SPacketPlayerListItem.AddPlayerData addPlayerData;
    private final SPacketPlayerListItem.Action action;

    public EventPlayer(SPacketPlayerListItem.AddPlayerData addPlayerData, SPacketPlayerListItem.Action action) {
        this.addPlayerData = addPlayerData;
        this.action = action;
    }

    public SPacketPlayerListItem.AddPlayerData getPlayerData() {
        return this.addPlayerData;
    }

    public SPacketPlayerListItem.Action getAction() {
        return this.action;
    }

}
