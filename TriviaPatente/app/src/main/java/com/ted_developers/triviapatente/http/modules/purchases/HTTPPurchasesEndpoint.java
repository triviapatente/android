package com.ted_developers.triviapatente.http.modules.purchases;

import com.ted_developers.triviapatente.models.purchases.ShopItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Antonio on 22/10/16.
 */
public interface HTTPPurchasesEndpoint {
    @GET("shop/list")
    Call<List<ShopItem>> getItems();
}
