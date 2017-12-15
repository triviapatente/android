package it.triviapatente.android.models.game;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.base.CommonPK;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Quiz extends CommonPK {
    @SerializedName("image_id") public Long image_id;
    @SerializedName("question") public String question;
    @SerializedName("answer") public Boolean answer;
    @SerializedName("category_id") public Long category_id;
    @SerializedName("round_id") public Long round_id;
    @SerializedName("my_answer") public Boolean my_answer;
    @SerializedName("answered_correctly") public Boolean answered_correctly;

    public Quiz() {}

    public List<Question> answers;
}
