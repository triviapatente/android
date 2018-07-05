package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Objects;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
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
    //progress bar
    private SeekBar progressBar;
    //private opponentNameView
    private TextView opponentNameView;
    //private opponentScoreView
    private TextView opponentScoreView;
    //private opponentNameView
    private TextView myNameView;
    //private opponentScoreView
    private TextView myScoreView;
    // context
    Context context;

    private Integer numberOfQuestions;

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
        progressBar = (SeekBar) itemView.findViewById(R.id.progress_bar);
        myScoreView = (TextView) itemView.findViewById(R.id.my_score_view);
        opponentScoreView = (TextView) itemView.findViewById(R.id.opponent_score_view);
        myNameView = (TextView) itemView.findViewById(R.id.my_name_view);
        opponentNameView = (TextView) itemView.findViewById(R.id.opponent_name_view);
        numberOfQuestions = context.getResources().getInteger(R.integer.number_of_questions_per_round) * context.getResources().getInteger(R.integer.number_of_rounds);

    }
    @Override
    public void bind(Game element) {
        User opponent = new User(element.opponent_id, element.opponent_username, element.opponent_name, element.opponent_surname, element.opponent_image);
        playButton.goToGame(element, opponent);
        myScoreView.setText(element.myScore + "");
        opponentScoreView.setText(element.opponentScore + "");
        progressBar.setMax(numberOfQuestions);
        progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        progressBar.setProgress(numberOfQuestions - element.remainingAnswersCount);
        myNameView.setText(R.string.me);
        opponentNameView.setText(opponent.getDisplayFirstName());
        if(element.ended) {
            setSummary(element);
            if(element.winner_id != null) {
                if(element.winner_id.equals(SharedTPPreferences.currentUser().id)) {
                    String winnerText = TPUtils.translateEmoticons(context.getString(R.string.me_winner));
                    myNameView.setText(winnerText);
                    opponentNameView.setText(opponent.getDisplayFirstName());
                } else if(element.winner_id.equals(element.opponent_id)) {
                    myNameView.setText(R.string.me);
                    String winnerText = TPUtils.translateEmoticons(context.getString(R.string.opponent_winner));
                    opponentNameView.setText(winnerText.replace("%s", opponent.getDisplayFirstName()));
                }
            }
        } else {
            if(!element.my_turn) {
                setWait(element);
            } else {
                setPlayNow(element);
            }
        }
        if(element.opponent_name != null && element.opponent_surname != null) {
            setUsernameText(element.opponent_name + " " + element.opponent_surname);
        } else { setUsernameText(element.opponent_username); }
        TPUtils.injectUserImage(context, opponent, profilePicture, false);
    }
    private String getRemainingAnswerText(Game g) {
        int templateId = g.remainingAnswersCount == 1 ? R.string.remaining_answer : R.string.remaining_answers;
        return context.getString(templateId).replace("%d", "" + g.remainingAnswersCount);

    }
    private void setPlayNow(Game g) {
        playButton.setPlayNow();
        statusText.setText(getRemainingAnswerText(g));
        trafficLights.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_traffic_lights_green));
    }

    private void setSummary(Game g) {
        playButton.setSummary();
        String text = null;
        if(!g.started) {
            text = context.getString(R.string.game_annulled_status);
        } else if(g.winner_id != null) {
            if(g.winner_id.equals(SharedTPPreferences.currentUser().id))
                text = context.getString(R.string.you_won_status);
            else
                text = context.getString(R.string.opponent_won_status);

        } else {
            text = context.getString(R.string.tie_status);
        }

        statusText.setText(text);
        trafficLights.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_traffic_lights_red));
    }

    private void setWait(Game g) {
        playButton.setWait();
        statusText.setText(context.getString(R.string.remaining_answers).replace("%d", "" + g.remainingAnswersCount));
        trafficLights.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_traffic_lights_yellow));
    }

    private void setUsernameText(String text) {
        usernameText.setText(text);
    }
}
