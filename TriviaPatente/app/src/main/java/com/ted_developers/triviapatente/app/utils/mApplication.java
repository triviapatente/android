package com.ted_developers.triviapatente.app.utils;

import android.app.Application;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.views.first_access.FirstAccessActivity;
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
        // app
        app = this;
        // fonts
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/OpenSans-Regular.ttf");
        // init
        RetrofitManager.init(this);
        BaseSocketManager.init(this);
        SharedTPPreferences.init(this);
        baseSocketManager.listen(getResources().getString(R.string.socket_event_invite_created), InviteUser.class, inviteCreatedCallback);
    }

    public static boolean isPointInsideView(int x, int y, View view) {
        Rect area = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

        //point is inside view bounds
        return area.contains(x, y);
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

    private static mApplication app;
    public static mApplication getInstance() {
        return app;
    }

    public void goToLoginPage() {
        Intent intent = new Intent(this, FirstAccessActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(getResources().getString(R.string.is_old_session), true);
        startActivity(intent);
    }
}
