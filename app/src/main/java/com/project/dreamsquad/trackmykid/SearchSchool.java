package com.project.dreamsquad.trackmykid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.project.dreamsquad.trackmykid.activity.MainActivity;
import com.project.dreamsquad.trackmykid.models.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class SearchSchool extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button saveSchoolLocation;
    UserProfile userProfile = UserProfile.getInstance();
    OkHttpClient client = new OkHttpClient();

    private int kidSchoolAttributeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_school);

        saveSchoolLocation = (Button) findViewById(R.id.saveSchoolLocation);

//        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
//        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                Log.d("Maps", "Place selected: " + place.getName());
//                System.out.println(place.getName().toString());
//                UserProfile.getInstance().setKidSchool(place.getName().toString());
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.d("Maps", "An error occurred: " + status);
//                System.out.println("Error at selecting a place");
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        saveSchoolLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetData().execute("");
                new UpdateAttribute().execute("");

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String credentials = userProfile.getLoggedUserName() + ":" + userProfile.getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/attributes/computed")
                    .get()
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                System.out.println(responseBody);
                JSONArray jsonArray = new JSONArray(responseBody);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject rec = jsonArray.getJSONObject(i);
                    String description = rec.getString("description");
                    System.out.println("Description Name: " + description);
                    if (description.contains("Kids School")) {
                        kidSchoolAttributeId = rec.getInt("id");
                        System.out.println("Kids School attribute id: " + kidSchoolAttributeId);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Executed";
        }
    }

    private class UpdateAttribute extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("Assign Attributes called");

            MediaType mediaType = MediaType.parse("application/json");
            String credentials = userProfile.getLoggedUserName() + ":" + userProfile.getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            int attributeId = -1;
            String description = "";
            String attribute = "";

            description = userProfile.getLoggedUserName() + "'s Kids School: ";
            attribute = userProfile.getKidSchool();

            RequestBody body = RequestBody.create(mediaType, "{\n\"id\":\"" + kidSchoolAttributeId + "\",\n        \"description\": \"" + description + "\",\n        " +
                    "\"attribute\":   \"" + attribute + "\",\n        " +
                    "\"expression\": \"NIL\",\n        " +
                    "\"type\": \"string\"\n" +
                    "    }\n");

            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/attributes/computed/" +kidSchoolAttributeId)
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", basic)
                    .build();
            try {
                    okhttp3.Response response = client.newCall(request).execute();
                System.out.println("Updated the attribute");
                    System.out.println(response.code());
                } catch (Exception e) {
                    System.out.println("Exception at assigning school name");
                    e.printStackTrace();
                    return "Failed";
                }
            return "Executed";

        }}

    }

