package com.project.dreamsquad.trackmykid.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.UserProfile;


/**
 * Created by this pc on 19-05-17.
 */

public class ContactsFragment extends Fragment {

    private View view;
    private ImageView driverContact;
    private ImageView schoolContact;
    private TextView busNumber;
    private TextView schoolNum;
    private TextView vanNum;
    private TextView driverName;
    private TextView driverNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.school_details_tab, container, false);
        driverContact = (ImageView) view.findViewById(R.id.driverContactImage);
        schoolContact = (ImageView) view.findViewById(R.id.schoolContactImage);
        busNumber = (TextView) view.findViewById(R.id.bus_number);
        schoolNum = (TextView) view.findViewById(R.id.school_num);
        vanNum = (TextView) view.findViewById(R.id.contact_number);
        driverName = (TextView) view.findViewById(R.id.driver_name);
        driverNumber = (TextView) view.findViewById(R.id.contact_number_driver);

        final String s = "0717289213";

        busNumber.setText(UserProfile.getInstance().getVehicleNum());
        driverName.setText(UserProfile.getInstance().getDriverName());
        driverNumber.setText(UserProfile.getInstance().getDriverContactNumber());
        driverContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDriverNumber();           }
        });
        schoolContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSchoolNumber();
            }
        });

        return view;
    }

    public void callSchoolNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);

                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("tel:" + schoolNum.getText().toString()));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("tel:" + schoolNum.getText().toString()));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void callDriverNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);

                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("tel:" + vanNum.getText().toString()));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("tel:" + vanNum.getText().toString()));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
