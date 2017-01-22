package com.ted_developers.triviapatente.app.views.game_page;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;

public class ChatActivity extends TPActivity {

    private User opponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_opponent)), User.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        if(opponent != null) { actionBar.setTitle(opponent.toString()); }
    }

    @Override
    protected Drawable getActionBarProfilePicture() {
        // todo return opponent image
        return ContextCompat.getDrawable(this, R.drawable.image_no_profile_picture);
    }
}