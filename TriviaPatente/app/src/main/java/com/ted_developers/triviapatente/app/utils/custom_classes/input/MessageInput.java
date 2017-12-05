package com.ted_developers.triviapatente.app.utils.custom_classes.input;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 06/01/17.
 */
// TODO remove
@Deprecated
public class MessageInput extends RelativeLayout implements View.OnClickListener {
    Button sendButton;
    ProgressBar loadingSendingMessageProgressBar;
    EditText inputMessageText;
    Drawable enabledSendingDrawable, disabledSendingDrawable;

    public MessageInput(Context context) {
        super(context);
        init(context);
    }

    public MessageInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MessageInput(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_message_input, this);
        // send button
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);
        sendButton.setEnabled(false);
        // send button drawables
        enabledSendingDrawable = ContextCompat.getDrawable(context, R.drawable.chat_send_button_enabled);
        disabledSendingDrawable = ContextCompat.getDrawable(context, R.drawable.chat_image_send_button);
        // loading
        loadingSendingMessageProgressBar = (ProgressBar) findViewById(R.id.loadingSendingMessage);
        // input text
        inputMessageText = (EditText) findViewById(R.id.messageInputText);
        inputMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (inputMessageText.getText().toString().trim().equals("")) {
                    sendButton.setBackground(disabledSendingDrawable);
                    sendButton.setEnabled(false);
                } else if (!sendButton.isEnabled()) {
                    sendButton.setBackground(enabledSendingDrawable);
                    sendButton.setEnabled(true);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton: {
                sendButton.setVisibility(INVISIBLE);
                loadingSendingMessageProgressBar.setVisibility(VISIBLE);
                // TODO send message and then stop loading
                // TODO trim message
            } break;
        }
    }
}
