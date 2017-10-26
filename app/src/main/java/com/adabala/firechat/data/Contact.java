package com.adabala.firechat.data;

/**
 * Created by adabala on 22/10/2017.
 */

public class Contact {

    String name;
    String phoneNumber;
    Boolean isFriend = false;
    String chatHead;

    public Contact() {

    }

    public Contact(String name, String phoneNumber, Boolean isFriend) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isFriend = isFriend;
    }

    public Contact(String name, String phoneNumber, Boolean isFriend, String chatHead) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isFriend = isFriend;
        this.chatHead = chatHead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean isFriend() {
        return isFriend;
    }

    public void setFriend(Boolean friend) {
        isFriend = friend;
    }

    public String getChatHead() {
        return chatHead;
    }

    public void setChatHead(String chatHead) {
        this.chatHead = chatHead;
    }
}
