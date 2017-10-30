package com.adabala.firechat.firebase;

import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.di.Injector;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by adabala on 17/10/2017.
 */

public class FCFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Inject
    ApplicationAccess applicationAccess;

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.getAppComponent().inject(FCFirebaseInstanceIdService.this);
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d("Refreshed token: " + refreshedToken);
        applicationAccess.saveFcmToken(refreshedToken);
    }
}
