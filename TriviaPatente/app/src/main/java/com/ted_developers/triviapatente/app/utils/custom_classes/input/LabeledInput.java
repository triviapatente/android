package com.ted_developers.triviapatente.app.utils.custom_classes.input;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 25/10/16.
 */
public class LabeledInput extends LinearLayout {
    private TextView label;
    private EditText input;

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
        input = (EditText) inflater.inflate(R.layout.tp_edittext_template, null);
        input.setHeight((int) getResources().getDimension(R.dimen.field_height));
        // label from template
        label = (TextView) inflater.inflate(R.layout.tp_errorlabel_template, null);
        this.addView(label);
        this.addView(input);
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
    public void showLabel(String text, int labelColor) {
        label.setTextColor(labelColor);
        showLabel(text);
    }

    // get edittext editable
    public Editable getText() {
        return input.getText();
    }

    // get edittext text
    public String toString() {
        return input.getText().toString();
    }
}
