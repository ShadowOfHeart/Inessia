package gg.vape.event.events.callables;

import gg.vape.event.events.Cancellable;
import gg.vape.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable {

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

}
