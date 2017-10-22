package com.adabala.firechat.chat;

import com.adabala.firechat.database.ApplicationAccess;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by adabala on 22/10/2017.
 */

public class MessageTransportSignal {

    private ApplicationAccess applicationAccess;

    public MessageTransportSignal(ApplicationAccess applicationAccess) {

    }

    public void publish(ChatMessage message) {

    }

    public void subscribe(String chatSessionId) {
        final DatabaseReference databaseReference = applicationAccess.chatReference.child(chatSessionId);
        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    //TODO retrieve chat message
                } else {
                    databaseReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }
}
