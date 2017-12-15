package it.triviapatente.android.socket.modules.auth;

import it.triviapatente.android.app.utils.custom_classes.callbacks.SocketCallback;
import it.triviapatente.android.models.auth.Hints;
import it.triviapatente.android.models.responses.Success;
import it.triviapatente.android.socket.modules.base.BaseSocketManager;

import org.json.JSONObject;

/**
 * Created by Antonio on 31/10/16.
 */
public class AuthSocketManager extends BaseSocketManager {
    public void global_infos(SocketCallback<Hints> cb) {
        emit("global_infos", new JSONObject(), Hints.class, cb);
    }

    public void logout(SocketCallback<Success> cb) {
        emit("logout", null, Success.class, cb);
    }
}
