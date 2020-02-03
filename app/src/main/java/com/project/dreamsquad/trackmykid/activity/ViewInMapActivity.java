package com.project.dreamsquad.trackmykid.activity;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.Device;
import com.project.dreamsquad.trackmykid.models.Position;
import com.project.dreamsquad.trackmykid.others.GoogleMapPath;
import com.project.dreamsquad.trackmykid.others.GoogleMapsPath;
import com.project.dreamsquad.trackmykid.others.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ws.WebSocketCall;

/**
 * Created by this pc on 20-05-17.
 */

public class ViewInMapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    Toolbar toolbar;
    PrefManager pref;
    private TabLayout tabLayout;
    String image_address = "http://media.gettyimages.com/photos/male-high-school-student-portrait-picture-id98680202?s=170667a";
    ImageView student_image;
    private ViewPager viewPager;
    Button edit_location;

    private GoogleMap Map;
    SupportMapFragment mapFragment;
    GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    private static final int MY_PERMISSION_REQUEST_READ_FINE_LOCATION = 111;


    public static final int REQUEST_DEVICE = 1;
    public static final int RESULT_SUCCESS = 1;

    private GoogleMap map;
    private Handler handler = new Handler();
    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<Long, Device> devices = new HashMap<>();
    private Map<Long, Position> positions = new HashMap<>();
    private Map<Long, Marker> markers = new HashMap<>();

    private WebSocketCall webSocket;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            return;
        }

        // other 'case' lines to check for other
        // permissions this app might request

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_in_map_layout);
        pref = new PrefManager(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar8);
        toolbar.setTitle(pref.getName());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        setSupportActionBar(toolbar);


//        edit_location = (Button) toolbar.findViewById(R.id.edit_location);
//        student_image = (ImageView) findViewById(R.id.student_image);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

//        edit_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(ViewInMapActivity.this, MainActivity.class);
//                pref.setNotify(1);
//                startActivity(intent);
//            }
//        });

//        Glide.with(getApplicationContext()).load(image_address)
//                .centerCrop()
//                .crossFade()
//                .thumbnail(0.5f)
//                .override(500, 500)
//                .bitmapTransform(new CircleTransform(getApplicationContext()))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(student_image);

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        createViewPager(viewPager);

//        tabLayout = (TabLayout) findViewById(R.id.tab_host);
//        tabLayout.setupWithViewPager(viewPager);


//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {

//
//                if (tabLayout.getSelectedTabPosition() == 0) {
//                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_query_builder_orange_24dp);
//                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_phone_grey_24dp);
//                   tabLayout.getTabAt(2).setIcon(R.drawable.ic_date_range_grey_24dp);
//                    pref.setTabNumber(1);
//                }
//                if (tabLayout.getSelectedTabPosition() == 1) {
//                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_query_builder_grey_24dp);
//                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_phone_orange_24dp);
//                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_date_range_grey_24dp);
//                    pref.setTabNumber(2);
//                }
//                if(tabLayout.getSelectedTabPosition()==2){
//                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_query_builder_grey_24dp);
//                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_phone_grey_24dp);
//                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_date_range_orange_24dp);
//                    pref.setTabNumber(3);}
//
//                finish();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//
//            }
//        });
//        setupTabIcons();

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_REQUEST_READ_FINE_LOCATION);

                // MY_PERMISSION_REQUEST_READ_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        //                    }
        googleApiClient = new GoogleApiClient.Builder(ViewInMapActivity.this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        try {

            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3);
            if (mapFragment == null) {
                mapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.map3, mapFragment).commit();
            }

            //                    }
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    // The next two lines tell the new client that “this” current class will handle connection stuff
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    //fourth line adds the LocationServices API endpoint from GooglePlayServices
                    .addApi(LocationServices.API)
                    .build();

            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    Map = googleMap;
                    // Add a marker in Sydney, Australia, and move the camera.
//                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    Map.setMyLocationEnabled(true);
//                    View mapView = mapFragment.getView();
//                    if (mapView != null &&
//                            mapView.findViewById(1) != null) {
//                        // Get the button view
//                        View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
//                        // and next place it, on bottom right (as Google Maps app)
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
//                                locationButton.getLayoutParams();
//                        // position on right bottom
//                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//                        layoutParams.setMargins(0,0,0,0);


                    //dummy coordinates are used here

                    search(Map, new LatLng(26.2520000, 78.1889999));

                    //drawing green path on the map

                    GoogleMapPath googleMapPath = new GoogleMapPath(ViewInMapActivity.this, Map, new LatLng(26.2520000, 78.1889999), new LatLng(26.2520944, 78.1794855));

                    Map.addMarker(new MarkerOptions().position(new LatLng(26.2520944, 78.1794855)).title("Bus").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    //drawing the red path from the end of green path

                    GoogleMapsPath googleMapsPath = new GoogleMapsPath(ViewInMapActivity.this, Map, new LatLng(26.2520944, 78.1794855), new LatLng(26.2530944, 78.1798855));

                    Map.addMarker(new MarkerOptions().position(new LatLng(26.2530944, 78.1798855)).title("Home").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

//    private void setupTabIcons() {
//
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_query_builder_orange_24dp);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_phone_grey_24dp);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_date_range_grey_24dp);
//    }
//
//    private void createViewPager(ViewPager viewPager) {
//
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new BusTrackFragment(), "Tab 1");
//        adapter.addFrag(new ContactsFragment(), "Tab 2");
//        adapter.addFrag(new Calender_Fragment(), "Tab 3");
//        viewPager.setAdapter(adapter);
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location == null) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(ViewInMapActivity.this, 9000);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    public void search(GoogleMap googleMap,LatLng latLng){

        Map=googleMap;

        Map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        Map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in, animating the camera.
        Map.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
//        Map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        Map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onResume() {
        super.onResume();
        //Now lets connect to the API
        googleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            googleApiClient.disconnect();
        }


    }

    @Override
    public void onLocationChanged(Location location) {

       //getting the location co-ordinates on location change
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }
}
