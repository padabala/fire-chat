package com.adabala.firechat.database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.adabala.firechat.interfaces.RegistrationCompletionListener;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

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
    private DatabaseReference chatReference;

    public ApplicationAccess(Context context, FirebaseDatabase firebaseDatabase, Prefser prefser) {
        this.context = context;
        this.firebaseDatabase = firebaseDatabase;
        this.prefser = prefser;

        this.userReference = firebaseDatabase.getReference(USERS_REF);
        this.chatReference = firebaseDatabase.getReference(CHAT_REF);
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
}
