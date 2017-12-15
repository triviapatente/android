package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Antonio on 27/12/16.
 */
@Parcel
public class SuccessAnsweredCorrectly extends Success {
    @SerializedName("correct_answer") public Boolean correct_answer;

    public SuccessAnsweredCorrectly() {}
}
