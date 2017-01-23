package com.ted_developers.triviapatente.models;

/**
 * Created by Antonio on 22/01/17.
 */
public enum EventActions {
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

    EventActions(String action) {
        this.action = action;
    }

    public String toString() {
        return action;
    }
}
