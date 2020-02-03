package com.project.dreamsquad.trackmykid.fragments;/*
 * Copyright 2015 - 2016 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.Device;
import com.project.dreamsquad.trackmykid.models.Position;
import com.project.dreamsquad.trackmykid.models.Update;
import com.project.dreamsquad.trackmykid.models.User;
import com.project.dreamsquad.trackmykid.others.MainApplication;
import com.project.dreamsquad.trackmykid.others.WebService;
import com.project.dreamsquad.trackmykid.others.WebServiceCallback;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;
import retrofit2.Retrofit;

public class  MainFragment extends SupportMapFragment implements OnMapReadyCallback {

    public static final int REQUEST_DEVICE = 1;
    public static final int RESULT_SUCCESS = 1;

    //Github Traccar Manager

    public final static String EVENT_LOGIN = "eventLogin";
    public final static String EVENT_TOKEN = "eventToken";
    public final static String KEY_TOKEN = "keyToken";
    private final static int REQUEST_FILE_CHOOSER = 1;
    private AssetManager assetManager;
    private LocalBroadcastManager broadcastManager;


    private GoogleMap map;
    private Handler handler = new Handler();
    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<Long, Device> devices = new HashMap<>();
    private Map<Long, Position> positions = new HashMap<>();
    private Map<Long, Marker> markers = new HashMap<>();

    private WebSocketCall webSocket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("Inside on create");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getMapAsync(this);

        assetManager = getActivity().getAssets();
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main, menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_devices:
//                startActivityForResult(new Intent(getContext(), DevicesActivity.class), REQUEST_DEVICE);
//                return true;
//            case R.id.action_logout:
//                PreferenceManager.getDefaultSharedPreferences(getContext())
//                        .edit().putBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false).apply();
//                ((MainApplication) getActivity().getApplication()).removeService();
//                getActivity().finish();
//                startActivity(new Intent(getContext(), LoginActivity.class));
//                return true;
//            case R.id.action_changePassword:
//                startActivity(new Intent(getContext(), ChangePassword.class));
//                return true;
//            case R.id.action_profile:
//                startActivity(new Intent(getContext(), EditProfileInfo.class));
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Inside Activity Result");
        if (requestCode == REQUEST_DEVICE && resultCode == RESULT_SUCCESS) {
            System.out.println("request and result is true;");
            long deviceId = data.getLongExtra(DevicesFragment.EXTRA_DEVICE_ID, 0);
            System.out.println("DEVICE ID ISSSS       " + deviceId );

            System.out.println(positions.size());
            System.out.println(Arrays.toString(positions.keySet().toArray()));
            System.out.println(Arrays.toString(positions.keySet().toArray()));


            Position position = positions.get(deviceId);
            if (position != null) {
                System.out.println("Position is not null");
                map.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(position.getLatitude(), position.getLongitude())));
                markers.get(deviceId).showInfoWindow();
            }
            System.out.println("end of on acitity reuslut");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("Inside on Mpa reary");
        map = googleMap;
        map.setTrafficEnabled(true);

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                @SuppressLint("RestrictedApi") View view = getLayoutInflater(null).inflate(R.layout.view_info, null);
                ((TextView) view.findViewById(R.id.title)).setText(marker.getTitle());
                ((TextView) view.findViewById(R.id.details)).setText(marker.getSnippet());
                return view;
            }
        });

        createWebSocket();
    }

    private String formatDetails(Position position) {
        final MainApplication application = new MainApplication();
        final User user = application.getUser();

        SimpleDateFormat dateFormat;
        if(user.getTwelveHourFormat()) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        String speedUnit = getString(R.string.user_kn);
        double factor = 1;
        if (user.getSpeedUnit() != null) {
            switch (user.getSpeedUnit()) {
                case "kmh":
                    speedUnit = getString(R.string.user_kmh);
                    factor = 1.852;
                    break;
                case "mph":
                    speedUnit = getString(R.string.user_mph);
                    factor = 1.15078;
                    break;
                default:
                    speedUnit = getString(R.string.user_kn);
                    factor = 1;
                    break;
            }
        }
        double speed = position.getSpeed() * factor;

        return new StringBuilder()
                .append(getString(R.string.position_time)).append(": ")
                .append(dateFormat.format(position.getFixTime())).append('\n')
                .append(getString(R.string.position_latitude)).append(": ")
                .append(String.format("%.5f", position.getLatitude())).append('\n')
                .append(getString(R.string.position_longitude)).append(": ")
                .append(String.format("%.5f", position.getLongitude())).append('\n')
                .append(getString(R.string.position_altitude)).append(": ")
                .append(String.format("%.1f", position.getAltitude())).append('\n')
                .append(getString(R.string.position_speed)).append(": ")
                .append(String.format("%.1f", speed)).append(' ')
                .append(speedUnit).append('\n')
                .append(getString(R.string.position_course)).append(": ")
                .append(String.format("%.1f", position.getCourse()))
                .toString();
    }

    private void handleMessage(String message) throws IOException {
        System.out.println("Message is "+message);
        if(message.contains("events")){
            System.out.println("YUPPPPPP");
            return;
        }
        Update update = objectMapper.readValue(message, Update.class);
        if (update != null && update.positions != null) {
            for (Position position : update.positions) {
                long deviceId = position.getDeviceId();
                if (devices.containsKey(deviceId)) {
                    LatLng location = new LatLng(position.getLatitude(), position.getLongitude());
                    Marker marker = markers.get(deviceId);

                    CameraUpdate center=
                            CameraUpdateFactory.newLatLng(new LatLng(location.latitude, location.longitude));
                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);
                    map.moveCamera(center);
                    map.animateCamera(zoom);

                    if (marker == null) {
                        Activity activity = getActivity();
                        Bitmap smallMarker = null;
                        if(activity != null) {
                            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.schoolvan_icon);
                            Bitmap.createBitmap(bm);
                            smallMarker = Bitmap.createScaledBitmap(bm, 100, 100, false);
                        }
                        marker = map.addMarker(new MarkerOptions()
                                .title(devices.get(deviceId).getName())
                                        .position(location)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        );
                        markers.put(deviceId, marker);
                    } else {
                        marker.setPosition(location);
                    }
                    //marker.setSnippet(formatDetails(position));
                    positions.put(deviceId, position);
                }
            }
        }
    }



    @Override
    public void onDestroy() {
        System.out.println("Inside on destroy");
        super.onDestroy();
        if (webSocket != null) {
            webSocket.cancel();
        }
    }

    private void reconnectWebSocket() {
        System.out.println("Inside reconnect web socket");
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    createWebSocket();
                }
            }
        });
    }

    private void createWebSocket() {
        System.out.println("Inside create web socket");
        final MainApplication application = new MainApplication();
        application.getServiceAsync(new MainApplication.GetServiceCallback() {
            @Override
            public void onServiceReady(final OkHttpClient client, final Retrofit retrofit, WebService service) {
                System.out.println("Inside on service ready");
                User user = application.getUser();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(user.getLatitude(), user.getLongitude()), user.getZoom()));
                service.getDevices().enqueue(new WebServiceCallback<List<Device>>(getContext()) {
                    @Override
                    public void onSuccess(retrofit2.Response<List<Device>> response) {
                        System.out.println("Inside on success");
                        for (Device device : response.body()) {
                            if (device != null) {
                                devices.put(device.getId(), device);
                            }
                        }

                        Request request = new Request.Builder().url(retrofit.baseUrl().url().toString() + "api/socket").build();
                        webSocket = WebSocketCall.create(client, request);
                        webSocket.enqueue(new WebSocketListener() {
                            @Override
                            public void onOpen(WebSocket webSocket, Response response) {
                            }

                            @Override
                            public void onFailure(IOException e, Response response) {
                                reconnectWebSocket();
                            }

                            @Override
                            public void onMessage(ResponseBody message) throws IOException {
                                final String data = message.string();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            System.out.println("Data  incoming " + Calendar.getInstance().getTime());
                                            handleMessage(data);
                                        } catch (IOException e) {
                                            Log.w(MainFragment.class.getSimpleName(), e);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onPong(Buffer payload) {
                            }

                            @Override
                            public void onClose(int code, String reason) {
                                reconnectWebSocket();
                            }
                        });
                    }
                });
            }

            @Override
            public boolean onFailure() {
                return false;
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra(KEY_TOKEN);
            String code = "updateNotificationToken && updateNotificationToken('" + token + "')";
//            getWebView().evaluateJavascript(code, null);
            System.out.println("Brodcast reciever code    : "+code);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(EVENT_TOKEN);
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }


}

