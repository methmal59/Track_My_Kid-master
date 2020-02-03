package com.project.dreamsquad.trackmykid.others;


import android.content.Context;

import com.project.dreamsquad.trackmykid.models.UserProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class WebServiceCallback<T> implements Callback<T> {

    private Context context;
    UserProfile userProfile = UserProfile.getInstance();

    public WebServiceCallback(Context context) {
        this.context = context;
    }

    public abstract void onSuccess(Response<T> response);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response);
        } else {
            onFailure(call, new ServiceException(response.message()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        String text = "ABC";
        if (t instanceof ServiceException) {
            System.out.println("Wrong username or password, please try again");
        } else {
            System.out.println("Please check your network connection and try again");
        }
//        Toast.makeText(context, text + " ", Toast.LENGTH_LONG).show();
    }

}
