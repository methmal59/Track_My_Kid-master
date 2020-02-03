package com.project.dreamsquad.trackmykid.others;

import com.project.dreamsquad.trackmykid.models.Command;
import com.project.dreamsquad.trackmykid.models.CommandType;
import com.project.dreamsquad.trackmykid.models.Device;
import com.project.dreamsquad.trackmykid.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WebService {

    @FormUrlEncoded
    @POST("/api/session")
    Call<User> addSession(@Field("email") String email, @Field("password") String password);

    @GET("/api/devices")
    Call<List<Device>> getDevices();

    @GET("/api/commandtypes")
    Call<List<CommandType>> getCommandTypes(@Query("deviceId") long deviceId);

    @POST("/api/commands")
    Call<Command> sendCommand(@Body Command command);
}
