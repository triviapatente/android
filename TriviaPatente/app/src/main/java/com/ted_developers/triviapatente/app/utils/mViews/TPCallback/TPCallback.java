package com.ted_developers.triviapatente.app.utils.mViews.TPCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Antonio on 25/10/16.
 */
public abstract class TPCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        mOnResponse(call, response);
        then();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        mOnFailure(call, t);
        then();
    }

    // things to do on response
    public abstract void mOnResponse(Call<T> call, Response<T> response);
    // things to do on failure
    public abstract void mOnFailure(Call<T> call, Throwable t);
    // things to do finally
    public abstract void then();
}
