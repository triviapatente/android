package com.ted_developers.triviapatente.app.views.game_page;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPGameActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPDetailsDialog;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPLeaveDialog;
import com.ted_developers.triviapatente.app.views.AlphaView;
import com.ted_developers.triviapatente.http.modules.game.HTTPGameEndpoint;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.auth.User;
import com.ted_developers.triviapatente.models.game.Question;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessDecrement;
import com.ted_developers.triviapatente.models.responses.SuccessRoundDetails;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class FragmentGameOptions extends Fragment {
    TPGameActivity activity;

    public FragmentGameOptions() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (TPGameActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_options, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /*@OnClick(R.id.gameChatButton)
    public void gameChatButtonClick() {
        Intent intent = new Intent(activity, AlphaView.class);
        *//*Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(getString(R.string.extra_string_opponent), RetrofitManager.gson.toJson(opponent));*//*
        startActivity(intent);
    }*/

    @OnClick(R.id.gameDetailsButton)
    public void gameDetailsButtonClick() {
        TPUtils.blurContainerIntoImageView(activity, activity.activityContainer, activity.blurredBackgroundView);
        activity.blurredBackgroundContainer.setVisibility(View.VISIBLE);
        //showing modal
        activity.gameSocketManager.round_details(activity.gameID, new SocketCallback<SuccessRoundDetails>() {
            @Override
            public void response(SuccessRoundDetails response) {
                if(response.success) {
                    final List<Question> answers = response.answers;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new TPDetailsDialog(
                                    activity,
                                    TPUtils.getUserScoreFromID(answers, activity.currentUser.id),
                                    TPUtils.getUserScoreFromID(answers, activity.opponent.id),
                                    activity.currentUser.id, activity.opponent.id, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    activity.blurredBackgroundContainer.setVisibility(View.GONE);
                                }
                            }).show();
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.gameLeaveButton)
    public void gameLeaveButtonClick() {
        TPUtils.blurContainerIntoImageView(activity, activity.activityContainer, activity.blurredBackgroundView);
        activity.blurredBackgroundContainer.setVisibility(View.VISIBLE);
        //showing modal
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
                                        activity.onBackPressed();
                                    }
                                }

                                @Override
                                public void mOnFailure(Call<Success> call, Throwable t) {}

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
            public void mOnFailure(Call<SuccessDecrement> call, Throwable t) {}

            @Override
            public void then() {}
        });
    }

}
