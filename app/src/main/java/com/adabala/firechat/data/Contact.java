package com.adabala.firechat.data;

/**
 * Created by adabala on 22/10/2017.
 */

public class Contact {

    String name;
    String phoneNumber;
    Boolean isFriend = false;

    public Contact() {

    }

    public Contact(String name, String phoneNumber, Boolean isFriend) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isFriend = isFriend;
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
}
