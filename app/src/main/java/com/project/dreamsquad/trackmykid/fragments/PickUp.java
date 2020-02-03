package com.project.dreamsquad.trackmykid.fragments;

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

import com.project.dreamsquad.trackmykid.DropOff;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PickUp extends Fragment implements OnMapReadyCallback {
    //    private AutocompleteSupportFragment placeAutoComplete;
    private GoogleMap mMap;
    private LatLng latitudeLogitude;
    private MarkerOptions markerOptions = new MarkerOptions();
    private String placeName;
    private Marker marker;
    private Button saveSchoolLocation;
    private AlertDialog.Builder builder;
    private UserProfile userProfile;
    private Button dropButton;

    private String getFenceLocation;
    private double pickupDistanceattribute;
    OkHttpClient client = new OkHttpClient();
    private boolean isaNewUser;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_pick_up, container, false);
        System.out.println("Clas seka call wenawada");
//        new GetSavedLocation().execute("");
        //   new GetPickupNotificationDistance().execute("");
        saveSchoolLocation = (Button) v.findViewById(R.id.savePickLocation);
        dropButton = (Button) v.findViewById(R.id.dropBtn);


//        placeAutoComplete = (AutocompleteSupportFragment) getFragmentManager().findFragmentById(R.id.place_autocompleteDropp);
//        placeAutoComplete.setCountry("LK");
//        placeAutoComplete.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
//                latitudeLogitude = place.getLatLng();
//                System.out.println("Lattidue at top " + latitudeLogitude.latitude +":" + latitudeLogitude.longitude);
//                placeName = place.getName().toString();
//                goToSelectedLocation();
//                System.out.println("PLace : " + placeName);
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//                Log.d("Maps", "An error occurred: " + status);
//                System.out.println("Error at selecting a place  " +status);
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapPickUp);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        saveSchoolLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker == null) {
                    Toast.makeText(getActivity(), "Please select a pickup location", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (latitudeLogitude == null) {
                        DropOff fragment = new DropOff();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commitAllowingStateLoss();
                    } else {
                        CameraUpdate center =
                                CameraUpdateFactory.newLatLng(new LatLng(latitudeLogitude.latitude, latitudeLogitude.longitude));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);

                        userProfile = UserProfile.getInstance();
                        userProfile.setPickUpLocation(latitudeLogitude);
                        System.out.println("saved the pickup location as: " + latitudeLogitude);

                        userProfile.setPickupLat(latitudeLogitude.latitude + "");
                        userProfile.setPickupLong(latitudeLogitude.longitude + "");

                        System.out.println("saving location: " + latitudeLogitude);
                        System.out.println("Is a new user " + isaNewUser);

                        if (userProfile.isPickupGeofenceExists()) {
                            new UpdatePickupGeoLocation().execute("");
                        } else
                            new CreatePickupGeoLocation().execute("");

                        new UpdateAttributes().execute("");

                        System.out.println("Value of pickup notf ID is " + userProfile.getPickupNotificationId());
                        if (userProfile.getPickupNotificationId() == 0)
                            new CreatePickupNotification().execute("");


                        Toast.makeText(getContext(), R.string.pickup_location_saved, Toast.LENGTH_SHORT).show();

                        DropOff fragment = new DropOff();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commitAllowingStateLoss();
                    }


                }
            }
        });

        dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropOff fragment = new DropOff();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commitAllowingStateLoss();


            }
        });

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                return;

        } else
            System.out.println("Permission accepted at 1");
        System.out.println("Location services onnnnnn");

        mMap.setMyLocationEnabled(true);

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

        LatLng ll = new LatLng(Double.parseDouble(UserProfile.getInstance().getPickupLat()), Double.parseDouble(UserProfile.getInstance().getPickupLong()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 14.0f));
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

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        System.out.println("Location services on");

        Criteria criteria = new Criteria();

        Location location1 = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        System.out.println("Printing");

        if (location1 != null) {
            double latitude = location1.getLatitude();
            double longitude = location1.getLongitude();
            System.out.println(latitude);
            System.out.println(longitude);
//                latitudeLogitude = new LatLng(latitude, longitude);
//                if (marker != null)
//                    marker.remove();


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(placeAutoComplete != null && getActivity() != null && !getActivity().isFinishing()) {
//            getFragmentManager().beginTransaction().remove(placeAutoComplete).commit();
//        }
    }


    public void enableCurrentLocation() {
        System.out.println("Inside add markers");
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            System.out.println("Permission denied at 2");
//
//            return;
//        } else {
//            System.out.println("Permission granted at 2");
//
//            mMap.setMyLocationEnabled(true);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                {
                    boolean showRationale = shouldShowRequestPermissionRationale( permissions[0] );
                    if (! showRationale) {
                        new AlertDialog.Builder(getContext())
                                .setMessage("Location services should be enabled to set the pickup location")
                                .setPositiveButton("App settings", new
                                        DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                                System.exit(0);
                                            }
                                        })
                                .show();
                    }else {
                        new AlertDialog.Builder(getContext())
                                .setMessage("Location services should be enabled to set the pickup location")
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

                LatLng ll = new LatLng(Double.parseDouble(UserProfile.getInstance().getPickupLat()), Double.parseDouble(UserProfile.getInstance().getPickupLong()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 14.0f));
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

//        switch (requestCode) {
//            case 1: {
//                // If request is cancelled, the result arrays are empty.
//                System.out.println("Loc on on on");
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    System.out.println("Location services on");
//                    mMap.setMyLocationEnabled(true);
//                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//                    enableCurrentLocation();

//                    LocationManager locationManager = (LocationManager)
//                            getActivity().getSystemService(Context.LOCATION_SERVICE);
//                    Criteria criteria = new Criteria();
//
//                    Location location1 = locationManager.getLastKnownLocation(locationManager
//                            .getBestProvider(criteria, false));
//                    System.out.println("Printing");
//
//                    if (location1 != null) {
//                        double latitude = location1.getLatitude();
//                        double longitude = location1.getLongitude();
//                        System.out.println(latitude);
//                        System.out.println(longitude);




//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }


    public void goToSelectedLocation() {
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(latitudeLogitude.latitude, latitudeLogitude.longitude));
        System.out.println("Lattidue at bottom " + latitudeLogitude.latitude +":" + latitudeLogitude.longitude);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        if (marker != null)
            marker.remove();

        marker = mMap.addMarker(new MarkerOptions()
                .position(latitudeLogitude)
                .draggable(true));


        //mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
    }



