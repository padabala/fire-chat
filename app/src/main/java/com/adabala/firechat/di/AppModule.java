package com.adabala.firechat.di;

import android.app.Application;
import android.content.Context;

import com.adabala.firechat.FireChatApplication;
import com.adabala.firechat.chat.MessageTransportSignal;
import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.utils.Constants;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * Created by adabala on 17/10/2017.
 */

@Module
public class AppModule {

    private FireChatApplication application;

    public AppModule(FireChatApplication application) {
        this.application = application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }

    @Provides
    @Inject
    Context providesApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Inject
    Prefser providesSharedPreferences(Context context){
        return new Prefser(context.getSharedPreferences(Constants.SharedPrefs.SHARED_PREFS_KEY, Context.MODE_PRIVATE));
    }

    @Provides
    @Inject
    FirebaseAuth providesFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Inject
    FirebaseDatabase providesFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Inject
    ApplicationAccess providesApplicationAccess(Context context, FirebaseDatabase firebaseDatabase, Prefser prefser) {
        return new ApplicationAccess(context, firebaseDatabase, prefser);
    }

    @Provides
    @Inject
    MessageTransportSignal providesMessageTransportSignal(ApplicationAccess applicationAccess) {
        return new MessageTransportSignal(applicationAccess);
    }
}
