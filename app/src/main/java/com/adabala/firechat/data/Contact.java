package com.adabala.firechat.data;

import com.adabala.firechat.chat.MessageListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by adabala on 22/10/2017.
 * Contacts data object to hold various data related to contact and event listeners for incoming messages.
 */

public class Contact {

    private String name;
    private String phoneNumber;
    private Boolean isFriend = false;
    private String chatHead;
    private MessageListener messageListener;
    private ChildEventListener childEventListener;
    private int numberOfUnreadMessages = 0;

    public Contact() {
        setChildEventListener();
    }

    public Contact(String name, String phoneNumber, Boolean isFriend) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isFriend = isFriend;
        setChildEventListener();
    }

    public Contact(String name, String phoneNumber, Boolean isFriend, String chatHead) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isFriend = isFriend;
        this.chatHead = chatHead;
        setChildEventListener();
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

    public ChildEventListener getChildEventListener() {
        return childEventListener;
    }

    public void incrementUnreadMesgCount() {
        numberOfUnreadMessages ++;
    }

    public int getNumberOfUnreadMessages() {
        return  numberOfUnreadMessages;
    }

    public void resetUnreadMessageCount() {
        numberOfUnreadMessages = 0;
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    /*
    * Listens to messages sent by this contact
    */
    private void setChildEventListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot != null && dataSnapshot.getValue() != null
                        && dataSnapshot.child("senderId").getValue().toString().equalsIgnoreCase(phoneNumber)
                        && dataSnapshot.child("status").getValue(Integer.class) == 1) {
                    incrementUnreadMesgCount();
                    if(messageListener != null) {
                        messageListener.onMessageReceived();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
