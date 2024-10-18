package gg.vape.event.events;

public interface Cancellable {

    boolean isCancelled();

    void cancel();

}
