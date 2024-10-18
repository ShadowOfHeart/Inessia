package gg.vape.event.events.impl;

import gg.vape.event.events.Event;

public class ChatEvent implements Event {
    public String message;
    public ChatEvent(String message) {
        this.message = message;
    }
}
