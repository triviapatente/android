package it.triviapatente.android.models.purchases;

import com.google.gson.annotations.SerializedName;
import it.triviapatente.android.models.base.CommonPK;

import org.parceler.Parcel;

/**
 * Created by Antonio on 22/10/16.
 */
@Parcel
public class ShopItem extends CommonPK {
    @SerializedName("price")
    public String price;
    @SerializedName("emoji")
    public String emojii;
    @SerializedName("name")
    public String name;

    public ShopItem() {}
}
