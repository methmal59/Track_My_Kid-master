package com.project.dreamsquad.trackmykid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.dreamsquad.trackmykid.activity.MainActivity;
import com.project.dreamsquad.trackmykid.fragments.PickUp;
import com.project.dreamsquad.trackmykid.models.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DropOff extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng latitudeLogitude;
    private MarkerOptions markerOptions = new MarkerOptions();
    private String placeName;
    private Marker marker;
    private Button saveDropLocation;
    private AlertDialog.Builder builder;
    private UserProfile userProfile;
    private Button pickUpButton;

    private String getFenceLocation;
    private double dropoffDistanceAttribute;
    private Boolean isaNewUser;
    OkHttpClient client = new OkHttpClient();
//    AutocompleteSupportFragment placeAutoComplete;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_drop_off, container, false);
        //new GetSavedLocationn().execute("");
//        new GetDropoffNotificationDistance().execute("");
        System.out.println("Called api calls");

        saveDropLocation = (Button) v.findViewById(R.id.saveDropLocation);
        pickUpButton = (Button) v.findViewById(R.id.pick_upBtn);

//        placeAutoComplete = (AutocompleteSupportFragment) getFragmentManager().findFragmentById(R.id.place_autocompleteDropp);
//        placeAutoComplete.setCountry("LK");
//        placeAutoComplete.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
//                latitudeLogitude = place.getLatLng();
//                System.out.println("Lattidue at top " + latitudeLogitude.latitude+ ":" + latitudeLogitude.longitude);
//                placeName = place.getName().toString();
//                System.out.println("slected " + placeName);
//                goToSelectedLocation();
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//                Log.d("Maps", "An error occurred: " + status);
//                System.out.println("Error at selecting a place  " +status);
//            }
//        });




        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapDropOff);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        saveDropLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(marker == null){
                    Toast.makeText(getActivity(), "Please select a drop off location", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (latitudeLogitude == null)
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    else {


                        CameraUpdate center =
                                CameraUpdateFactory.newLatLng(new LatLng(latitudeLogitude.latitude, latitudeLogitude.longitude));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);

                        userProfile = UserProfile.getInstance();
                        userProfile.setDropOffLocation(latitudeLogitude);
                        System.out.println("saved the drop off location as: " + latitudeLogitude);
                        //Toast.makeText(DropOff.this, "Saved the locations succesfully", Toast.LENGTH_LONG);
                        System.out.println("Status of is a new user " + isaNewUser);

                        userProfile.setDropoffLat(latitudeLogitude.latitude + "");
                        userProfile.setDropoffLong(latitudeLogitude.longitude + "");


                        if (userProfile.isDropoffGeofenceExists())
                            new UpdateDropoffGeoLocation().execute("");
                        else
                            new CreateDropoffGeoLocation().execute("");

                        if (userProfile.getDropoffNotificationId() == 0)
                            new CreateDropoffNotification().execute("");

                        new UpdateAttributes().execute("");


                        Toast.makeText(getContext(), R.string.dropoff_location_saved, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), MainActivity.class));

                    }

                }
            }
        });

        pickUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickUp fragment = new PickUp();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        return v;
    }

    public void onDestroy() {
        super.onDestroy();
//        if(placeAutoComplete != null && getActivity() != null && !getActivity().isFinishing()) {
//            getFragmentManager().beginTransaction().remove(placeAutoComplete).commit();
//        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }else
            mMap.setMyLocationEnabled(true);

        LocationManager lm = (LocationManager)
                getContext().getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        if (!gps_enabled ) {
            new AlertDialog.Builder(getContext())
                    .setMessage("Please enable location services")
                    .setPositiveButton("Settings", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .show();

        }


        LatLng ll = new LatLng(Double.parseDouble(UserProfile.getInstance().getDropoffLat()), Double.parseDouble(UserProfile.getInstance().getDropoffLong()));

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(ll, 14.0f) );
        marker = mMap.addMarker(new MarkerOptions()
                .position(ll)
                .draggable(true));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitudeLogitude = latLng;
                if(marker!=null)
                    marker.remove();

                marker = mMap.addMarker(new MarkerOptions()
                        .position(latitudeLogitude)
                        .draggable(true));

            }
        });


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                System.out.println("Dragging started");
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }
        });
