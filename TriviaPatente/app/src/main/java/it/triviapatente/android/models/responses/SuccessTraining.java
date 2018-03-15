package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import it.triviapatente.android.models.training.Training;

/**
 * Created by donadev on 15/03/18.
 */
@Parcel
public class SuccessTraining extends Success {
    @SerializedName("training")
    public Training training;

    public SuccessTraining() {}
}
