package com.ted_developers.triviapatente.app.utils.custom_classes.listElements.normal;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ted_developers.triviapatente.app.utils.custom_classes.images.RoundedImageView;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.views.expandable_list.TPExpandableList;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Game;

/**
 * Created by Antonio on 28/11/16.
 */
public class PlayerRankHolder extends TPHolder<User> {
    // player data
    RoundedImageView profilePicture;
    TextView usernameTextField;
    // position field
    // todo finish implementing


    public PlayerRankHolder(View itemView) {
        super(itemView);
    }

    public PlayerRankHolder(View itemView, Context context) {
        super(itemView);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    public void bind(User element) {

    }
}
