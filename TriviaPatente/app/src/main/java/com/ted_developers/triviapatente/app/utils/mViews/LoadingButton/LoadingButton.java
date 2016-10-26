package com.ted_developers.triviapatente.app.utils.mViews.LoadingButton;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 23/10/16.
 */
public class LoadingButton extends Button implements ManageLoading {
    private String prevText = "";
    private ProgressBar progressBar;

    public LoadingButton(Context context) {
        super(context);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        // create progress bar
        progressBar = new ProgressBar(this.getContext());
        // set relative layout params
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.loading_size), (int) getResources().getDimension(R.dimen.loading_size));
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        // add params
        progressBar.setLayoutParams(params);
        // set customized progress
        progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.progress));
        // add progress bar
        ((RelativeLayout) this.getParent()).addView(progressBar);
    }

    // start spinning
    @Override
    public void startLoading() {
        prevText = this.getText().toString();
        this.setText("");
        if(progressBar == null) {
            init();
        }
        else if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    // stop spinning
    @Override
    public void stopLoading() {
        if(progressBar != null) {
            progressBar.setVisibility(View.GONE);
            this.setText(prevText);
        }
    }

}
