package com.ikiugu.pushnotifications.model;

public class Notification extends BaseModel {
    private String senderToken;
    private String recipientUserName;
    private String message;

    public Notification() {
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }

    public String getRecipientUserName() {
        return recipientUserName;
    }

    public void setRecipientUserName(String recipientUserName) {
        this.recipientUserName = recipientUserName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
