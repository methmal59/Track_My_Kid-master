package com.project.dreamsquad.trackmykid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.Profile;
import com.project.dreamsquad.trackmykid.others.PrefManager;
import com.project.dreamsquad.trackmykid.others.ProfileRecycleGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by this pc on 27-05-17.
 */

public class ProfileFragment extends Fragment {
    ImageView profile_image;
    ProfileRecycleGrid adapter;
    RecyclerView recyclerView;
    List<Profile> profileList;
    ImageView add;
    AlertDialog alertDialog;
    PrefManager pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);
        profile_image = (ImageView) view.findViewById(R.id.img_profile);
        add = (ImageView) view.findViewById(R.id.add);

        recyclerView = (RecyclerView) view.findViewById(R.id.profile_grid);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        pref = new PrefManager(getActivity());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateProfileFragment.newInstance(new CreateProfileFragment.CreateProfileDismiss() {
                    @Override
                    public void onDismiss() {
                        getProfileCards();
                    }
                }).show(getChildFragmentManager(), "NEW_PROFILE");
            }
        });

        getProfileCards();
        return view;
    }

    public void getProfileCards() {

        profileList = new ArrayList<>();

        //filling the profile cards with dummy data
        profileList.add(new Profile("Rajesh Gupta", R.drawable.pi, "Father", "+91 903 335 6708", "rajeshgupta@gmail.com"));
        profileList.add(new Profile("Rajesh Gupta", R.drawable.pi, "Father", "+91 903 335 6708", "rajeshgupta@gmail.com"));
        if (pref.getPName() != null) {
            profileList.add(new Profile(pref.getPName(), R.drawable.pi, pref.getRelation(), pref.getPContact(), pref.getEmail()));
        }
        adapter = new ProfileRecycleGrid(profileList, getActivity());
        recyclerView.setAdapter(adapter);
    }
}
