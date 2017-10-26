package com.adabala.firechat.chat;

import android.view.View;
import android.widget.TextView;

import com.adabala.firechat.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by adabala on 26/10/2017.
 */

public class MessageListAdapter extends FirebaseListAdapter<ChatMessage> {

    public MessageListAdapter(FirebaseListOptions<ChatMessage> firebaseListOptions) {
        super(firebaseListOptions);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    protected void populateView(View view, ChatMessage chatMessage, int position) {
        Timber.d("######populateView");
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView senderName = (TextView) view.findViewById(R.id.sender_name);
        TextView sentTime = (TextView) view.findViewById(R.id.time);

        message.setText(chatMessage.getMessage());
        senderName.setText(chatMessage.getSenderId());

        Date date = new Date(chatMessage.getTimeStamp());
        DateFormat formatter = new SimpleDateFormat("HH:mm");

        sentTime.setText(formatter.format(date));
    }
}