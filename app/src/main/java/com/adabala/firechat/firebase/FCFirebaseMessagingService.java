package com.adabala.firechat.firebase;

import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.di.Injector;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

public class FCFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    ApplicationAccess applicationAccess;

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.getAppComponent().inject(FCFirebaseMessagingService.this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(applicationAccess.showNotification) {
            //TODO implement showing push notification on new message received.
        }
    }
}
