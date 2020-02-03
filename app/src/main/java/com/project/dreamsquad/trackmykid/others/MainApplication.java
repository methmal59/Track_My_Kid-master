package com.project.dreamsquad.trackmykid.others;

/*
 * Copyright 2015 - 2016 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.project.dreamsquad.trackmykid.models.User;
import com.project.dreamsquad.trackmykid.models.UserProfile;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MainApplication extends AppCompatActivity {
    private static long loggedInId;
    private static String loggedInName;
    private static String loggedInEmail;

    public static long getId(){
        return loggedInId;
    }
    public static String getLoggedInName(){ return loggedInName; }
    public static String getLoggedInEmail(){return loggedInEmail;}

    public static final String PREFERENCE_AUTHENTICATED = "authenticated";
    public static final String PREFERENCE_URL = "url";
    public static final String PREFERENCE_EMAIL = "email";
    public static final String PREFERENCE_PASSWORD = "password";

    private String url;
    private String password;
    private String email;

    UserProfile userProfile;
    SharedPreferences preferences;

    PrefManager prefManager;

    private static final String DEFAULT_SERVER = UserProfile.getInstance().getUrl(); // local - http://10.0.2.2:8082
//    private static final String DEFAULT_SERVER = "http://localhost:8082/"; // local - http://10.0.2.2:8082

    public interface GetServiceCallback {
        void onServiceReady(OkHttpClient client, Retrofit retrofit, WebService service);
        boolean onFailure();
    }

    public OkHttpClient client;


    private WebService service;
    private Retrofit retrofit;
    private User user;
    CookieManager cookieManager;

    private final List<GetServiceCallback> callbacks = new LinkedList<>();

    public void getServiceAsync(GetServiceCallback callback) {
        System.out.println("MAIN APP");
        if (service != null) {
            callback.onServiceReady(client, retrofit, service);
        } else {
            if (callbacks.isEmpty()) {
//                removeService();
                initService();
            }
            callbacks.add(callback);
        }
    }

    public WebService getService() { return service; }
    public User getUser() { return user; }

    public void removeService() {
        service = null;
        user = null;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        System.out.println("Inside oncreate");
        super.onCreate(savedInstance);
        System.out.println("Inside oncreate");
        System.out.println("After on create");

//        if (!preferences.contains(PREFERENCE_URL)) {
//            preferences.edit().putString(PREFERENCE_URL, DEFAULT_SERVER).apply();
//        }
    }


    public void initService() {
        System.out.println("Indiside initiaiaiaaiaiaiaiaia serviceeeeee");
//        if(getSharedPreferences("userinfo", Context.MODE_PRIVATE) != null) {
//            preferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
//            url = preferences.getString(PREFERENCE_URL, "");
//            email = preferences.getString(PREFERENCE_EMAIL, "");
//            password = preferences.getString(PREFERENCE_PASSWORD, "");
//        }else{
            url = UserProfile.getInstance().getUrl();
            email = UserProfile.getInstance().getLoggedUserName();
            password = UserProfile.getInstance().getLoggedPassword();
        System.out.println(url + "   " + email + "    " + password);
//        }
        System.out.println("After pref initialize");
        userProfile = UserProfile.getInstance();

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager)).build();
        try {
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(url)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            System.out.println("CCC");
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal arugment exception bla blaa");
            e.printStackTrace();
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            for (GetServiceCallback callback : callbacks) {
                callback.onFailure();
            }
            callbacks.clear();
        }

        final WebService service = retrofit.create(WebService.class);

        service.addSession(email, password).enqueue(new WebServiceCallback<User>(MainApplication.this) {
            @Override
            public void onSuccess(Response<User> response) {
                System.out.println("Success");
                loggedInId = response.body().getId();
                userProfile.setLoggedUserId((int)response.body().getId());
                loggedInName = response.body().getName();
                loggedInEmail = response.body().getEmail();
                System.out.println("Heres the details");
                System.out.println(loggedInId);
                System.out.println(loggedInName);
                System.out.println(loggedInEmail);

                MainApplication.this.service = service;
                MainApplication.this.user = response.body();
                for (GetServiceCallback callback : callbacks) {
                    callback.onServiceReady(client, retrofit, service);
                }
                callbacks.clear();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Failed");
                System.out.println("Reason  " + t.getLocalizedMessage());
                System.out.println(t.getMessage());
                System.out.println(t.getStackTrace());
                boolean handled = false;
                for (GetServiceCallback callback : callbacks) {
                    handled = callback.onFailure();
                }
                callbacks.clear();
                if (!handled) {
                    super.onFailure(call, t);
                }
            }
        });
    }

}

