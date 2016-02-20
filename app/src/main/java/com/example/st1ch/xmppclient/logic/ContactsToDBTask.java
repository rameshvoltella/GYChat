package com.example.st1ch.xmppclient.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.CheckBox;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.db.ContactDBOpenHelper;
import com.example.st1ch.xmppclient.user.activity.LoginActivity;
import com.example.st1ch.xmppclient.user.logic.Contact;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by st1ch on 23.10.15.
 */
public class ContactsToDBTask extends AsyncTask<ArrayList<String>,ArrayList<String>,Void> {

    private String LOG_TAG = "chatLog";

    private Collection<RosterEntry> entries;
    private static ArrayList<String> arrayList;

    private Map<String, Presence> contactsPresenceList;
    private String userJID;

    private Presence entryPresence;
    private String user;
    private String userName;
    private Roster roster;
    private CheckBox checkOnline;
    private Context context;

    private static ArrayList<Contact> contactList = new ArrayList<Contact>();

    private SQLiteDatabase contactsDB;
    private ContactDBOpenHelper contactDBOpenHelper;
    private Cursor cursor;


    public ContactsToDBTask(Context context, String userJid){
        this.context = context;
        this.userJID = userJid;
    }

    @Override
    protected Void doInBackground(ArrayList<String>... params) {
        getEntriesToMap();
        return null;
    }

    private void getEntriesToMap(){
        roster = Roster.getInstanceFor(ConnectClientTask.getConnection());
        if(!roster.isLoaded()) {
            try {
                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        entries = roster.getEntries();
       // contactsPresenceList = new HashMap<String, Presence>();

        for(RosterEntry entry: entries){
            String jid = entry.getUser()+ "@" + context.getString(R.string.serviceName);
            String name = entry.getName();
            entryPresence = roster.getPresence(jid);
            if(!userJID.trim().equals(LoginActivity.getUsername()+"@"+
                    context.getString(R.string.serviceName))){
               // contactsPresenceList.put(jid, entryPresence);
                writeContactsToDB(jid, name);
                Log.d(LOG_TAG, jid + " " + name + " " + entryPresence.getType().toString());
               /* if(entryPresence.getType().toString().trim().equals("available")){
                    arrayList.add(userName + ": online");
                }
                else arrayList.add(userName +": offline");
                */
            }
        }
    }

    private void writeContactsToDB(String jid, String name){
        contactDBOpenHelper = new ContactDBOpenHelper(context.getApplicationContext());
        contactsDB = contactDBOpenHelper.getWritableDatabase();
        String[] resCol = new String[] {ContactDBOpenHelper.KEY_NAME_COLUMN, ContactDBOpenHelper.KEY_JID_COLUMN};

        cursor = contactsDB.query(ContactDBOpenHelper.CONTACTS_TABLE_NAME, resCol, null,
                null, null, null, null);
        if(cursor.getCount() == 0){
            addToDB(jid, name);
        } else {
            String where = ContactDBOpenHelper.KEY_JID_COLUMN + " = " +"\"" + jid + "\"";
            cursor = contactsDB.query(ContactDBOpenHelper.CONTACTS_TABLE_NAME, resCol, where,
                    null, null, null, null);
            if(!cursor.moveToFirst()){
                addToDB(jid, name);
            }  else if(cursor.getCount() > 0) {
                cursor.moveToFirst();

                String jidDB = cursor.getString(cursor.getColumnIndex(ContactDBOpenHelper.KEY_JID_COLUMN));
                String nameDB = cursor.getString(cursor.getColumnIndex(ContactDBOpenHelper.KEY_NAME_COLUMN));
                if(!name.equals(nameDB)){
                    updateInDB(jidDB, nameDB, name);
                }
            }
        }
        cursor.close();
    }

    private void addToDB(String jid, String name){
        ContentValues cv = new ContentValues();
        int img = R.mipmap.ic_launcher;
        cv.put(ContactDBOpenHelper.KEY_JID_COLUMN, jid);
        cv.put(ContactDBOpenHelper.KEY_NAME_COLUMN, name);
        cv.put(ContactDBOpenHelper.KEY_IMG_COLUMN, img);
        contactsDB.insert(ContactDBOpenHelper.CONTACTS_TABLE_NAME, null, cv);
        Log.d("chatLog", "Inserted new value in table contacts : " + jid + " " + name);
    }

    private void updateInDB(String jidDB, String nameDB, String newName){
        ContentValues updatedName = new ContentValues();
        updatedName.put(ContactDBOpenHelper.KEY_NAME_COLUMN, newName);
        String where = ContactDBOpenHelper.KEY_JID_COLUMN + " = " + "\"" + jidDB + "\"";
        contactsDB.update(ContactDBOpenHelper.CONTACTS_TABLE_NAME, updatedName, where, null);
        Log.d("chatLog", "Updated contact name from : " + nameDB + " to " + newName
                + " jid: " + jidDB);
    }

    @Override
    protected void onProgressUpdate(ArrayList<String>... values) {
        super.onProgressUpdate(values);
    }
}
