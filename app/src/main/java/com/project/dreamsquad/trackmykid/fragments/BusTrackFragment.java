package com.project.dreamsquad.trackmykid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.UserProfile;

/**
 * Created by this pc on 19-05-17.
 */

public class BusTrackFragment extends Fragment {

    private View view;
    private UserProfile userProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Infalting view");
        view=inflater.inflate(R.layout.bus_track_layout,container,false);
        userProfile = UserProfile.getInstance();
        System.out.println("Infalting bus track layout");

        TextView location = (TextView) view.findViewById(R.id.reminder1Location);
        TextView schoolName = (TextView) view.findViewById(R.id.schoolName) ;
        TextView locationDrop = (TextView) view.findViewById(R.id.locationDrop);

        if(userProfile.getPickUpReminderDistance() != 0)
            location.setText(userProfile.getPickUpReminderDistance()+"km to the Pickup Location");

        if(userProfile.getKidSchool() != null && !userProfile.getKidSchool().isEmpty())
            schoolName.setText(userProfile.getKidSchool());

        if(userProfile.getDropOffReminderDistance() != 0)
            locationDrop.setText(userProfile.getDropOffReminderDistance()+"km to the Drop Off Location");
        return view;
    }
}
