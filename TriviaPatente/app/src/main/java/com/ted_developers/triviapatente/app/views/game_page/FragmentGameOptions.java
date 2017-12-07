package com.ted_developers.triviapatente.app.views.game_page;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPGameActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.dialogs.TPLeaveDialog;
import com.ted_developers.triviapatente.app.views.game_page.round_details.RoundDetailsActivity;
import com.ted_developers.triviapatente.http.modules.game.HTTPGameEndpoint;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessDecrement;
import com.ted_developers.triviapatente.models.responses.SuccessRoundDetails;

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
        //showing modal
        Intent intent = new Intent(getContext(), RoundDetailsActivity.class);
        intent.putExtra(getString(R.string.extra_long_game), activity.gameID);
        intent.putExtra(getString(R.string.extra_string_opponent), new Gson().toJson(activity.opponent));
        intent.putExtra(getString(R.string.extra_string_from_game_options), true);
        startActivity(intent);
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
                                        gameDetailsButtonClick();
                                    }
                                }

                                @Override
                                public void mOnFailure(Call<Success> call, Throwable t) {
                                    Log.e("Failure", "failure on leave game request");
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
                Log.e("Failure", "failure on leave game request");
            }

            @Override
            public void then() {}
        });
    }

}
