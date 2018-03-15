package it.triviapatente.android.models.auth;

import com.google.gson.annotations.SerializedName;

import it.triviapatente.android.models.responses.Success;

import org.parceler.Parcel;

/**
 * Created by Antonio on 31/10/16.
 */
@Parcel
public class GlobalInfos extends Success {
    @SerializedName("global_rank_position") public Integer global_rank_position;
    // @SerializedName("friends_rank_position") public Integer friends_rank_position;
    // @SerializedName("stats") public List<Category> stats;
    @SerializedName("privacy_policy_last_update") public String privacy_policy_last_update;
    @SerializedName("terms_and_conditions_last_update") public String terms_and_conditions_last_update;
    @SerializedName("training_stats") public TrainingStats trainingStats;
}
