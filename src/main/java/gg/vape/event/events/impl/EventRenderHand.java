package gg.vape.event.events.impl;

import gg.vape.event.events.Event;
import net.minecraft.util.EnumHandSide;

public class EventRenderHand implements Event {

    public EnumHandSide e;

    public EventRenderHand(EnumHandSide e) {
        this.e = e;
    }


}
