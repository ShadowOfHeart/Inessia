package gg.vape.event.events.impl;

import gg.vape.event.events.Event;

public class MouseEvent implements Event {

    public int button;

    public MouseEvent(int button) {
        this.button = button;
    }

}
