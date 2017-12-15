package it.triviapatente.android.app.views;

import android.os.Bundle;
import android.widget.TextView;

import com.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;

// TODO remove
@Deprecated
public class AlphaView extends TPActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha_view);
        ((TextView)findViewById(R.id.alphaTextView)).setText(TPUtils.translateEmoticons("Questa funzionalità sarà presto disponibile, continua a giocare! 0x1F60A"));
    }
}
