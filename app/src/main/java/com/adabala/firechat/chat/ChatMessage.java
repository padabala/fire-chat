package com.adabala.firechat.chat;

/**
 * Created by adabala on 17/10/2017.
 */

public class ChatMessage {

    private String message;
    private String timeStamp;
    private String senderId;
    private String chatSessionId;

    public ChatMessage() {

    }

    public ChatMessage(String message, String senderId, String timeStamp) {

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

    public String getChatSessionId() {
        return chatSessionId;
    }

    public void setChatSessionId(String chatSessionId) {
        this.chatSessionId = chatSessionId;
    }
}
