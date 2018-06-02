package it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.custom_classes.animation.ResizeAnimation;
import it.triviapatente.android.app.utils.custom_classes.animation.TranslateAnimation;
import it.triviapatente.android.app.utils.custom_classes.buttons.LoadingButton;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.utils.custom_classes.images.RoundedImageView;
import it.triviapatente.android.app.views.game_page.play_round.PlayRoundActivity;
import it.triviapatente.android.models.auth.User;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.game.Round;

/**
 * Created by Antonio on 24/12/16.
 */
public class QuizHolder implements View.OnClickListener{
    private ImageView quizImage;
    private boolean isImageBig = false;
    private AnimationSet toSmall, toBig;
    private Animation fadeIn, fadeOut;
    private int resizeDuration = 200, fadeDuration = 100, imageSmallSize, imageBigSize;
    private TextView quizDescription;
    private LoadingButton trueButton, falseButton;
    private View itemView;
    private TextView roundNameView;
    private TextView categoryNameView;
    private RoundedImageView categoryImageView;
    private TextView opponentNameView;
    private RoundedImageView opponentImageView;
    private Context context;
    private Quiz element;
    private Round round;
    private Category category;
    private User opponent;
    private LinearLayout quizDescriptionBox;
    private String baseUrl;

    public QuizHolder(PlayRoundActivity context, Quiz quizElement, Round round, Category category, User opponent) {
        itemView = LayoutInflater.from(context).inflate(R.layout.view_pager_element_quiz_holder, null, false);
        this.context = context;
        this.round = round;
        this.category = category;
        this.opponent = opponent;
        init();
        bind(quizElement);
    }

    private void init() {
        // base url
        baseUrl = context.getString(R.string.baseUrl);
        // binding elements
        quizImage = (ImageView) itemView.findViewById(R.id.quizImage);
        quizDescription = (TextView) itemView.findViewById(R.id.quizDescription);
        quizDescriptionBox = (LinearLayout) itemView.findViewById(R.id.quizDescriptionBox);
        trueButton = (LoadingButton) itemView.findViewById(R.id.trueButton);
        falseButton = (LoadingButton) itemView.findViewById(R.id.falseButton);
        roundNameView = itemView.findViewById(R.id.roundNameView);
        categoryNameView = itemView.findViewById(R.id.categoryNameView);
        categoryImageView = itemView.findViewById(R.id.categoryImageView);
        opponentNameView = itemView.findViewById(R.id.opponentNameView);
        opponentImageView = itemView.findViewById(R.id.opponentImageView);
        // image resize and translate animation
        setAnimations();
        // setting elements
        quizDescription.setMovementMethod(new ScrollingMovementMethod()); // to allow scroll
        //quizImage.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        roundNameView.setText("Round " + round.number);
        categoryNameView.setText(category.hint);
        opponentNameView.setText(opponent.toString());
        TPUtils.picasso.load(TPUtils.getCategoryImageFromID(context, category.id)).into(categoryImageView);
        TPUtils.injectUserImage(context, opponent, opponentImageView);
    }

    private void setAnimations() {
        setFadeInAnimation();
        setResizeAnimations();
    }

    private void setFadeInAnimation() {
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                quizDescription.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        fadeIn.setDuration(fadeDuration);
    }

    private void setFadeOutAnimation() {
        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                quizDescription.setVisibility(View.GONE);
                quizImage.startAnimation(toBig);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        fadeOut.setDuration(fadeDuration);
    }

    private void setResizeAnimations() {
        imageSmallSize = (int) context.getResources().getDimension(R.dimen.quiz_image_size_small);
        imageBigSize = (int) context.getResources().getDimension(R.dimen.quiz_image_size_big);
        quizDescriptionBox.post(new Runnable() {
            @Override
            public void run() {
                int dx = calculateXDelta();
                setToSmallAnimation(dx);
                setToBigAnimation(dx);
                setFadeOutAnimation();
            }
        });
    }

    private void setToSmallAnimation(int dx) {
        toSmall = new AnimationSet(context, null);
        toSmall.addAnimation(new ResizeAnimation(quizImage, imageBigSize, imageBigSize, imageSmallSize, imageSmallSize));
        toSmall.addAnimation(new TranslateAnimation(quizImage, dx, 0, 0, 0));
        toSmall.setDuration(resizeDuration);
        toSmall.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                quizDescription.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void setToBigAnimation(int dx) {
        toBig = new AnimationSet(context, null);
        toBig.addAnimation(new ResizeAnimation(quizImage, imageSmallSize, imageSmallSize, imageBigSize, imageBigSize));
        toBig.addAnimation(new TranslateAnimation(quizImage, 0, dx, 0, 0));
        toBig.setDuration(resizeDuration);
    }

    private int calculateXDelta() {
        return (quizDescriptionBox.getMeasuredWidth() - imageBigSize) / 2;
    }

    public void bind(Quiz element) {
        this.element = element;
        quizDescription.setText(element.question);
        if(element.image_id == null) {
            quizImage.setVisibility(View.GONE);
        } else {
            quizImage.setVisibility(View.VISIBLE);
            TPUtils.picasso
                    .load(TPUtils.getQuizImageFromID(context, element.image_id))
                    .error(R.drawable.image_no_image_found)
                    .into(quizImage);
        }
        // if already answered
        if(element.my_answer != null) {
            alreadyAnswered(element.my_answer);
        }
    }

    public void alreadyAnswered(boolean answer) {
        if(answer) {
            setButtonClicked(trueButton, false);
        } else {
            setButtonClicked(falseButton, false);
        }
        setButtonClickable(false);
    }

    @Override
    public void onClick(View v) {
        boolean answer;
        final LoadingButton clicked;
        switch (v.getId()) {
            case R.id.trueButton: { answer = true; clicked = trueButton; } break;
            case R.id.falseButton: { answer = false; clicked = falseButton; } break;
            case R.id.quizImage: {
                changeImageSize();
                return;
            }
            default:return;
        }
        ((PlayRoundActivity) context).sendAnswer(answer, element.id, new ValueCallback<Boolean>() {

            @Override
            public void onReceiveValue(Boolean success) {
                if(!success) setButtonUnClicked(clicked);
                clicked.stopLoading();
            }
        });
        setButtonClicked(clicked, true);
        // disable clicks
        setButtonClickable(false);
    }

    private void changeImageSize() {
        if(isImageBig) {
            quizImage.startAnimation(toSmall);
            isImageBig = false;
        } else {
            quizDescription.startAnimation(fadeOut);
            isImageBig = true;
        }
    }

    private void setButtonClicked(LoadingButton btn, Boolean loading) {
        btn.setBackground(ContextCompat.getDrawable(context, R.drawable.button_true_or_false_clicked));
        btn.setTextColor(Color.WHITE);
        if(loading) btn.startLoading();
    }

    private void setButtonUnClicked(LoadingButton btn) {
        btn.setBackground(ContextCompat.getDrawable(context, R.drawable.button_true_false));
        btn.setTextColor(ContextCompat.getColor(context, R.color.main_color_on_white));
    }

    private void setButtonClickable(boolean clickable) {
        trueButton.setClickable(clickable);
        falseButton.setClickable(clickable);
    }

    public View getItemView() {
        return itemView;
    }
}
