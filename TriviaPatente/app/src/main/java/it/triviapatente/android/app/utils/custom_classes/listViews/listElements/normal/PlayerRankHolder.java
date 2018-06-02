package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.images.RoundedImageView;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.TPHolder;
import it.triviapatente.android.models.auth.User;

/**
 * Created by Antonio on 28/11/16.
 */
public class PlayerRankHolder extends TPHolder<User> {

    // player data
    RoundedImageView profilePicture;
    TextView usernameTextField, positionTextField, scoreTextField;
    // context
    Context context;

    public PlayerRankHolder(View itemView) {
        super(itemView);
        init(itemView.getContext());
    }

    private void init(final Context context) {
        this.context = context;
        profilePicture = (RoundedImageView) itemView.findViewById(R.id.profilePicture);
        usernameTextField = (TextView) itemView.findViewById(R.id.username);
        positionTextField = (TextView) itemView.findViewById(R.id.position);
        scoreTextField = (TextView) itemView.findViewById(R.id.score);
    }

    @Override
    public void bind(User element) {
        if(element.id == SharedTPPreferences.currentUser().id) {
            itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.mainColor));
            positionTextField.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            scoreTextField.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            usernameTextField.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
            positionTextField.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
            scoreTextField.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
            usernameTextField.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }
        // set image
        TPUtils.injectUserImage(context, element, profilePicture);
        // set username or, if possible, name and surname
        usernameTextField.setText(element.toString());
        // set position
        if(element.position == 1) positionTextField.setText(TPUtils.translateEmoticons(context.getString(R.string.first_position)));
        else if(element.position == 2) positionTextField.setText(TPUtils.translateEmoticons(context.getString(R.string.second_position)));
        else if(element.position == 3) positionTextField.setText(TPUtils.translateEmoticons(context.getString(R.string.third_position)));
        else positionTextField.setText(String.valueOf(element.position));
        // set score
        scoreTextField.setText(String.valueOf(element.score).concat(" km percorsi"));
    }
}