//    private class GetPickupNotificationDistance extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
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
//                    if (description.contains("Pickup Notification Distance")) {
//                        pickupDistanceattribute = Double.parseDouble(rec.getString("attribute"));
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

    private class CreatePickupGeoLocation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            int calenderId = userProfile.getPickupCalendarId();
            double distance = userProfile.getPickUpReminderDistance() * 1000.0;

            String pickupGeofenceName = UserProfile.getInstance().getLoggedUserName() + "'s Pickup geo fence";
            String area = "CIRCLE (" + latitudeLogitude.latitude + " " + latitudeLogitude.longitude + ", " + distance+ ")";
            System.out.println("Pickup notifcation distance " + userProfile.getPickUpReminderDistance());

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
                System.out.println("Geo Fence created succesfully");
                System.out.println(response);
                System.out.println("Response coee" + response.code());
                if(response.code() == 200) {
                    userProfile.setPickupGeofenceExists(true);
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    System.out.println("Geo Fence Id is: " + jsonObject.getInt("id"));
                    userProfile.setPickupGeofenceId(jsonObject.getInt("id"));
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

    private class CreatePickupNotification extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            int calenderId = 0;
            String pickupNotificationName = UserProfile.getInstance().getLoggedUserName() + "'s Pickup Notification";

            MediaType mediaType = MediaType.parse("application/json");
            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body;
            if(userProfile.isPickNotificationEnabled())
                body = RequestBody.create(mediaType, "{\n        \"attributes\": {\n        \t\"name\" : \""+pickupNotificationName+"\"\n        },\n        \"calendarId\": \""+userProfile.getPickupCalendarId()+"\",\n        \"always\": true,\n        \"type\": \"geofenceEnter\",\n        \"notificators\":\"firebase,web\"}");
            else
                body = RequestBody.create(mediaType, "{\n        \"attributes\": {\n        \t\"name\" : \""+pickupNotificationName+"\"\n        },\n        \"calendarId\": \""+userProfile.getPickupCalendarId()+"\",\n        \"always\": true,\n        \"type\": \"geofenceEnter\",\n        \"notificators\":\"web\"}");

            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/notifications")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                okhttp3.Response  response = client.newCall(request).execute();
                System.out.println(response.code());
                JSONObject someObject = new JSONObject(response.body().string());
                int id = someObject.getInt("id");
                System.out.println("Id is " + id);
                userProfile.setPickupNotificationId(id);
                System.out.println("Pickup Notification created succesfully");
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Unable to create the pickup notication, please try again later", Toast.LENGTH_LONG).show();

                System.out.println("Exception at Pickup");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("End of assign device");
            return "Executed";
        }

    }

    private class UpdatePickupGeoLocation extends AsyncTask<String, Void, String> {
        private double distance = userProfile.getPickUpReminderDistance() * 1000.0;

        @Override
        protected String doInBackground(String... params) {
            String pickupGeofenceName = UserProfile.getInstance().getLoggedUserName() + "'s Pickup geo fence";
            String area = "CIRCLE (" + latitudeLogitude.latitude + " " + latitudeLogitude.longitude + ", " + distance + ")";

            int geoFenceId = userProfile.getPickupGeofenceId();
            int calendarId = userProfile.getPickupCalendarId();
            System.out.println("Inside update pickup geo location "  + userProfile.getPickUpReminderDistance());
            MediaType mediaType = MediaType.parse("application/json");

            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            System.out.println(UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword());
            System.out.println(geoFenceId);
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, " {\n        \"id\": \""+geoFenceId+"\",\n        \"calendarId\": \""+calendarId+"\",\n        \"name\": \""+pickupGeofenceName+"\",\n        \"area\": \""+area+"\"\n    }");
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
            int geoFenceId = userProfile.getPickupGeofenceId();
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
                System.out.println("Device id is " + deviceId + "    " + "Geo fence id: " + geoFenceId);
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
                    attributeId = userProfile.getPickupLatID();
                    description = userProfile.getLoggedUserName() + "'s Pickup lat: ";
                    attribute = latitudeLogitude.latitude + "";

                } else if (counter == 1) {
                    attributeId = userProfile.getPickupLongID();
                    description = userProfile.getLoggedUserName() + "'s Pickup long: ";
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
                        System.out.println("asdasdadasda");
                        System.out.println(latitudeLogitude);
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
