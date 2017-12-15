package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Antonio on 11/12/16.
 */
@Parcel
public class Accepted extends Success{
    @SerializedName("accepted") public Boolean accepted;
}
