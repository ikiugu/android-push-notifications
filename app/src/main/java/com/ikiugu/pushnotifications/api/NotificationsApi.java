package com.ikiugu.pushnotifications.api;


import com.ikiugu.pushnotifications.model.Notification;
import com.ikiugu.pushnotifications.model.Subscription;
import com.ikiugu.pushnotifications.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationsApi {
    @POST("/users")
    Call<User> handleUser(@Body User user);

    @POST("/sendMessage")
    Call<Notification> sendMessage(@Body Notification notification);

    @POST("/subscribe/weather")
    Call<Subscription> subscribeToWeather(@Body Subscription subscription);

    @POST("/handleNotifications")
    Call<User> handleNotifications(@Body User user);

}
