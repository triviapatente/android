package com.ted_developers.triviapatente.app.utils;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;

import butterknife.ButterKnife;

/**
 * Created by Antonio on 08/12/16.
 */
public class TPActivity extends AppCompatActivity {

    GameSocketManager gameSocketManager = new GameSocketManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(needsLeaveRoom()) {
            gameSocketManager.leave_room(new SocketCallback<Success>() {
                @Override
                public void response(Success response) {
                    if(response.success) {
                        // todo do init round
                        Log.i("TEST", "ROOM LEAVATA");
                    } else {
                        // todo vedere come avvisare
                        Log.i("TEST", "ERRORE NEL LEAVE ROOM!!");
                    }
                }
            });
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if(needsFullScreen()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    protected boolean needsLeaveRoom() {
        return true;
    }

    protected boolean needsFullScreen() {
        return true;
    }
}
