package com.project.dreamsquad.trackmykid.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


import com.google.android.libraries.places.api.Places;


import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.fragments.MainFragment;
import com.project.dreamsquad.trackmykid.models.UserProfile;
import com.project.dreamsquad.trackmykid.others.MainApplication;
import com.project.dreamsquad.trackmykid.others.Validation_Signup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EditProfileInfo extends Fragment {
    private TextView parentNameInput;
    private TextView userInput;
    private TextView emailInput;
    private TextView addressInput;
    private TextView v1;
    private TextView v2, v3, v4, v5, v6, v7;
    private TextView parentMobileInput;
    private TextView kidsNameInput;
    private TextView kidsBirthdayInput;
    private TextView driverNameInput;
    private TextView driverMobileInput;
    private TextView kidsSchoolAtt;
    private ImageView kidImage;
    private Button saveKidImage;
    //    private TextView kidSchool;
    private Button saveChanges_button;
    private TextInputLayout kidBirthdayLayout;

    private String parentName;
    private String email;
    private String address;
    private String parentMobile;
    private String kidsName;
    private String kidsBirthday;
    private String vanNumber;
    private String kidsSchool;
    private String vv1, vv2, vv3, vv4, vv5, vv6, vv7;

    private String loggedInName = MainApplication.getLoggedInName();
    private String loggedInEmail = MainApplication.getLoggedInEmail();
    private String loggedInPassword;

    private int mobileAttributeId;
    private int emailAttributeId;
    private int addressAttributeId;
    private int kidsNameAttributeId;
    private int kidsBirthdayAttributeId;
    private int parentNameAttributeId;
    private int kidsSchoolAttributeId;
    int groupId;
    private boolean isaNewUser;
    // private int geoFenceId;
    private String getFenceLocation;
    private String kidImageUpload;
    AutocompleteSupportFragment placeAutoComplete;
    ProgressDialog progress;
    UserProfile userProfile;
    private com.google.android.libraries.places.api.model.Place placeSchool;
    String vannum;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    private int attributeCreateCounter = -1;
    OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_edit_profile_info, container, false);
        super.onCreate(savedInstanceState);
        userProfile = UserProfile.getInstance();
        // new GetSavedSchoolLocationn().execute("");

        if (UserProfile.getInstance().isSignedUpUser()) {
            UserProfile.getInstance().setSignedUpUser(false);
            try {
//                progress = new ProgressDialog(getContext());
//                progress.setMessage(getString(R.string.app_loadingSignup));
//                progress.setCancelable(false);
//                progress.show();
            } catch (Exception e) {
                System.out.println("error at on sign up");
                e.printStackTrace();
            }
        }
        //new SetData().execute("");

        parentNameInput = (TextView) v.findViewById(R.id.parentName_Profile);
        userInput = (TextView) v.findViewById(R.id.username_Profile);
        emailInput = (TextView) v.findViewById(R.id.email_proofile);
        addressInput = (TextView) v.findViewById(R.id.address_profile);
        parentMobileInput = (TextView) v.findViewById(R.id.parentMobile_Profile);
        kidsNameInput = (TextView) v.findViewById(R.id.kidName);
        kidsBirthdayInput = (TextView) v.findViewById(R.id.kidBirthday);
        driverNameInput = (TextView) v.findViewById(R.id.driverName);
        driverMobileInput = (TextView) v.findViewById(R.id.driverMobile);
        saveChanges_button = (Button) v.findViewById(R.id.button_saveData);
        kidImage = (ImageView) v.findViewById(R.id.kidImage);
        // saveKidImage = (Button) v.findViewById(R.id.kidImageBtn);
        kidBirthdayLayout = (TextInputLayout) v.findViewById(R.id.kidBirthdayLayout);
        kidsSchoolAtt = (TextView) v.findViewById(R.id.place_autocompleteEditProfileSchooll);
        v1 = (TextView) v.findViewById(R.id.van1Profile);
        v2 = (TextView) v.findViewById(R.id.van2Profile);
        v3 = (TextView) v.findViewById(R.id.van3Profile);
        v4 = (TextView) v.findViewById(R.id.van4Profile);
        v5 = (TextView) v.findViewById(R.id.van5Profile);
        v6 = (TextView) v.findViewById(R.id.van6Profile);
        v7 = (TextView) v.findViewById(R.id.van7Profile);


        String image_address = "https://images.pexels.com/photos/159823/kids-girl-pencil-drawing-159823.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940";
        Glide.with(getActivity()).load(image_address)
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(0.5f)
                .into(kidImage);


        kidsBirthdayInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        kidBirthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                System.out.println("onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                kidsBirthdayInput.setText(date);
            }
        };


        userInput.setText(loggedInEmail);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loggedInPassword = userProfile.getLoggedPassword();
