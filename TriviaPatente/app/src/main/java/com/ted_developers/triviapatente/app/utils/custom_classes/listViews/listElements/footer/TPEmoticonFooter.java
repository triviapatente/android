package com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.footer;

import android.view.View;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;

/**
 * Created by Antonio on 30/12/16.
 */
public class TPEmoticonFooter extends TPFooter {
    public TPEmoticonFooter(View itemView) {
        super(itemView);
        TextView emoticonTextView = (TextView) itemView.findViewById(R.id.emoticonTextView);
        emoticonTextView.setText(TPUtils.translateEmoticons(emoticonTextView.getText().toString()));
    }
}
