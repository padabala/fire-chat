package com.adabala.firechat.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adabala.firechat.R;
import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.di.Injector;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by adabala on 26/10/2017.
 */

public class MessageListAdapter extends FirebaseRecyclerAdapter<ChatMessage, MessageListAdapter.ViewHolder> {

    private Context mContext;

    @Inject
    ApplicationAccess applicationAccess;

    public MessageListAdapter(FirebaseRecyclerOptions<ChatMessage> options, Context context) {
        super(options);
        this.mContext = context;
        Injector.INSTANCE.getAppComponent().inject(MessageListAdapter.this);
    }

    @Override
    public DatabaseReference getRef(int position) {
        return super.getRef(position);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, ChatMessage model) {
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.message.setText(model.getMessage());
        viewHolder.senderName.setText(model.getSenderId());
        Date date = new Date(model.getTimeStamp());
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        viewHolder.sentTime.setText(formatter.format(date));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        viewHolder.senderName.setVisibility(View.GONE);
        if(model.getSenderId().equalsIgnoreCase(applicationAccess.getVerifiedPhoneNumber())){
            params.gravity = Gravity.RIGHT;
            viewHolder.messageLayout.setLayoutParams(params);
            viewHolder.messageLayout.setBackgroundResource(R.drawable.sent_message_bg);
        } else {
            params.gravity = Gravity.LEFT;
            viewHolder.messageLayout.setLayoutParams(params);
            viewHolder.messageLayout.setBackgroundResource(R.drawable.received_mesg_bg);
            getRef(position).child("status").setValue(0);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_message, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView message;
        private TextView senderName;
        private TextView sentTime;
        private LinearLayout messageLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
            senderName = (TextView) itemView.findViewById(R.id.sender_name);
            sentTime = (TextView) itemView.findViewById(R.id.time);
            messageLayout = (LinearLayout) itemView.findViewById(R.id.message_layout);
        }
    }
}
