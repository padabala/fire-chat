package com.adabala.firechat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adabala on 25/10/2017.
 */

public class FireChatDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FireChat.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ChatMessagesContract.MessageEntry.TABLE_NAME + " (" +
                    ChatMessagesContract.MessageEntry._ID + " INTEGER PRIMARY KEY," +
                    ChatMessagesContract.MessageEntry.COLUMN_NAME_SENDER_ID + " TEXT," +
                    ChatMessagesContract.MessageEntry.COLUMN_NAME_RECEIVER_ID + " TEXT," +
                    ChatMessagesContract.MessageEntry.COLUMN_NAME_MESSAGE + " TEXT," +
                    ChatMessagesContract.MessageEntry.COLUMN_NAME_TIMESTAMP + " LONG)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ChatMessagesContract.MessageEntry.TABLE_NAME;

    public FireChatDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
