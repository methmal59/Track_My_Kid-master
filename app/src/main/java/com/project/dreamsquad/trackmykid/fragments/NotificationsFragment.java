package com.project.dreamsquad.trackmykid.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.Notifications;
import com.project.dreamsquad.trackmykid.others.NotificationRecycleGrid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by this pc on 11-05-17.
 */

public class NotificationsFragment extends DialogFragment {

    private View view;
    private RecyclerView.LayoutManager layoutManager;
    NotificationRecycleGrid adapter;
    RecyclerView recyclerView;
    List<Notifications> notificationsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.notification_recycle_layout,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.notification_grid);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getNotification();
        return view;
    }

    public void getNotification(){

        notificationsList=new ArrayList<>();

        //filling cards with dummy data(Data from JSON API will be filled here)

        notificationsList.add(new Notifications("Drop Reminder","Bus is about to reach at drop location.","2.0 km away from shivaji park","4:30pm"));
        notificationsList.add(new Notifications("Drop Reminder","Bus is about to reach at drop location.","2.0 km away from shivaji park","4:30pm"));
        adapter=new NotificationRecycleGrid(notificationsList,getActivity());
        recyclerView.setAdapter(adapter);
    }
}