//        loggedInPassword = preferences.getString(MainApplication.PREFERENCE_PASSWORD, "");

        saveChanges_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveData();
            }
        });


        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyAOjD2BjQlaMlVpkT__VBTsX5gRb3EVSUg");
        }

//        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setCountry("LK")
//                .setTypeFilter(82)
//                .build();


//        placeAutoComplete = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocompleteEditProfileSchooll);
//        placeAutoComplete.setCountry("LK");
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME,
//                Place.Field.ID,
//                Place.Field.LAT_LNG);
//        placeAutoComplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//        placeAutoComplete.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
//                                                         @Override
//                                                         public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
//                                                             placeSchool = place;
//                                                             Log.d("Maps", "Place selected: " + place.getName());
//                                                             System.out.println(place.getName().toString());
//                                                             UserProfile.getInstance().setKidSchool(place.getName().toString());
//                                                             UserProfile.getInstance().setSchoolLocation(place.getLatLng());
//                                                             kidsSchool = place.getName().toString();
//                                                             System.out.println("Kids school is:   " + kidsSchool);
////                if(userProfile.isSchoolGeofenceExists())
////                    new UpdateSchoolGeoLocation().execute("");
////                else
////                    new CreateSchoolGeoLocation().execute("");
////
////                if(userProfile.getSchoolNotificationId() == 0)
////                    new CreateSchoolNotification().execute("");
////                if(isaNewUser)
////                    new CreateSchoolGeoLocation().execute("");
////                else
////                    new UpdateSchoolGeoLocation().execute("");
//                                                         }
//
//                                                         @Override
//                                                         public void onError(@NonNull Status status) {
//                                                             Log.d("Maps", "An error occurred: " + status);
//                                                             System.out.println("Error at selecting a place  " +status);
//                                                         }
//            });

//        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
//// Call either setLocationBias() OR setLocationRestriction().
//                .setCountry("LK")
//                .setSessionToken(tokenPlaces)
//                .build();


