package com.ted_developers.triviapatente.models.auth;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.responses.Success;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 31/10/16.
 */
@Parcel
public class Hints extends Success {
    @SerializedName("global_rank_position") public Integer global_rank_position;
    // @SerializedName("friends_rank_position") public Integer friends_rank_position;
    // @SerializedName("stats") public List<Category> stats;
    @SerializedName("privacy_policy_last_update") public String privacy_policy_last_update;
    @SerializedName("terms_and_conditions_last_update") public String terms_and_conditions_last_update;
}
