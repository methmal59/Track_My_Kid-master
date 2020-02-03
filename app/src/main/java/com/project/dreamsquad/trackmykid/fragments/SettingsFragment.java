package com.project.dreamsquad.trackmykid.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.activity.MainActivity;
import com.project.dreamsquad.trackmykid.models.UserProfile;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by this pc on 11-05-17.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private View view;
    TextView select,pickup_change,drop_change,unselect, reminder1, reminder2;
    CheckBox checkBox1,checkBox2,checkBox3,checkBox4;
    AlertDialog alertDialog;
    LayoutInflater li;
    View promptsView;
    AlertDialog.Builder alertDialogBuilder;
    UserProfile userProfile;
    Button saveNotification;
    private double pickupDistance =0;
    private double dropoffDistance = 0;


    private double pickupDistanceattributee;
    private int pickupDistanceIDattribute;
    private double dropoffDistanceattributee;
    private int dropoffDistanceIDattribute;
    private String pickupEnabledattribute;
    private int pickupEnabledIDattribute;
    private String dropoffEnabledattribute;
    private int dropoffEnabledIDattribute;
    private int schoolAttributeId;
    private String schoolEnabledAttribute;
    OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.settings_layout,container,false);
        userProfile = UserProfile.getInstance();
        select=(TextView)view.findViewById(R.id.select_all);
        checkBox1=(CheckBox)view.findViewById(R.id.pickupbox);
        checkBox2=(CheckBox)view.findViewById(R.id.dropbox);
        checkBox3=(CheckBox)view.findViewById(R.id.reachedbox);
       // checkBox4=(CheckBox)view.findViewById(R.id.leftbox);
        pickup_change=(TextView)view.findViewById(R.id.pickup_change);
        drop_change=(TextView)view.findViewById(R.id.drop_change);
        unselect=(TextView)view.findViewById(R.id.unselect_all);
        reminder1 = (TextView) view.findViewById(R.id.reminder_content_setText1);
        reminder2 = (TextView) view.findViewById(R.id.reminder_contentSetText2);
        saveNotification = (Button) view.findViewById(R.id.saveNotification);
        setData();

        select.setOnClickListener(this);
        unselect.setOnClickListener(this);
        pickup_change.setOnClickListener(this);
        drop_change.setOnClickListener(this);



//            reminder1.setText("Currently " + Integer.parseInt(pickupDistanceattribute) / 1000 + "km before pick up spot");
//            reminder2.setText("Currently " + Integer.parseInt(dropoffDistanceattribute) / 1000 + "km before drop spot");


        saveNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox1.isChecked() == true){
                    userProfile.setPickNotificationEnabled(true);
                    pickupEnabledattribute = "true";
                    System.out.println("Pickup enabled");
                }else{
                    pickupEnabledattribute = "false";
                    userProfile.setPickNotificationEnabled(false);
                    System.out.println("Pick notification disabled");
                }

                if(checkBox2.isChecked() == true){
                    dropoffEnabledattribute = "true";
                    userProfile.setDropOffNotificationEnabled(true);
                    System.out.println("Dropp offf notification enabled");
                }else{
                    dropoffEnabledattribute = "false";
                    userProfile.setDropOffNotificationEnabled(false);
                    System.out.println("Drop off notification disabled");
                }

                if(checkBox3.isChecked() == true){
                    schoolEnabledAttribute = "true";
                    userProfile.setSchoolNotificationEnabled(true);
                    System.out.println("School notiifcation enabled");
                }else{
                    schoolEnabledAttribute = "false";
                    userProfile.setSchoolNotificationEnabled(false);
                    System.out.println("School notification disabled");
                }
                new UpdateNotificationStatus().execute("");
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        return view;
    }

    public void setData(){
        System.out.println("Writing " + userProfile.getPickUpReminderDistance());
        reminder1.setText("Currently " + userProfile.getPickUpReminderDistance() + "km before pick up spot");
        reminder2.setText("Currently " + userProfile.getDropOffReminderDistance() + "km before drop spot");
        if(userProfile.isPickNotificationEnabled())
            checkBox1.setChecked(true);
        else
            checkBox1.setChecked(false);

        if(userProfile.isDropOffNotificationEnabled())
            checkBox2.setChecked(true);
        else
            checkBox2.setChecked(false);
        if(userProfile.isSchoolNotificationEnabled())
            checkBox3.setChecked(true);
        else
            checkBox3.setChecked(false);
        }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.select_all:

                checkBox1.setChecked(true);
                checkBox2.setChecked(true);
                checkBox3.setChecked(true);
