package com.ted_developers.triviapatente.http.modules.game;

import com.ted_developers.triviapatente.models.responses.SuccessBadge;
import com.ted_developers.triviapatente.models.responses.SuccessGameUser;
import com.ted_developers.triviapatente.models.responses.SuccessInvite;
import com.ted_developers.triviapatente.models.responses.SuccessInvites;
import com.ted_developers.triviapatente.models.responses.UserToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPGameEndpoint {
    @FormUrlEncoded
    @POST("game/new")
    Call<SuccessGameUser> newGame(@Field("opponent") long opponent_id);

    @POST("game/new/random")
    Call<SuccessGameUser> newRandomGame();

    @GET("game/invites")
    Call<SuccessInvites> getPendingInvites();

    @GET("game/invites/badge")
    Call<SuccessBadge> getPendingInvitesBadge();

    @FormUrlEncoded
    @POST("game/invites/{game_id}")
    Call<SuccessInvite> processInvite(@Path("game_id") long game_id, @Field("accepted") boolean accepted);
}
