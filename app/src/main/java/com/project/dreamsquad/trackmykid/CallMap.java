package com.project.dreamsquad.trackmykid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.dreamsquad.trackmykid.activity.MainActivityOld;
import com.project.dreamsquad.trackmykid.others.PrefManager;

public class CallMap extends Fragment {

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

            startActivity(new Intent(getActivity(), MainActivityOld.class));


            return view;
        }


    }

