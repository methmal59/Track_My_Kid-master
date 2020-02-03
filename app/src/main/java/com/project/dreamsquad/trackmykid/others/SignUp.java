package com.project.dreamsquad.trackmykid.others;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.activity.LoginActivity;
import com.project.dreamsquad.trackmykid.activity.MainActivity;
import com.project.dreamsquad.trackmykid.models.UserProfile;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Retrofit;
import java.io.IOException;


public class SignUp extends AppCompatActivity {
    private TextView passInput;
    private TextView confirmPasswordInput;
    private TextView parentNameInput;
    private TextView userInput;
    private TextView parentMobileInput;
    private TextView v1, v2, v3, v4, v5, v6, v7;
    private TextView goToLogin;
    private ImageView backButtonSignup;
    private Button button_signup;

    private String loginUsername;
    private String loginPassword;
    private String parentName;
    private String confirmPassword;
    private String vanNumber;
    private String parentMobile;
    private String van1, van2, van3, van4, van5, van6, van7;
    SharedPreferences preferences;
    ProgressDialog progress;
    static MainApplication application;
    String token;



    int returnedUserId;
    int groupId;
    int deviceId;
    UserProfile userProfile;

    OkHttpClient client = new OkHttpClient();

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(v1.getText().length() == 1)
                v2.requestFocus();
        }
    };

    private TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(v2.getText().length() == 1)
                v3.requestFocus();
        }
    };

    private TextWatcher textWatcher3 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(v3.getText().length() == 1)
                v4.requestFocus();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        parentNameInput = (TextView) findViewById(R.id.parentName);
        userInput = (TextView) findViewById(R.id.email);
        passInput = (TextView) findViewById(R.id.password);
        confirmPasswordInput = (TextView) findViewById(R.id.confirmPassword);
        parentMobileInput = (TextView) findViewById(R.id.parentMobile);
        button_signup = (Button) findViewById(R.id.button_signup);
        goToLogin = (TextView) findViewById(R.id.goToLogin);
        backButtonSignup = (ImageView) findViewById(R.id.backButtonSignup);


        v1 = (EditText) findViewById(R.id.van1);
        v2 = (EditText) findViewById(R.id.van2);
        v3 = (EditText) findViewById(R.id.van3);
        v4 = (EditText) findViewById(R.id.van4);
        v5 = (EditText) findViewById(R.id.van5);
        v6 = (EditText) findViewById(R.id.van6);
        v7 = (EditText) findViewById(R.id.van7);