//        enableCurrentLocation();

    }




    public void enableCurrentLocation() {
        System.out.println("Inside add markers");
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            System.out.println("Permission is already given");
            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location1 = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));

            if(location1 != null) {
                double latitude = location1.getLatitude();
                double longitude = location1.getLongitude();
                System.out.println(latitude);
                System.out.println(longitude);
//                latitudeLogitude = new LatLng(latitude, longitude);
//                if (marker != null)
//                    marker.remove();


            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                {
                    boolean showRationale = shouldShowRequestPermissionRationale( permissions[0] );
                    if (! showRationale) {
                        new AlertDialog.Builder(getContext())
                                .setMessage("Location services should be enabled to set the dropoff location")
                                .setPositiveButton("App settings", new
                                        DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            }
                                        })
                                .show();
                    }else {
                        new AlertDialog.Builder(getContext())
                                .setMessage("Location services should be enabled to set the dropoff location")
                                .setPositiveButton("Ok", new
                                        DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                                            }
                                        })
                                .show();
                    }
                    return;
                }
            } else {

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                LocationManager lm = (LocationManager)
                        getContext().getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!gps_enabled) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Please enable location services")
                            .setPositiveButton("Settings", new
                                    DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    })
                            .show();

                }

                LatLng ll = new LatLng(Double.parseDouble(UserProfile.getInstance().getDropoffLat()), Double.parseDouble(UserProfile.getInstance().getDropoffLong()));

                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(ll, 14.0f) );
                marker = mMap.addMarker(new MarkerOptions()
                        .position(ll)
                        .draggable(true));

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        latitudeLogitude = latLng;
                        if (marker != null)
                            marker.remove();

                        marker = mMap.addMarker(new MarkerOptions()
                                .position(latitudeLogitude)
                                .draggable(true));
                        System.out.println("Selected location: " + latitudeLogitude);

                    }
                });
            }
        }


    }

    public void goToSelectedLocation(){
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(latitudeLogitude.latitude, latitudeLogitude.longitude));
        System.out.println("Lattidue at bottom " + latitudeLogitude.latitude +":" + latitudeLogitude.longitude);

        CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        if(marker !=  null)
            marker.remove();

        marker = mMap.addMarker(new MarkerOptions()
                .position(latitudeLogitude)
                .draggable(true));

//        mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
    }


