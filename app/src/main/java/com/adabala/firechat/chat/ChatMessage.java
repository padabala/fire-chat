package com.adabala.firechat.chat;

/**
 * Created by adabala on 17/10/2017.
 */

public class ChatMessage {

    private String message;
    private Long timeStamp;
    private String senderId;
    private String receiverId;
    private int status;

    public ChatMessage() {

    }

    public ChatMessage(String message, Long timeStamp, String senderId, String receiverId, int status) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
