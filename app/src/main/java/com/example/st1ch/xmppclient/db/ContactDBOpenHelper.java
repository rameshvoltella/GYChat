package com.example.st1ch.xmppclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by st1ch on 03.12.15.
 */
public class ContactDBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "contacts.db";
    private static final int DB_VERSION = 1;
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME_COLUMN = "NAME_COLUMN";
    public static final String KEY_JID_COLUMN = "JID_COLUMN";
    public static final String KEY_IMG_COLUMN = "IMG_COLUMN";
    public static final String CONTACTS_TABLE_NAME = "Contacts";
    private static final String DB_CREATE = "create table " +
            CONTACTS_TABLE_NAME + " (" + KEY_ID + " integer primary key autoincrement, " +
            KEY_NAME_COLUMN + " text not null, " +
            KEY_JID_COLUMN + " text not null, " +
            KEY_IMG_COLUMN + " integer not null" + ");";

    public ContactDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " +
                oldVersion + " to " + newVersion +
                ", which will destroy all old data");
        db.execSQL("DROP TABLE IF IT EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }
}