//                checkBox4.setChecked(true);
                select.setVisibility(View.GONE);
                unselect.setVisibility(View.VISIBLE);
//                userProfile.setPickNotificationEnabled(true);
//                userProfile.setDropOffNotificationEnabled(true);
//                System.out.println("All notifications enabled");
                break;

            case R.id.unselect_all:

                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
//                checkBox4.setChecked(false);
                unselect.setVisibility(View.GONE);
                select.setVisibility(View.VISIBLE);
//                userProfile.setPickNotificationEnabled(false);
//                userProfile.setDropOffNotificationEnabled(false);
//                System.out.println("All notoifcation disabled");
//                System.out.println(userProfile.isPickNotificationEnabled());
                break;

            case R.id.pickup_change:

                //alert dialog to set pick up reminder
                li = LayoutInflater.from(getActivity());
                promptsView = li.inflate(R.layout.pickup_dialog, null);
                Button skip = (Button) promptsView.findViewById(R.id.skipPickUpDistance);
                Button setPickUp = (Button) promptsView.findViewById(R.id.setPickUpDistance);
                final TextView distance=(TextView)promptsView.findViewById(R.id.distance);
                ImageView add=(ImageView)promptsView.findViewById(R.id.addPickup);
                ImageView sub=(ImageView)promptsView.findViewById(R.id.subPickup);
                distance.setText(userProfile.getPickUpReminderDistance()+" km");
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        double d;
                        String a[]=distance.getText().toString().split(" ");
                        d=Double.parseDouble(a[0]);
                        pickupDistance=d+0.5;
                        distance.setText(pickupDistance+" km");
                        pickupDistanceattributee = pickupDistance;

                    }
                });

                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        System.out.println("insdie sub on click");
                        double d;
                        String a[]=distance.getText().toString().split(" ");
                        d=Double.parseDouble(a[0]);
                        if(d>=1.0)
                            pickupDistance=d-0.5;
                        System.out.println("pickup distance  " + pickupDistance);
                        distance.setText(pickupDistance+" km");
                        pickupDistanceattributee = pickupDistance;


                    }
                });
                skip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });

                setPickUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Remind with " + pickupDistanceattributee + "km to pickup");
                        userProfile.setPickUpReminderDistance(pickupDistanceattributee);
                        reminder1.setText("Currently " + pickupDistanceattributee + "km before pick up spot");
                        new UpdatePickupDistance().execute("");
                        alertDialog.dismiss();
                    }
                });

                alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                alertDialogBuilder.setView(promptsView);
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;

            case R.id.drop_change:

                //alert dialog to set drop reminder

                li = LayoutInflater.from(getActivity());
                promptsView = li.inflate(R.layout.dropdialog_layout, null);
                Button skipd = (Button) promptsView.findViewById(R.id.skipDropoff);
                Button setDropOff = (Button) promptsView.findViewById(R.id.setDropoff);
                final TextView distance1=(TextView)promptsView.findViewById(R.id.distance);
                ImageView add1=(ImageView)promptsView.findViewById(R.id.addDropoff);
                ImageView sub1=(ImageView)promptsView.findViewById(R.id.subDropoff);
                distance1.setText(userProfile.getDropOffReminderDistance()+" km");

                add1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        double d;
                        String a[]=distance1.getText().toString().split(" ");
                        d=Double.parseDouble(a[0]);
                        dropoffDistance=d+0.5;
                        distance1.setText(dropoffDistance+" km");
                        dropoffDistanceattributee = dropoffDistance;

                    }
                });

                sub1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        double d;
                        String a[]=distance1.getText().toString().split(" ");
                        d=Double.parseDouble(a[0]);
                        if(d>=1.0)
                            dropoffDistance=d-0.5;
                        distance1.setText(dropoffDistance+" km");
                        dropoffDistanceattributee = dropoffDistance;

                    }
                });
                skipd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });

                setDropOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Remind with " + dropoffDistanceattributee + "km to dropoff");
                        userProfile.setDropOffReminderDistance(dropoffDistanceattributee);
                        reminder2.setText("Currently " + dropoffDistanceattributee + "km before drop spot");
                        new UpdateDropoffDistance().execute("");
                        alertDialog.dismiss();
                    }
                });

                alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                alertDialogBuilder.setView(promptsView);
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

        }
    }

