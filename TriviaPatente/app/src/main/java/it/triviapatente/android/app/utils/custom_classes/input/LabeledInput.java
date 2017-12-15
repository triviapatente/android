package it.triviapatente.android.app.utils.custom_classes.input;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.triviapatente.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 25/10/16.
 */
public class LabeledInput extends LinearLayout {
    // TODO add custom attribute from xml management and make as library on github
    private TextView label;
    private EditText input;
    private String hint;

    public LabeledInput(Context context) {
        super(context);
        init(context);
    }

    public LabeledInput(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }
    public LabeledInput(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.setOrientation(LinearLayout.VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(context);
        // input from template
        input = (EditText) inflater.inflate(R.layout.edittext_template, null);
        input.setHeight((int) getResources().getDimension(R.dimen.field_height));
        // label from template
        label = (TextView) inflater.inflate(R.layout.errorlabel_template, null);
        this.addView(label);
        this.addView(input);
        input.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hint == null && input.getHint() != null) hint = input.getHint().toString();
                if(hint != null) input.setHint(input.hasFocus()? "" : hint);
            }
        });
        hideLabel();
    }

    // to set if the edittext is for password input
    public void setPassword(boolean is) {
        if(!is) {
            input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    // set edittext hint
    public void setHint(String hint) {
        input.setHint(hint);
    }

    // hide label of labeled input
    public void hideLabel() {
        if(label.getVisibility() == VISIBLE) {
            label.setVisibility(GONE);
            input.setBackgroundResource(R.drawable.edittext_rounded);
        }
    }

    // show label of labeled input with given text shown
    public void showLabel(String text) {
        if(label.getVisibility() == GONE) {
            label.setVisibility(VISIBLE);
            input.setBackgroundResource(R.drawable.labeledinput_input);
        }
        label.setText(text);
    }

    // show label of labeled input with given text shown, label and border of choosen color
    public void showLabel(String text, @ColorInt int labelColor) {
        label.setTextColor(labelColor);
        showLabel(text);
    }

    // get edittext editable
    public Editable getText() {
        return input.getText();
    }
    // set text programmatically
    public void setText(String text) {
        input.setText(text);
    }

    // get edittext text
    public String toString() {
        return input.getText().toString();
    }

    // auto - correction manager

    private LabeledInput toWatch = null;
    private boolean isValid = true;

    // to force check
    public void check() { check(Color.WHITE); }
    public void check(@ColorInt int labelColor) {
        hideLabel();
        isValid = false;
        for(LabeledInputError e : errorsToCheck) {
            if (!e.isValid(input)) {
                showLabel(e.toString(), labelColor);
                return;
            }
        }
        isValid = true;
    }

    // text watcher
    private boolean watcherActive = false;
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            check();
        }
    },
                otherWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            check();
        }
    };

    // queue of possible errors
    private List<LabeledInputError> errorsToCheck = new ArrayList<>();

    public void setAutoCheck(boolean active) {
        if(active && !watcherActive) {
            watcherActive = true;
            input.addTextChangedListener(watcher);
            if(toWatch != null)
                toWatch.input.addTextChangedListener(otherWatcher); // make sure on update to check also this
        } else if(watcherActive) {
            watcherActive = false;
            input.removeTextChangedListener(watcher);
            if(toWatch != null) toWatch.input.addTextChangedListener(otherWatcher);
        }
    }

    public boolean isValid() { return  isValid; }

    public boolean hasAutoCheck() {
        return watcherActive;
    }

    // public void setErrorsToCheck(LabeledInputError ... errors) { setErrorsToCheck(errors); }
    public void setErrorsToCheck(LabeledInput toWatch, LabeledInputError ... errors) {
        for(LabeledInputError e: errors) {
            if(toWatch != null && e == LabeledInputError.PASSWORD_EQUALS) {
                e.setOtherInput(toWatch.input);
                this.toWatch = toWatch;
            }
            errorsToCheck.add(e);
        }
    }

}
