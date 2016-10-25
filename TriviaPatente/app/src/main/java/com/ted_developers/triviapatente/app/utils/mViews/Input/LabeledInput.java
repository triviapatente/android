package com.ted_developers.triviapatente.app.utils.mViews.Input;

import android.content.Context;
import android.graphics.Color;
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

    public void setPassword(boolean is) {
        if(!is) {
            input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public void setHint(String hint) {
        input.setHint(hint);
    }

    public void hideLabel() {
        label.setVisibility(GONE);
        input.setBackgroundResource(R.drawable.edittext_rounded);
    }

    public void showLabel(String text) {
        label.setVisibility(VISIBLE);
        label.setText(text);
        input.setBackgroundResource(R.drawable.labeledinput_input);
    }

    public void showLabel(String text, int labelColor) {
        // TODO find workaround to change color
        showLabel(text);
    }

    public Editable getText() {
        return input.getText();
    }
}
