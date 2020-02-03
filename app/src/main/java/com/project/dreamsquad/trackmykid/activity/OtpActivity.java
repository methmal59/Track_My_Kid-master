package com.project.dreamsquad.trackmykid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.project.dreamsquad.trackmykid.R;

/**
 * Created by this pc on 11-05-17.
 */

public class OtpActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Toolbar toolbar;
    ImageView back;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        fab=(FloatingActionButton)findViewById(R.id.fab2);
        toolbar=(Toolbar)findViewById(R.id.title_bar);
        back=(ImageView)toolbar.findViewById(R.id.back_button);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent back to sign in activity
                startActivity(new Intent(OtpActivity.this,SignInActivity.class));
                finish();
            }
        });



//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //intent to main activity
//                startActivity(new Intent(OtpActivity.this,MainActivity.class));
//                finish();
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //intent to main activity
                startActivity(new Intent(OtpActivity.this,LoginActivity.class));
                finish();
            }
        });

    }
}
