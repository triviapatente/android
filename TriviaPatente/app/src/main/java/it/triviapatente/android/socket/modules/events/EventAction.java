package it.triviapatente.android.socket.modules.events;

/**
 * Created by donadev on 15/11/17.
 */

public enum EventAction {
    create("create"),
    update("update"),
    destroy("destroy");

    private final String raw;

    EventAction(String raw) {
        this.raw = raw;
    }

    static EventAction from(String raw) {
        return EventAction.valueOf(raw);
    }
}