//        v1.addTextChangedListener(textWatcher);
//        v2.addTextChangedListener(textWatcher2);
//        v3.addTextChangedListener(textWatcher3);

        preferences = getApplication().getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        parentNameInput.requestFocus();

        parentMobileInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(parentMobileInput.getText().length()==10)
                    v1.requestFocus();
                return false;
            }
        });

        backButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        v1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(v1.getText().toString().length() == 1){
                    v2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });

        v2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(v2.getText().toString().length() == 1){
                    v3.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });

        v3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(v3.getText().toString().length() == 1){
                    v4.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
//        v2.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if(v2.getText().toString().length() == 1)
//                    v3.requestFocus();
//                return false;
//            }
//        });
//
//        v3.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if(v3.getText().toString().length() == 1)
//                    v4.requestFocus();
//                return false;
//            }
//        });

        v4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                System.out.println(v4.getText().toString());
                if(v4.getText().length() == 1)
                    v5.requestFocus();
                return false;
            }
        });

        v5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                System.out.println(v5.getText().toString());
                if(v5.getText().length() == 1)
                    v6.requestFocus();
                return false;
            }
        });

        v6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(v6.getText().length() == 1)
                    v7.requestFocus();
                return false;
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void displayAlert(String text){
        String msg = text + " Please try again!";
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SignUp.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SignUp.this);
        }
        builder.setTitle("Signup Error!")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public boolean validateData(){
        if(parentName.isEmpty() || loginUsername.isEmpty() || loginPassword.isEmpty() || confirmPassword.isEmpty() || parentMobile.isEmpty()){
            displayAlert("Only Vehicle Number can be Empty,");
            return false;
        }else{
            Validation_Signup signupChecker = new Validation_Signup();
            int validCode = signupChecker.validateDataSignup(parentName,loginUsername,loginPassword,confirmPassword,parentMobile);

            if(validCode == 101){
                displayAlert("You can only have alphabetical characters inside Parent Name,");
                return false;
            }
            else if( validCode== 102) {
                displayAlert("User name should be at least 6 characters longs, and can only have alphabetical and numerical characters,");
                return false;
            }
            else if( validCode == 103) {
                displayAlert("Confirm password doesn't match with the password you entered above,");
                return false;
            }
            else if(validCode == 104) {
                displayAlert("Password must have a number, a letter, at least 6 characters long and shouldn't have any spaces,");
                return false;
            }
            else if(validCode == 105) {
                displayAlert("Phone Number should exactly be 10 NUMBERS,");
                return false;
            }
            else
                return true;
        }
    }

    public void onSignUp(View view){
        System.out.println("signup");

        parentName = parentNameInput.getText().toString();
        loginUsername = userInput.getText().toString();
        loginPassword = passInput.getText().toString();
        confirmPassword = confirmPasswordInput.getText().toString();
        parentMobile = parentMobileInput.getText().toString();

        van1 = v1.getText().toString();
        van2 = v2.getText().toString();
        van3 = v3.getText().toString();
        van4 = v4.getText().toString();
        van5 = v5.getText().toString();
        van6 = v6.getText().toString();
        van7 = v7.getText().toString();

        String vanLetters = "0";

        if(van1.isEmpty() || van2.isEmpty() || van3.isEmpty()){
            if(van1.isEmpty())
                vanLetters = van2+van3;
            else if(van2.isEmpty())
                vanLetters = van1+van3;
            else if(van3.isEmpty())
                vanLetters= van1+van2;
        }else
            vanLetters = van1+van2+van3;

        vanNumber = vanLetters+van4+van5+van6+van7;


        if(!validateData())
            return;

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.app_loadingSignup));
        progress.setCancelable(false);
        progress.show();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n\"name\": \"" + parentName + "\",\r\n\"email\":\"" + loginUsername + "\",\r\n\"password\": \""+loginPassword+"\"\r\n}");

        Request request = new Request.Builder()
                .url(UserProfile.getInstance().getUrl()+"/api/users")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if (progress.isShowing()) {
                            progress.dismiss();
                            System.out.println("After progress drismdddddissed");
                        }
                        Toast.makeText(getApplicationContext(), "Network Error, please check your network connection and try again!", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("444");
                if (!response.isSuccessful()) {
                    System.out.println("SQL Error");
                    if (progress.isShowing()) {
                        progress.dismiss();
                        System.out.println("After progress dismissed");
                    }
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "A user from that username already exists, please enter a different username", Toast.LENGTH_LONG).show();
                        }
                    });
                    throw new IOException("Unexpected code " + response);
                }

                else if(response.code() == 200) {
                    try {
                        if (progress.isShowing()) {
                            progress.dismiss();
                            System.out.println("After progress dismissed");
                        }
                        UserProfile.getInstance().setLoggedUserName(loginUsername);
                        UserProfile.getInstance().setLoggedPassword(loginPassword);
                        userProfile = UserProfile.getInstance();
                        UserProfile.getInstance().setVehicleNum(vanNumber);
                        UserProfile.getInstance().setParentName(parentName);
                        UserProfile.getInstance().setParentContactNumber(parentMobile);
                        UserProfile.getInstance().setVehicleNum(vanNumber);


                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        returnedUserId = Integer.parseInt(jsonObject.getString("id"));
                        userProfile.setLoggedUserId(returnedUserId);

//                        preferences
//                                .edit()
//                                .putBoolean(MainApplication.PREFERENCE_AUTHENTICATED, true)
//                                .putString(MainApplication.PREFERENCE_EMAIL, loginUsername)
//                                .putString(MainApplication.PREFERENCE_PASSWORD, loginPassword)
//                                .apply();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(MainApplication.PREFERENCE_AUTHENTICATED,true);
                        editor.putString(MainApplication.PREFERENCE_EMAIL, loginUsername);
                        editor.putString(MainApplication.PREFERENCE_PASSWORD, loginPassword);
                        editor.apply();

                    } catch (JSONException e) {
                        if (progress.isShowing())
                            progress.dismiss();
                        e.printStackTrace();
                    }
                    loginUser();
                }
            }
        });
    }

    public void loginUser(){
//        try {
//            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        userProfile = UserProfile.getInstance();
        userProfile.setLoggedUserName(loginUsername);
        userProfile.setLoggedPassword(loginPassword);
        userProfile.setAuthenticated(true);

        login();
    }

    private void login() {
        System.out.println("inside login function");
        try {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                    token = preferences1.getString("registration_id", null);
                    new SetToken().execute();
                    System.out.println("Inside run");
                    application = new MainApplication();

                    application.getServiceAsync(new MainApplication.GetServiceCallback() {

                        @Override
                        public void onServiceReady(OkHttpClient client, Retrofit retrofit, WebService service) {
                            System.out.println("222");

                            getApplication();
                            GoToMainClass();

                    new AssignAttributes().execute("");
                    new GetGroupId().execute("");

//                    new GetCalendarData().execute("");
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }

                        @Override
                        public boolean onFailure() {
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    System.out.println("333");

                                    if (progress.isShowing()) {
                                        progress.dismiss();
                                        System.out.println("After progress dismissed");
                                    }
                                    Toast.makeText(getApplicationContext(), "Unable to sign you up, please try again later", Toast.LENGTH_LONG).show();
                                }
                            });
                            System.out.println("Failure at login when signed up");
                            return false;
                        }

                    });
                }
            });
            System.out.println("111");

        }catch (Exception e){
            System.out.println("Exception at login function 227");
            e.printStackTrace();
        }
    }

    public void GoToMainClass(){
        UserProfile.getInstance().setIsaNewUser(true);

        UserProfile.getInstance().setSignedUpUser(true);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private class AssignAttributes extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("Assign Attributes called");
            try {
                MediaType mediaType = MediaType.parse("application/json");
                Response response;
                JSONObject jsonObject =null;
                String credentials = userProfile.getMainCredentials();
                String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


                String descriptionValue = "NIL";
                String attributeValue = "NIL";

                for(int count = 0; count <14; count++) {
                    if(count == 0){
                        descriptionValue = loginUsername + "'s Parent Name: ";
                        attributeValue = parentName;
                        System.out.println("11");
                    }
                    else if(count == 1){
                        userProfile.setParentNameAttributeId(jsonObject.getInt("id"));
                        descriptionValue = loginUsername + "'s van ID: ";
                        attributeValue = vanNumber;
                        System.out.println("12");
                    }
                    else if(count == 2){
                        userProfile.setVehicleNumAttributeId(jsonObject.getInt("id"));
                        descriptionValue = loginUsername + "'s Mobile: ";
                        attributeValue = parentMobile;
                        System.out.println("13");
                    }
                    else if(count == 3){
                        userProfile.setMobileAttributeId(jsonObject.getInt("id"));
                        descriptionValue = loginUsername + "'s Email: ";
                        attributeValue = "";
                        System.out.println("14");
                    }
                    else if(count == 4){
                        userProfile.setEmailAttributeId(jsonObject.getInt("id"));
                        descriptionValue = loginUsername + "'s Home Address: ";
                        attributeValue = "";
                        System.out.println("15");
                    }
                    else if(count == 5){
                        userProfile.setAddressAttributeId(jsonObject.getInt("id"));
                        descriptionValue = loginUsername + "'s Kids Name: ";
                        attributeValue = "";
                        System.out.println("16");
                    }
                    else if(count == 6){
                        userProfile.setKidsNameAttributeId(jsonObject.getInt("id"));
                        descriptionValue = loginUsername + "'s Kids Birthday: ";
                        attributeValue = "";
                        System.out.println("17");
                    }
                    else if(count == 7){
                        userProfile.setKidsBirthdayAttributeId(jsonObject.getInt("id"));
                        descriptionValue = loginUsername + "'s Kids School: ";
                        attributeValue = "";
                        System.out.println("18");
                    }else if(count == 8){
                        userProfile.setKidsSchoolAttributeId(jsonObject.getInt("id"));
                        descriptionValue = loginUsername + "'s : Pickup Notification Distance: ";
                        attributeValue = "1000";
                        System.out.println("19");
                    }else if(count == 9){
                        userProfile.setPickupReminderDistanceAttributeId(jsonObject.getInt("id"));
                        descriptionValue = loginUsername + "'s Dropoff Notification Distance: ";
                        attributeValue = "1000";
                        System.out.println("20");
                    }else if(count == 10){
                        descriptionValue = loginUsername + "'s Pickup lat: ";
                        attributeValue = "6.9271";
                    }else if(count == 11){
                        descriptionValue = loginUsername + "'s Pickup long: ";
                        attributeValue = "79.8612";
                    }else if(count == 12){
                        descriptionValue = loginUsername + "'s Dropoff lat: ";
                        attributeValue = "6.9271";
                    }else if(count == 13){
                        descriptionValue = loginUsername + "'s Dropoff long: ";
                        attributeValue = "79.8612";
                    }

                    RequestBody body = RequestBody.create(mediaType, "\n    {\n      \n        " +
                            "\"description\": \"" + descriptionValue + "\",\n        " +
                            "\"attribute\":   \"" + attributeValue + "\",\n        " +
                            "\"expression\": \"NIL\",\n        " +
                            "\"type\": \"string\"\n" +
                            "    }\n");

                    Request request = new Request.Builder()
                            .url(userProfile.getUrl()+"/api/attributes/computed")
                            .post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", basic)
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {

                        response = client.newCall(request).execute();
                        System.out.println("Printing response");
                        System.out.println(response);
                        System.out.println("descriptionValue " + descriptionValue);
                        System.out.println("attributeValue  " + attributeValue);
                        System.out.println("credentials"  +credentials);

                        jsonObject = new JSONObject(response.body().string());
                    } catch (Exception e) {
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if (progress.isShowing())
                                    progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Error Saving your Data!", Toast.LENGTH_LONG).show();                            }
                        });
                        System.out.println("Exception as assigning attributes 663");
                        e.printStackTrace();
                    }

                    body = RequestBody.create(mediaType, "{\n\t\"userId\": \""+UserProfile.getInstance().getLoggedUserId()+"\", \n\t\"attributeId\" :\""+jsonObject.getInt("id")+"\"\n}");
                    request = new Request.Builder()
                            .url("http://5.189.154.215:8082/api/permissions")
                            .post(body)
                            .addHeader("Authorization", basic)
                            .build();

                    try {
                        response = client.newCall(request).execute();
                    } catch (Exception e) {
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if (progress.isShowing())
                                    progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Error Saving your Data!", Toast.LENGTH_LONG).show();                            }
                        });
                        System.out.println("Exception as assigning attributes 687");
                        e.printStackTrace();
                    }


                }

                userProfile.setDropoffReminderDistanceAttributeId(jsonObject.getInt("id"));
            } catch (Exception e) {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if (progress.isShowing())
                            progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Error Saving your data", Toast.LENGTH_LONG).show();                    }
                });
                System.out.println("Exception at assgining attributes 704");
                e.printStackTrace();
            }
            System.out.println("End of Assign Attributes");
            return "Executed";
        }
    }


    private class GetGroupId extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("GetGroupId called");
            System.out.println("Get Group ID");
            MediaType mediaType = MediaType.parse("application/json");

            String credentials = userProfile.getMainCredentials();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+ "/api/groups?all=true")
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
                try {
                    jsonArray = new JSONArray(responseBody);

                    System.out.println("For loop");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject rec = jsonArray.getJSONObject(i);
                        String groupName = rec.getString("name");
                        System.out.println("Group Name: " + groupName);
                        if(groupName.equals(vanNumber)) {
                            vanExists = true;
                            groupId = rec.getInt("id");
                            userProfile.setGroupId(groupId);
                            driverName = (JSONObject)rec.get("attributes");
                            userProfile.setDriverName(driverName.getString("Driver Name"));
                            userProfile.setDriverContactNumber(driverName.getString("Driver Contact"));
                            System.out.println("Printintgnignigngn" + driverName.getString("Driver Name"));
                            System.out.println("Printintgnignigngn" + driverName.getString("Driver Contact"));
                            break;
                        }
                    }
                    if(vanExists) {
                        new Thread(new Runnable() {
                            public void run() {
                                // a potentially time consuming task
                                new AssignDevice().execute("");
                            }
                        }).start();
                    }
                    else{
                        System.out.println("A group doesnt exists");
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                System.out.println("Inside run");
                                Toast.makeText(getApplicationContext(), "A van with the given Registration number does not exist. Change it in the profile page,", Toast.LENGTH_LONG).show();
                                if(progress.isShowing())
                                    progress.dismiss();
//                                GoToMainClass();
                            }
                        });

                    }
                    System.out.println("Group ID is " + groupId);
                } catch (JSONException e) {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if (progress.isShowing())
                                progress.dismiss();
                            Toast.makeText(getApplicationContext(), "We can't find your School Van at the moment", Toast.LENGTH_LONG).show();
                        }
                    });
                    System.out.println("Json Exception at 356");
                    e.printStackTrace();
                }

            } catch (IOException e) {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if (progress.isShowing())
                            progress.dismiss();
                        Toast.makeText(getApplicationContext(), "We can't find your School Van at the moment", Toast.LENGTH_LONG).show();
                    }
                });                System.out.println("IOException at 361");
                e.printStackTrace();
            }
            System.out.println("End of get group ID");
            return "Executed";
        }
    }

    private class AssignDevice extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            System.out.println("Inside assign device");


            System.out.println("Inside assignDevice");
            System.out.println("user id: " + returnedUserId);
            System.out.println("Group id: " + groupId);
            MediaType mediaType = MediaType.parse("application/json");
            String credentials = userProfile.getMainCredentials();
            System.out.println("");
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            RequestBody body = RequestBody.create(mediaType, "{\n    \t\"userId\" :\"" + returnedUserId + "\",\n        \"groupId\": \""+groupId+"\"\n        }\n");
            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/permissions")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", basic)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Group assigned I think");
                System.out.println(response.code());
                System.out.println(response.body().string());
                if(response.code() == 200 || response.code() == 204) {
                    new Thread(new Runnable() {
                        public void run() {
                            // a potentially time consuming task
                            new GetDeviceId().execute("");
                        }
                    }).start();
                }
                else{
                    if (progress.isShowing())
                        progress.dismiss();
                    Toast.makeText(getApplicationContext(), "We can't find your School Van at the moment", Toast.LENGTH_LONG).show();
                }


            } catch (IOException e) {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if (progress.isShowing())
                            progress.dismiss();
                        Toast.makeText(getApplicationContext(), "We can't find your School Van at the moment", Toast.LENGTH_LONG).show();
                    }
                });
                System.out.println("Exception at Assign Device 409");
                e.printStackTrace();
            }

            System.out.println("End of assign device");
            return "Executed";
        }
    }

    private class GetDeviceId extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            MediaType mediaType = MediaType.parse("application/json");

            String credentials = userProfile.getLoggedUserName() + ":" + userProfile.getLoggedPassword();
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder()
                    .url(userProfile.getUrl()+"/api/devices")
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
                    System.out.println("Getting the device id at signup");
                    System.out.println(response.code() + "   " + responseBody);
                    System.out.println("Device id " + deviceId);
                    if(response.code() == 200)
                        userProfile.setDeviceId(deviceId);
                    else{
                        if (progress.isShowing())
                            progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Can't find the device ID", Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                if (progress.isShowing())
                    progress.dismiss();
                Toast.makeText(getApplicationContext(), "Can't find the device ID", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            System.out.println("End of get group ID");
            return "Executed";
        }
    }

    private class SetToken extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
                FirebaseInstanceId.getInstance().getToken();
            } catch (IOException e) {
                System.out.println("Exception deleting token " + e.getStackTrace());
            }
            return null;
        }
    }
