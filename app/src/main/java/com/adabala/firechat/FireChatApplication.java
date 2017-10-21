package com.adabala.firechat;

import android.app.Application;

import com.adabala.firechat.di.AppComponent;
import com.adabala.firechat.di.AppModule;
import com.adabala.firechat.di.DaggerAppComponent;
import com.adabala.firechat.di.Injector;

import timber.log.Timber;

/**
 * Created by adabala on 17/10/2017.
 */

public class FireChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        AppModule appModule = new AppModule(this);

        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .build();

        Injector.INSTANCE.setAppComponent(appComponent);
    }
}
