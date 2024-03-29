package it.triviapatente.android.socket.modules.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SocketCallback;
import it.triviapatente.android.app.utils.mApplication;
import it.triviapatente.android.firebase.FirebaseMessagingService;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.responses.Success;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Antonio on 31/10/16.
 */
public class BaseSocketManager {
    protected static Socket mSocket;
    public final static int timeout = 6000;
    private static Context sharedContext;
    public static boolean isConnected() {
        return mSocket.connected();
    }

    public static void disconnect()
    {
        joinedRooms.clear();
        mSocket.disconnect();
    }

    private static String TOKEN_KEY;
    private static String DEVICE_ID_KEY;

    private static IO.Options getIO() throws KeyManagementException, NoSuchAlgorithmException {
        IO.Options opts = new IO.Options();
        opts.secure = true;
        return opts;
    }
    public static void init(Context context) {
        try {
            sharedContext = context;
            HttpsURLConnection.setDefaultHostnameVerifier(RetrofitManager.hostnameVerifier);
            mSocket = IO.socket(sharedContext.getString(R.string.baseUrl), getIO());
            mSocket.io().timeout(timeout);
            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    joinedRooms = new HashMap<>();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        TOKEN_KEY = context.getString(R.string.shared_token_key);
        DEVICE_ID_KEY = context.getString(R.string.device_id_key);
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
    private JSONObject getTimeoutResponseJSON() {
        try {
            JSONObject output = new JSONObject();
            output.put("success", false);
            output.put("timeout", true);
            output.put("status_code", 0);
            return output;
        } catch (JSONException e) {
            return null;
        }
    }
    private <T extends Success> T getTimeoutResponse(Class<T> outputClass) {
        JSONObject object = getTimeoutResponseJSON();
        return RetrofitManager.gson.fromJson(object.toString(), outputClass);
    }
    private <T extends Success> void callOnTimeout(final SocketCallback<T> cb, final Class<T> outputClass) {
        T response = getTimeoutResponse(outputClass);
        cb.response(response);
    }

    public <T extends Success> void emit(final String path, final JSONObject parameters, final Class<T> outputClass, final SocketCallback<T> cb) {
        JSONObject body = generateBodyFrom(parameters);
        try {
            final Emitter e = mSocket.emit(path, body);
            final Handler handler = new Handler(Looper.getMainLooper());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (e.hasListeners(path)) {
                        e.off(path);
                        callOnTimeout(cb, outputClass);
                    }
                }
            }, timeout);
            e.once(path, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (args.length > 0) {
                        JSONObject data = (JSONObject) args[0];
                        T response = RetrofitManager.gson.fromJson(data.toString(), outputClass);
                        // check for 401 error
                        if (needsAuthentication(response)) {
                            mApplication.getInstance().goToLoginPage();
                        } else {
                            // propagate response
                            cb.response(response);
                        }
                    }
                }
            });
        } catch(Exception e) {
            callOnTimeout(cb, outputClass);
        }

    }
    //metodo che a partire da parametri sfusi ottiene il body della richiesta socket con il token
    public JSONObject generateBodyFrom(JSONObject params) {
        JSONObject output = new JSONObject();
        try {
            output.put("body", params);
            output.put(TOKEN_KEY, SharedTPPreferences.getToken());
            output.put(DEVICE_ID_KEY, SharedTPPreferences.deviceId());
        } catch(JSONException e) {
            Log.e("JSONException", e.getLocalizedMessage());
        }
        return output;
    }

    public <T extends Success> void listen(String path, final Class<T> outputClass, final SocketCallback<T> cb) {
        mSocket.off(path);
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

    public static Map<String, Long> joinedRooms = new HashMap<>();
    public void leave(final SocketCallback<Success> cb) {
        leave("game", cb);
    }
    public void leave() {
        leave(new SocketCallback<Success>() {
            @Override
            public void response(Success response) {
                Log.i("Room", "Left? " + response.success);
            }
        });
    }
    public void leave(final String type, final SocketCallback<Success> cb) {
        if(joinedRooms.containsKey(type)) {
            this.emit("leave_room", buildJSONObject(new Pair<String, Object>("type", type)), Success.class, new SocketCallback<Success>() {
                @Override
                public void response(Success response) {
                    joinedRooms.remove(type);
                    cb.response(response);
                }
            });
        } else {
            Success response = new Success(true, 200);
            cb.response(response);
        }
    }
    public void join(final Long id, final String type, final SocketCallback<Success> cb) {
        final SocketCallback<Success> handler = new SocketCallback<Success>() {
            @Override
            public void response(Success response) {
                FirebaseMessagingService.clearNotifications(sharedContext, id);
                cb.response(response);
            }
        };
        if(this.isJoined(id, type)) {
            Success response = new Success(true, 200);
            handler.response(response);
        } else {
            JSONObject params = buildJSONObject(new Pair<String, Object>("type", type), new Pair<String, Object>("id", id));
            this.emit("join_room", params, Success.class, new SocketCallback<Success>() {
                @Override
                public void response(Success response) {
                    if (response.success) {
                        joinedRooms.put(type, id);
                    }
                    handler.response(response);
                }
            });
        }
    }
    public Boolean isJoined(Long id, String type) {
        return joinedRooms.get(type) != null && joinedRooms.get(type).equals(id);
    }

    public void stopListen(String... events) {
        for(String event : events) {
            this.stopListen(event);
        }
    }

}
