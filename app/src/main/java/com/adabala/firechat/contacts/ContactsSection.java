package com.adabala.firechat.contacts;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adabala.firechat.R;
import com.adabala.firechat.chat.MessageListener;
import com.adabala.firechat.data.Contact;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by adabala on 22/10/2017.
 */

public class ContactsSection extends StatelessSection {

    private ArrayList<Contact> contacts;
    private String headerTitle;

    private ContactSelectedListener contactSelectedListener;

    public ContactsSection(@LayoutRes int headerResourceId, @LayoutRes int itemResourceId, ArrayList<Contact> contacts, String headerTitle, ContactSelectedListener listener) {
        super(new SectionParameters.Builder(itemResourceId)
                .headerResourceId(headerResourceId)
                .build());
        this.contacts = contacts;
        this.headerTitle = headerTitle;
        this.contactSelectedListener = listener;
        sortContacts();
    }

    public void updateContacts(ArrayList<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        sortContacts();
    }

    @Override
    public int getContentItemsTotal() {
        return this.contacts.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.name.setText(contacts.get(position).getName());
        itemViewHolder.number.setText(contacts.get(position).getPhoneNumber());

        itemViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactSelectedListener.onContactsSelected(contacts.get(position));
                contacts.get(position).resetUnreadMessageCount();
                itemViewHolder.mesgCount.setText(String.valueOf(""));
                itemViewHolder.mesgCountLayout.setVisibility(View.GONE);
            }
        });

        contacts.get(position).setMessageListener(new MessageListener() {
            @Override
            public void onMessageReceived() {
                itemViewHolder.mesgCount.setText(String.valueOf(contacts.get(position).getNumberOfUnreadMessages()));
                itemViewHolder.mesgCountLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.headerTitle.setText(headerTitle);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout itemLayout;
        private TextView name;
        private TextView number;
        private TextView mesgCount;
        private LinearLayout mesgCountLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
            mesgCount = (TextView) itemView.findViewById(R.id.mesg_count);
            mesgCountLayout = (LinearLayout) itemView.findViewById(R.id.mesg_count_layout);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView headerTitle;
        private LinearLayout headerLayout;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView) itemView.findViewById(R.id.title);
            headerLayout = (LinearLayout) itemView.findViewById(R.id.header_layout);
        }
    }

    private void sortContacts() {
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact1, Contact contact2) {
                if (contact1.isFriend() == contact2.isFriend())
                    return contact1.getName().compareTo(contact2.getName());
                else {
                    return contact1.isFriend() ? -1 : 1;
                }
            }
        });
    }
}