//                System.out.println("School set Text");
////        if(!userProfile.getKidSchool().isEmpty() || userProfile.getKidSchool() != null)
////            placeAutoComplete.setText(UserProfile.getInstance().getKidSchool());
//        placeAutoComplete.setFilter(typeFilter);
//        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                placeSchool = place;
//                Log.d("Maps", "Place selected: " + place.getName());
//                System.out.println(place.getName().toString());
//                UserProfile.getInstance().setKidSchool(place.getName().toString());
//                UserProfile.getInstance().setSchoolLocation(place.getLatLng());
//                kidsSchool = place.getName().toString();
//                System.out.println("Kids school is:   " + kidsSchool);
////                if(userProfile.isSchoolGeofenceExists())
////                    new UpdateSchoolGeoLocation().execute("");
////                else
////                    new CreateSchoolGeoLocation().execute("");
////
////                if(userProfile.getSchoolNotificationId() == 0)
////                    new CreateSchoolNotification().execute("");
////                if(isaNewUser)
////                    new CreateSchoolGeoLocation().execute("");
////                else
////                    new UpdateSchoolGeoLocation().execute("");
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.d("Maps", "An error occurred: " + status);
//                System.out.println("Error at selecting a place");
//            }
//        });

        setData();

        return v;
    }


    public void setData() {

        parentMobileInput.setText(userProfile.getParentContactNumber());
        emailInput.setText(userProfile.getEmail());
        addressInput.setText(userProfile.getHomeAddress());
        kidsNameInput.setText(userProfile.getKidName());
        kidsBirthdayInput.setText(userProfile.getKidsBirthday());
        kidsSchoolAtt.setText(userProfile.getKidSchool());
        parentNameInput.setText(userProfile.getParentName());
        driverNameInput.setText(userProfile.getDriverName());
        driverMobileInput.setText(userProfile.getDriverContactNumber());
        driverNameInput.setText(userProfile.getDriverName());
        driverMobileInput.setText(userProfile.getDriverContactNumber());

        String vnum = "0000000";
        char[] vehicleArray = vnum.toCharArray();
        if (userProfile.getVehicleNum() != null)
            vehicleArray = userProfile.getVehicleNum().toCharArray();

        System.out.println("Trying to print");
//        if(userProfile.getKidImage() != null) {
//            //System.out.println("Indise setting kids image " + userProfile.getKidImage());
//            System.out.println("Kid image is not null");
//            Bitmap bitmap = StringToBitMap(userProfile.getKidImage());
//            kidImage.setImageBitmap(bitmap);
//        }

        if (vehicleArray.length == 7) {
            v1.setText(vehicleArray[0] + "");
            v2.setText(vehicleArray[1] + "");
            v3.setText(vehicleArray[2] + "");
            v4.setText(vehicleArray[3] + "");
            v5.setText(vehicleArray[4] + "");
            v6.setText(vehicleArray[5] + "");
            v7.setText(vehicleArray[6] + "");
        } else if (vehicleArray.length == 6) {
            v1.setText("-");
            v2.setText(vehicleArray[0] + "");
            v3.setText(vehicleArray[1] + "");
            v4.setText(vehicleArray[2] + "");
            v5.setText(vehicleArray[3] + "");
            v6.setText(vehicleArray[4] + "");
            v7.setText(vehicleArray[5] + "");
        }

    }

    public void displayAlert(String text) {
        String msg = text + " Please try again!";
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Profile editing error!")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (placeAutoComplete != null && getActivity() != null && !getActivity().isFinishing()) {
            getFragmentManager().beginTransaction().remove(placeAutoComplete).commit();
        }
    }


    public boolean validateData() {
        Validation_Signup validation_signup = new Validation_Signup();
        int result = validation_signup.validateEditProfile(parentName, parentMobile, kidsName);

        if (result == 101) {
            displayAlert("You can only have alphabetical characters in Parent Name, ");
            return false;
        } else if (result == 105) {
            displayAlert("Parent Mobile should exactly be 10 numbers, ");
            return false;
        } else if (result == 106) {
            displayAlert("You can only have alphabetical characters in Kids Name, ");
            return false;
        } else if (vv1.length() > 1 || vv2.length() > 1 || vv3.length() > 1 || vv4.length() > 1 || vv5.length() > 1 || vv6.length() > 1 || vv7.length() > 1) {
            displayAlert("Fill the blanks in Vehicle Number only with 1 character each, ");
            return false;
        } else
            return true;

    }


    public void onSaveData() {
        parentName = parentNameInput.getText().toString();
        email = emailInput.getText().toString();
        address = addressInput.getText().toString();
        parentMobile = parentMobileInput.getText().toString();
        kidsName = kidsNameInput.getText().toString();
        kidsBirthday = kidsBirthdayInput.getText().toString();
        kidsSchool = kidsSchoolAtt.getText().toString();

        vv1 = v1.getText().toString();
        vv2 = v2.getText().toString();
        vv3 = v3.getText().toString();
        vv4 = v4.getText().toString();
        vv5 = v5.getText().toString();
        vv6 = v6.getText().toString();
        vv7 = v7.getText().toString();
        String vanLetters = "";
        if (vv1.isEmpty() || vv2.isEmpty() || vv3.isEmpty()) {
            if (vv1.isEmpty())
                vanLetters = vv2 + vv3;
            else if (vv2.isEmpty())
                vanLetters = vv1 + vv3;
            else if (vv3.isEmpty())
                vanLetters = vv1 + vv2;
        } else
            vanLetters = vv1 + vv2 + vv3;
        vannum = vanLetters + vv4 + vv5 + vv6 + vv7;


        if (parentName.isEmpty()) {
            displayAlert("Parent name cannot be empty, ");
            return;
        } else if (parentMobile.isEmpty()) {
            displayAlert("Parent Mobile cannot be empty, ");
            return;
        }

        if (!validateData())
            return;

        userProfile.setParentName(parentName);
        userProfile.setEmail(email);
        userProfile.setHomeAddress(address);
        userProfile.setParentContactNumber(parentMobile);
        userProfile.setKidName(kidsName);
        userProfile.setKidsBirthday(kidsBirthday);
        userProfile.setKidImage(kidImageUpload);
        userProfile.setKidSchool(kidsSchool);

        System.out.println("School geo fence");
        System.out.println(vannum);
        System.out.println(userProfile.getVehicleNum());
        System.out.println(vannum.equals(userProfile.getVehicleNum()));
        if (!vannum.equals(userProfile.getVehicleNum())) {
            System.out.println("Not equal");
            new DeletePreviousGroup().execute("");
        }
//        if(userProfile.isSchoolGeofenceExists() && placeSchool != null)
//            new UpdateSchoolGeoLocation().execute("");
//        else if(placeSchool!=null)
//            new CreateSchoolGeoLocation().execute("");
        System.out.println(userProfile.isSchoolGeofenceExists());

//        if(userProfile.getSchoolNotificationId() == 0)
//            new CreateSchoolNotification().execute("");


        new UpdateAttributes().execute("");


        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
        //       android.R.anim.fade_out);
        System.out.println("printing current tag");
        fragmentTransaction.replace(R.id.frame, fragment, "View Kid");
        ((MainActivity) getActivity())
                .setActionBarTitle("View Kid");
        fragmentTransaction.commitAllowingStateLoss();


    }

    private class UpdateAttributes extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("Assign Attributes called");

            MediaType mediaType = MediaType.parse("application/json");
            System.out.println("Printing admin info");
            System.out.println(UserProfile.getInstance().getLoggedUserName() + "    " + UserProfile.getInstance().getLoggedPassword());