//    private class GetDropoffNotificationDistance extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            System.out.println("Inside drop off notification distance");
//            double distance = userProfile.getDropOffReminderDistance() * 1000.0;
//
//            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
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
//                    if (description.contains("Dropoff Notification Distance")) {
//                        dropoffDistanceAttribute = Double.parseDouble(rec.getString("attribute"));
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return "Executed";
//        }
//
//    }

    private class CreateDropoffGeoLocation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            int calenderId = 0;
            double distance = userProfile.getDropOffReminderDistance() * 1000.0;

            String pickupGeofenceName = UserProfile.getInstance().getLoggedUserName() + "'s Dropoff geo fence";
            String area = "CIRCLE (" + latitudeLogitude.latitude + " " + latitudeLogitude.longitude + ", " + distance + ")";

            System.out.println("Inside create pickup geo location");
            MediaType mediaType = MediaType.parse("application/json");
            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, "{\n\t\n    \t\"calendarId\":\"" + calenderId + "\",\n        \"name\": \"" + pickupGeofenceName + "\",\n        \"area\": \"" + area + "\"\n        \n    }\n    \n   ");
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/geofences")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Response coee" + response.code());
                if(response.code() == 200) {
                    userProfile.setDropoffGeofenceExists(true);
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    System.out.println("Drop off Geo Fence Id is: " + jsonObject.getInt("id"));
                    userProfile.setDropoffGeofenceId(jsonObject.getInt("id"));
                }
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Unable to save the location, please try again later", Toast.LENGTH_LONG).show();

                System.out.println("Exception at Pickup");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            System.out.println("End of assign device");
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new LinkDeviceAndGeofence().execute("");

        }
    }

    private class CreateDropoffNotification extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            int calenderId = 0;
            String dropNotificationName = UserProfile.getInstance().getLoggedUserName() + "'s Dropoff Notification";

            MediaType mediaType = MediaType.parse("application/json");
            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body;
            if(userProfile.isDropOffNotificationEnabled())
                body = RequestBody.create(mediaType, "{\n        \"attributes\": {\n        \t\"name\" : \""+dropNotificationName+"\"\n        },\n        \"calendarId\": \""+userProfile.getDropoffCalendarId()+"\",\n        \"always\": true,\n        \"type\": \"geofenceEnter\",\n        \"notificators\":\"firebase,web\"}");
            else
                body = RequestBody.create(mediaType, "{\n        \"attributes\": {\n        \t\"name\" : \""+dropNotificationName+"\"\n        },\n        \"calendarId\": \""+userProfile.getDropoffCalendarId()+"\",\n        \"always\": true,\n        \"type\": \"geofenceEnter\",\n        \"notificators\":\"web\"}");

            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/notifications")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                System.out.println("Dropoff Notification created succesfully");
                System.out.println("Response coee" + response.code());
                JSONObject someObject = new JSONObject(response.body().string());
                int id = someObject.getInt("id");
                System.out.println("Id is " + id);
                userProfile.setDropoffNotificationId(id);
                System.out.println("Drop Notification created succesfully");
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Unable to create the dropoff notication, please try again later", Toast.LENGTH_LONG).show();

                System.out.println("Exception at Pickup");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("End of assign device");
            return "Executed";
        }

    }

    private class UpdateDropoffGeoLocation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            int calenderId = 0;
            double distance = userProfile.getDropOffReminderDistance() * 1000.0;

            String pickupGeofenceName = UserProfile.getInstance().getLoggedUserName() + "'s Dropoff geo fence";
            String area = "CIRCLE (" + latitudeLogitude.latitude + " " + latitudeLogitude.longitude + ", " + distance + ")";

            System.out.println("Inside update pickup geo location");
            MediaType mediaType = MediaType.parse("application/json");

            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            System.out.println(UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword());

            int geoFenceId = userProfile.getDropoffGeofenceId();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, " {\n        \"id\": \""+geoFenceId+"\",\n        \"calendarId\": 0,\n        \"name\": \""+pickupGeofenceName+"\",\n        \"area\": \""+area+"\"\n    }");
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/geofences/"+ geoFenceId)
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();


            try {
                Response response = client.newCall(request).execute();
                System.out.println("Geo Fence updated succesfully");
                System.out.println("Resposne oce " + response.code());
                System.out.println(response.body().string());
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Unable to save the location, please try again later", Toast.LENGTH_LONG).show();


                System.out.println("Exception at Pickup");
                e.printStackTrace();
            }

            System.out.println("End of assign device");
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new LinkDeviceAndGeofence().execute("");

        }
    }

    private class LinkDeviceAndGeofence extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            int geoFenceId = userProfile.getDropoffGeofenceId();
            int deviceId = userProfile.getDeviceId();
            MediaType mediaType = MediaType.parse("application/json");

            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            RequestBody body = RequestBody.create(mediaType, "{\n        \"deviceId\" : \""+ deviceId+"\",\n        \"geofenceId\" : \""+geoFenceId+"\"\n      \n      \n    \n    }");
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/permissions")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Resposne code of linking the geofence and the device " + response.code());
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Unable to save the location, please try again later", Toast.LENGTH_LONG).show();


                System.out.println("Exception at Pickup");
                e.printStackTrace();
            }

            System.out.println("End of assign device");
            return "Executed";
        }
    }

    private class UpdateAttributes extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            MediaType mediaType = MediaType.parse("application/json");
            System.out.println(UserProfile.getInstance().getLoggedUserName() + "    " + UserProfile.getInstance().getLoggedPassword());
            String credentials = userProfile.getMainCredentials();

            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            int attributeId = -1;
            String description = "";
            String attribute = "";

            for (int counter = 0; counter < 2; counter++) {

                if (counter == 0) {
                    attributeId = userProfile.getDropofflatID();
                    description = userProfile.getLoggedUserName() + "'s Dropoff lat: ";
                    attribute = latitudeLogitude.latitude + "";
                } else if (counter == 1) {
                    attributeId = userProfile.getDropofflongID();
                    description = userProfile.getLoggedUserName() + "'s Dropoff long: ";
                    attribute = latitudeLogitude.longitude + "";
                }


                    RequestBody body = RequestBody.create(mediaType, "{\n\"id\":\"" + attributeId + "\",\n        \"description\": \"" + description + "\",\n        " +
                            "\"attribute\":   \"" + attribute + "\",\n        " +
                            "\"expression\": \"NIL\",\n        " +
                            "\"type\": \"string\"\n" +
                            "    }\n");

                    Request request = new Request.Builder()
                            .url(userProfile.getUrl() + "/api/attributes/computed/" + attributeId)
                            .put(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .addHeader("Authorization", basic)
                            .build();
                    try {
                        okhttp3.Response response = client.newCall(request).execute();
                        System.out.println(response.code());
                        System.out.println(response.body().string());
                    } catch (Exception e) {
                        System.out.println("Exception at assigning attributes 282");
                        e.printStackTrace();
                        return "Failed";
                    }
                }
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//                    Toast.makeText(getActivity(), "Data saved successfully!", Toast.LENGTH_LONG).show();
//                }
//            });

            return "Executed";


        }}
}
