package com.adabala.firechat.utils;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.adabala.firechat.data.Contact;
import com.adabala.firechat.database.ApplicationAccess;

import java.util.ArrayList;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by adabala on 22/10/2017.
 * This Activity is extendable and it performs reading contacts from phonebook
 * using cursor loader and calls sync contacts from firebase database
 */

public class ContactsSyncActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    ArrayList<Contact> phoneBookContacts;

    @Inject
    ApplicationAccess applicationAccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneBookContacts = new ArrayList<>();
    }

    /*
    * Initialize cursor loader for reading contacts.
     */
    public void initContactsCursorLoader() {
        Timber.d("initContactsCursorLoader ");
        getLoaderManager().initLoader(0, null ,this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.d("onCreateLoader");

        String[] projection = { ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };

        return new CursorLoader(ContactsSyncActivity.this,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " NOTNULL AND " + ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " > 0",
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Timber.d("onLoadFinished");
        phoneBookContacts.clear();
        if(data != null && data.getCount() > 0) {
            while(data.moveToNext()) {
                try {
                    String name = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+","");
                    if(name != null && phoneNumber != null && !name.isEmpty() && !phoneNumber.isEmpty()) {
                        Contact contact = new Contact(name, phoneNumber, false);
                        phoneBookContacts.add(contact);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            applicationAccess.syncContacts(phoneBookContacts);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
