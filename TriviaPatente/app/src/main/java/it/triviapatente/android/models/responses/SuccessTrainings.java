package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import it.triviapatente.android.models.auth.TrainingStats;
import it.triviapatente.android.models.training.Training;

/**
 * Created by donadev on 15/03/18.
 */
@Parcel
public class SuccessTrainings {
    @SerializedName("trainings")
    public List<Training> trainings;
    @SerializedName("stats")
    public TrainingStats stats;

    public SuccessTrainings() {

    }
}
