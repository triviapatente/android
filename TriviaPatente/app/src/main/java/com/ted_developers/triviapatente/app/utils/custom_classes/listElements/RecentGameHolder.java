package com.ted_developers.triviapatente.app.utils.custom_classes.listElements;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.buttons.PlayButton;
import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.models.game.Game;

/**
 * Created by Antonio on 12/11/16.
 */
public class RecentGameHolder extends TPHolder<Game> {
    // status strings
    private String playNowStatus, detailsStatus, newGameStatus;
    // status traffic lights
    private Drawable playNowTL, detailsTL, newGameTL;
    // status text view
    private TextView statusText;
    // player name
    private TextView usernameText;
    // traffic lights
    private ImageView trafficLights;
    // profile picture
    private RoundedImageView profilePicture;
    // play button
    private PlayButton playButton;

    public RecentGameHolder(View itemView) {
        super(itemView);
    }

    public RecentGameHolder(View itemView, Context context) {
        super(itemView);
        bind(context);
    }

    private void bind(Context context) {
        // status
        playNowStatus = context.getResources().getString(R.string.play_now_status);
        detailsStatus = context.getResources().getString(R.string.details_status);
        newGameStatus = context.getResources().getString(R.string.new_game_status);
        // traffic lights
        playNowTL = ContextCompat.getDrawable(context, R.drawable.traffic_lights_green);
        detailsTL = ContextCompat.getDrawable(context, R.drawable.traffic_lights_yellow);
        newGameTL = ContextCompat.getDrawable(context, R.drawable.traffic_lights_red);
        // other elements
        playButton = (PlayButton) itemView.findViewById(R.id.recentGameButton);
        profilePicture = (RoundedImageView) itemView.findViewById(R.id.profilePicture);
        trafficLights = (ImageView) itemView.findViewById(R.id.trafficLightsimage);
        usernameText = (TextView) itemView.findViewById(R.id.username);
        statusText = (TextView) itemView.findViewById(R.id.status);
    }

    @Override
    public void bind(Game element) {
        if(element.ended) {
            setNewGame();
        } else if(element.my_turn) {
            setPlayNow();
        } else {
            setDetails();
        }
        setUsernameText(element.opponent_name);
        // todo set opponent picture
    }

    private void setPlayNow() {
        playButton.setPlayNow();
        statusText.setText(playNowStatus);
        trafficLights.setImageDrawable(playNowTL);
    }

    private void setNewGame() {
        playButton.setNewGame();
        statusText.setText(newGameStatus);
        trafficLights.setImageDrawable(newGameTL);
    }

    private void setDetails() {
        playButton.setDetails();
        statusText.setText(detailsStatus);
        trafficLights.setImageDrawable(detailsTL);
    }

    private void setUsernameText(String text) {
        usernameText.setText(text);
    }

    private void setProfilePicture(Drawable image) {
        profilePicture.setImageDrawable(image);
    }
}
