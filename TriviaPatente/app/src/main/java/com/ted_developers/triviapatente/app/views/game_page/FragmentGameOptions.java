package com.ted_developers.triviapatente.app.views.game_page;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPActivity;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPGameActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPLeaveDialog;
import com.ted_developers.triviapatente.app.views.game_page.round_details.RoundDetailsActivity;
import com.ted_developers.triviapatente.app.views.menu_activities.ContactsActivity;
import com.ted_developers.triviapatente.app.views.rank.RankActivity;
import com.ted_developers.triviapatente.http.modules.game.HTTPGameEndpoint;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.game.Round;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessDecrement;
import com.ted_developers.triviapatente.models.responses.SuccessRoundDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class FragmentGameOptions extends Fragment {
    TPGameActivity activity;

    @BindView(R.id.gameDetailsButton) Button gameDetailsButton;

    public FragmentGameOptions() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (TPGameActivity) context;
    }

    private Boolean isFirstRound() {
        return activity.currentRound != null && activity.currentRound.number == 1;
    }
    private int getGameDetailsButtonVisibility() {
        return isFirstRound() ? View.GONE : View.VISIBLE;
    }
    public void setRound(Round round) {
        activity.currentRound = round;
        gameDetailsButton.setVisibility(getGameDetailsButtonVisibility());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_options, container, false);
        ButterKnife.bind(this, view);
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
        //showing modal
        Intent intent = new Intent(getContext(), RoundDetailsActivity.class);
        intent.putExtra(getString(R.string.extra_long_game), activity.gameID);
        intent.putExtra(getString(R.string.extra_string_opponent), new Gson().toJson(activity.opponent));
        intent.putExtra(getString(R.string.extra_string_from_game_options), !mainOnBackPressed);
        startActivity(intent);
    }

    @OnClick(R.id.gameLeaveButton)
    public void gameLeaveButtonClick() {
        TPUtils.blurContainerIntoImageView(activity, activity.activityContainer, activity.blurredBackgroundView);
        activity.blurredBackgroundContainer.setVisibility(View.VISIBLE);
        //showing modal
        showGameLeaveModal();
    }

    private void showGameLeaveModal() {
        final HTTPGameEndpoint httpGameEndpoint = RetrofitManager.getHTTPGameEndpoint();
        httpGameEndpoint.getLeaveDecrement(activity.gameID).enqueue(new TPCallback<SuccessDecrement>() {
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
                            httpGameEndpoint.leaveGame(activity.gameID).enqueue(new TPCallback<Success>() {
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

}
