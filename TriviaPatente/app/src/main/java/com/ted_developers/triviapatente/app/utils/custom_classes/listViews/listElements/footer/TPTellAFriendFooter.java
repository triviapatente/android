package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer;

import android.content.Intent;
import android.view.View;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.views.AlphaView;

/**
 * Created by Antonio on 30/12/16.
 */
public class TPTellAFriendFooter extends TPFooter {
    public TPTellAFriendFooter(final View itemView) {
        super(itemView);
        itemView.findViewById(R.id.tellAFriendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, itemView.getContext().getString(R.string.invite_friends_string) + " " + itemView.getContext().getString(R.string.download_link));
                sendIntent.setType("text/plain");
                itemView.getContext().startActivity(sendIntent);
            }
        });
    }
}
