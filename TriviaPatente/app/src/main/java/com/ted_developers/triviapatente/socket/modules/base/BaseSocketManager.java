package com.ted_developers.triviapatente.socket.modules.base;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.SharedTPPreferences;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.mApplication;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.socket.modules.auth.AuthSocketManager;

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

    public static boolean isConnected() {
        return mSocket.connected();
    }

    public static void disconnect() {
        mSocket.disconnect();
    }

    private static String TOKEN_KEY;

    public static void init(Context context) {
        try {
            mSocket = IO.socket(context.getString(R.string.baseUrl));
            mSocket.io().timeout(timeout);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        TOKEN_KEY = context.getString(R.string.shared_token_key);
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
            JSONObject room = createRoom(id, type);
            emit("join_room", room, Success.class, onJoinRoomCallback);
            this.id = id;
            this.type = type;
        }
    }

    public void leave_room(final SocketCallback<Success> onLeaveRoomCallback) {
        if(id != null && type != null) {
            JSONObject room = createRoom(id, type);
            emit("leave_room", room, Success.class, onLeaveRoomCallback);
            id = null;
            type = null;
        }
    }

    private JSONObject createRoom(Long id, String type) {
        try{
            JSONObject room = new JSONObject();
            room.put("id", id);
            room.put("type", type);
            return room;
        } catch (JSONException e) {
            return null;
        }
    }

    public <T extends Success> void emit(final String path, final JSONObject parameters, final Class<T> outputClass, final SocketCallback<T> cb) {
        listen(path, outputClass, new SocketCallback<T>() {
            @Override
            public void response(T response) {
                // unregister from event
                mSocket.off(path);
                // check for 401 error
                if(needsAuthentication(response)) {
                    mApplication.getInstance().goToLoginPage();
                } else {
                    // propagate response
                    cb.response(response);
                }
            }
        });
        JSONObject body = generateBodyFrom(parameters);
        mSocket.emit(path, body);
    }
    //metodo che a partire da parametri sfusi ottiene il body della richiesta socket con il token
    public JSONObject generateBodyFrom(JSONObject params) {
        JSONObject output = new JSONObject();
        try {
            output.put("body", params);
            output.put(TOKEN_KEY, SharedTPPreferences.getToken());
        } catch(JSONException e) {
            Log.e("JSONException", e.getLocalizedMessage());
        }
        return output;
    }

    public <T extends Success> void listen(String path, final Class<T> outputClass, final SocketCallback<T> cb) {
        mSocket.on(path, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(args.length > 0) {
                    JSONObject data = (JSONObject) args[0];
                    T response = RetrofitManager.gson.fromJson(data.toString(), outputClass);
                    cb.response(response);
                }
            }
        });
    }

    private <T extends Success> boolean needsAuthentication(T response) {
        return response.status_code != null && response.status_code == 401;
    }

    public void stopListen(String path) {
        mSocket.off(path);
    }

    protected JSONObject buildJSONObject(Pair<String, Object>... parameters) {
        try {
            JSONObject data = new JSONObject();
            for(Pair<String, Object> dataPair : parameters) {
                data.put(dataPair.first, dataPair.second);
            }
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
