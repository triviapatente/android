package com.ted_developers.triviapatente.socket.modules.auth;

import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antonio on 31/10/16.
 */
public class AuthSocketManager extends BaseSocketManager {
    public void authenticate(SocketCallback<Hints> cb) {
        String token = SharedTPPreferences.getToken();
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("token", token);
        } catch (JSONException e) {}
        emit("auth", parameters, Hints.class, cb);
    }

    public void logout(SocketCallback<Success> cb) {
        emit("logout", null, Success.class, cb);
    }
}
