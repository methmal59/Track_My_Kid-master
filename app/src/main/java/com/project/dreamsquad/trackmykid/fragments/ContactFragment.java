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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.dreamsquad.trackmykid.R;

/**
 * Created by this pc on 27-05-17.
 */

public class ContactFragment extends Fragment {

    private View view;
    private TextView dreamSquadContact;
    private TextView subject;
    private TextView message;
    private Button submit;
    private LinearLayout contactDreamSquadlayout;
    private LinearLayout dreamSquadweblayout;

    private String subjectText;
    private String messageText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaznceState) {
        view=inflater.inflate(R.layout.contactus,container,false);

        dreamSquadContact = (TextView) view.findViewById(R.id.contactDreamSquad);
        subject = (TextView) view.findViewById(R.id.subject);
        message = (TextView) view.findViewById(R.id.message);
        submit = (Button) view.findViewById(R.id.submit);
        contactDreamSquadlayout = (LinearLayout) view.findViewById(R.id.contactSquadLayout);
        dreamSquadweblayout = (LinearLayout) view.findViewById(R.id.dreamSquadWebLayout);

        contactDreamSquadlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDreamSquadGroup();
            }
        });

        dreamSquadweblayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWebPage();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectText = subject.getText().toString();
                messageText = message.getText().toString();

                if(messageText.isEmpty()) {
                    Toast.makeText(getContext(), "Message body can't be empty, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                emailDreamSquad();
            }
        });

        return view;
    }

    public void callDreamSquadGroup(){
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
                callIntent.setData(Uri.parse("tel:" + dreamSquadContact.getText().toString()));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("tel:" + dreamSquadContact.getText().toString()));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void emailDreamSquad(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@dreamsquadgroup.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, subjectText);
        i.putExtra(Intent.EXTRA_TEXT   , messageText);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Failed to send the email, please try again later!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToWebPage(){
        String url = "http://www.dreamsquadgroup.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
