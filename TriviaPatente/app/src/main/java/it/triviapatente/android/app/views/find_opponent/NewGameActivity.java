package it.triviapatente.android.app.views.find_opponent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.triviapatente.android.R;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;

import butterknife.BindString;
import butterknife.OnClick;

public class NewGameActivity extends TPActivity {
    @BindString(R.string.new_game_title) String title;

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

    @Override
    protected String getToolbarTitle(){ return title; }
    @Override
    protected int getBackButtonVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }
}
