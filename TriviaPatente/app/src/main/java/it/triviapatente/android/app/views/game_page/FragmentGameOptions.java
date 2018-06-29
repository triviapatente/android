package it.triviapatente.android.app.views.game_page;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindInt;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPActivity;
import it.triviapatente.android.app.utils.baseActivityClasses.TPGameActivity;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SimpleCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.TPCallback;
import it.triviapatente.android.app.utils.custom_classes.dialogs.TPLeaveDialog;
import it.triviapatente.android.app.views.game_page.round_details.RoundDetailsActivity;
import it.triviapatente.android.http.modules.game.HTTPGameEndpoint;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.game.Game;
import it.triviapatente.android.models.game.Round;
import it.triviapatente.android.models.responses.Success;
import it.triviapatente.android.models.responses.SuccessDecrement;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentGameOptions extends Fragment {
    TPActivity activity;

    @BindView(R.id.gameDetailsButton) Button gameDetailsButton;
    @BindView(R.id.gameLeaveButton) Button gameLeaveButton;
    @BindView(R.id.gameBellButton) Button gameBellButton;
    @BindView(R.id.gameBellTextView) TextView gameBellTextView;

    @BindInt(R.integer.defaultBellInfoDelay) int defaultBellInfoDelay;
    private Game game;
    private Long maxAge;
    private Boolean ticklingEnabled = false;
    public FragmentGameOptions() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (TPActivity) context;
        this.ticklingEnabled = false;
    }
    private void applyEnabled() {
        gameDetailsButton.setEnabled(isEnabled);
        gameLeaveButton.setEnabled(isEnabled);
        gameBellButton.setEnabled(isEnabled);
    }
    public void enable() {
        isEnabled = true;
        applyEnabled();
    }
    public void disable() {
        isEnabled = false;
        applyEnabled();
    }
    private Boolean isGameActivity() {
        return (activity instanceof TPGameActivity);
    }
    private TPGameActivity getGameActivity() {
        if(isGameActivity()) return (TPGameActivity)activity;
        return null;
    }
    private Boolean isEnabled = true;

    private Boolean isFirstRound() {
        if(isGameActivity()) return getGameActivity().currentRound != null && getGameActivity().currentRound.number == 1;
        return null;
    }
    private int getGameDetailsButtonVisibility() {
        return isGameActivity() && !isFirstRound() ? View.VISIBLE : View.GONE;
    }
    private int getGameBellButtonVisibility() {
        return isGameActivity() && ticklingEnabled && !isFirstRound() ? View.VISIBLE : View.GONE;
    }
    public void setRound(Round round) {
        if(isGameActivity()) {
            getGameActivity().currentRound = round;
        }
        updateButtonVisibility();
    }
    private void updateButtonVisibility() {
        gameDetailsButton.setVisibility(getGameDetailsButtonVisibility());
        gameBellButton.setVisibility(getGameBellButtonVisibility());
        gameBellTextView.setVisibility(getGameBellButtonVisibility());
    }
    public void enableTickling(Game game, Long maxAge) {
        this.game = game;
        this.ticklingEnabled = true;
        this.maxAge = maxAge;
        setDefaultBellInfoText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_options, container, false);
        ButterKnife.bind(this, view);
        enable();
        if(isGameActivity()) {
            setDefaultBellInfoText();
        } else {
            updateButtonVisibility();
        }
        return view;
    }

    // TODO remove
    /*@OnClick(R.id.gameChatButton)
    public void gameChatButtonClick() {
        Intent intent = new Intent(activity, AlphaView.class);
        *//*Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));*//*
        startActivity(intent);
    }*/

    @OnClick(R.id.gameDetailsButton)
    public void gameDetailsButtonClick() {
        gotoRoundDetails(false);
    }
    public void gotoRoundDetails(Boolean mainOnBackPressed) {
        if(isGameActivity()) {
            //showing modal
            Intent intent = new Intent(getContext(), RoundDetailsActivity.class);
            intent.putExtra(getString(R.string.extra_long_game), getGameActivity().gameID);
            intent.putExtra(getString(R.string.extra_string_opponent), new Gson().toJson(getGameActivity().opponent));
            intent.putExtra(getString(R.string.extra_string_from_game_options), !mainOnBackPressed);
            startActivity(intent);
        } else {

        }
    }
    private SimpleCallback leaveGameCallback;
    public void enableTrainMode(SimpleCallback leaveGameCallback) {
        this.leaveGameCallback = leaveGameCallback;
    }

    @OnClick(R.id.gameLeaveButton)
    public void gameLeaveButtonClick() {
        if(isGameActivity()) {
            TPUtils.blurContainerIntoImageView(activity, activity.activityContainer, activity.blurredBackgroundView);
            activity.blurredBackgroundContainer.setVisibility(View.VISIBLE);
            //showing modal
            showGameLeaveModal();
        } else {
            leaveGameCallback.execute();
        }
    }

    private void onTickleError() {
        Snackbar.make(getView(), R.string.httpConnectionError, Snackbar.LENGTH_LONG).setAction(R.string.httpConnectionErrorRetryButton, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameBellButtonClick();
            }
        }).show();
    }
    private void setDefaultBellInfoText() {
        if(ticklingEnabled && game != null) gameBellTextView.setText(game.getExpirationDescription(getContext(), maxAge));
    }
    private void setBellInfoText(int textId) {
        gameBellTextView.setText(textId);
        gameBellTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setDefaultBellInfoText();
            }
        }, defaultBellInfoDelay);
    }
    @OnClick(R.id.gameBellButton)
    public void gameBellButtonClick() {
        if(!isGameActivity()) return;
        ObjectAnimator
                .ofFloat(gameBellButton, "rotation", 0, 25, -25, 25, -25, 0)
                .setDuration(800)
                .start();

        if(getGameActivity().currentRound.alreadyTickled) setBellInfoText(R.string.game_user_notification_user_already_notified);
        else {
            RetrofitManager.getHTTPGameEndpoint().tickleGame(getGameActivity().currentRound.id).enqueue(new Callback<Success>() {
                @Override
                public void onResponse(Call<Success> call, Response<Success> response) {
                    if (response.body().success) {
                        setBellInfoText(R.string.game_user_notification_user_notified);
                    } else {
                        onTickleError();
                    }
                }

                @Override
                public void onFailure(Call<Success> call, Throwable t) {
                    onTickleError();
                }
            });
        }
    }

    private void showGameLeaveModal() {
        if(!isGameActivity()) return;
        final HTTPGameEndpoint httpGameEndpoint = RetrofitManager.getHTTPGameEndpoint();
        httpGameEndpoint.getLeaveDecrement(getGameActivity().gameID).enqueue(new TPCallback<SuccessDecrement>() {
            @Override
            public void mOnResponse(Call<SuccessDecrement> call, Response<SuccessDecrement> response) {
                if(response.body().success) {
                    new TPLeaveDialog(activity, response.body().decrement, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            activity.blurredBackgroundContainer.setVisibility(View.GONE);
                        }
                    }) {
                        @Override
                        public void onNegativeButtonClick() {
                            httpGameEndpoint.leaveGame(getGameActivity().gameID).enqueue(new TPCallback<Success>() {
                                @Override
                                public void mOnResponse(Call<Success> call, Response<Success> response) {
                                    if(response.body().success) {
                                        gotoRoundDetails(true);
                                    } else {
                                        mOnFailure(null, null);
                                    }
                                }

                                @Override
                                public void mOnFailure(Call<Success> call, Throwable t) {
                                    Toast.makeText(
                                            getContext(),
                                            ((TPActivity) getActivity()).httpConnectionError + " " + ((TPActivity) getActivity()).httpConnectionErrorRetryButton,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                                @Override
                                public void then() {}
                            });
                        }

                        @Override
                        public void onPositiveButtonClick() {
                            cancel();
                        }
                    }.show();
                }
            }

            @Override
            public void mOnFailure(Call<SuccessDecrement> call, Throwable t) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), ((TPActivity) getActivity()).httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                        .setAction(((TPActivity) getActivity()).httpConnectionErrorRetryButton, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activity.blurredBackgroundContainer.setVisibility(View.VISIBLE);
                                showGameLeaveModal();
                            }
                        })
                        .show();
                activity.blurredBackgroundContainer.setVisibility(View.GONE);
            }

            @Override
            public void then() {}
        });
    }

    public void fromLeaveToComplete() {
        gameLeaveButton.setBackgroundResource(R.drawable.button_game_complete);
    }

}
