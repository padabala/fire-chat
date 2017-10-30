package com.adabala.firechat.database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.adabala.firechat.chat.ChatMessage;
import com.adabala.firechat.data.Contact;
import com.adabala.firechat.interfaces.RegistrationCompletionListener;
import com.adabala.firechat.utils.Constants;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

import static com.adabala.firechat.utils.Constants.Firebase.CHAT_HEADS;
import static com.adabala.firechat.utils.Constants.Firebase.CHAT_REF;
import static com.adabala.firechat.utils.Constants.Firebase.FCM_TOKEN;
import static com.adabala.firechat.utils.Constants.Firebase.USERS_REF;
import static com.adabala.firechat.utils.Constants.SharedPrefs.FCM_TOKEN_KEY;
import static com.adabala.firechat.utils.Constants.SharedPrefs.VERIFIED_PHONENUMBER_KEY;

/**
 * Created by adabala on 17/10/2017.
 */

public class ApplicationAccess {

    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private Prefser prefser;

    private DatabaseReference userReference;
    public DatabaseReference chatReference;

    public BehaviorSubject<HashMap<String, ArrayList<Contact>>> contactsSubject;

    HashMap<String, ArrayList<Contact>> hashMap = new HashMap<>();
    private ArrayList<Contact> friends = new ArrayList<>();
    private ArrayList<Contact> invitees = new ArrayList<>();

    public ApplicationAccess(Context context, FirebaseDatabase firebaseDatabase, Prefser prefser) {
        this.context = context;
        this.firebaseDatabase = firebaseDatabase;
        this.prefser = prefser;

        this.userReference = firebaseDatabase.getReference(USERS_REF);
        this.chatReference = firebaseDatabase.getReference(CHAT_REF);

        contactsSubject = BehaviorSubject.create();
    }

    public void saveVerifiedPhoneNumber(@NonNull String phoneNumber) {
        prefser.put(VERIFIED_PHONENUMBER_KEY, phoneNumber);
    }

    public void removeVerifiedPhoneNumber() {
        prefser.remove(VERIFIED_PHONENUMBER_KEY);
    }

    public String getVerifiedPhoneNumber() {
        return prefser.get(VERIFIED_PHONENUMBER_KEY, String.class, null);
    }

    public void saveFcmToken(String fcmToken) {
        prefser.put(FCM_TOKEN_KEY, fcmToken);
        if (getVerifiedPhoneNumber() != null) {
            updateFCMTokenToUserAccount();
        }
    }

    public String getFCMToken() {
        return prefser.get(FCM_TOKEN_KEY, String.class, null);
    }

    private void updateFCMTokenToUserAccount() {
        userReference.child(getVerifiedPhoneNumber()).child(FCM_TOKEN).setValue(getFCMToken());
    }

    public void registerUser(final String phoneNumber, final RegistrationCompletionListener registrationCompletionListener) {

        Map<String, Object> accountRoot = new HashMap<>();
        accountRoot.put(String.format("%s/%s", phoneNumber, "phoneNumber"), phoneNumber);
        accountRoot.put(String.format("%s/%s", phoneNumber, FCM_TOKEN), getFCMToken());

        userReference.updateChildren(accountRoot, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Timber.d("Successfully registered user");
                    saveVerifiedPhoneNumber(phoneNumber);
                    registrationCompletionListener.onRegistrationSuccess();
                } else {
                    registrationCompletionListener.onRegistrationFailed();
                }
            }
        });
    }

    public void unregisterUser() {
        userReference.child(getVerifiedPhoneNumber()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Timber.d("Successfully unregistered user");
                removeVerifiedPhoneNumber();
            }
        });
    }

    public void syncContacts(ArrayList<Contact> phoneBookContacts) {

        for(final Contact contact : phoneBookContacts) {
            userReference.child(contact.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null && dataSnapshot.getValue() != null) {
                        invitees.remove(contact);
                        contact.setChatHead(dataSnapshot.child(CHAT_HEADS).child(getVerifiedPhoneNumber()).getValue(String.class));
                        contact.setFriend(true);
                        friends.add(contact);
                        chatReference.child(contact.getChatHead()).addChildEventListener(contact.getChildEventListener());
                    } else {
                        friends.remove(contact);
                        contact.setFriend(false);
                        invitees.add(contact);
                    }
                    hashMap.put(Constants.ContactStatus.FRIENDS, friends);
                    hashMap.put(Constants.ContactStatus.INVITES, invitees);

                    contactsSubject.onNext(hashMap);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public Single<String> getChatSessionForUser(final String recipientId) {

        return Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final SingleEmitter<String> emitter) throws Exception {
                userReference.child(getVerifiedPhoneNumber()).child(CHAT_HEADS).child(recipientId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String chatHeadKey;
                        if(dataSnapshot != null && dataSnapshot.getValue() != null) {
                            chatHeadKey = dataSnapshot.getValue(String.class);
                            emitter.onSuccess(chatHeadKey);
                        } else {
                            chatHeadKey = userReference.child(getVerifiedPhoneNumber()).child(CHAT_HEADS).child(recipientId).push().getKey();
                            userReference.child(getVerifiedPhoneNumber()).child(CHAT_HEADS).child(recipientId).setValue(chatHeadKey);
                            userReference.child(recipientId).child(CHAT_HEADS).child(getVerifiedPhoneNumber()).setValue(chatHeadKey);
                            emitter.onSuccess(chatHeadKey);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        emitter.onError(databaseError.toException());
                    }
                });
            }
        });
    }

    public void sendMessage(String message, String chatHead, String receiverId) {
        ChatMessage chatMessage = new ChatMessage(message, System.currentTimeMillis(), getVerifiedPhoneNumber(), receiverId, 1);
        chatReference.child(chatHead).push().setValue(chatMessage);
    }
}
