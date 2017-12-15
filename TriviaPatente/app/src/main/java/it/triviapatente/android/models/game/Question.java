package it.triviapatente.android.models.game;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Question extends CommonPK {
    @SerializedName("answer") public Boolean answer;
    @SerializedName("quiz_id") public Long quiz_id;
    @SerializedName("round_id") public Long round_id;
    @SerializedName("user_id") public Long user_id;
    @SerializedName("correct") public Boolean correct;

    public Question() {}
}
