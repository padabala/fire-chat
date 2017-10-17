package com.adabala.firechat.di;

/**
 * Created by adabala on 17/10/2017.
 */

public enum Injector {
    INSTANCE;

    AppComponent applicationComponent;

    public void setAppComponent(AppComponent appComponent) {
        this.applicationComponent = appComponent;
    }

    public AppComponent getAppComponent() {
        return applicationComponent;
    }
}
