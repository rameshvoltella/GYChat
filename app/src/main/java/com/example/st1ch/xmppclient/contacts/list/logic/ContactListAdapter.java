package com.example.st1ch.xmppclient.contacts.list.logic;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.db.ContactDBOpenHelper;
import com.example.st1ch.xmppclient.user.logic.Contact;

import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by st1ch on 21.10.15.
 */
public class ContactListAdapter extends BaseAdapter {

    private List<Contact> contactList;
    private LayoutInflater layoutInflater;

    public ContactListAdapter(Context context, Map<String, Contact> contactMap){
        this.contactList = new ArrayList<Contact>(contactMap.values());
        //layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setContactList(List<Contact> data) {
        contactList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = (Contact) getItem(position);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.contact_list_item, null);
        }

        //String contactName = contactList.get(position).getName();
        if(contact != null){
            Log.d("chatLog", "contact adapter: " + contact.getName() + " " + contact.getImgId() + " "
                    + contact.isOnline());
            ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.contactslist_img);
            ImageView ivStatus = (ImageView) convertView.findViewById(R.id.contactslist_status_img);
            TextView tvName = (TextView) convertView.findViewById(R.id.contactslist_name_tv);
            TextView tvOnline = (TextView) convertView.findViewById(R.id.contactslist_online_tv);

            ivPhoto.setImageResource(contact.getImgId());
            tvName.setText(contact.getName());
            tvOnline.setText(convertOnline(contact.isOnline()));
            ivStatus.setImageResource(R.drawable.offline);
        }
        return convertView;
    }

    public String convertOnline(boolean isOnline){
        if(isOnline){
            return "online";
        } else return "offline";
    }

    public void swapItems(List<Contact> items){
        this.contactList = items;
        notifyDataSetChanged();
    }


}
