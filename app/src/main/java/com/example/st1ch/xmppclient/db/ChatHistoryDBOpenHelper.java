package com.example.st1ch.xmppclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by st1ch on 04.12.15.
 */
public class ChatHistoryDBOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "chat_history.db";
    private static final int DB_VERSION = 1;
    public static final String KEY_ID = "_id";
    public static String KEY_TABLE_NAME = "";
    public static final String KEY_MESSAGE_COLUMN = "MESSAGE_COLUMN";
    private static String DB_CREATE = " (" +
            KEY_ID + " integer primary key autoincrement, " +
            KEY_MESSAGE_COLUMN + " text not null);";

    public ChatHistoryDBOpenHelper(Context context, String tableName) {
        super(context, DB_NAME, null, DB_VERSION);
        KEY_TABLE_NAME = "\"" + tableName + "\"";
        Log.d("chatLog", "constructor : " + KEY_TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + KEY_TABLE_NAME + DB_CREATE);
        Log.d("chatLog", "created new table : " + KEY_TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " +
                oldVersion + " to " + newVersion +
                ", which will destroy all old data");
        db.execSQL("DROP TABLE IF IT EXISTS " + KEY_TABLE_NAME);
        onCreate(db);
    }
}
