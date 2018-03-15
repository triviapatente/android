package it.triviapatente.android.models.training;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import it.triviapatente.android.models.base.CommonPK;
import it.triviapatente.android.models.game.Quiz;

/**
 * Created by donadev on 15/03/18.
 */
@Parcel
public class Training extends CommonPK {
    @SerializedName("user_id")
    public String userId;
    @SerializedName("numberOfErrors")
    public int numberOfErrors;

    public List<Quiz> questions;

    public Training() {}
}
