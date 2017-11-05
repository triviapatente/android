package com.ted_developers.triviapatente.app.views;

import android.os.Bundle;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;

@Deprecated
public class AlphaView extends TPActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha_view);
        ((TextView)findViewById(R.id.alphaTextView)).setText(TPUtils.translateEmoticons("Questa funzionalità sarà presto disponibile, continua a giocare! 0x1F60A"));
    }
}
