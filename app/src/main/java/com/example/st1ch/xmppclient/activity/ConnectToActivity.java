package com.example.st1ch.xmppclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.chat.activity.ChatActivity;
import com.example.st1ch.xmppclient.db.ContactDBOpenHelper;
import com.example.st1ch.xmppclient.contacts.list.logic.ContactListAdapter;
import com.example.st1ch.xmppclient.logic.FillContactsListTask;
import com.example.st1ch.xmppclient.user.logic.Contact;

import java.util.ArrayList;

/**
 * Created by denys on 23.10.15.
 */
public class ConnectToActivity extends Activity {

    private final String LOG_TAG = "chatLog";

    private ListView contactsLV;
    private static ContactListAdapter contactListAdapter;
    private static ArrayList<Contact> contactsList;

    private static CheckBox checkOnline;

    private String user;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_to);

        contactsList = new ArrayList<Contact>();

        contactsLV = (ListView) findViewById(R.id.usersList);
        contactsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactDBOpenHelper contactDBOpenHelper = new ContactDBOpenHelper(ConnectToActivity.this);
                SQLiteDatabase contactsDB = contactDBOpenHelper.getWritableDatabase();
                Intent intent;
                String selectedFromList = contactsList.get(position).getName();
                String sendTo = "";
                String[] resCol = new String[]{ContactDBOpenHelper.KEY_NAME_COLUMN, ContactDBOpenHelper.KEY_JID_COLUMN};
                String where = ContactDBOpenHelper.KEY_NAME_COLUMN + " = " + "\"" + selectedFromList + "\"";
                Cursor cursor = contactsDB.query(ContactDBOpenHelper.CONTACTS_TABLE_NAME, resCol, where,
                        null, null, null, null);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    int jidInd = cursor.getColumnIndex(ContactDBOpenHelper.KEY_JID_COLUMN);
                    sendTo = cursor.getString(jidInd);
                }
                intent = new Intent(ConnectToActivity.this, ChatActivity.class);
                intent.putExtra("sendTo", sendTo);
                intent.putExtra("writeTo", selectedFromList);
                startActivity(intent);
            }
        });
       // contactListAdapter = new ContactListAdapter(this, contactsList);
        contactsLV.setAdapter(contactListAdapter);

        checkOnline = (CheckBox) findViewById(R.id.checkOnline);
        checkOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        new FillContactsListTask(this).execute();

    }

    public static CheckBox getCheckOnline() {return checkOnline;    }

    public static ArrayList<Contact> getContactsList() {
        return contactsList;
    }

    public static ContactListAdapter getContactListAdapter(){
        return contactListAdapter;
    }

    public static void setContactsList(ArrayList<Contact> contactsList) {
        ConnectToActivity.contactsList = contactsList;
    }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Exit")
                .setMessage("Do you want exit")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectToActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertBuilder.show();
    }
}
