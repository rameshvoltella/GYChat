package com.example.st1ch.xmppclient.contacts.list.logic;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.db.ContactDBOpenHelper;

/**
 * Created by st1ch on 19.02.16.
 */
public class ContactsListCursorAdapter extends CursorAdapter {

    public ContactsListCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.contactslist_img);
        ImageView ivStatus = (ImageView) view.findViewById(R.id.contactslist_status_img);
        TextView tvName = (TextView) view.findViewById(R.id.contactslist_name_tv);
        TextView tvOnline = (TextView) view.findViewById(R.id.contactslist_online_tv);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBOpenHelper.KEY_NAME_COLUMN));
        ivPhoto.setImageResource(R.mipmap.icon);
        tvName.setText(name);
        tvOnline.setText("offline");
        ivStatus.setImageResource(R.drawable.offline);

    }
}
