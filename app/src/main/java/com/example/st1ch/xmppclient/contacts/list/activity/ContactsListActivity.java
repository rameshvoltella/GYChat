package com.example.st1ch.xmppclient.contacts.list.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.chat.activity.ChatActivity;
import com.example.st1ch.xmppclient.db.ContactDBOpenHelper;
import com.example.st1ch.xmppclient.logic.ConnectClientTask;
import com.example.st1ch.xmppclient.logic.ContactListAdapter;
import com.example.st1ch.xmppclient.user.logic.Contact;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.packet.RosterPacket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by st1ch on 18.02.16.
 */
public class ContactsListActivity extends FragmentActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    final String LOG_TAG = "chatLog";
    private static final int CM_DELETE_ID = 1;

    private SimpleCursorAdapter cursorAdapter;
    private ContactDBOpenHelper helper;
    private SQLiteDatabase contactsDB;

    private Roster roster;
    private Presence entryPresence;

    private ListView contactsListView;
    private Map<String, Contact> contactsMap;
    private ContactListAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_to);

        helper = new ContactDBOpenHelper(this);
        contactsDB = helper.getWritableDatabase();

        String[] from = new String[] {ContactDBOpenHelper.KEY_NAME_COLUMN, ContactDBOpenHelper.KEY_JID_COLUMN};
        int[] to = new int[] { R.id.list_item_text_view };

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list, null, from, to, 0);

        contactsMap = new HashMap<String, Contact>();

        contactAdapter = new ContactListAdapter(this, contactsMap);

        contactsListView = (ListView) findViewById(R.id.usersList);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                String sendTo = "";
                String writeTo = "";
                String[] resCol = new String[]{ContactDBOpenHelper.KEY_ID, ContactDBOpenHelper.KEY_JID_COLUMN};
                String where = ContactDBOpenHelper.KEY_ID + " = " + id;
                Cursor cursor = contactsDB.query(ContactDBOpenHelper.CONTACTS_TABLE_NAME, resCol, where,
                        null, null, null, null);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    int jidInd = cursor.getColumnIndex(ContactDBOpenHelper.KEY_JID_COLUMN);
                    int nameInd = cursor.getColumnIndex(ContactDBOpenHelper.KEY_NAME_COLUMN);
                    sendTo = cursor.getString(jidInd);
                    writeTo = cursor.getString(jidInd);
                }
                intent = new Intent(ContactsListActivity.this, ChatActivity.class);
                intent.putExtra("sendTo", sendTo);
                intent.putExtra("writeTo", writeTo);
                startActivity(intent);
            }
        });
        contactsListView.setAdapter(contactAdapter);
        getSupportLoaderManager().initLoader(0, null, this);

        registerForContextMenu(contactsListView);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.context_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case CM_DELETE_ID:
                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                contactsDB.delete(ContactDBOpenHelper.CONTACTS_TABLE_NAME, ContactDBOpenHelper.KEY_ID + " = " + acmi.id,
                        null);
                getSupportLoaderManager().getLoader(0).forceLoad();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    private void createRoster(){
        roster = Roster.getInstanceFor(ConnectClientTask.getConnection());

        roster.addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<String> addresses) {
                List<String> list = new ArrayList<String>(addresses);
                Log.d(LOG_TAG, "entry " + list.get(0));
            }

            @Override
            public void entriesUpdated(Collection<String> addresses) {

            }

            @Override
            public void entriesDeleted(Collection<String> addresses) {

            }

            @Override
            public void presenceChanged(Presence presence) {
                TextView tvOnline = (TextView) findViewById(R.id.contactslist_online_tv);
            }
        });
        if(!roster.isLoaded()) {
            try {
                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ContactCursorLoader(this, contactsDB);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {


       // data.moveToFirst();
        while(data.moveToNext()){
            String jid = data.getString(data.getColumnIndexOrThrow(ContactDBOpenHelper.KEY_JID_COLUMN));
            String name = data.getString(data.getColumnIndexOrThrow(ContactDBOpenHelper.KEY_NAME_COLUMN));
            int img = data.getInt(data.getColumnIndexOrThrow(ContactDBOpenHelper.KEY_IMG_COLUMN));
            contactsMap.put(jid, new Contact(jid, name, img));
        }

        Log.d(LOG_TAG, " map size: " + contactsMap.size());
       // getSupportLoaderManager().getLoader(0).forceLoad();
        contactAdapter.swapItems(new ArrayList<Contact>(contactsMap.values()));
       // cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    static class ContactCursorLoader extends CursorLoader{

        SQLiteDatabase db;

        public ContactCursorLoader(Context context, SQLiteDatabase db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            return getAllData();
        }

        public Cursor getAllData(){
            return db.query(ContactDBOpenHelper.CONTACTS_TABLE_NAME, null, null,
                    null, null, null, null);
        }
    }

}
