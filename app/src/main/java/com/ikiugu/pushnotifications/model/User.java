package com.ikiugu.pushnotifications.model;

public class User extends BaseModel {
    private String userName;
    private String userToken;
    private boolean Notification;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public boolean getNotification() {
        return Notification;
    }

    public void setNotification(boolean notification) {
        Notification = notification;
    }
}
