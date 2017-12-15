package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.buttons.PlayButton;
import it.triviapatente.android.app.utils.custom_classes.images.RoundedImageView;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.TPHolder;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Game;

/**
 * Created by Antonio on 12/11/16.
 */
public class RecentGameHolder extends TPHolder<Game> {
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
    // context
    Context context;

    public RecentGameHolder(View itemView) {
        super(itemView);
        bind(itemView.getContext());
    }

    private void bind(Context context) {
        this.context = context;
        // other elements
        playButton = (PlayButton) itemView.findViewById(R.id.recentGameButton);
        profilePicture = (RoundedImageView) itemView.findViewById(R.id.profilePicture);
        trafficLights = (ImageView) itemView.findViewById(R.id.trafficLightsimage);
        usernameText = (TextView) itemView.findViewById(R.id.username);
        usernameText.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
        statusText = (TextView) itemView.findViewById(R.id.status);
    }

    @Override
    public void bind(Game element) {
        User opponent = new User(element.opponent_id, element.opponent_username, element.opponent_name, element.opponent_surname, element.opponent_image);
        playButton.goToGame(element.id, opponent);
        if(element.ended) {
            setSummary();
        } else {
            if(!element.my_turn) {
                setWait();
            } else {
                setPlayNow();
            }
        }
        if(element.opponent_name != null && element.opponent_surname != null) {
            setUsernameText(element.opponent_name + " " + element.opponent_surname);
        } else { setUsernameText(element.opponent_username); }
        TPUtils.injectUserImage(context, opponent, profilePicture);
    }

    private void setPlayNow() {
        playButton.setPlayNow();
        statusText.setText(context.getString(R.string.play_now_status));
        trafficLights.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_traffic_lights_green));
    }

    private void setSummary() {
        playButton.setSummary();
        statusText.setText(context.getString(R.string.new_game_status));
        trafficLights.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_traffic_lights_red));
    }

    private void setWait() {
        playButton.setWait();
        statusText.setText(context.getString(R.string.details_status));
        trafficLights.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_traffic_lights_yellow));
    }

    private void setUsernameText(String text) {
        usernameText.setText(text);
    }
}
