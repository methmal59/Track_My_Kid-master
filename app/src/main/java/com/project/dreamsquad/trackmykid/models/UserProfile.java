package com.project.dreamsquad.trackmykid.models;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.dreamsquad.trackmykid.activity.LoginFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserProfile {

    OkHttpClient client = new OkHttpClient();

    private String loggedUserName;
    private String loggedPassword;
    private int loggedUserId;

    private String parentName;
    private String email;
    private String parentContactNumber;
    private String vehicleNum;
    private String homeAddress;
    private String kidsBirthday;
    private String kidName;
    private String kidSchool;
    private String driverName;
    private String driverContactNumber;
    private String kidImage;

    private int mobileAttributeId;
    private int emailAttributeId;
    private int addressAttributeId;
    private int kidsNameAttributeId;
    private int kidsBirthdayAttributeId;
    private int parentNameAttributeId;
    private int kidsSchoolAttributeId;
    private int vehicleNumAttributeId;



    private String logginError = "Error, Please try again!!!!!!";
//    private String url = "http://85.5.55.236:8082/";
//    private String url = "http://ctrlhelp.internet-box.ch:8082";
//    private String url = "http://5.189.154.215:8082";
    private String url = "http://trackmykid.info:8082";


    private boolean authenticated = false;

    private LatLng pickUpLocation;
    private LatLng dropOffLocation;
    private LatLng schoolLocation;

    private double pickUpReminderDistance;
    private double dropOffReminderDistance;
    private int pickupReminderDistanceAttributeId;
    private int dropoffReminderDistanceAttributeId;
    private boolean pickNotificationEnabled;
    private boolean dropOffNotificationEnabled;
    private boolean schoolNotificationEnabled;
    private boolean isaNewUser = false;
    private int kidImageAttributeId;
    private boolean signedUpUser;

    private static UserProfile instance = null;
    private int pickupNotificationId;
    private int dropoffNotificationId;
    private int schoolNotificationId;

    private int pickupGeofenceId;
    private int dropoffGeofenceId;
    private int schoolGeofenceId;
    private String pickupGeofenceLocation;
    private String dropoffGeofenceLocation;
    private String schoolGeofenceLocation;
    private boolean pickupGeofenceExists;
    private boolean dropoffGeofenceExists;
    private boolean schoolGeofenceExists;

    private String pickupLat;
    private int pickupLatID;
    private int pickupLongID;
    private int dropofflatID;
    private int dropofflongID;
    private String pickupLong;
    private String dropoffLat;
    private String dropoffLong;

    private int deviceId;
    private int groupId;
    private String deviceName;

    private int pickupCalendarId;
    private int dropoffCalendarId;
    private int schoolCalendarId;

    private String mainCredentials = "admin:Udana@321";

    public void getUserData(){
        new GetUserData().execute("");

    }

    private UserProfile() {
        //new SetData().execute("");

    }

    public static UserProfile getInstance() {
        if (null == instance) {
            System.out.println("USERPORFILE CREATED AGAIN");
            instance = new UserProfile();
        }
        System.out.println("DIDNT CREATE THE USERPROFILE AGAIN");
        return instance;

    }

    public String getMainCredentials() { return mainCredentials; }

    public int getLoggedUserId() { return loggedUserId; }

    public void setLoggedUserId(int loggedUserId) { this.loggedUserId = loggedUserId; }

    public void setLogout(){
        instance = null;
    }

    public void setPickupGeofenceId(int pickupGeofenceId) { this.pickupGeofenceId = pickupGeofenceId; }

    public void setDropoffGeofenceId(int dropoffGeofenceId) { this.dropoffGeofenceId = dropoffGeofenceId; }

    public void setSchoolGeofenceId(int schoolGeofenceId) { this.schoolGeofenceId = schoolGeofenceId; }

    public int getPickupCalendarId() { return pickupCalendarId; }

    public void setPickupCalendarId(int pickupCalendarId) { this.pickupCalendarId = pickupCalendarId; }

    public int getDropoffCalendarId() { return dropoffCalendarId; }

    public void setDropoffCalendarId(int dropoffCalendarId) { this.dropoffCalendarId = dropoffCalendarId; }

    public int getSchoolCalendarId() { return schoolCalendarId; }

    public void setSchoolCalendarId(int schoolCalendarId) { this.schoolCalendarId = schoolCalendarId; }

    public int getGroupId() { return groupId; }

    public void setGroupId(int groupId) { this.groupId = groupId; }

    public int getDeviceId() { return deviceId; }

    public void setDeviceId(int deviceId) { this.deviceId = deviceId; }

    public boolean isPickupGeofenceExists() { return pickupGeofenceExists; }

    public boolean isDropoffGeofenceExists() { return dropoffGeofenceExists; }

    public boolean isSchoolGeofenceExists() { return schoolGeofenceExists; }

    public void setPickupGeofenceExists(boolean pickupGeofenceExists) { this.pickupGeofenceExists = pickupGeofenceExists; }

    public void setDropoffGeofenceExists(boolean dropoffGeofenceExists) { this.dropoffGeofenceExists = dropoffGeofenceExists; }

    public void setSchoolGeofenceExists(boolean schoolGeofenceExists) { this.schoolGeofenceExists = schoolGeofenceExists; }

    public int getPickupGeofenceId() { return pickupGeofenceId; }

    public int getDropoffGeofenceId() { return dropoffGeofenceId; }

    public void setPickupNotificationId(int pickupNotificationId) { this.pickupNotificationId = pickupNotificationId; }

    public void setDropoffNotificationId(int dropoffNotificationId) { this.dropoffNotificationId = dropoffNotificationId; }

    public void setSchoolNotificationId(int schoolNotificationId) { this.schoolNotificationId = schoolNotificationId; }

    public int getSchoolGeofenceId() { return schoolGeofenceId; }

    public boolean isSchoolNotificationEnabled() { return schoolNotificationEnabled; }

    public void setSchoolNotificationEnabled(boolean schoolNotificationEnabled) { this.schoolNotificationEnabled = schoolNotificationEnabled; }

    public int getPickupNotificationId() { return pickupNotificationId; }

    public int getDropoffNotificationId() { return dropoffNotificationId; }

    public int getSchoolNotificationId() { return schoolNotificationId; }

    public String getKidImage() { return kidImage; }

    public void setKidImage(String kidImage) { this.kidImage = kidImage; }

    public int getKidImageAttributeId() { return kidImageAttributeId; }

    public void setSignedUpUser(boolean signedUpUser) { this.signedUpUser = signedUpUser; }

    public boolean isSignedUpUser() { return signedUpUser; }

    public boolean isIsaNewUser() { return isaNewUser; }

    public void setIsaNewUser(boolean isaNewUser) { this.isaNewUser = isaNewUser; }

    public double getPickUpReminderDistance() { return pickUpReminderDistance; }

    public void setPickUpReminderDistance(double pickUpReminderDistance) { this.pickUpReminderDistance = pickUpReminderDistance; }

    public double getDropOffReminderDistance() { return dropOffReminderDistance; }

    public void setDropOffReminderDistance(double dropOffReminderDistance) { this.dropOffReminderDistance = dropOffReminderDistance; }

    public boolean isPickNotificationEnabled() { return pickNotificationEnabled; }

    public void setPickNotificationEnabled(boolean pickNotificationEnabled) { this.pickNotificationEnabled = pickNotificationEnabled; }

    public boolean isDropOffNotificationEnabled() { return dropOffNotificationEnabled; }

    public void setDropOffNotificationEnabled(boolean dropOffNotificationEnabled) { this.dropOffNotificationEnabled = dropOffNotificationEnabled; }

    public LatLng getSchoolLocation() { return schoolLocation; }

    public void setSchoolLocation(LatLng schoolLocation) { this.schoolLocation = schoolLocation; }

    public LatLng getPickUpLocation() { return pickUpLocation; }

    public void setPickUpLocation(LatLng pickUpLocation) { this.pickUpLocation = pickUpLocation; }

    public LatLng getDropOffLocation() { return dropOffLocation; }

    public void setDropOffLocation(LatLng dropOffLocation) { this.dropOffLocation = dropOffLocation; }

    public boolean isAuthenticated() { return authenticated; }

    public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }

    public String getHomeAddress() { return homeAddress; }

    public void setHomeAddress(String homeAddress) { this.homeAddress = homeAddress; }

    public String getKidsBirthday() { return kidsBirthday; }

    public void setKidsBirthday(String kidsBirthday) { this.kidsBirthday = kidsBirthday; }

    public String getLogginError() { return logginError; }

    public void setLogginError(String logginError) { this.logginError = logginError; }

    public String getUrl() {
        return url;
    }

    public String getLoggedUserName() {
        return loggedUserName;
    }

    public void setLoggedUserName(String loggedUserName) {
        this.loggedUserName = loggedUserName;
    }

    public String getLoggedPassword() {
        return loggedPassword;
    }

    public void setLoggedPassword(String loggedPassword) {
        this.loggedPassword = loggedPassword;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentContactNumber() {
        return parentContactNumber;
    }

    public void setParentContactNumber(String parentContactNumber) { this.parentContactNumber = parentContactNumber; }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getKidName() {//new SetData().execute("");
        return kidName;
    }

    public void setKidName(String kidName) { this.kidName = kidName; }

    public String getKidSchool() {
        //new SetData().execute("");
        return kidSchool;
    }

    public void setKidSchool(String kidSchool) { this.kidSchool = kidSchool; }

    public String getDriverName() { return driverName; }

    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getDriverContactNumber() { return driverContactNumber; }

    public void setDriverContactNumber(String driverContactNumber) { this.driverContactNumber = driverContactNumber; }

    public void setMobileAttributeId(int mobileAttributeId) { this.mobileAttributeId = mobileAttributeId; }

    public void setEmailAttributeId(int emailAttributeId) { this.emailAttributeId = emailAttributeId; }

    public void setAddressAttributeId(int addressAttributeId) { this.addressAttributeId = addressAttributeId; }

    public void setKidsNameAttributeId(int kidsNameAttributeId) { this.kidsNameAttributeId = kidsNameAttributeId; }

    public void setKidsBirthdayAttributeId(int kidsBirthdayAttributeId) { this.kidsBirthdayAttributeId = kidsBirthdayAttributeId; }

    public void setParentNameAttributeId(int parentNameAttributeId) { this.parentNameAttributeId = parentNameAttributeId; }

    public void setKidsSchoolAttributeId(int kidsSchoolAttributeId) { this.kidsSchoolAttributeId = kidsSchoolAttributeId; }

    public void setVehicleNumAttributeId(int vehicleNumAttributeId) { this.vehicleNumAttributeId = vehicleNumAttributeId; }

    public int getMobileAttributeId() {
        return mobileAttributeId;
    }

    public int getEmailAttributeId() {
        return emailAttributeId;
    }

    public int getAddressAttributeId() {
        return addressAttributeId;
    }

    public int getKidsNameAttributeId() {
        return kidsNameAttributeId;
    }

    public int getKidsBirthdayAttributeId() {
        return kidsBirthdayAttributeId;
    }

    public int getParentNameAttributeId() {
        return parentNameAttributeId;
    }

    public int getKidsSchoolAttributeId() {
        return kidsSchoolAttributeId;
    }

    public int getVehicleNumAttributeId() {
        return vehicleNumAttributeId;
    }

    public int getPickupReminderDistanceAttributeId() { return pickupReminderDistanceAttributeId; }

    public void setPickupReminderDistanceAttributeId(int pickupReminderDistanceAttributeId) { this.pickupReminderDistanceAttributeId = pickupReminderDistanceAttributeId; }

    public void setDropoffReminderDistanceAttributeId(int dropoffReminderDistanceAttributeId) { this.dropoffReminderDistanceAttributeId = dropoffReminderDistanceAttributeId; }

    public int getDropoffReminderDistanceAttributeId() { return dropoffReminderDistanceAttributeId; }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public int getPickupLatID() {
        return pickupLatID;
    }

    public void setPickupLatID(int pickupLatID) {
        this.pickupLatID = pickupLatID;
    }

    public int getPickupLongID() {
        return pickupLongID;
    }

    public void setPickupLongID(int pickupLongID) {
        this.pickupLongID = pickupLongID;
    }

    public int getDropofflatID() {
        return dropofflatID;
    }

    public void setDropofflatID(int dropofflatID) {
        this.dropofflatID = dropofflatID;
    }

    public int getDropofflongID() {
        return dropofflongID;
    }

    public void setDropofflongID(int dropofflongID) {
        this.dropofflongID = dropofflongID;
    }

    public String getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(String pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getDropoffLat() {
        return dropoffLat;
    }

    public void setDropoffLat(String dropoffLat) {
        this.dropoffLat = dropoffLat;
    }

    public String getDropoffLong() {
        return dropoffLong;
    }

    public void setDropoffLong(String dropoffLong) {
        this.dropoffLong = dropoffLong;
    }

    //    private class SetData extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            String credentials = loggedUserName + ":" + loggedPassword;
//            System.out.println("Creditials inside User Profile"+ loggedUserName + "    " + loggedPassword);
//
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
//            String desValue;
//            try {
//                okhttp3.Response response = client.newCall(request).execute();
//                System.out.println("AAAAA");
//                String responseBody = null;
//                try {
//                    responseBody = response.body().string();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(responseBody);
//                JSONArray jsonArray = new JSONArray(responseBody);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject rec = jsonArray.getJSONObject(i);
//                    String description = rec.getString("description");
//                    System.out.println("Description Name: " + description);
//                    if (description.contains("Kids Name")) {
//                        //kidsNameAttributeId = rec.getInt("id");
//                        kidName = rec.getString("attribute");
//                    }else if (description.contains("van ID")) {
//                        vehicleNum = rec.getString("attribute");
//                    }else if (description.contains("Kids School")) {
//                        kidSchool = rec.getString("attribute");
//                    }
//
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return "Executed";
//
//        }
//    }

    private class GetUserData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
//            String credentials = loggedUserName + ":" + loggedPassword;
            String credentials = loggedUserName+":"+loggedPassword;
            System.out.println("Credentials are " + credentials);
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder()
                    .url(url+"/api/attributes/computed")
                    .get()
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            String desValue;
            try {
                System.out.println("Printing dataa");
                okhttp3.Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                System.out.println("Response body:   "+responseBody);
                JSONArray jsonArray = new JSONArray(responseBody);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject rec = jsonArray.getJSONObject(i);
                    String description = rec.getString("description");
                    System.out.println("Description Name: " + description);
                    if (description.contains("van ID")) {
                        vehicleNumAttributeId = rec.getInt("id");
                        vehicleNum = rec.getString("attribute");
                        System.out.println(vehicleNum);
                    } else if (description.contains("Mobile")) {
                        mobileAttributeId = rec.getInt("id");
                        parentContactNumber = rec.getString("attribute");
                        System.out.println(parentContactNumber);
                    } else if (description.contains("Email")) {
                        emailAttributeId = rec.getInt("id");
                        email = rec.getString("attribute");
                        System.out.println(email);
                    } else if (description.contains("Home Address")) {
                        addressAttributeId = rec.getInt("id");
                        homeAddress = rec.getString("attribute");
                        System.out.println(homeAddress);
                    } else if (description.contains("Kids Name")) {
                        kidsNameAttributeId = rec.getInt("id");
                        kidName = rec.getString("attribute");
                        System.out.println(kidName);
                    } else if (description.contains("Kids Birthday")) {
                        kidsBirthdayAttributeId = rec.getInt("id");
                        kidsBirthday = rec.getString("attribute");
                        System.out.println(kidsBirthday);
                    } else if (description.contains("Parent Name")) {
                        parentNameAttributeId = rec.getInt("id");
                        parentName = rec.getString("attribute");
                        System.out.println("Printing parents name   " +parentName);
                    } else if (description.contains("Kids School")) {
                        kidsSchoolAttributeId = rec.getInt("id");
                        kidSchool = rec.getString("attribute");
                        System.out.println(kidSchool);
                    } else if(description.contains("Kid Image")){
                        kidImage = rec.getString("attribute");
                        kidImageAttributeId = rec.getInt("id");
                    } else if (description.contains("Pickup Notification Distance")) {
                        pickUpReminderDistance =  Double.parseDouble(rec.getString("attribute")) / 1000.0;
                        pickupReminderDistanceAttributeId = rec.getInt("id");
                    } else if (description.contains("Dropoff Notification Distance")) {
                        dropoffReminderDistanceAttributeId = rec.getInt("id");
                        dropOffReminderDistance = Double.parseDouble(rec.getString("attribute")) / 1000.0;
                    } else if (description.contains("Pickup lat")) {
                        pickupLatID = rec.getInt("id");
                        pickupLat = rec.getString("attribute");
                    }else if (description.contains("Pickup long")) {
                        pickupLongID = rec.getInt("id");
                        pickupLong = rec.getString("attribute");
                    }else if (description.contains("Dropoff lat")) {
                        dropofflatID = rec.getInt("id");
                        dropoffLat = rec.getString("attribute");
                    }else if (description.contains("Dropoff long")) {
                        dropofflongID = rec.getInt("id");
                        dropoffLong = rec.getString("attribute");
                        System.out.println("GOTOTOTOTOTTsadsadasdsa "+dropoffLat);

                    }
                }
            } catch (JSONException e) {
                System.out.println("Err");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("EEE");
                e.printStackTrace();
            }
            System.out.println("Pickup reminder distance " + pickUpReminderDistance);
            System.out.println("Drop off reminder " + dropOffReminderDistance);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new GetNotificationData().execute("");
            new GetDeviceId().execute("");
        }
    }

    private class GetNotificationData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String credentials = loggedUserName + ":" + loggedPassword;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder()
                    .url(url+"/api/notifications")
                    .get()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();


            String desValue;
            try {
                System.out.println("Printing data");
                okhttp3.Response response = client.newCall(request).execute();
                System.out.println("AAAAA");
                String responseBody = response.body().string();
                System.out.println(responseBody);
                JSONArray jsonArray = new JSONArray(responseBody);
                JSONObject description;
                String notificationType;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject rec = jsonArray.getJSONObject(i);
                    description = (JSONObject) rec.get("attributes");
                    System.out.println("Printing attr");
                    System.out.println(description.toString());
                    notificationType = description.getString("name");
                    if (notificationType.contains("Pickup Notification")) {
                        pickupNotificationId = rec.getInt("id");
                        String jarr = rec.getString("notificators");
                        if(jarr.contains("firebase"))
                            pickNotificationEnabled  = true;
                        System.out.println("Pick up notification status " + pickNotificationEnabled);
                    } else if (notificationType.contains("Dropoff Notification")) {
                        dropoffNotificationId = rec.getInt("id");
                        String jarr = rec.getString("notificators");
                        if(jarr.contains("firebase"))
                            dropOffNotificationEnabled  = true;
                    } else if (notificationType.contains("School Notification")) {
                        schoolNotificationId = rec.getInt("id");
                        String jarr = rec.getString("notificators");
                        if(jarr.contains("firebase"))
                            schoolNotificationEnabled  = true;
                    }
                }
                System.out.println("Printing Notification IDs");
                System.out.println(pickupNotificationId);
                System.out.println(dropoffNotificationId);
                System.out.println(schoolNotificationId);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Done getting data   adadasdad");
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new GetSavedLocation().execute("");

        }

    }

    private class GetSavedLocation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder()
                    .url(url+"/api/geofences")
                    .get()
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONArray jsonArray = new JSONArray(responseBody);
                System.out.println("Size of array " + jsonArray.length());
                System.out.println("Getting geo locations " + response.code() + "    " + responseBody) ;
                if (jsonArray.length() > 0) {
                    System.out.println("Beforre for loop");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        System.out.println("Inside for loop");
                        JSONObject rec = jsonArray.getJSONObject(i);
                        String description = rec.getString("name");
                        System.out.println(description);
                        if (description.contains("Pickup geo fence")) {
                            System.out.println("inside for loop if statement");
                            pickupGeofenceLocation = rec.getString("area");
                            pickupGeofenceId = rec.getInt("id");
                            pickupGeofenceExists = true;
                            System.out.println("New user false inside if");
                            System.out.println("Pick location :" + pickupGeofenceLocation);
                            System.out.println("savd geo fence id " + pickupGeofenceId);
                        }else if (description.contains("Dropoff geo fence")) {
                            dropoffGeofenceLocation = rec.getString("area");
                            dropoffGeofenceId = rec.getInt("id");
                            dropoffGeofenceExists = true;
                            System.out.println("Is a new user false inside for loop");
                        }else if (description.contains("School geo fence")) {
                            schoolGeofenceLocation = rec.getString("area");
                            schoolGeofenceId = rec.getInt("id");
                            schoolGeofenceExists = true;
                            System.out.println("School geo fence id " + schoolGeofenceId);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            new GetDeviceId().execute("");
        }
    }

    private class GetDeviceId extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            MediaType mediaType = MediaType.parse("application/json");

            String credentials = loggedUserName + ":" + loggedPassword;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder()
                    .url(url+"/api/devices")
                    .get()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONArray jsonArray = new JSONArray(responseBody);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject rec = jsonArray.getJSONObject(i);
                    deviceId = rec.getInt("id");
                    deviceName = rec.getString("name");
                    System.out.println("Getting the device id at userprofile");
                    System.out.println(response.code() + "   " + responseBody);
                    System.out.println("Device id : " + deviceId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            System.out.println("End of get group ID");
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new GetGroupId().execute("");
        }

    }

    private class GetGroupId extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("GetGroupId called");
            System.out.println("Get Group ID");
            MediaType mediaType = MediaType.parse("application/json");

            String credentials = loggedUserName + ":" + loggedPassword;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            Request request = new Request.Builder()
                    .url(url+"/api/groups")
                    .get()
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONArray jsonArray = null;
                JSONObject driverName;
                Boolean vanExists = false;

                jsonArray = new JSONArray(responseBody);

                System.out.println("For loop");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject rec = jsonArray.getJSONObject(i);
                    String groupName = rec.getString("name");
                    System.out.println("Group Name: " + groupName);
                    if (groupName.equals(vehicleNum)) {
                        vanExists = true;
                        groupId = rec.getInt("id");

                        driverName = (JSONObject)rec.get("attributes");
                        setDriverName(driverName.getString("Driver Name"));
                        setDriverContactNumber(driverName.getString("Driver Contact"));
                        System.out.println("Pritnting driver info");
                        System.out.println(getDriverName());
                        System.out.println(getDriverContactNumber());
                        break;
                    }
                }

                System.out.println("Group ID is " + groupId);


                System.out.println("End of get group ID");
                return "Executed";
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new GetCalendarData().execute("");
        }
    }

    private class GetCalendarData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String credentials = loggedUserName +":"+ loggedPassword;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder()
                    .url(url+"/api/calendars/")
                    .get()
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();


            String desValue;
            try {
                okhttp3.Response response = client.newCall(request).execute();
                System.out.println("Getting calendar data at user profile: " + response.code());
                String responseBody = response.body().string();
                JSONArray jsonArray = new JSONArray(responseBody);
                System.out.println("Calendar body: " + responseBody);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject rec = jsonArray.getJSONObject(i);
                    String description = rec.getString("name");
                    System.out.println("Description Name: " + description);
                    if (description.contains("Pickup Calendar")) {
                        setPickupCalendarId(rec.getInt("id"));
                    } else if (description.contains("Dropoff Calendar")) {
                        setDropoffCalendarId(rec.getInt("id"));
                    } else if (description.contains("School Calendar")) {
                        setSchoolCalendarId(rec.getInt("id"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            new DeleteTokenTask().execute();

            System.out.println("Completed User Profile");
        }
    }

//    private class DeleteTokenTask extends AsyncTask<Void, Void, Void>
//    {
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                FirebaseInstanceId.getInstance().deleteInstanceId();
//                FirebaseInstanceId.getInstance().getToken();
//            } catch (IOException e) {
//                System.out.println("Exception deleting token " + e.getStackTrace());
//            }
//            return null;
//        }
//    }



}


