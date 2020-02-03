package com.project.dreamsquad.trackmykid.others;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.dreamsquad.trackmykid.models.Notifications;
import com.project.dreamsquad.trackmykid.R;

import java.util.List;

/**
 * Created by this pc on 14-05-17.
 */

public class NotificationRecycleGrid extends RecyclerView.Adapter<NotificationRecycleGrid.MyHolder>{

    public RecyclerView re;
    private List<Notifications> dataSet ;
    public Context context=null;

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView reminder;
        TextView bus_status;
        TextView school;
        TextView day;

        public MyHolder(View itemView)
        {
            super(itemView);
            this.reminder = (TextView) itemView.findViewById(R.id.reminder);
            this.bus_status = (TextView) itemView.findViewById(R.id.bus_status);
            this.school=(TextView)itemView.findViewById(R.id.school);
            this.day=(TextView) itemView.findViewById(R.id.day);
        }
    }

    public NotificationRecycleGrid(List<Notifications> dataSet, Context context) {

        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_layout, parent, false);
        NotificationRecycleGrid.MyHolder myHolder=new NotificationRecycleGrid.MyHolder(view);
        re = (RecyclerView) parent.findViewById(R.id.notification_grid);
        return myHolder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        TextView reminder = holder.reminder;
        TextView bus_status = holder.bus_status;
        TextView school=holder.school;
        TextView day=holder.day;
        reminder.setText(dataSet.get(position).getReminder());
        bus_status.setText(dataSet.get(position).getBus_status());
        school.setText(dataSet.get(position).getSchool());
        day.setText(dataSet.get(position).getDay());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }




}
