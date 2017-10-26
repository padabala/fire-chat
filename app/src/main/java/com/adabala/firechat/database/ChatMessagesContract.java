package com.adabala.firechat.database;

import android.provider.BaseColumns;

/**
 * Created by adabala on 25/10/2017.
 */

public final class ChatMessagesContract {

    private ChatMessagesContract() {}

    public static class MessageEntry implements BaseColumns {
        public static final String TABLE_NAME = "chatMessages";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_SENDER_ID = "senderId";
        public static final String COLUMN_NAME_RECEIVER_ID = "receiverId";
        public static final String COLUMN_NAME_MESSAGE = "message";
    }
}
