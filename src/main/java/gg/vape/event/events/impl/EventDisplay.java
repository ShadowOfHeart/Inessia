package gg.vape.event.events.impl;

import gg.vape.event.events.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventDisplay implements Event {
    public float ticks;
    public ScaledResolution sr;

    public EventDisplay(float t, ScaledResolution sr) {
        this.sr = sr;
        ticks = t;
    }
}
