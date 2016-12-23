package com.ted_developers.triviapatente.app.utils;

import android.app.Application;
import android.util.Log;
import android.view.View;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.game.Invite;
import com.ted_developers.triviapatente.models.responses.InviteUser;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Antonio on 22/10/16.
 */
public class mApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // fonts
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/OpenSans-Regular.ttf");
        // init
        RetrofitManager.init(this);
        BaseSocketManager.init(this);
        SharedTPPreferences.init(this);
        baseSocketManager.listen(getResources().getString(R.string.socket_event_invite_created), InviteUser.class, inviteCreatedCallback);
    }

    public static boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        //point is inside view bounds
        if(( x > viewX && x < (viewX + view.getWidth())) &&
                ( y > viewY && y < (viewY + view.getHeight()))){
            return true;
        } else {
            return false;
        }
    }


    protected BaseSocketManager baseSocketManager = new BaseSocketManager();
    SocketCallback<InviteUser> inviteCreatedCallback = new SocketCallback<InviteUser>() {
        @Override
        public void response(InviteUser response) {
            response.invite.sender_name = response.user.name;
            response.invite.sender_surname = response.user.surname;
            response.invite.sender_username = response.user.username;
            response.invite.sender_image = response.user.image;
            final Invite invite = response.invite;
            ReceivedData.addInvite(invite);
        }
    };
}
