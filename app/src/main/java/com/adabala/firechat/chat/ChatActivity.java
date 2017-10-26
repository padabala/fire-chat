package com.adabala.firechat.chat;

import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adabala.firechat.R;
import com.adabala.firechat.contacts.ContactsActivity;
import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.databinding.ActivityChatBinding;
import com.adabala.firechat.di.Injector;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by adabala on 22/10/2017.
 */

public class ChatActivity extends AppCompatActivity{

    ActivityChatBinding mBinding;
    private String recipientId;
    private String chatHead;

    private MessageListAdapter messageListAdapter;

    FirebaseRecyclerAdapter<ChatMessage, ViewHolder> adapter;

    @Inject
    ApplicationAccess applicationAccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.INSTANCE.getAppComponent().inject(ChatActivity.this);
        mBinding = DataBindingUtil.setContentView(ChatActivity.this, R.layout.activity_chat);
        mBinding.setHandlers(ChatActivity.this);

        recipientId = getIntent().getStringExtra("recipientId");

        if(recipientId != null && !recipientId.isEmpty()) {
            applicationAccess.getChatSessionForUser(recipientId).subscribe(new Consumer<String>() {
                @Override
                public void accept(String cHead) throws Exception {
                    chatHead = cHead;
                    Query query = applicationAccess.chatReference.child(cHead);

//                    FirebaseListOptions<ChatMessage> firebaseListOptions = new FirebaseListOptions.Builder<ChatMessage>()
//                            .setLayout(R.layout.chat_message)
//                            .setQuery(query, ChatMessage.class)
//                            .build();

                    FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>().setQuery(query, ChatMessage.class).build();

                     adapter = new FirebaseRecyclerAdapter<ChatMessage, ViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(ViewHolder holder, int position, ChatMessage model) {
                            ViewHolder viewHolder = (ViewHolder) holder;
                            viewHolder.message.setText(model.getMessage());
                            viewHolder.senderName.setText(model.getSenderId());
                            Date date = new Date(model.getTimeStamp());
                            DateFormat formatter = new SimpleDateFormat("HH:mm");
                            viewHolder.sentTime.setText(formatter.format(date));
                        }

                        @Override
                        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_message, parent, false);
                            return new ViewHolder(view);
                        }
                    };
                    //messageListAdapter = new MessageListAdapter(firebaseListOptions);
                    mBinding.chatList.setAdapter(adapter);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                    mBinding.chatList.setLayoutManager(layoutManager);

                    adapter.startListening();
                }
            });
        }
    }

    public void onSendClicked(View view) {
        String message = mBinding.message.getText().toString();
        if(message != null && !message.isEmpty()) {
            applicationAccess.sendMessage(message, chatHead, recipientId);
            mBinding.message.setText("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        adapter.stopListening();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        TextView senderName;
        TextView sentTime;

        public ViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
            senderName = (TextView) itemView.findViewById(R.id.sender_name);
            sentTime = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
