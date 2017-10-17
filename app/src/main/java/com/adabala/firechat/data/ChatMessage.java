package com.adabala.firechat.data;

/**
 * Created by adabala on 17/10/2017.
 */

public class ChatMessage {

    private String message;
    private String timeStamp;
    private String senderId;
    private String chatId;

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

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
