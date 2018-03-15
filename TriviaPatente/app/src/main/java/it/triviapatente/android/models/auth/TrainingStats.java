package it.triviapatente.android.models.auth;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by donadev on 15/03/18.
 */
@Parcel
public class TrainingStats {
    @SerializedName("total")
    public int total;
    @SerializedName("1_2errors")
    public int errors_12;
    @SerializedName("3_4errors")
    public int errors_34;
    @SerializedName("correct")
    public int no_errors;
    @SerializedName("more_errors")
    public int more_errors;

    public TrainingStats() {}

}
