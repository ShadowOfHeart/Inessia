package gg.vape.event.events.impl;

import gg.vape.event.events.Event;
import gg.vape.event.events.callables.EventCancellable;
import net.minecraft.network.Packet;

public class EventPacket extends EventCancellable implements Event {

    private Packet packet;

    public EventPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }



}
