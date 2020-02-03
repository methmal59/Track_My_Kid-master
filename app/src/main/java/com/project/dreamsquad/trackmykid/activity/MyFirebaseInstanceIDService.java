package com.project.dreamsquad.trackmykid.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.dreamsquad.trackmykid.models.UserProfile;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "FirebaseIIDServiceDemo";
    public String[] name;
    String token;
    private int userID = UserProfile.getInstance().getLoggedUserId();
    private String userEmail = UserProfile.getInstance().getLoggedUserName();
    private String parentName = UserProfile.getInstance().getParentName();



    @Override
    public void onTokenRefresh() {
        token = FirebaseInstanceId.getInstance().getToken();
        System.out.println("my firebase token " + token );
        Log.d("Token", "Your token is:  " + token);

        sendtoServer(token);
        System.out.println("TOKEn TOKEN TOKEN111");



    }

    public void sendtoServer(String token){
        System.out.println("Token called");
        new UpdateToken().execute("");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Save to SharedPreferences
        editor.putString("registration_id", token);
        editor.apply();
        System.out.println("TOKEn TOKEN TOKEN");
        System.out.println("Userid: " + userID);
        System.out.println("useremail: "+userEmail);
        System.out.println("parentName: "+parentName);

    }

    private class UpdateToken extends AsyncTask<String, Void, String> {
//        private int userID = UserProfile.getInstance().getLoggedUserId();
//        private String userEmail = UserProfile.getInstance().getLoggedUserName();
//        private String parentName = UserProfile.getInstance().getParentName();
        OkHttpClient client = new OkHttpClient();



        @Override
        protected String doInBackground(String... params) {
            MediaType mediaType = MediaType.parse("application/json");

            System.out.println("Boommmm");
            System.out.println(UserProfile.getInstance().getLoggedUserName());
            System.out.println(UserProfile.getInstance().getLoggedPassword());
//            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
            String credentials = UserProfile.getInstance().getMainCredentials();
            System.out.println("printing token crede");
            System.out.println("Token credentials: " + credentials);
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, "{\n        \"attributes\": {\n        \t\"notificationTokens\" : \""+token+"\"\n        },\n        \"name\": \""+parentName+"\",\n        \"email\": \""+userEmail+"\",\n        \"id\": \""+userID+"\"\n}");
            Request request = new Request.Builder()
                    .url(UserProfile.getInstance().getUrl()+"/api/users/"+userID)
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Token Status" + response.code());
                System.out.println("token: " +response.body().string());
                System.out.println("token: " +response.toString());
            } catch (IOException e) {
                System.out.println("Error saving the token");
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("TOken saved Sucessfully");

        }
    }
}
