package com.project.dreamsquad.trackmykid.fragments;

/*
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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.models.Device;
import com.project.dreamsquad.trackmykid.others.MainApplication;
import com.project.dreamsquad.trackmykid.others.WebService;
import com.project.dreamsquad.trackmykid.others.WebServiceCallback;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DevicesFragment extends ListFragment {

    public static final String EXTRA_DEVICE_ID = "deviceId";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MainApplication application = new MainApplication();
        application.getServiceAsync(new MainApplication.GetServiceCallback() {
            @Override
            public void onServiceReady(OkHttpClient client, Retrofit retrofit, WebService service) {
                service.getDevices().enqueue(new WebServiceCallback<List<Device>>(getContext()) {
                    @Override
                    public void onSuccess(Response<List<Device>> response) {
                        setListAdapter(new ArrayAdapter<>(getContext(), R.layout.list_item, android.R.id.text1, response.body()));
                    }
                });
            }


            @Override
            public boolean onFailure() {
                return false;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Activity activity = getActivity();
        System.out.println("Indise List onclick");
        if (activity != null) {
            System.out.println("device is not null");
            Device device = (Device) getListAdapter().getItem(position);
            //activity.setResult(MainFragment.RESULT_SUCCESS, new Intent().putExtra(EXTRA_DEVICE_ID, device.getId()));
            activity.finish();
        }
        System.out.println("actitivy is nullcd3");
    }

}
