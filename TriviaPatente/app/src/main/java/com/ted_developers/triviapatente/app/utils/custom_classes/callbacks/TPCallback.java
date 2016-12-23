package com.ted_developers.triviapatente.app.utils.custom_classes.callbacks;

import com.ted_developers.triviapatente.app.utils.mApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Antonio on 25/10/16.
 */
public abstract class TPCallback<T> implements Callback<T> {
    protected boolean doThen = true;
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.code() == 401) {
            mApplication.getInstance().goToLoginPage();
        } else {
            mOnResponse(call, response);
            if(doThen) {
                then();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        mOnFailure(call, t);
        if(doThen) {
            then();
        }
    }

    // things to do on response
    public abstract void mOnResponse(Call<T> call, Response<T> response);
    // things to do on failure
    public abstract void mOnFailure(Call<T> call, Throwable t);
    // things to do finally
    public abstract void then();
}
