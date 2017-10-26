package com.adabala.firechat.di;

import com.adabala.firechat.RegistrationActivity;
import com.adabala.firechat.WelcomeActivity;
import com.adabala.firechat.chat.ChatActivity;
import com.adabala.firechat.chat.MessageListAdapter;
import com.adabala.firechat.contacts.ContactsActivity;
import com.adabala.firechat.firebase.FCFirebaseInstanceIdService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by adabala on 17/10/2017.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(WelcomeActivity welcomeActivity);
    void inject(RegistrationActivity registrationActivity);
    void inject(ContactsActivity contactsActivity);
    void inject(FCFirebaseInstanceIdService fcFirebaseInstanceIdService);
    void inject(ChatActivity chatActivity);
    void inject(MessageListAdapter messageListAdapter);
}
