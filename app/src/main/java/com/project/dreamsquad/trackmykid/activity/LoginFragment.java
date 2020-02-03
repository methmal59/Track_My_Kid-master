package com.project.dreamsquad.trackmykid.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.google.firebase.iid.FirebaseInstanceId;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.fragments.MainFragment;
import com.project.dreamsquad.trackmykid.models.UserProfile;
import com.project.dreamsquad.trackmykid.others.MainApplication;
import com.project.dreamsquad.trackmykid.others.PrefManager;
import com.project.dreamsquad.trackmykid.others.SignUp;
import com.project.dreamsquad.trackmykid.others.WebService;

/*
 * Copyright 2016 Anton Tananaev (anton.tananaev@gmail.com)
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

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {

    private TextView emailInput;
    private TextView passwordInput;
    private View loginButton, signupBtn;
    private TextView goToSignUp;
    String loginEmail, loginPassword;
    String token;

    PrefManager prefManager;
    static UserProfile userProfile;
    static MainApplication application;
    SharedPreferences preferences;

    public static MainApplication getApplication() { return application; }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            loginButton.setEnabled(
                    emailInput.getText().length() > 0 && passwordInput.getText().length() > 0);
        }

    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("inside loginnn");
        View view = inflater.inflate(R.layout.activity_login_fragment, container, false);
        System.out.println("Inside login");
        emailInput = (TextView) view.findViewById(R.id.input_email);
        passwordInput = (TextView) view.findViewById(R.id.input_password);
        loginButton = view.findViewById(R.id.button_login);
        goToSignUp = (TextView) view.findViewById(R.id.goToSignUp);
        signupBtn = view.findViewById(R.id.button_signup);

        emailInput.addTextChangedListener(textWatcher);
        passwordInput.addTextChangedListener(textWatcher);


        preferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);

//        preferences.edit().putString(
//                                           MainApplication.PREFERENCE_URL, "http://85.5.55.236:8082").apply();
//
//        emailInput.setText(preferences.getString(MainApplication.PREFERENCE_EMAIL, null));
//
//        if (preferences.getBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false)) {
//            login();
//        }
//
//
//        view.findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View dialogView = inflater.inflate(R.layout.view_settings, null);
//                final EditText input = (EditText) dialogView.findViewById(R.id.input_url);
//
//                input.setText(preferences.getString(MainApplication.PREFERENCE_URL, null));
//
//                new AlertDialog.Builder(getContext())
//                        .setTitle(R.string.settings_title)
//                        .setView(dialogView)
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                String url = input.getText().toString();
//                                if (HttpUrl.parse(url) != null) {
//                                    preferences.edit().putString(
//                                            MainApplication.PREFERENCE_URL, url).apply();
//                                } else {
//                                    Toast.makeText(getContext(), R.string.error_invalid_url, Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, null)
//                        .show();
//            }
//        });

        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignUp.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignUp.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmail = emailInput.getText().toString();
                loginPassword = passwordInput.getText().toString();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(MainApplication.PREFERENCE_AUTHENTICATED,false);
                editor.putString(MainApplication.PREFERENCE_EMAIL, loginEmail);
                editor.putString(MainApplication.PREFERENCE_PASSWORD, loginPassword);
                editor.apply();

                loginUser();
            }
        });

        return view;
    }
    public void loginUser(){

        userProfile = UserProfile.getInstance();
        userProfile.setLoggedUserName(loginEmail);
        userProfile.setLoggedPassword(loginPassword);
        userProfile.setAuthenticated(true);

        System.out.println("Printing beofre value");
        System.out.println("Beofre value   " + preferences.getBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false));

        login();
    }

    private void login() {
        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage(getString(R.string.app_loading));
        progress.setCancelable(false);
        progress.show();

        System.out.println("Before Consutructor");
        application = new MainApplication();
//        (MainApplication) getActivity().getApplication();
        System.out.println("Before Main Application");
        application.getServiceAsync(new MainApplication.GetServiceCallback() {
            @Override
            public void onServiceReady(OkHttpClient client, Retrofit retrofit, WebService service) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                System.out.println("LOGGED IN SUCCESFULLY");
                SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(getContext());
//                token = preferences1.getString("registration_id", null);
//                new UpdateToken().execute("");


                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(MainApplication.PREFERENCE_AUTHENTICATED,true);
                editor.putString(MainApplication.PREFERENCE_EMAIL, loginEmail);
                editor.putString(MainApplication.PREFERENCE_PASSWORD, loginPassword);
                editor.apply();


                System.out.println("After value   " + preferences.getBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false));
                System.out.println("Email " + preferences.getString(MainApplication.PREFERENCE_EMAIL ,null));
                System.out.println("Password " + preferences.getString(MainApplication.PREFERENCE_PASSWORD, null));
                // getActivity().finish();

                startActivity(new Intent(getContext(), MainActivity.class));
                MainFragment mainFragment = new MainFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                //       android.R.anim.fade_out);
                System.out.println("printing current tag");
                fragmentTransaction.add(mainFragment, "view map");
//                fragmentTransaction.replace(R.id.frame, mainFragment, "view map");
                fragmentTransaction.commitAllowingStateLoss();

                new SetToken().execute();

            }

            @Override
            public boolean onFailure() {
                System.out.println("AM");
                if (progress.isShowing()) {
                    progress.dismiss();
                    System.out.println("After progress dismissed");
                }
//                if(userProfile.getLogginError() != null) {
//                    String text = userProfile.getLogginError();
//                    Toast.makeText(getContext(), text + " ", Toast.LENGTH_LONG).show();
//                }
//                else
                    Toast.makeText(getContext(), "Invalid Username or Password!", Toast.LENGTH_LONG).show();
//                preferences
//                        .edit()
//                        .putBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false)
//                        .apply();

                SharedPreferences.Editor editor = preferences.edit();
//                editor.putBoolean(MainApplication.PREFERENCE_AUTHENTICATED,false);
                editor.apply();
                return false;
            }
        });
    }

//    private class UpdateToken extends AsyncTask<String, Void, String> {
//        private int userID = UserProfile.getInstance().getLoggedUserId();
//        private String userEmail = UserProfile.getInstance().getLoggedUserName();
//        private String parentName = UserProfile.getInstance().getParentName();
//        OkHttpClient client = new OkHttpClient();
//
//        @Override
//        protected String doInBackground(String... params) {
//            MediaType mediaType = MediaType.parse("application/json");
//
//            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
//            System.out.println("Token credentials: " + credentials);
//            final String basic =
//                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//            RequestBody body = RequestBody.create(mediaType, "{\n        \"attributes\": {\n        \t\"notificationTokens\" : \""+token+"\"\n        },\n        \"name\": \""+parentName+"\",\n        \"email\": \""+userEmail+"\",\n        \"id\": \""+userID+"\"\n}");
//            Request request = new Request.Builder()
//                    .url(UserProfile.getInstance().getUrl()+"/api/users/"+userID)
//                    .put(body)
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Authorization", basic)
//                    .addHeader("cache-control", "no-cache")
//                    .build();
//
//            try {
//                Response response = client.newCall(request).execute();
//                System.out.println("Token Status" + response.code());
//                System.out.println("token: " +response.body().string());
//                System.out.println("token: " +response.toString());
//            } catch (IOException e) {
//                System.out.println("Error saving the token");
//                e.printStackTrace();
//            }
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            System.out.println("TOken saved Sucessfully");
//
//        }
//    }



    private class SetToken extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
                FirebaseInstanceId.getInstance().getToken();
            } catch (IOException e) {
                System.out.println("Exception deleting token " + e.getStackTrace());
            }
            return null;
        }
    }

}
