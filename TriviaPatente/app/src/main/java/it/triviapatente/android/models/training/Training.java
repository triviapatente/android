package it.triviapatente.android.models.training;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import it.triviapatente.android.models.base.CommonPK;
import it.triviapatente.android.models.game.Quiz;

/**
 * Created by donadev on 15/03/18.
 */
@Parcel
public class Training extends CommonPK implements Comparable<Training> {
    @SerializedName("user_id")
    public String userId;
    @SerializedName("numberOfErrors")
    public int numberOfErrors;

    public List<Quiz> questions;

    public Training() {}

    @Override
    public int compareTo(@NonNull Training training) {
        return training.getCreatedAt().compareTo(getCreatedAt());
    }
}
