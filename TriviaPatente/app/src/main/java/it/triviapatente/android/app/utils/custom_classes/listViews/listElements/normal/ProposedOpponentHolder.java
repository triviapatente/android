package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.buttons.PlayButton;
import it.triviapatente.android.app.utils.custom_classes.images.RoundedImageView;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.TPHolder;
import it.triviapatente.android.models.auth.User;

/**
 * Created by Antonio on 28/11/16.
 */
public class ProposedOpponentHolder extends TPHolder<User> {
    // player data
    RoundedImageView profilePicture;
    TextView usernameTextField, scoreTextField;
    // play button
    PlayButton playButton;
    // context
    Context context;

    ImageView imageView;

    public ProposedOpponentHolder(View itemView) {
        super(itemView);
        init(itemView.getContext());
    }

    private void init(final Context context) {
        this.context = context;
        profilePicture = (RoundedImageView) itemView.findViewById(R.id.profilePicture);
        usernameTextField = (TextView) itemView.findViewById(R.id.username);
        scoreTextField = (TextView) itemView.findViewById(R.id.score);
        playButton = (PlayButton) itemView.findViewById(R.id.playButton);
        imageView = (ImageView) itemView.findViewById(R.id.blurredView);
    }

    @Override
    public void bind(User element) {
        // set image
        TPUtils.injectUserImage(context, element, profilePicture);
        // set username or, if possible, name and surname
        usernameTextField.setText(element.toString());
        // set score
        scoreTextField.setText(String.valueOf(element.score));
        // set appropriate button
        if(element.last_game_won == null || element.last_game_won) {
            playButton.setPlayNow();
        } else {
            playButton.setNewGame(false);
        }
        // send invite on click
        playButton.sendInvite(element);
    }
}
