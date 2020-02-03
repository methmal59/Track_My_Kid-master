package com.project.dreamsquad.trackmykid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.dreamsquad.trackmykid.others.PrefManager;

/**
 * Created by this pc on 27-05-17.
 */

public class LocationFragment extends Fragment {

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

        startActivity(new Intent(getActivity(), PickUp.class));
//
//        Fragment fragment = new PIckUpFragment();
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                android.R.anim.fade_out);
//        fragmentTransaction.replace(R.id.frame_location, fragment);
//        fragmentTransaction.commitAllowingStateLoss();
//
//        pickup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //changing the layout of the buttons and opening the fragment according to the button click
//                if(pref.getButtonNotify()==0){
//                    pickup.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_pickup_active, 0, 0, 0);
//                    pickup.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    pickup.setBackground(getResources().getDrawable(pickup_bkgrnd));
//                    drop.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_drop_down_inactive, 0, 0, 0);
//                    drop.setTextColor(getResources().getColor(R.color.splashTitle));
//                    drop.setBackground(getResources().getDrawable(drop_bkgrnd));
//                    Fragment fragment = new PIckUpFragment();
//                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                            android.R.anim.fade_out);
//                    fragmentTransaction.replace(R.id.frame_location, fragment);
//                    fragmentTransaction.commitAllowingStateLoss();
//                    pref.setButtonNotify(1);
//                    pref.setButton2Notify(0);
//                }}
//        });
//
//        drop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //changing the layout of the buttons and opening the fragment according to the button click
//                if(pref.getButton2Notify()==0){
//                    drop.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_drop_down_active, 0, 0, 0);
//                    drop.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    drop.setBackground(getResources().getDrawable(pickup_bkgrnd));
//                    pickup.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_pickup_inactive, 0, 0, 0);
//                    pickup.setTextColor(getResources().getColor(R.color.splashTitle));
//                    pickup.setBackground(getResources().getDrawable(drop_bkgrnd));
//                    Fragment fragment = new DropFragment();
//                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                            android.R.anim.fade_out);
//                    fragmentTransaction.replace(R.id.frame_location, fragment);
//                    fragmentTransaction.commitAllowingStateLoss();
//                    pref.setButtonNotify(0);
//                    pref.setButton2Notify(1);
//
//                }}
//        });
        return view;
    }

//    public void changeButton()
//    {
//        intializeButtons();
//
//        //changing the layout of the buttons
//        drop.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_drop_down_active, 0, 0, 0);
//        drop.setTextColor(getResources().getColor(R.color.colorPrimary));
//        drop.setBackground(getResources().getDrawable(pickup_bkgrnd));
//        pickup.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_pickup_inactive, 0, 0, 0);
//        pickup.setTextColor(getResources().getColor(R.color.splashTitle));
//        pickup.setBackground(getResources().getDrawable(drop_bkgrnd));
//
//    }
//
//    public void intializeButtons(){
//
//        //intailaizing buttons
//        pickup=(Button)view.findViewById(R.id.pick_up);
//        drop=(Button)view.findViewById(R.id.drop);
//    }
}