//    private class UpdateToken extends AsyncTask<String, Void, String> {
//        private int userID = UserProfile.getInstance().getLoggedUserId();
//        private String userEmail = UserProfile.getInstance().getLoggedUserName();
//        private String parentName = UserProfile.getInstance().getParentName();
//        OkHttpClient client = new OkHttpClient();
//
//        @Override
//        protected String doInBackground(String... params) {
//            MediaType mediaType = MediaType.parse("application/json");
//
//            String credentials = UserProfile.getInstance().getLoggedUserName() + ":" + UserProfile.getInstance().getLoggedPassword();
//            System.out.println("Token credentials: " + credentials);
//            final String basic =
//                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//            RequestBody body = RequestBody.create(mediaType, "{\n        \"attributes\": {\n        \t\"notificationTokens\" : \""+token+"\"\n        },\n        \"name\": \""+parentName+"\",\n        \"email\": \""+userEmail+"\",\n        \"id\": \""+userID+"\"\n}");
//            Request request = new Request.Builder()
//                    .url(UserProfile.getInstance().getUrl()+"/api/users/"+userID)
//                    .put(body)
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Authorization", basic)
//                    .addHeader("cache-control", "no-cache")
//                    .build();
//
//            try {
//                Response response = client.newCall(request).execute();
//                System.out.println("Token Status" + response.code());
//                System.out.println("token: " +response.body().string());
//                System.out.println("token: " +response.toString());
//            } catch (IOException e) {
//                System.out.println("Error saving the token");
//                e.printStackTrace();
//            }
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            System.out.println("TOken saved Sucessfully");
//
//        }
//    }

