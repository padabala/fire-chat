package com.adabala.firechat.di;

import com.adabala.firechat.RegistrationActivity;
import com.adabala.firechat.WelcomeActivity;
import com.adabala.firechat.contacts.ContactsActivity;
import com.adabala.firechat.firebase.FCFirebaseInstanceIdService;

import dagger.Component;

/**
 * Created by adabala on 17/10/2017.
 */

@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(WelcomeActivity welcomeActivity);
    void inject(RegistrationActivity registrationActivity);
    void inject(ContactsActivity contactsActivity);
    void inject(FCFirebaseInstanceIdService fcFirebaseInstanceIdService);
}
