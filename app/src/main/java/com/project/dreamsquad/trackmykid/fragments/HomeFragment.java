package com.project.dreamsquad.trackmykid.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.activity.StudentTrackingActivity;
import com.project.dreamsquad.trackmykid.models.Student;
import com.project.dreamsquad.trackmykid.models.UserProfile;
import com.project.dreamsquad.trackmykid.others.HomeRecycleGrid;
import com.project.dreamsquad.trackmykid.others.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by this pc on 11-05-17.
 */

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView.LayoutManager layoutManager;
    HomeRecycleGrid adapter;
    RecyclerView recyclerView;
    List<Student> studentList;
    PrefManager pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.card_recycler_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.card_grid);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pref = new PrefManager(getActivity());

        Drawable d = getResources().getDrawable(R.drawable.placeholder);
        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.placeholder);


        getInformation();
        return view;

    }

    public void getInformation() {
        studentList = new ArrayList<>();

        //filling cards with dummy data(Data from JSON API will be filled here)
        UserProfile userProfile = UserProfile.getInstance();
        String name = userProfile.getKidName();
        String school = userProfile.getKidSchool();
        //String status = userProfile.getStatus();

        studentList.add(new Student(name, school, "At School"));
        //studentList.add(new Student("Kasun Perera", "Royal College", "At Van"));
        adapter = new HomeRecycleGrid(getActivity(), studentList, new HomeRecycleGrid.VenueAdapterClickCallbacks() {

            @Override
            public void onCardClick(String p) {

                Toast.makeText(getActivity(), p + "", Toast.LENGTH_LONG).show();
                pref.setName(p);
                startActivity(new Intent(getActivity(), StudentTrackingActivity.class));
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
