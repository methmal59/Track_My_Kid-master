package com.project.dreamsquad.trackmykid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.project.dreamsquad.trackmykid.activity.LoginActivity;
import com.project.dreamsquad.trackmykid.activity.LoginFragment;
import com.project.dreamsquad.trackmykid.models.UserProfile;
import com.project.dreamsquad.trackmykid.others.MainApplication;
import com.project.dreamsquad.trackmykid.others.PrefManager;

import java.io.IOException;

public class Logout extends Fragment {

    private View view;
    Button pickup,drop;
    PrefManager pref;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        pref=new PrefManager(getActivity());
        pref.setButton2Notify(0);
        pref.setButtonNotify(0);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //view=inflater.inflate(R.layout.fragment_location,container,false);

        new DeleteToken().execute();

        SharedPreferences preferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        preferences
                .edit()
                .putBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false)
                .putString(MainApplication.PREFERENCE_EMAIL, null)
                .putString(MainApplication.PREFERENCE_PASSWORD, null)
                .apply();

        UserProfile userProfile = UserProfile.getInstance();

        userProfile.setLoggedUserName(null);
        userProfile.setLoggedPassword(null);
        userProfile.setParentName(null);
        userProfile.setEmail(null);
        userProfile.setParentContactNumber(null);
        userProfile.setVehicleNum(null);
        userProfile.setKidName(null);
        userProfile.setKidSchool(null);
        userProfile.setDriverName(null);
        userProfile.setDriverContactNumber(null);
        userProfile.setLogginError(null);
        userProfile.setAuthenticated(false);
        userProfile.setPickUpLocation(null);
        userProfile.setDropOffLocation(null);
        userProfile.setSchoolLocation(null);
        userProfile.setPickUpReminderDistance(0);
        userProfile.setDropOffReminderDistance(0);
        userProfile.setPickNotificationEnabled(false);
        userProfile.setDropOffNotificationEnabled(false);
        userProfile.setIsaNewUser(false);

        UserProfile.getInstance().setLogout();

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();

        return view;
    }

    private class DeleteToken extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                System.out.println("Exception deleting token " + e.getStackTrace());
            }
            return null;
        }
    }

}
