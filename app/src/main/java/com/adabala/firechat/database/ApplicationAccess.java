package com.adabala.firechat.database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.adabala.firechat.data.Contact;
import com.adabala.firechat.interfaces.RegistrationCompletionListener;
import com.adabala.firechat.utils.Constants;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

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
        HashMap<String, ArrayList<Contact>> hashMap = new HashMap<>();
        final ArrayList<Contact> friends = new ArrayList<>();
        final ArrayList<Contact> invites = new ArrayList<>();

        for(final Contact contact : phoneBookContacts) {
            userReference.child(contact.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {
                        contact.setFriend(true);
                        friends.add(contact);
                    } else {
                        invites.add(contact);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        hashMap.put(Constants.ContactStatus.FRIENDS, friends);
        hashMap.put(Constants.ContactStatus.INVITES, invites);

        contactsSubject.onNext(hashMap);
    }
}
