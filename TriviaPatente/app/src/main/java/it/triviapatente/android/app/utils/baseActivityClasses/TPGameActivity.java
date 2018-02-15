package it.triviapatente.android.app.utils.baseActivityClasses;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import it.triviapatente.android.R;

import it.triviapatente.android.app.utils.Foreground;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SocketCallback;
import it.triviapatente.android.app.views.game_page.ChooseCategoryActivity;
import it.triviapatente.android.app.views.game_page.FragmentGameHeader;
import it.triviapatente.android.app.views.game_page.GameMainPageActivity;
import it.triviapatente.android.app.views.game_page.play_round.PlayRoundActivity;
import it.triviapatente.android.app.views.game_page.round_details.RoundDetailsActivity;
import it.triviapatente.android.app.views.main_page.MainPageActivity;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Round;
import it.triviapatente.android.models.responses.Success;
import it.triviapatente.android.socket.modules.base.BaseSocketManager;
import it.triviapatente.android.socket.modules.events.GameLeftEvent;

/**
 * Created by Antonio on 04/01/17.
 */
public abstract class TPGameActivity extends TPActivity {
    // game data
    public User opponent;
    public Round currentRound;
    public Category currentCategory;
    public Long gameID;
    // game header
    protected FragmentGameHeader gameHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        opponent = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_opponent)), User.class);
        currentRound = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_round)), Round.class);
        currentCategory = RetrofitManager.gson.fromJson(intent.getStringExtra(this.getString(R.string.extra_string_category)), Category.class);
        gameID = intent.getLongExtra(this.getString(R.string.extra_long_game), (currentRound == null)? -1 : currentRound.game_id);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        defaultReinit(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!redirecting) {
            BaseSocketManager.disconnect();
        }
    }

    @Override
    protected void awakenFromBackground() {
        super.awakenFromBackground();
        BaseSocketManager.connect(new SimpleCallback() {
            @Override
            public void execute() {
                defaultReinit(true);
            }
        }, new SimpleCallback() {
            @Override
            public void execute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
            }
        });
    }

    @Override
    public void startActivityFromFragment(android.support.v4.app.Fragment fragment, Intent intent, int requestCode) {
        startActivityFromFragment(fragment, intent, requestCode, null);
    }

    protected void gotoChooseCategory() {
        Intent intent = new Intent(this, ChooseCategoryActivity.class);
        intent.putExtra(this.getString(R.string.extra_string_round), RetrofitManager.gson.toJson(currentRound));
        intent.putExtra(this.getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));
        startActivity(intent);
        finish();
    }

    protected void gotoPlayRound() {
        Intent intent = new Intent(this, PlayRoundActivity.class);
        intent.putExtra(this.getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));
        intent.putExtra(this.getString(R.string.extra_string_round), RetrofitManager.gson.toJson(currentRound));
        intent.putExtra(this.getString(R.string.extra_string_category), RetrofitManager.gson.toJson(currentCategory));
        startActivity(intent);
        finish();
    }

    protected void gotoRoundDetails() {
        Intent intent = new Intent(this, RoundDetailsActivity.class);
        intent.putExtra(this.getString(R.string.extra_long_game), gameID);
        intent.putExtra(getString(R.string.extra_string_opponent), new Gson().toJson(opponent));
        startActivity(intent);
    }


    private void listenUserLeft() {
        gameSocketManager.listenGameLeft(new SocketCallback<GameLeftEvent>() {
            @Override
            public void response(GameLeftEvent response) {
                Intent i = new Intent(TPGameActivity.this, RoundDetailsActivity.class);
                if(response.annulled) {
                    i.putExtra(getString(R.string.extra_string_opponent_annulled), true);
                } else {
                    i.putExtra(getString(R.string.extra_string_opponent_has_left), true);
                }
                i.putExtra(getString(R.string.extra_long_game), gameID);
                i.putExtra(getString(R.string.extra_string_opponent), new Gson().toJson(opponent));
                startActivity(i);
            }
        });
    }
    protected abstract void customReinit();
    protected void defaultReinit(final Boolean withCustom) {
        if(gameID == -1) return;
        gameSocketManager.join(gameID, new SocketCallback<Success>() {
            @Override
            public void response(Success response) {
                if(!(TPGameActivity.this instanceof RoundDetailsActivity)) listenUserLeft();
                if(withCustom) customReinit();
            }
        });

    }

    private void unlistenUserLeft() {
        if(this instanceof RoundDetailsActivity) return;
        gameSocketManager.stopListen(getString(R.string.socket_event_user_left));
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        unlistenUserLeft();
    }

    @Override
    public void startActivityFromFragment(@NonNull Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityFromFragment(fragment, intent, requestCode, options);
        unlistenUserLeft();
    }
    @Override
    public void startActivityFromFragment(@NonNull Fragment fragment, Intent intent, int requestCode) {
        startActivityFromFragment(fragment, intent, requestCode, null);
    }

    @Override
    public void startActivityFromFragment(android.support.v4.app.Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityFromFragment(fragment, intent, requestCode, options);
        unlistenUserLeft();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        // init fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        gameHeader = (FragmentGameHeader) fragmentManager.findFragmentById(R.id.gameHeader);
        if(needsSetHeader()) gameHeader.setHeader(currentRound, currentCategory);
    }

    protected boolean needsSetHeader() { return true; }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
    }

    @Override
    protected boolean needsLeaveRoom() {
        return false;
    }
}
