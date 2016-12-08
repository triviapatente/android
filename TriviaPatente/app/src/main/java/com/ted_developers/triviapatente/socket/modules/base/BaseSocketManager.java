package com.ted_developers.triviapatente.socket.modules.base;

import android.content.Context;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.Success;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 31/10/16.
 */
public class BaseSocketManager {
    protected static Socket mSocket;
    public final static int timeout = 6000;
    private static Long id = null;
    private static String type = null;

    public static void init(Context context) {
        try {
            mSocket = IO.socket(context.getString(R.string.baseUrl));
            mSocket.io().timeout(timeout);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void connect(final SimpleCallback onConnectCallback, final SimpleCallback onTimeoutCallback) {
        if(mSocket.connected()) {
            onConnectCallback.execute();
        }
        else {
            mSocket.on("connect", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    mSocket.off("connect");
                    mSocket.off(Socket.EVENT_CONNECT_ERROR);
                    onConnectCallback.execute();
                }
            });
            mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    mSocket.off(Socket.EVENT_CONNECT_ERROR);
                    mSocket.off("connect");
                    onTimeoutCallback.execute();
                }
            });
            mSocket.connect();
        }

    }

    public void join_room(Long id, String type, final SocketCallback<Success> onJoinRoomCallback) {
        if(this.id == null && this.type == null) {
            try {
                JSONObject room = new JSONObject("{id: " + id + ", type: " + type + "}");
                emit("join_room", room, Success.class, onJoinRoomCallback);
                this.id = id;
                this.type = type;
            } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    public void leave_room(final SocketCallback<Success> onLeaveRoomCallback) {
        if(id != null && type != null) {
            try {
                JSONObject room = new JSONObject("{id: " + id + ", type: " + type + "}");
                emit("leave_room", room, Success.class, onLeaveRoomCallback);
                id = null;
                type = null;
            } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    public <T extends Success> void emit(final String path, JSONObject parameters, Class<T> outputClass, final SocketCallback<T> cb) {
        listen(path, outputClass, new SocketCallback<T>() {
            @Override
            public void response(T response) {
                // unregister from event
                mSocket.off(path);
                // propagate response
                cb.response(response);
            }
        });
        mSocket.emit(path, parameters);
    }

    public <T extends Success> void listen(String path, final Class<T> output, final SocketCallback<T> cb) {
        mSocket.on(path, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(args.length > 0) {
                    JSONObject data = (JSONObject) args[0];
                    T response = RetrofitManager.gson.fromJson(data.toString(), output);
                    cb.response(response);
                }
            }
        });
    }

}
