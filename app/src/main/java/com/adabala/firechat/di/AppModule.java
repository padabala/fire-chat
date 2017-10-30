package com.adabala.firechat.di;

import android.app.Application;
import android.content.Context;

import com.adabala.firechat.FireChatApplication;
import com.adabala.firechat.chat.MessageTransportSignal;
import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.database.ChatMessageDbHelper;
import com.adabala.firechat.database.FireChatDbHelper;
import com.adabala.firechat.utils.Constants;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context providesApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Prefser providesSharedPreferences(Context context){
        return new Prefser(context.getSharedPreferences(Constants.SharedPrefs.SHARED_PREFS_KEY, Context.MODE_PRIVATE));
    }

    @Provides
    @Singleton
    FirebaseAuth providesFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    FirebaseDatabase providesFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    ApplicationAccess providesApplicationAccess(Context context, FirebaseDatabase firebaseDatabase, Prefser prefser) {
        return new ApplicationAccess(context, firebaseDatabase, prefser);
    }

    @Provides
    @Singleton
    MessageTransportSignal providesMessageTransportSignal(ApplicationAccess applicationAccess) {
        return new MessageTransportSignal(applicationAccess);
    }

    @Provides
    @Singleton
    FireChatDbHelper providesFireChatDbHelper(Context context) {
        return new FireChatDbHelper(context);
    }

    @Provides
    @Singleton
    ChatMessageDbHelper providesChatMessageDbHelper(FireChatDbHelper fireChatDbHelper) {
        return new ChatMessageDbHelper(fireChatDbHelper);
    }

    @Provides
    @Singleton
    PhoneNumberUtil providesPhoneNumberUtil(Context context) {
        return PhoneNumberUtil.getInstance();
    }
}
