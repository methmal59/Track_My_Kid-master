package com.project.dreamsquad.trackmykid.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.fragments.MainFragment;
import com.project.dreamsquad.trackmykid.models.UserProfile;
import com.project.dreamsquad.trackmykid.others.ConnectionDetector;
import com.project.dreamsquad.trackmykid.others.MainApplication;
import com.project.dreamsquad.trackmykid.others.WebService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by this pc on 12-05-17.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    Animation animation1,animation2;
    ImageView logo;
    LinearLayout linearLayout;
    ConnectionDetector connectionDetector;
    static MainApplication application;
    SharedPreferences preferences;
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        preferences = getApplication().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        System.out.println("Hiii");
        logo=(ImageView)findViewById(R.id.app_icon);
        linearLayout=(LinearLayout)findViewById(R.id.layout);


        //intialising animations to be used on splash screen
        animation1 = AnimationUtils.loadAnimation(this,R.anim.shake_animation);
        animation2 = AnimationUtils.loadAnimation(this,R.anim.animate_layout);
        linearLayout.startAnimation(animation1);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                System.out.println("AAA");
                System.out.println("After value   " + preferences.getBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false));
                System.out.println("Email " + preferences.getString(MainApplication.PREFERENCE_EMAIL ,null));
                System.out.println("Password " + preferences.getString(MainApplication.PREFERENCE_PASSWORD, null));
                // This method will be executed once the timer is over
                // Start your app main activity
                connectionDetector=new ConnectionDetector(SplashActivity.this);

                if(connectionDetector.isConnectingToInternet()) {
                    System.out.println("Before checking");
                    if (preferences.getBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false)){
                        System.out.println("Preference authenticated trueeeeeeeee");
                        login();
                    }
                    else {
                        System.out.println("Not a logged in user");
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        // close this activity
                        finish();
                    }

//                    if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
//                    {
//                        // call Login Activity
//                    }
//                    else
//                    {
//                        // Stay at the current activity.
//                    }

                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setMessage("Please connect to the Internet")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void login() {
//        final ProgressDialog progress = new ProgressDialog(getContext());
//        progress.setMessage(getString(R.string.app_loading));
//        progress.setCancelable(false);
//        progress.show();
        UserProfile.getInstance().setLoggedUserName(preferences.getString(MainApplication.PREFERENCE_EMAIL ,null));
        UserProfile.getInstance().setLoggedPassword(preferences.getString(MainApplication.PREFERENCE_PASSWORD, null));

        application = new MainApplication();
        application.getServiceAsync(new MainApplication.GetServiceCallback() {
            @Override
            public void onServiceReady(OkHttpClient client, Retrofit retrofit, WebService service) {
//                if (progress.isShowing()) {
//                    progress.dismiss();
//                }
                System.out.println("LOGGED IN SUCCESFULLY");

                System.out.println("After value   " + preferences.getBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false));
                System.out.println("Email " + preferences.getString(MainApplication.PREFERENCE_EMAIL ,null));
                System.out.println("Password " + preferences.getString(MainApplication.PREFERENCE_PASSWORD, null));

                // getActivity().finish();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                MainFragment mainFragment = new MainFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                //       android.R.anim.fade_out);
                System.out.println("printing current tag");
                fragmentTransaction.add(mainFragment, "view map");
//                fragmentTransaction.replace(R.id.frame, mainFragment, "view map");
                fragmentTransaction.commitAllowingStateLoss();
            }

            @Override
            public boolean onFailure() {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(MainApplication.PREFERENCE_AUTHENTICATED,false);
                editor.apply();
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                // close this activity
                finish();


                return false;
            }
        });
    }

}
