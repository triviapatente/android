package it.triviapatente.android.app.utils.custom_classes;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.models.stats.Progress;

/**
 * Created by donadev on 28/06/18.
 */

public class DialogBottomSheet {
    public enum ButtonType {
        BLUE, TRANSPARENT;
    }
    private View v;

    @BindView(R.id.dialogBottomSheetImageView) ImageView imageView;
    @BindView(R.id.dialogBottomSheetTitle) TextView titleView;
    @BindView(R.id.dialogBottomSheetMessage) TextView messageView;
    @BindView(R.id.dialogBottomSheetAction1) Button action1;
    @BindView(R.id.dialogBottomSheetAction2) Button action2;
    @BindView(R.id.dialogBottomSheetLayout) RelativeLayout sheetLayout;
    @BindView(R.id.dialogBottomSheetInternalLayout) LinearLayout internalSheetLayout;
    @BindView(R.id.dialogBottomSheetProgress) ProgressBar progressView;
    @BindView(R.id.backgroundOpacityLayer) View opacityLayer;
    private String title, message;
    private Integer image;
    private String firstAction, secondAction;
    private SimpleCallback dismissListener;
    private ButtonType type1 = ButtonType.BLUE;
    private ButtonType type2 = ButtonType.BLUE;

    @OnClick(R.id.dialogBottomSheetAction1)
    void firstClick() {
        if(firstActionCallback != null) firstActionCallback.execute();
    }
    @OnClick(R.id.dialogBottomSheetAction2)
    void secondClick() {
        if(secondActionCallback != null) secondActionCallback.execute();
    }
    private SimpleCallback firstActionCallback;
    private SimpleCallback secondActionCallback;

    private Context getContext() {
        return v.getContext();
    }

    public void setContent(int title, int message) {
        this.title = getContext().getString(title);
        this.message = getContext().getString(message);
    }
    public void setContent(String title, String message) {
        this.title = title;
        this.message = message;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public void setFirstAction(int text, ButtonType type, SimpleCallback cb) {
        firstActionCallback = cb;
        firstAction = getContext().getString(text);
        type1 = type;
    }
    public void setSecondAction(int text, ButtonType type, SimpleCallback cb) {
        secondActionCallback = cb;
        secondAction = getContext().getString(text);
        type2 = type;
    }
    public void setOnDismissListener(SimpleCallback dismissListener) {
        this.dismissListener = dismissListener;
    }
    private void refreshGUI() {
        imageView.setVisibility(image == null ? View.GONE : View.VISIBLE);
        if(image != null) imageView.setImageResource(image);
        titleView.setText(title);
        messageView.setText(message);
        refreshAction(action1, type1, firstAction);
        refreshAction(action2, type2, secondAction);

    }
    private BottomSheetBehavior getBehavior() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) sheetLayout.getLayoutParams();
        return (BottomSheetBehavior) params.getBehavior();
    }
    private void refreshAction(Button action, ButtonType type, String content) {
        action.setVisibility(content == null ? View.GONE : View.VISIBLE);
        action.setText(content);
        if(content != null) {
            initButtonGUI(action, type);
        }
    }
    private int getTextColor(ButtonType type) {
        if(type == ButtonType.BLUE) return android.R.color.white;
        return R.color.mainColor;
    }
    private Integer getBackgroundDrawable(ButtonType type) {
        if(type == ButtonType.BLUE) return R.drawable.button_true_or_false_clicked;
        return null;
    }
    private void initButtonGUI(Button action, ButtonType type) {
        action.setTextColor(ContextCompat.getColor(getContext(), getTextColor(type)));
        Integer drawable = getBackgroundDrawable(type);
        if(drawable == null) action.setBackground(null);
        else action.setBackgroundResource(drawable);
    }
    public void show() {
        refreshGUI();
        setBackground(true);
        getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void setBackground(Boolean expanded) {
        opacityLayer.animate().setDuration(300).alpha(expanded ? 1 : 0).start();
    }
    public void dismiss() {
        getBehavior().setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public DialogBottomSheet(View v) {
        this.v = v;
        ButterKnife.bind(this, v);
        getBehavior().setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                if(state == BottomSheetBehavior.STATE_HIDDEN || state == BottomSheetBehavior.STATE_COLLAPSED) {
                    setBackground(false);
                    if(dismissListener != null) dismissListener.execute();
                }
                else if(state == BottomSheetBehavior.STATE_EXPANDED) setBackground(true);
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }
    public void startLoading() {
        progressView.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), R.color.mainColor),
                android.graphics.PorterDuff.Mode.SRC_IN);
        progressView.setVisibility(View.VISIBLE);
        internalSheetLayout.setVisibility(View.INVISIBLE);
    }
    public void stopLoading() {
        progressView.setVisibility(View.INVISIBLE);
        internalSheetLayout.setVisibility(View.VISIBLE);
    }

}
