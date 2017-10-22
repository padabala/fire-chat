package com.adabala.firechat.utils;


import android.Manifest;

/**
 * Created by adabala on 17/10/2017.
 */

public class Constants {

    public static final String[] REQUIRED_PERMISSIONS = new String [] {Manifest.permission.READ_CONTACTS};
    public static final int PERMISSION_REQUEST_CODE = 10001;

    public class SharedPrefs {
        public static final String SHARED_PREFS_KEY = "groupChat";
        public static final String VERIFIED_PHONENUMBER_KEY = "verifiedPhoneNumber";
        public static final String FCM_TOKEN_KEY = "fcmTokenKey";
    }

    public class Firebase {
        public static final String USERS_REF = "users";
        public static final String CHAT_REF = "chats";

        public static final String PHONE_NUMBER = "phoneNumber";
        public static final String FCM_TOKEN = "fcmToken";
    }

    public class ContactStatus {
        public static final String FRIENDS = "friends";
        public static final String INVITES = "invites";
    }
}
