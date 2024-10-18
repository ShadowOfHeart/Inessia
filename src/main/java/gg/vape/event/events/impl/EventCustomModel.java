package gg.vape.event.events.impl;

import gg.vape.event.events.Event;
import gg.vape.event.events.callables.EventCancellable;
import net.minecraft.client.entity.AbstractClientPlayer;

public class EventCustomModel extends EventCancellable implements Event {
    private AbstractClientPlayer player;

    public EventCustomModel(AbstractClientPlayer player) {
        this.player = player;
    }

    public void setPlayer(AbstractClientPlayer player) {
        this.player = player;
    }

    public AbstractClientPlayer getPlayer() {
        return this.player;
    }
}