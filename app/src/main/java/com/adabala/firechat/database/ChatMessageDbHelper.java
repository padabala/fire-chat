package com.adabala.firechat.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adabala.firechat.chat.ChatMessage;

import java.util.ArrayList;

import static com.adabala.firechat.database.ChatMessagesContract.MessageEntry.COLUMN_NAME_MESSAGE;
import static com.adabala.firechat.database.ChatMessagesContract.MessageEntry.COLUMN_NAME_RECEIVER_ID;
import static com.adabala.firechat.database.ChatMessagesContract.MessageEntry.COLUMN_NAME_SENDER_ID;
import static com.adabala.firechat.database.ChatMessagesContract.MessageEntry.COLUMN_NAME_TIMESTAMP;
import static com.adabala.firechat.database.ChatMessagesContract.MessageEntry.TABLE_NAME;


/**
 * Created by adabala on 25/10/2017.
 */

public class ChatMessageDbHelper {

    SQLiteDatabase writableDataBase;
    SQLiteDatabase readableDataBase;

    public ChatMessageDbHelper(FireChatDbHelper fireChatDbHelper) {
        writableDataBase = fireChatDbHelper.getWritableDatabase();
        readableDataBase = fireChatDbHelper.getReadableDatabase();
    }

    public void writeMessage(ChatMessage chatMessage) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME_TIMESTAMP, chatMessage.getTimeStamp());
        contentValues.put(COLUMN_NAME_SENDER_ID, chatMessage.getSenderId());
        contentValues.put(COLUMN_NAME_RECEIVER_ID, chatMessage.getReceiverId());
        contentValues.put(COLUMN_NAME_MESSAGE, chatMessage.getMessage());

        writableDataBase.insert(TABLE_NAME, null ,contentValues);
    }

    public ArrayList<ChatMessage> loadMessagesForUser(String senderId) {

        String[] projection = {
                COLUMN_NAME_TIMESTAMP,
                COLUMN_NAME_SENDER_ID,
                COLUMN_NAME_RECEIVER_ID,
                COLUMN_NAME_MESSAGE
        };

        String selection = COLUMN_NAME_SENDER_ID + " = ? OR " + COLUMN_NAME_RECEIVER_ID + " = ?";

        String[] selectionArgs = { senderId, senderId };

        String sortOrder =
                COLUMN_NAME_TIMESTAMP + " ASC";

        Cursor cursor = readableDataBase.query(TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
                );

        ArrayList<ChatMessage> chatMessages = new ArrayList<>();

        while( cursor!= null && cursor.moveToNext() ) {

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_MESSAGE)));
            chatMessage.setTimeStamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_TIMESTAMP)));
            chatMessage.setSenderId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_SENDER_ID)));
            chatMessage.setReceiverId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_RECEIVER_ID)));

            chatMessages.add(chatMessage);
        }

        cursor.close();

        return chatMessages;
    }
}
