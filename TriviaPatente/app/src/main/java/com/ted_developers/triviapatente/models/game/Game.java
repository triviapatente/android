package com.ted_developers.triviapatente.models.game;

import com.google.gson.annotations.SerializedName;
import com.ted_developers.triviapatente.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Game extends CommonPK {
    @SerializedName("creator_id")
    public Long creator_id;
    @SerializedName("winner_id")
    public Long winner_id;
    @SerializedName("ended")
    public Boolean ended;

    public Game() {}
}
