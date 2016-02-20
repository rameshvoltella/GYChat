package com.example.st1ch.xmppclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by st1ch on 02.12.15.
 */
public class LoginDBOpenHelper extends SQLiteOpenHelper {

    private static final String KEY_ID = "_id";
    public static final String KEY_JID_COLUMN = "JID_COLUMN";
    public static final String KEY_PASSWORD_COLUMN = "PASSWORD_COLUMN";
    private static final String DB_NAME = "userLoginDB.db";
    public static final String DB_TABLE_NAME = "UserLogin";
    private static final int DB_VERSION  =1;
    private static final String DB_CREATE = "create table " +
            DB_TABLE_NAME + " (" + KEY_ID + " integer primary key autoincrement, " +
            KEY_JID_COLUMN + " text not null, " +
            KEY_PASSWORD_COLUMN + " text not null);";

    public LoginDBOpenHelper(Context context) {
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
        db.execSQL("DROP TABLE IF IT EXISTS " + DB_TABLE_NAME);
        onCreate(db);
    }
}
