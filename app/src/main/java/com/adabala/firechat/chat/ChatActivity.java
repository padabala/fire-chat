package com.adabala.firechat.chat;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adabala.firechat.R;
import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.databinding.ActivityChatBinding;
import com.adabala.firechat.di.Injector;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.Query;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by adabala on 22/10/2017.
 */

public class ChatActivity extends AppCompatActivity{

    ActivityChatBinding mBinding;
    private String recipientId;
    private String chatHead;

    private MessageListAdapter messageListAdapter;

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
                    Query query = applicationAccess.chatReference.child(cHead)
                            .orderByKey();

                    FirebaseListOptions<ChatMessage> firebaseListOptions = new FirebaseListOptions.Builder<ChatMessage>()
                            .setLayout(R.layout.chat_message)
                            .setQuery(query, ChatMessage.class)
                            .build();

                    messageListAdapter = new MessageListAdapter(firebaseListOptions);
                    mBinding.chatList.setAdapter(messageListAdapter);
                }
            });
        }
    }

    public void onSendClicked(View view) {
        String message = mBinding.message.getText().toString();
        if(message != null && !message.isEmpty()) {
            applicationAccess.sendMessage(message, chatHead, recipientId);
        }
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
}
