package com.project.dreamsquad.trackmykid.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.fragments.NotificationsFragment;
import com.project.dreamsquad.trackmykid.fragments.TrackingFragment;
import com.project.dreamsquad.trackmykid.others.PrefManager;

/**
 * Created by this pc on 19-05-17.
 */

public class StudentTrackingActivity extends AppCompatActivity {
    Toolbar toolbar;
    PrefManager pref;
    Fragment fragment1 = new NotificationsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_tracking_layout);
        pref = new PrefManager(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(pref.getName());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        //opening the tracking fragment
        Fragment fragment = new TrackingFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, fragment, "TrackFrag");
        fragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if (item.getItemId() == R.id.action_message) {
//
//            if (pref.getNotifyStatus() == null) {
//                getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1, "TrackFrag").addToBackStack("TrackFrag").commit();
//                pref.setNotifyStatus("1");
//                Log.e("1", "1");
//            } else {
//
//                //removing the fragment on the top of stack in fragment container
//                getSupportFragmentManager().popBackStack();
//                pref.setNotifyStatus(null);
//                Log.e("Null", "Null");
//            }
//        }

        return true;
    }

    @Override
    protected void onStop() {
        pref.setNotifyStatus(null);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

