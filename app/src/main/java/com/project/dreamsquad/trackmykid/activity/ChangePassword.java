package com.project.dreamsquad.trackmykid.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.UserProfile;
import com.project.dreamsquad.trackmykid.others.MainApplication;
import com.project.dreamsquad.trackmykid.others.Validation_Signup;
import com.project.dreamsquad.trackmykid.others.WebService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class ChangePassword extends Fragment {
    TextView oldPass;
    TextView newPass;
    TextView newPassConfirm;
    TextView loggedInUserName;
    ImageView backButtonChangePassword;
    Button button_changePassword;

    String oldPassword;
    String newPassword;
    String newPasswordConfirm;
    String userName;
    String realEmail;
    ProgressDialog progress;

    ProgressDialog dialog;
    OkHttpClient client = new OkHttpClient();
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_change_password, container, false);
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);


        oldPass = (TextView) view.findViewById(R.id.oldPassword);
        newPass = (TextView) view.findViewById(R.id.newPassword);
        newPassConfirm = (TextView) view.findViewById(R.id.newPasswordConfirm);
        loggedInUserName = (TextView) view.findViewById(R.id.changePasswordUserName);
        button_changePassword = (Button) view.findViewById(R.id.button_changePassword);

        loggedInUserName.requestFocus();
        preferences = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);


        button_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Inside change password");
                onChangePassword();
            }
        });

        return view;

    }

    public void displayAlert(String text){
        String msg = text + " Please try again!";
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Password changing Error!")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public boolean validateData() {
        if(userName.isEmpty()){
            displayAlert("User name cannot be empty,");
            return false;
        }
        else if (oldPassword.isEmpty()) {
            displayAlert("Old password cannot be Empty,");
            return false;
        } else if (newPassword.isEmpty()) {
            displayAlert("New Password cannot be empty,");
            return false;
        } else if (newPasswordConfirm.isEmpty()) {
            displayAlert("Confirm Password cannot be Empty,");
            return false;
        } else {
            Validation_Signup signupChecker = new Validation_Signup();
            int validCode = signupChecker.validateDataChangePassword(newPassword, newPasswordConfirm);

            if (validCode == 103) {
                displayAlert("New password and confirm password doesn't match,");
                return false;
            } else if (validCode == 104) {
                displayAlert("Password must have a number, a letter, at least 6 characters long and shouldn't have any spaces,");
                return false;
            } else
                return true;

        }
    }



    public void onChangePassword(){
        System.out.println("Change Password");

        oldPassword = oldPass.getText().toString();
        newPassword = newPass.getText().toString();
        newPasswordConfirm = newPassConfirm.getText().toString();
        userName = loggedInUserName.getText().toString();


        realEmail = MainApplication.getLoggedInEmail();

        System.out.println("PRINTING USER NAME AND REAL USER NAME");
        System.out.println(userName);
        System.out.println(realEmail);
        if(userName.isEmpty()){
            displayAlert("Username cannot be empty!");
            return;
        }
        if (!userName.equals(realEmail)) {
            displayAlert("Invalid username, ");
            return;
        }

        if(!validateData())
            return;

        progress = new ProgressDialog(getContext());
        progress.setMessage(getString(R.string.app_loading));
        progress.setCancelable(false);
        progress.show();

        new CorrectOldPassword().execute("");

    }

    public void userValidated(){
        long userId = MainApplication.getId();
        String name = MainApplication.getLoggedInName();

        MediaType mediaType = MediaType.parse("application/json");
        String credentials = "admin" + ":" + "admin";

        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"name\" : \""+name+"\",\r\n\t\"email\" : \""+userName+"\",\r\n\t\"id\" : \""+userId+"\",\r\n\t\"password\" : \""+newPassword+"\"\r\n}");
        Request request = new Request.Builder()
                .url(UserProfile.getInstance().getUrl()+"/api/users/"+userId)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("Authorization", basic)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Network error");
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (progress.isShowing()) {
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Network Error, please check your network connection and try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("444");
                if (!response.isSuccessful()) {
                    System.out.println("SQL Error");
                    displayAlert("Invalid Username, ");
                    return;

                }

                if(response.code() == 200) {
                    try {
                        System.out.println("Printing success notes");

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(MainApplication.PREFERENCE_AUTHENTICATED,true);
                        editor.putString(MainApplication.PREFERENCE_PASSWORD, newPassword);
                        editor.apply();

                        String responseBody = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                if (progress.isShowing())
                                    progress.dismiss();
                                Toast.makeText(getActivity(), "Password changed Successfully", Toast.LENGTH_LONG).show();
                            }
                        });

                        System.out.println("Heree");
