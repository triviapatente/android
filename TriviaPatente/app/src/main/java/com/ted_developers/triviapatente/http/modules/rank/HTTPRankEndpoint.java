package com.ted_developers.triviapatente.http.modules.rank;

import com.ted_developers.triviapatente.models.responses.RankPosition;
import com.ted_developers.triviapatente.models.responses.SuccessUsers;

import retrofit2.Call;
import retrofit2.http.GET;
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
    Call<SuccessUsers> getSearchResult(@Query("query") String query);
}
