package com.ted_developers.triviapatente.socket.modules.base;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.Success;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Antonio on 31/10/16.
 */
public class BaseSocketManager {
    protected static Socket mSocket;

    public static void init(Context context) {
        try {
            mSocket = IO.socket(context.getString(R.string.baseUrl));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void connect(final SimpleCallback cb) {
        if(mSocket.connected()) {
            cb.execute();
        }
        else {
            mSocket.on("connect", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    mSocket.off("connect");
                    cb.execute();
                }
            });
            mSocket.connect();
        }

    }

    public static void join_room() {
        // TODO implement
    }

    public static void leave_room() {
        // TODO implement
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
                    Log.i("TEST", data.toString());
                    T response = RetrofitManager.gson.fromJson(data.toString(), output);
                    cb.response(response);
                }
            }
        });
    }

}