//            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            String credentials = userProfile.getMainCredentials();

            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            System.out.println("printing inside assign attributes");

            int attributeId = -1;
            String description = "";
            String attribute = "";

            for (int counter = 0; counter < 8; counter++) {

                if (counter == 0) {
                    System.out.println("printing stuff");
                    attributeId = userProfile.getMobileAttributeId();
                    description = loggedInEmail + "'s Mobile: ";
                    attribute = parentMobile;
                    System.out.println(attributeId);
                    System.out.println(attribute);
                } else if (counter == 1) {
                    attributeId = userProfile.getEmailAttributeId();
                    description = loggedInEmail + "'s Email: ";
                    attribute = email;
                } else if (counter == 2) {
                    attributeId = userProfile.getAddressAttributeId();
                    description = loggedInEmail + "'s Home Address: ";
                    attribute = address;
                    System.out.println("AATTTTT:  " + attributeId);
                    System.out.println(attribute);
                } else if (counter == 3) {
                    attributeId = userProfile.getKidsNameAttributeId();
                    description = loggedInEmail + "'s Kids Name: ";
                    attribute = kidsName;
                } else if (counter == 4) {
                    attributeId = userProfile.getKidsBirthdayAttributeId();
                    description = loggedInEmail + "'s Kids Birthday: ";
                    attribute = kidsBirthday;
                } else if (counter == 5) {
                    attributeId = userProfile.getParentNameAttributeId();
                    description = loggedInEmail + "'s Parent Name: ";
                    attribute = parentName;
                    System.out.println("Pritng attribute id");
                    System.out.println(attributeId);
                    System.out.println(parentName);
                }else if (counter == 6) {
                    System.out.println("Country 6");
                    attributeId = userProfile.getKidsSchoolAttributeId();
                    description = loggedInEmail + "'s Kids School: ";
                    if(kidsSchool == null)
                        attribute = userProfile.getKidSchool();
                    else
                        attribute = kidsSchool;
                    System.out.println("School saving t o server is: " + attribute );
                }else if(counter == 7){
                    attributeId = userProfile.getVehicleNumAttributeId();
                    description = loggedInEmail +"'s van ID";
                    attribute = vannum;
                    userProfile.setVehicleNum(vannum);
                    System.out.println("Saving van ID");
                }
//                else if (counter == 7) {
//                    attributeId = userProfile.getKidImageAttributeId();
//                    description = loggedInEmail + "'s Kid Image: ";
//                    attribute = kidImageUpload;
//                    System.out.println(kidImageUpload);
//                }0

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
        }

    }

