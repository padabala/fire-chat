package com.adabala.firechat.contacts;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.adabala.firechat.R;
import com.adabala.firechat.chat.ChatActivity;
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
import timber.log.Timber;

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

        friendsSection = new ContactsSection(R.layout.section_header, R.layout.contact_item, new ArrayList<Contact>()
                , getString(R.string.friends_header_text), ContactsActivity.this);
        inviteSection = new ContactsSection(R.layout.section_header, R.layout.contact_item, new ArrayList<Contact>()
                , getString(R.string.invite_header_text), ContactsActivity.this);

        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        sectionedRecyclerViewAdapter.addSection(friendsSection);
        sectionedRecyclerViewAdapter.addSection(inviteSection);

        mBinding.contactListView.setAdapter(sectionedRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ContactsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.contactListView.setLayoutManager(layoutManager);

        initContactsCursorLoader();
    }

    @Override
    public void onContactsSelected(Contact contact) {
        if(contact.isFriend()) {
            Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
            intent.putExtra("recipientId", contact.getPhoneNumber());
            intent.putExtra("recipientName", contact.getName());
            intent.putExtra("chatHead", contact.getChatHead());
            startActivity(intent);
        } else {
            Timber.d("onInviteClicked : " + contact.getPhoneNumber());
            Intent inviteIntent = new Intent(android.content.Intent.ACTION_SEND);
            inviteIntent.setType("text/plain");
            inviteIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FireChat");
            inviteIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Get Firechat from app store link");
            startActivity(Intent.createChooser(inviteIntent,  "Invite to FireChat"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        applicationAccess.contactsSubject.subscribe(new Consumer<HashMap<String, ArrayList<Contact>>>() {
            @Override
            public void accept(HashMap<String, ArrayList<Contact>> hashMap) throws Exception {
                friendsSection.updateContacts(hashMap.get(Constants.ContactStatus.FRIENDS));
                inviteSection.updateContacts(hashMap.get(Constants.ContactStatus.INVITES));
                sectionedRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
}
