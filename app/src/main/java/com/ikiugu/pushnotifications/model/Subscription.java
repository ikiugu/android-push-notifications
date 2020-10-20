package com.ikiugu.pushnotifications.model;

public class Subscription extends BaseModel {
    private boolean subscribe;
    private String userName;

    public Subscription() {
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