//    private class GetSavedSchoolLocationn extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            System.out.println("Inside saved location");
//            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
//            final String basic =
//                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//
//            Request request = new Request.Builder()
//                    .url("http://85.5.55.236:8082/api/geofences")
//                    .get()
//                    .addHeader("Authorization", basic)
//                    .addHeader("cache-control", "no-cache")
//                    .build();
//
//            try {
//                okhttp3.Response response = client.newCall(request).execute();
//                String responseBody = response.body().string();
//                JSONArray jsonArray = new JSONArray(responseBody);
//                System.out.println("Size of array " + jsonArray.length());
//                if (jsonArray.length() > 0){
//                    System.out.println("Size of array is " + jsonArray.length());
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject rec = jsonArray.getJSONObject(i);
//                        String description = rec.getString("name");
//                        if (description.contains("School geo fence")) {
//                            getFenceLocation = rec.getString("area");
//                            geoFenceId = rec.getInt("id");
//                            isaNewUser = false;
//                            System.out.println("Is a new user false inside for loop");
//                            break;
//                        }else{
//                            isaNewUser = true;
//                            System.out.println("Is a new user true inside else");
//                        }
//                    }
//                }else {
//                    isaNewUser = true;
//                    System.out.println("Is a new user true outside loop");
//                }
//            } catch (IOException e) {
//                Toast.makeText(getActivity(), "Error Opening the window, Please try again later!", Toast.LENGTH_LONG).show();
//
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return "Executed";
//        }
//
//    }

    private class CreateSchoolGeoLocation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            int calenderId = 0;
            String schoolGeo = UserProfile.getInstance().getLoggedUserName() + "'s School geo fence";
            String area = "CIRCLE (" + placeSchool.getLatLng().latitude + " " + placeSchool.getLatLng().longitude + ", " + 500 + ")";

            System.out.println("Inside create pickup geo location");
            MediaType mediaType = MediaType.parse("application/json");
            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, "{\n\t\n    \t\"calendarId\":\"" + calenderId + "\",\n        \"name\": \"" + schoolGeo + "\",\n        \"area\": \"" + area + "\"\n        \n    }\n    \n   ");
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/geofences")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();
                System.out.println("Geo Fence created succesfully");
                System.out.println(response);
                System.out.println("Response coee" + response.code());
                if(response.code() == 200) {
                    userProfile.setSchoolGeofenceExists(true);
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    System.out.println("School Geo Fence Id is: " + jsonObject.getInt("id"));
                    userProfile.setSchoolGeofenceId(jsonObject.getInt("id"));
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

    private class UpdateSchoolGeoLocation extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            int calenderId = 0;
            String schoolGeo = UserProfile.getInstance().getLoggedUserName() + "'s School geo fence";
            String area = "CIRCLE (" + placeSchool.getLatLng().latitude + " " + placeSchool.getLatLng().longitude + ", " + 500 + ")";

            System.out.println("Inside update pickup geo location");
            MediaType mediaType = MediaType.parse("application/json");

            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            System.out.println(UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword());
            int geoFenceId = userProfile.getSchoolGeofenceId();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, " {\n        \"id\": \""+geoFenceId+"\",\n        \"calendarId\": 0,\n        \"name\": \""+schoolGeo+"\",\n        \"area\": \""+area+"\"\n    }");
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/geofences/"+ geoFenceId)
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();


            try {
                okhttp3.Response response = client.newCall(request).execute();
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
            int geoFenceId = userProfile.getSchoolGeofenceId();
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
                okhttp3.Response response = client.newCall(request).execute();
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

    private class CreateSchoolNotification extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            int calenderId = 0;
            String pickupNotificationName = UserProfile.getInstance().getLoggedUserName() + "'s School Notification";

            MediaType mediaType = MediaType.parse("application/json");
            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, "{\n        \"attributes\": {\n        \t\"name\" : \""+pickupNotificationName+"\"\n        },\n        \"calendarId\": \""+userProfile.getSchoolCalendarId()+"\",\n        \"always\": true,\n        \"type\": \"geofenceEnter\",\n        \"web\":\""+userProfile.isPickNotificationEnabled()+"\",\n        \"mail\": true,\n        \"sms\": true\n}");
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
                String responseBody = response.body().string();
                JSONObject someObject = new JSONObject(responseBody);
                int id = someObject.getInt("id");
                System.out.println("Id is " + id);
                userProfile.setSchoolNotificationId(id);
                System.out.println("School Notification created succesfully");
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

    private class GetGroupId extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("GetGroupId calledddddddd");
            System.out.println("Get Group ID");
            MediaType mediaType = MediaType.parse("application/json");

            String credentials = userProfile.getMainCredentials();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/groups?all=true")
                    .get()
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONArray jsonArray = null;
                JSONObject driverName;
                Boolean vanExists = false;
                try {
                    jsonArray = new JSONArray(responseBody);

                    System.out.println("For loop");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject rec = jsonArray.getJSONObject(i);
                        String groupName = rec.getString("name");
                        System.out.println("Group Name: " + groupName);
                        System.out.println("Van Number" + vannum);
                        if(groupName.equals(userProfile.getVehicleNum())) {
                            vanExists = true;
                            userProfile.setGroupId(rec.getInt("id"));
                            driverName = (JSONObject)rec.get("attributes");
                            if(driverName.getString("Driver Name") != null)
                                userProfile.setDriverName(driverName.getString("Driver Name"));
                            if(driverName.getString("Driver Contact") != null)
                                userProfile.setDriverContactNumber(driverName.getString("Driver Contact"));
                            System.out.println("Printintgnignigngn" + driverName.getString("Driver Name"));
                            System.out.println("Printintgnignigngn" + driverName.getString("Driver Contact"));
                            break;
                        }else{
                            userProfile.setDriverName("");
                            userProfile.setDriverContactNumber("");
                        }
                    }
                    if(vanExists) {
                        new AssignDevice().execute("");
                        System.out.println("Van exisits");
                    }
                    else{
                        System.out.println("A group doesnt exists");
                        System.out.println("Inside run");

//                                MainFragment fragment = new MainFragment();
//                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                                //       android.R.anim.fade_out);
//                                System.out.println("printing current tag");
//                                fragmentTransaction.replace(R.id.frame, fragment, "View Kid");
//                                fragmentTransaction.commitAllowingStateLoss();
                    }