//    private class GetAttributeInfo extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            reminder1.setText("Currently " + pickupDistanceattributee + "km before pick up spot");
//            reminder2.setText("Currently " + dropoffDistanceattributee + "km before drop spot");
//            if(userProfile.isPickNotificationEnabled())
//                checkBox1.setChecked(true);
//            else
//                checkBox1.setChecked(false);
//
//            if(userProfile.isDropOffNotificationEnabled())
//                checkBox2.setChecked(true);
//            else
//                checkBox2.setChecked(false);
//            if(userProfile.isSchoolNotificationEnabled())
//                checkBox3.setChecked(true);
//            else
//                checkBox3.setChecked(false);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
//            System.out.println();
//            final String basic =
//                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//
//            Request request = new Request.Builder()
//                    .url("http://85.5.55.236:8082/api/attributes/computed")
//                    .get()
//                    .addHeader("Authorization", basic)
//                    .addHeader("cache-control", "no-cache")
//                    .build();
//
//            try {
//                okhttp3.Response response = client.newCall(request).execute();
//                String responseBody = response.body().string();
//                JSONArray jsonArray = new JSONArray(responseBody);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject rec = jsonArray.getJSONObject(i);
//                    String description = rec.getString("description");
//                    if (description.contains("Pickup Notification Distance")) {
//                        pickupDistanceattributee =  Double.parseDouble(rec.getString("attribute")) / 1000.0;
//                        pickupDistanceIDattribute = rec.getInt("id");
//                        System.out.println("Pickup");
//                        System.out.println(pickupDistanceIDattribute);
//                    } else if (description.contains("Dropoff Notification Distance")) {
//                        dropoffDistanceIDattribute = rec.getInt("id");
//                        dropoffDistanceattributee = Double.parseDouble(rec.getString("attribute")) / 1000.0;
////                    }else if (description.contains("Pickup Notification Enabled")) {
////                        pickupEnabledIDattribute = rec.getInt("id");
////                        pickupEnabledattribute = rec.getString("attribute");
////                        System.out.println("pickup status " + pickupEnabledattribute);
////                    }else if (description.contains("Dropoff Notification Enabled")) {
////                        dropoffEnabledIDattribute = rec.getInt("id");
////                        dropoffEnabledattribute = rec.getString("attribute");
////                        System.out.println("pickup status " + dropoffEnabledattribute);
//
//                    }
//                }
//            } catch (IOException e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(getActivity(), "Error Opening the window, Please try again later!", Toast.LENGTH_LONG).show();
//                    }
//                });
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            return "Executed";
//        }
//
//    }

    private class UpdatePickupDistance extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("Assign Attributes called");

            MediaType mediaType = MediaType.parse("application/json");