//                        MainFragment fragment = new MainFragment();
//                        System.out.println("Dis");
//                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        System.out.println("AAA");
//                        // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                        //       android.R.anim.fade_out);
//                        fragmentTransaction.replace(R.id.frame, fragment, "View Kid");
//                        System.out.println("Asdad");
//                        ((MainActivity) getActivity())
//                                .setActionBarTitle("View Kid");
//                        System.out.println("adas");
//                        fragmentTransaction.commitAllowingStateLoss();

                    } catch (Exception e) {
                        if (progress.isShowing())
                            progress.dismiss();
                        Toast.makeText(getActivity(), "Error changing the password", Toast.LENGTH_LONG).show();
                        System.out.println("Json Exception at Signup function.");
                        e.printStackTrace();
                    }
                    System.out.println("Got the new user's id successfully");
                    loginUser();
                }
            }
        });
    }




    public void loginUser(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        try {
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        }catch (Exception e){
            System.out.println("Exception at Change password loginuser function, Preference Manager");
            e.printStackTrace();
        }

        preferences
                .edit()
                .putBoolean(MainApplication.PREFERENCE_AUTHENTICATED, true)
                .putString(MainApplication.PREFERENCE_EMAIL, userName)
                .putString(MainApplication.PREFERENCE_PASSWORD, newPassword)
                .apply();

        UserProfile userProfile = UserProfile.getInstance();
        userProfile.setLoggedUserName(userName);
        userProfile.setLoggedPassword(newPassword);
        userProfile.setAuthenticated(true);

        login();
    }

    private void login() {
        System.out.println("inside login function at change password");
        try {
            final MainApplication application = new MainApplication();
            application.getServiceAsync(new MainApplication.GetServiceCallback() {
                @Override
                public void onServiceReady(OkHttpClient client, Retrofit retrofit, WebService service) {
                    getActivity();
                    if (progress.isShowing())
                        progress.dismiss();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }

                @Override
                public boolean onFailure() {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (progress.isShowing())
                                progress.dismiss();
                            Toast.makeText(getActivity(), "Unable to continue, please try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                    System.out.println("Failure at login when signed up");
                    return false;
                }
            });
        }catch (Exception e){
            if (progress.isShowing())
                progress.dismiss();
            System.out.println("Exception at login function 227");
            e.printStackTrace();
        }
    }

    private class CorrectOldPassword extends AsyncTask<String, Void, String> {
        String username = MainApplication.getLoggedInEmail();

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "email="+username+"&password="+oldPassword+"&undefined=");
            System.out.println("Email and password " + userName + "  " +oldPassword);
            Request request = new Request.Builder()
                    .url(UserProfile.getInstance().getUrl()+"/api/session")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Response " + response.code() + "  " +response.body().toString());
                if(response.code() == 200)
                    userValidated();
                else{
                    getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if (progress.isShowing())
                                progress.dismiss();
                                displayAlert("Previous Password is invalid, ");
                                return;

                        }
                    });
                }
            } catch (IOException e) {
                getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if (progress.isShowing())
                            progress.dismiss();
                        Toast.makeText(getActivity(), "Network Error, Please try again!", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }
            return "Executed";
        }

    }



}








