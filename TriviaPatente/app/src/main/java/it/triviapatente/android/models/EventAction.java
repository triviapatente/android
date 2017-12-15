package it.triviapatente.android.models;

/**
 * Created by Antonio on 22/01/17.
 */
public enum EventAction {
    create("create"),
    update("update"),
    destroy("destroy"),
    joined("joined"),
    left("left"),
    invited("invited"),
    answer("answer"),
    game_left("game_left"),
    message("message");

    String action;

    EventAction(String action) {
        this.action = action;
    }

    public static EventAction fromString(String actionString) {
        for(EventAction action : EventAction.values()) {
            if(action.toString().equals(actionString)) {
                return action;
            }
        }
        return null;
    }
    public String toString() {
        return action;
    }
}
