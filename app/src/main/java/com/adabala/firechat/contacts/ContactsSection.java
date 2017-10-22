package com.adabala.firechat.contacts;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adabala.firechat.R;
import com.adabala.firechat.data.Contact;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by adabala on 22/10/2017.
 */

public class ContactsSection extends StatelessSection {

    ArrayList<Contact> contacts;
    String headerTitle;

    private ContactSelectedListener contactSelectedListener;

    public ContactsSection(@LayoutRes int headerResourceId, @LayoutRes int itemResourceId, ArrayList<Contact> contacts, String headerTitle, ContactSelectedListener listener) {
        super(headerResourceId, itemResourceId);
        this.contacts = contacts;
        this.headerTitle = headerTitle;
        this.contactSelectedListener = listener;
    }

    public void updateContacts(ArrayList<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
    }

    @Override
    public int getContentItemsTotal() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.name.setText(contacts.get(position).getName());
        itemViewHolder.number.setText(contacts.get(position).getPhoneNumber());

        itemViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactSelectedListener.onContactsSelected(contacts.get(position));
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return super.getHeaderViewHolder(view);
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

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView headerTitle;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
