package com.adabala.firechat.chat;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.adabala.firechat.R;
import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.databinding.ActivityChatBinding;
import com.adabala.firechat.di.Injector;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by adabala on 22/10/2017.
 */

public class ChatActivity extends AppCompatActivity{

    ActivityChatBinding mBinding;
    private String recipientId;
    private String recipientName;
    private String chatHead;

    private MessageListAdapter messageListAdapter;
    private LinearLayoutManager layoutManager;

    @Inject
    ApplicationAccess applicationAccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.INSTANCE.getAppComponent().inject(ChatActivity.this);
        mBinding = DataBindingUtil.setContentView(ChatActivity.this, R.layout.activity_chat);
        mBinding.setHandlers(ChatActivity.this);

        recipientId = getIntent().getStringExtra("recipientId");
        recipientName = getIntent().getStringExtra("recipientName");
        chatHead = getIntent().getStringExtra("chatHead");


        if(recipientId != null && !recipientId.isEmpty()) {
            if(chatHead != null) {
                createAdapterAndSet();
            }
            applicationAccess.getChatSessionForUser(recipientId).subscribe(new Consumer<String>() {
                @Override
                public void accept(String cHead) throws Exception {
                    chatHead = cHead;
                    createAdapterAndSet();
                }
            });
        }
        mBinding.setContactName(recipientName);
    }

    private void createAdapterAndSet() {

        Query query = applicationAccess.chatReference.child(chatHead);

        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>().setQuery(query, ChatMessage.class).build();

        messageListAdapter = new MessageListAdapter(options, ChatActivity.this);
        mBinding.chatList.setAdapter(messageListAdapter);

        layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setStackFromEnd(true);
        mBinding.chatList.setLayoutManager(layoutManager);

        messageListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int currentItemCount = messageListAdapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (currentItemCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mBinding.chatList.scrollToPosition(positionStart);
                }
            }
        });

        messageListAdapter.startListening();

        messageListAdapter.notifyDataSetChanged();
    }

    public void onSendClicked(View view) {
        String message = mBinding.message.getText().toString();
        if(!message.isEmpty()) {
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
        if(messageListAdapter != null) {
            messageListAdapter.stopListening();
        }
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
