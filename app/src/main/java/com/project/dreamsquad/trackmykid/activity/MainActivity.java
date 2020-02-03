package com.project.dreamsquad.trackmykid.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.dreamsquad.trackmykid.Logout;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.fragments.ContactFragment;
import com.project.dreamsquad.trackmykid.fragments.HomeFragment;
import com.project.dreamsquad.trackmykid.fragments.MainFragment;
import com.project.dreamsquad.trackmykid.fragments.PickUp;
import com.project.dreamsquad.trackmykid.fragments.SettingsFragment;
import com.project.dreamsquad.trackmykid.models.UserProfile;
import com.project.dreamsquad.trackmykid.others.PrefManager;

/**
 * Created by this pc on 11-05-17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    //private FloatingActionButton add;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // index to identify current nav menu item
    public static int navItemIndex = 1;

    // tags used to attach the fragments
//    private static final String TAG_HOME = "home";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_LOCATION = "edit location";
//    private static final String TAG_SCHOOL = "about school";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_CONTACTS = "contact us";
    private static final String TAG_EDITPROFILE = "edit profile";
    private static final String TAG_CHANGEPASSWORD = "change password";
    private static final String TAG_LOGOUT = "logout";
    private static final String TAG_VIEWMAP = "view map";
    public static String CURRENT_TAG = TAG_VIEWMAP;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    PrefManager prefManager;
    AlertDialog alertDialog;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("1");
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager = new PrefManager(getApplicationContext());
        UserProfile.getInstance().getUserData();

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
//        add = (FloatingActionButton) header.findViewById(R.id.add_profile);
//        add.setOnClickListener(this);


        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null && prefManager.getNotify() == 0) {
            if(UserProfile.getInstance().isIsaNewUser()){
                navItemIndex = 2;
                CURRENT_TAG = TAG_PROFILE;
                loadHomeFragment();
            }else {
                navItemIndex = 1;
                CURRENT_TAG = TAG_VIEWMAP;
                loadHomeFragment();
            }
        } else {
            navItemIndex = 2;
            CURRENT_TAG = TAG_PROFILE;
            prefManager.setNotify(0);
            loadHomeFragment();
        }

        Location vanLoc = new Location(LocationManager.GPS_PROVIDER);
        Location userLoc = new Location(LocationManager.GPS_PROVIDER);
//        userLoc.setLatitude(UserProfile.getInstance().getPickUpLocation().latitude);
//        userLoc.setLongitude(UserProfile.getInstance().getPickUpLocation().longitude);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        setToolbarTitle();
        System.out.println("Disisisisisisis");
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
               // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                 //       android.R.anim.fade_out);
                System.out.println("printing current tag");
                System.out.println(CURRENT_TAG);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }


    private Fragment getHomeFragment() {
        System.out.println("3");
        switch (navItemIndex) {
//            case 0:
////                HomeFragment homeFragment = new HomeFragment();
////                return homeFragment;
//                TrackingFragment trackingFragment = new TrackingFragment();
//                return trackingFragment;
            case 1:
                MainFragment mainFragment = new MainFragment();
                return mainFragment;
            case 2:
                EditProfileInfo editProfileInfo = new EditProfileInfo();
                return editProfileInfo;
            case 3:
                PickUp pickUp = new PickUp();
                return pickUp;
            case 4:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
//            case 3:
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;
//            case 4:
//                AboutSchoolFragment aboutSchoolFragment = new AboutSchoolFragment();
//                return aboutSchoolFragment;
            case 5:
                ContactFragment contactFragment = new ContactFragment();
                return contactFragment;

            case 6:
                ChangePassword changePassword = new ChangePassword();
                return changePassword;
            case 7:
                UserProfile userProfile = UserProfile.getInstance();
                userProfile.setAuthenticated(false);
                Logout logout = new Logout();
                return logout;
            default:
                return new HomeFragment();
        }
    }


    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
//    private void selectNavMenu() {
//        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
//    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
//                    case R.id.nav_home:
//                        navItemIndex = 0;
//                        CURRENT_TAG = TAG_HOME;
//                        break;
//                    case R.id.nav_photos:
//                        navItemIndex = 1;
//                        CURRENT_TAG = TAG_PROFILE;
//                        break;
                    case R.id.nav_viewMap:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_VIEWMAP;
                        break;
                    case R.id.nav_editProfile:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_EDITPROFILE;
                        break;
                    case R.id.nav_movies:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_LOCATION;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
//                    case R.id.nav_notifications:
//                        navItemIndex = 4;
//                        CURRENT_TAG = TAG_SCHOOL;
//                        break;
                    case R.id.nav_contact:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_CONTACTS;
                        break;
                    case R.id.nav_changePassword:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_CHANGEPASSWORD;
                        break;
                    case R.id.nav_logout:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_LOGOUT;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
//                if (menuItem.isChecked()) {
//                    menuItem.setChecked(false);
//                } else {
//                    menuItem.setChecked(true);
//                }
//                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawer.getWindowToken(), 0);
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        System.out.println("111");
        if (shouldLoadHomeFragOnBackPress && prefManager.getNotifyStatus() == null) {
            // checking if user is on other navigation menu
            // rather than home
            System.out.println("Nav Item Index: " + navItemIndex);
            if (navItemIndex != 0 && navItemIndex != 1) {
                navItemIndex = 1;
                CURRENT_TAG = TAG_VIEWMAP;
                loadHomeFragment();
                return;
            }
            else{
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
            super.onBackPressed();
        } else {
            System.out.println("3333");
            getSupportFragmentManager().popBackStack();
            prefManager.setNotifyStatus(null);
            Log.e("Null", "Null");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if (item.getItemId() == R.id.action_refresh) {
//
////            Fragment fragment1 = new NotificationsFragment();
////            if (prefManager.getNotifyStatus() == null) {
////                getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment1, CURRENT_TAG).addToBackStack(CURRENT_TAG).commit();
////                prefManager.setNotifyStatus("1");
////                Log.e("1", "1");
////            } else {
////                getSupportFragmentManager().popBackStack();
////                prefManager.setNotifyStatus(null);
////                Log.e("Null", "Null");
////            }
//
//
//            MainFragment fragment = new MainFragment();
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//            //       android.R.anim.fade_out);
//            fragmentTransaction.replace(R.id.frame, fragment, "View Kid");
//            fragmentTransaction.commitAllowingStateLoss();
//        }

        return true;
    }


    @Override
    public void onClick(View view) {
//        if (add.getId() == view.getId()) {
//
//            LayoutInflater li = LayoutInflater.from(MainActivity.this);
//            View promptsView = li.inflate(R.layout.profile_new_dialog_layout, null);
//            ImageView edit = (ImageView) promptsView.findViewById(R.id.edit);
//            edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    alertDialog.dismiss();
//                }
//            });
//
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
//            alertDialogBuilder.setView(promptsView);
//            alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//
//        }
    }

    @Override
    protected void onStop() {
        prefManager.setNotifyStatus(null);
        super.onStop();
    }
}
