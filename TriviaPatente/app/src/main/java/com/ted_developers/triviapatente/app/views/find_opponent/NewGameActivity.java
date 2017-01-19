package com.ted_developers.triviapatente.app.views.find_opponent;

import android.content.Intent;
import android.os.Bundle;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;

import butterknife.OnClick;

public class NewGameActivity extends TPActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
    }

    @OnClick(R.id.findOpponent)
    public void findOpponent() {
        Intent intent = new Intent(this, FindOpponentActivity.class);
        intent.putExtra("random", false);
        this.startActivity(intent);
    }

    @OnClick(R.id.findRandomOpponent)
    public void findRandomOpponent() {
        Intent intent = new Intent(this, FindOpponentActivity.class);
        intent.putExtra("random", true);
        this.startActivity(intent);
    }
}
