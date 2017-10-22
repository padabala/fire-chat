package com.adabala.firechat.contacts;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.adabala.firechat.R;
import com.adabala.firechat.data.Contact;
import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.databinding.ActivityContactsBinding;
import com.adabala.firechat.di.Injector;
import com.adabala.firechat.utils.Constants;
import com.adabala.firechat.utils.ContactsSyncActivity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.functions.Consumer;

public class ContactsActivity extends ContactsSyncActivity implements ContactSelectedListener{

    ActivityContactsBinding mBinding;

    private ContactsSection friendsSection;
    private ContactsSection inviteSection;
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    @Inject
    ApplicationAccess applicationAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.INSTANCE.getAppComponent().inject(ContactsActivity.this);

        mBinding = DataBindingUtil.setContentView(ContactsActivity.this, R.layout.activity_contacts);
        mBinding.setHandlers(ContactsActivity.this);

        applicationAccess.contactsSubject.subscribe(new Consumer<HashMap<String, ArrayList<Contact>>>() {
            @Override
            public void accept(HashMap<String, ArrayList<Contact>> hashMap) throws Exception {
                friendsSection.updateContacts(hashMap.get(Constants.ContactStatus.FRIENDS));
                inviteSection.updateContacts(hashMap.get(Constants.ContactStatus.INVITES));
                sectionedRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        friendsSection = new ContactsSection(R.layout.section_header, R.layout.contact_item, new ArrayList<Contact>()
                , getString(R.string.friends_header_text), this);
        inviteSection = new ContactsSection(R.layout.section_header, R.layout.contact_item, new ArrayList<Contact>()
                , getString(R.string.invite_header_text), this);

        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        sectionedRecyclerViewAdapter.addSection(friendsSection);
        sectionedRecyclerViewAdapter.addSection(inviteSection);

        mBinding.contactListView.setAdapter(sectionedRecyclerViewAdapter);

        initContactsCursorLoader();
    }

    @Override
    public void onContactsSelected(Contact contact) {
        if(contact.isFriend()) {
            //TODO launch chat Activity
        } else {
            //TODO launch invitable intent
        }
    }
}
