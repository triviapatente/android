package it.triviapatente.android.app.views.game_page;

import android.content.Intent;
import android.os.Bundle;

import it.triviapatente.android.R;

import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.User;

// TODO remove
@Deprecated
public class ChatActivity extends TPActivity {

    private User opponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_opponent)), User.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
