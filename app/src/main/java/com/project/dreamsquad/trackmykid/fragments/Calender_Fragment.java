package com.project.dreamsquad.trackmykid.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.dreamsquad.trackmykid.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;

/**
 * Created by this pc on 19-05-17.
 */

public class Calender_Fragment extends Fragment {

    private View view;
    TextView month,year;
    ImageView left,right;
    SimpleDateFormat sdf;
    Date cal_date;
    Calendar startdate,endDate,prevDate;
    HorizontalCalendar horizontalCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.calender_layout,container,false);
        month=(TextView)view.findViewById(R.id.month);
        year=(TextView)view.findViewById(R.id.year);
        left=(ImageView)view.findViewById(R.id.decrease_month);
        right=(ImageView) view.findViewById(R.id.increase_month);


        //setting the start date from the first of present month
        startdate=Calendar.getInstance();
        startdate.set(Calendar.DATE,1);
        startdate.set(Calendar.HOUR, 00);
        startdate.set(Calendar.MINUTE, 00);
        startdate.set(Calendar.SECOND, 0);
        startdate.set(Calendar.MILLISECOND, 0);
        startdate.add(Calendar.MONTH, 0);


        //setting current date as the end date of the horizontal calender
        /** end after 1 month from now */
        endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH,0);
        endDate.add(Calendar.DATE,0);

//        prevDate = Calendar.getInstance();
//        prevDate.set(Calendar.DATE,1);
//        prevDate.set(Calendar.HOUR, 00);
//        prevDate.set(Calendar.MINUTE, 00);
//        prevDate.set(Calendar.SECOND, 0);
//        prevDate.set(Calendar.MILLISECOND, 0);
//        prevDate.add(Calendar.MONTH, -1);

        month.setText(new SimpleDateFormat("MMMM").format(startdate.getTime())+"");
        year.setText(new SimpleDateFormat("yyyy").format(startdate.getTime())+"");

        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .startDate(startdate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)   // Number of Dates cells shown on screen (Recommended 5)
                .dayNameFormat("EEE")	  // WeekDay text format
                .dayNumberFormat("dd")    // Date format
                .monthFormat("MMM") 	  // Month format
                .showDayName(true)	  // Show or Hide dayName text
                .showMonthName(false)	  // Show or Hide month text
                .textColor(Color.DKGRAY, Color.BLACK)    // Text color for none selected Dates, Text color for selected Date.
                .selectedDateBackground(Color.TRANSPARENT)  // Background color of the selected date cell.
                .selectorColor(Color.WHITE)// Color of the selection indicator bar (default to colorAccent).
                .textSizeDayNumber(15.0f)
                .build();

        //opening a fragment on every date

        Fragment fragment =new DatewiseFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commitAllowingStateLoss();

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                horizontalCalendar.selectDate(startdate.getTime(),true);
                Toast.makeText(getActivity(),startdate.getTime().toString(),Toast.LENGTH_LONG).show();

                //opening a fragment on every date
                Fragment fragment =new DatewiseFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commitAllowingStateLoss();

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(),new SimpleDateFormat("MMMM").format(endDate.getTime())+"",Toast.LENGTH_LONG).show();
//                month.setText(new SimpleDateFormat("MMMM").format(endDate.getTime()));
//                year.setText(new SimpleDateFormat("yyyy").format(endDate.getTime())+"");
//                getMonth();
                horizontalCalendar.selectDate(endDate.getTime(),true);
                Toast.makeText(getActivity(),startdate.getTime().toString(),Toast.LENGTH_LONG).show();

                //opening a fragment on every date
                Fragment fragment =new DatewiseFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commitAllowingStateLoss();


            }
        });

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                //do something

                Toast.makeText(getActivity(),date.toString(),Toast.LENGTH_LONG).show();

                //opening a fragment on every date
                Fragment fragment =new DatewiseFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Date date, int position) {
                return true;
            }
        });
        return view;
    }

}