//                        Toast.makeText(getContext(), "A van with the given reg number doesnot exist", Toast.LENGTH_LONG).show();

                    System.out.println("Group ID is " + groupId);
                } catch (JSONException e) {
                    System.out.println("Json Exception at 356");
                    e.printStackTrace();
                }

            } catch (IOException e) {
                System.out.println("IOException at 361");
                e.printStackTrace();
            }
            System.out.println("End of get group ID");
            return "Executed";
        }
    }

    private class AssignDevice extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            MediaType mediaType = MediaType.parse("application/json");
            String credentials = userProfile.getMainCredentials();
            System.out.println("");
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, "{\n    \t\"userId\" :\"" + userProfile.getLoggedUserId() + "\",\n        \"groupId\": \""+userProfile.getGroupId()+"\"\n        }\n");
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/permissions")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();
                System.out.println("Group assigned I think");
                System.out.println(response.code());
                System.out.println(response.body().string());
                if(response.code() == 204 || response.code() == 200) {
                    System.out.println("New device assigned succes");
                }
                System.out.println("Design shape eke assigned");

            } catch (IOException e) {
                System.out.println("Exception at Assign Device 409");
                e.printStackTrace();
            }

            System.out.println("End of assign device");
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MainFragment fragment = new MainFragment();
//                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                        // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                        //       android.R.anim.fade_out);
//                        System.out.println("printing current tag");
//                        fragmentTransaction.replace(R.id.frame, fragment, "View Kid");
//                        fragmentTransaction.commitAllowingStateLoss();
//                    }
//                });
                                }


    }

    private class DeletePreviousGroup extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            System.out.println("Inside assign device");

            MediaType mediaType = MediaType.parse("application/json");
            String credentials = userProfile.getLoggedUserName() + ":" + userProfile.getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"userId\" : \""+userProfile.getLoggedUserId()+"\",\n\t\"groupId\" : \""+userProfile.getGroupId()+"\"\n}");
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/permissions")
                    .delete(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();
                System.out.println(response.code());
                System.out.println(response.body().string());
                if(response.code() == 204 || response.code() == 200)
                    System.out.println("Previous group deleted succesfully");
                System.out.println("Previous group deleted");
                userProfile.setVehicleNum(vannum);

            } catch (IOException e) {
                System.out.println("Exception at Assign Device 409");
                e.printStackTrace();
            }

            System.out.println("End of assign device");
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new GetGroupId().execute("");
        }
    }
}


