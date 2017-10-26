package com.adabala.firechat.chat;

/**
 * Created by adabala on 17/10/2017.
 */

public class ChatMessage {

    private String message;
    private String timeStamp;
    private String senderId;
    private String receiverId;

    public ChatMessage() {

    }

    public ChatMessage(String message, String timeStamp, String senderId, String receiverId) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getTimeStampLong() {
        return Long.parseLong(this.timeStamp);
    }
}
