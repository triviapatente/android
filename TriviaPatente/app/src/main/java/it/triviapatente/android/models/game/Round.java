package it.triviapatente.android.models.game;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.base.CommonPK;
import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Round extends CommonPK {
    @SerializedName("number")
    public Integer number;
    @SerializedName("game_id")
    public Long game_id;
    @SerializedName("cat_id")
    public Long cat_id;
    @SerializedName("dealer_id")
    public Long dealer_id;

    @SerializedName("alreadyTickled")
    public Boolean alreadyTickled;

    public Round() {}
}
