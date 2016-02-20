package com.example.st1ch.xmppclient.logic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.st1ch.xmppclient.activity.ConnectToActivity;
import com.example.st1ch.xmppclient.db.ContactDBOpenHelper;
import com.example.st1ch.xmppclient.user.logic.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by st1ch on 18.02.16.
 */
public class FillContactsListTask extends AsyncTask<Void, Void, Void> {

    private final String LOG_TAG = "chatLog";
    Context context;

    //Map contactsMap = new HashMap<String, Contact>();
    ArrayList<Contact> contactsList;

    public FillContactsListTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        getContactsFromDB();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ConnectToActivity.setContactsList(contactsList);
        ConnectToActivity.getContactListAdapter().notifyDataSetChanged();
    }

    public void getContactsFromDB(){
        ContactDBOpenHelper helper = new ContactDBOpenHelper(context);
        SQLiteDatabase contactsDB = helper.getReadableDatabase();
        String[] resCol = new String[] {ContactDBOpenHelper.KEY_NAME_COLUMN, ContactDBOpenHelper.KEY_JID_COLUMN};
        Cursor cursor = contactsDB.query(ContactDBOpenHelper.CONTACTS_TABLE_NAME, resCol, null,
                null, null, null, null);
        int colIndJID = cursor.getColumnIndex(ContactDBOpenHelper.KEY_JID_COLUMN);
        int colIndName = cursor.getColumnIndex(ContactDBOpenHelper.KEY_NAME_COLUMN);

        contactsList = new ArrayList<Contact>();

        while(cursor.moveToNext()){
            String jidDB = cursor.getString(colIndJID);
            String nameDB = cursor.getString(colIndName);
            contactsList.add(new Contact(jidDB, nameDB));
            Log.d(LOG_TAG, "Contact from db : " + jidDB + " " + nameDB);
        }
    }


}
