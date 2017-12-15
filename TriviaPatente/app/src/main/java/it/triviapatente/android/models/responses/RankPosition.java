package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.auth.User;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Antonio on 05/11/17.
 */

@Parcel
public class RankPosition {
    @SerializedName("rank") public List<User> rank;
    @SerializedName("my_position") public Integer my_position;
}
