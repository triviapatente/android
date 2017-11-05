package com.ted_developers.triviapatente.http.modules.rank;

import com.ted_developers.triviapatente.models.responses.Matches;
import com.ted_developers.triviapatente.models.responses.RankPosition;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessDecrement;
import com.ted_developers.triviapatente.models.responses.SuccessGameUser;
import com.ted_developers.triviapatente.models.responses.SuccessGames;
import com.ted_developers.triviapatente.models.responses.SuccessUsers;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPRankEndpoint {
    /*
    @GET("rank/global")
    Call<RankPosition> getUsers();
    */

    @GET("rank/global")
    Call<RankPosition> getUsers(@Query("thresold") Integer thresold, @Query("direction") String direction);

    @GET("rank/search")
    Call<Matches> getSearchResult(@Query("query") String query);
}