//    private class GiveCalendarAccess extends AsyncTask<String, Void, String> {
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            int userId = returnedUserId;
//            int calendarId=-1;
//            MediaType mediaType = MediaType.parse("application/json");
//            String credentials =  "admin:admin";
//            final String basic =
//                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//
//            for(int calId = 0; calId < 3; calId++) {
//                if(calId == 0)
//                    calendarId = userProfile.getPickupCalendarId();
//                else if(calId == 1)
//                    calendarId = userProfile.getDropoffCalendarId();
//                else if(calId == 2)
//                    calendarId = userProfile.getSchoolCalendarId();
//
//                RequestBody body = RequestBody.create(mediaType, "{\n\t\"userId\" : \""+userId+"\",\n\t\"calendarId\" :\""+calendarId+"\"\n}");
//                Request request = new Request.Builder()
//                        .url(userProfile.getUrl()+"/api/permissions")
//                        .post(body)
//                        .addHeader("Content-Type", "application/json")
//                        .addHeader("Authorization", basic)
//                        .addHeader("cache-control", "no-cache")
//                        .build();
//                try {
//                    Response response = client.newCall(request).execute();
//                    System.out.println("Calendar access to user " + response.code());
//                } catch (IOException e) {
//                    Toast.makeText(getApplicationContext(), "Unable to save the location, please try again later", Toast.LENGTH_LONG).show();
//
//
//                    System.out.println("Exception at Pickup");
//                    e.printStackTrace();
//                }
//            }
//
//            System.out.println("End of assign device");
//            return "Executed";
//        }
//    }
//
//    private class GetCalendarData extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            String credentials = "admin:admin";
//            System.out.println("Credentials are " + credentials);
//            final String basic =
//                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//
//            Request request = new Request.Builder()
//                    .url(userProfile.getUrl()+"/api/calendars/")
//                    .get()
//                    .addHeader("Authorization", basic)
//                    .addHeader("cache-control", "no-cache")
//                    .build();
//
//            String desValue;
//            try {
//                okhttp3.Response response = client.newCall(request).execute();
//                System.out.println("Getting calendar data: " + response.code());
//                String responseBody = response.body().string();
//                JSONArray jsonArray = new JSONArray(responseBody);
//                System.out.println("Calendar body: " + responseBody);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject rec = jsonArray.getJSONObject(i);
//                    String description = rec.getString("name");
//                    System.out.println("Description Name: " + description);
//                    if (description.contains("Pickup Calendar")) {
//                        userProfile.setPickupCalendarId(rec.getInt("id"));
//                        System.out.println("Printing calendar IDs: "+rec.getInt("id"));
//                    } else if (description.contains("Dropoff Calendar")) {
//                        userProfile.setDropoffCalendarId(rec.getInt("id"));
//                        System.out.println("Printing calendar IDs: "+rec.getInt("id"));
//                    } else if (description.contains("School Calendar")) {
//                        userProfile.setSchoolCalendarId(rec.getInt("id"));
//                        System.out.println("Printing calendar IDs: "+rec.getInt("id"));
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
////            new GiveCalendarAccess().execute("");
//        }
//    }




}