//            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            String credentials = userProfile.getMainCredentials();

            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            System.out.println("Creditials " + UserProfile.getInstance().getLoggedUserName() + "   " + UserProfile.getInstance().getLoggedPassword());

            System.out.println("printing inside assign attributes");

            int attributeId = userProfile.getPickupReminderDistanceAttributeId();
            String description = UserProfile.getInstance().getLoggedUserName() + "'s Pickup Notification Distance: ";
            double attribute = pickupDistanceattributee * 1000;
            System.out.println("Saved in the server as " + pickupDistance + "x1000") ;

            RequestBody body = RequestBody.create(mediaType, "{\n\"id\":\"" + attributeId + "\",\n        \"description\": \"" + description + "\",\n        " +
                    "\"attribute\":   \"" + attribute + "\",\n        " +
                    "\"expression\": \"NIL\",\n        " +
                    "\"type\": \"string\"\n" +
                    "    }\n");

            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/attributes/computed/" + attributeId)
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", basic)
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                System.out.println("Printing results");
                System.out.println(response.code());
                System.out.println(response.body());
                System.out.println(response.body().string());
                } catch (Exception e) {
                System.out.println("Exception at updating pickup distance");
                getActivity().runOnUiThread(new Runnable() {
                        public void run() { Toast.makeText(getActivity(), "Error saving data, Please try again later!", Toast.LENGTH_LONG).show(); }
                    });
                    e.printStackTrace();
                    return "Failed";
                }

            System.out.println("Success");
            return "Executed";
        }

    }

    private class UpdateDropoffDistance extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            System.out.println("Drop off distance called");

            MediaType mediaType = MediaType.parse("application/json");
            String credentials = userProfile.getMainCredentials();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            System.out.println("printing inside assign attributes");

            int attributeId = userProfile.getDropoffReminderDistanceAttributeId();
            String description = UserProfile.getInstance().getLoggedUserName() + "'s Dropoff Notification Distance: ";
            double attribute = dropoffDistance * 1000.0;

            RequestBody body = RequestBody.create(mediaType, "{\n\"id\":\"" + attributeId + "\",\n        \"description\": \"" + description + "\",\n        " +
                    "\"attribute\":   \"" + attribute + "\",\n        " +
                    "\"expression\": \"NIL\",\n        " +
                    "\"type\": \"string\"\n" +
                    "    }\n");

            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/attributes/computed/" + attributeId)
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", basic)
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                System.out.println(response.code());
            } catch (Exception e) {
                System.out.println("Exception at updating pickup distance");
                getActivity().runOnUiThread(new Runnable() {
                    public void run() { Toast.makeText(getActivity(), "Error saving data, Please try again later!", Toast.LENGTH_LONG).show(); }
                });
                e.printStackTrace();
                return "Failed";
            }

            System.out.println("Success");
            return "Executed";
        }

    }

    private class UpdateNotificationStatus extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            System.out.println("Update notification status called");

            MediaType mediaType = MediaType.parse("application/json");
            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            System.out.println("Not Cred:  "+credentials);
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            System.out.println("printing inside update nofitications");
            int attributeId = 0;
            String description = "";
            String attribute = "";
            int calendarId = -1;

            for(int count = 0; count < 3; count++) {

                if(count == 0) {
                    System.out.println("Inside pickup");
                    attributeId = userProfile.getPickupNotificationId();
                    description = UserProfile.getInstance().getLoggedUserName() + "'s Pickup Notification: ";
                    attribute = pickupEnabledattribute;
                    calendarId = userProfile.getPickupCalendarId();
                }else if(count == 1){
                    System.out.println("Inside dropoff");
                    attributeId = userProfile.getDropoffNotificationId();
                    description = UserProfile.getInstance().getLoggedUserName() + "'s Dropoff Notification: ";
                    attribute = dropoffEnabledattribute;
                    calendarId = userProfile.getDropoffCalendarId();
                }else if(count == 2){
                    System.out.println("Inside droasdpoff");
                    attributeId = userProfile.getSchoolNotificationId();
                    description = UserProfile.getInstance().getLoggedUserName() + "'s School Notification: ";
                    attribute = dropoffEnabledattribute;
                    calendarId = userProfile.getSchoolCalendarId();
                }

                RequestBody body;
                if(attribute.equals("true"))
                    body = RequestBody.create(mediaType, " {\n        \"id\":\""+ attributeId+"\",\n        \"attributes\": {\n            \"name\": \""+description+"\"\n        },\n        \"calendarId\": \""+calendarId+"\",\n        \"always\": true,\n        \"type\": \"geofenceEnter\",\n        \"notificators\":\"firebase,web\"   }");
                else
                    body = RequestBody.create(mediaType, " {\n        \"id\":\""+ attributeId+"\",\n        \"attributes\": {\n            \"name\": \""+description+"\"\n        },\n        \"calendarId\": \""+calendarId+"\",\n        \"always\": true,\n        \"type\": \"geofenceEnter\",\n        \"notificators\":\"web\"  }");

                Request request = new Request.Builder()
                        .url(userProfile.getUrl()+"/api/notifications/"+attributeId)
                        .put(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", basic)
                        .addHeader("cache-control", "no-cache")
                        .build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    System.out.println("Attr id: " + attributeId);
                    System.out.println(response.code());
                    System.out.println(response.body().string());
                } catch (Exception e) {
                    System.out.println("Exception at updating pickup distance");
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "Error saving data, Please try again later!", Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                    return "Failed";
                }
            }

            System.out.println("Success");
            return "Executed";
        }
    }
}
