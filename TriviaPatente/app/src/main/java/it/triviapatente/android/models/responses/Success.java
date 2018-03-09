package it.triviapatente.android.models.responses;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class Success {
    @SerializedName("success") public Boolean success;
    @SerializedName("status_code") public Integer status_code;
    @SerializedName("timeout") public Boolean timeout;

    public Success() {}
    public Success(Boolean success, Integer status_code) {
        this.success = success;
        this.status_code = status_code;
    }
}
