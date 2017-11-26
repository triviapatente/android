package com.ted_developers.triviapatente.app.utils.custom_classes.callbacks;

import com.ted_developers.triviapatente.socket.modules.events.GameLeftEvent;

/**
 * Created by Antonio on 31/10/16.
 */
public interface SocketCallback<T> {
    public void response(T response);
}
