package com.example.st1ch.xmppclient.chat.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.db.ChatHistoryDBOpenHelper;
import com.example.st1ch.xmppclient.chat.logic.ChatAdapter;
import com.example.st1ch.xmppclient.chat.logic.ChatMessage;
import com.example.st1ch.xmppclient.logic.ConnectClientTask;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by st1ch on 09.12.15.
 */
public class ChatActivity extends Activity implements View.OnClickListener {

    private EditText messageET;
    private static ListView messagesContainer;
    private Button sendBtn;
    private static ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private String sendTo="";
    private TextView writeTo;

    private SQLiteDatabase chatHistoryDB;
    private ChatHistoryDBOpenHelper chatHistoryDBOpenHelper;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);
        writeTo = (TextView) findViewById(R.id.tvWriteTo);
        chatHistory = new ArrayList<ChatMessage>();
        adapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        sendBtn.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent.hasExtra("sendTo")) sendTo = intent.getStringExtra("sendTo");
        if(intent.hasExtra("writeTo")) writeTo.setText(intent.getStringExtra("writeTo"));

        chatHistoryDBOpenHelper = new ChatHistoryDBOpenHelper(this, sendTo);
        chatHistoryDB = chatHistoryDBOpenHelper.getWritableDatabase();
        try{
            getHistory();
        }catch (Exception ex){
            chatHistoryDBOpenHelper.onCreate(chatHistoryDB);
            getHistory();
        }

    }

    private void getHistory(){
        String[] columns = new String[]{ChatHistoryDBOpenHelper.KEY_MESSAGE_COLUMN};
        cursor = chatHistoryDB.query(ChatHistoryDBOpenHelper.KEY_TABLE_NAME, columns,
                null, null, null, null, null);
        if(cursor.getCount() != 0) {
            int messInd = cursor.getColumnIndex(ChatHistoryDBOpenHelper.KEY_MESSAGE_COLUMN);
            while (cursor.moveToNext()) {
                String message = cursor.getString(messInd);
                if(message.trim().split("!@#%%#@!")[0].equals("Me")){
                    ChatMessage chatMessage = new ChatMessage(true, message.trim().split("!@#%%#@!")[1]);
                    chatMessage.setDate(message.trim().split("!@#%%#@!")[2]);
                    displayMessage(chatMessage);
                } else {
                    ChatMessage chatMessage = new ChatMessage(false, message.trim().split("!@#%%#@!")[1]);
                    chatMessage.setDate(message.trim().split("!@#%%#@!")[2]);
                    displayMessage(chatMessage);
                }

            }
        }
    }


    public static void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private static void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        String messageText = messageET.getText().toString();
        String messTextDate = messageText + "!@#%%#@!"
                + DateFormat.getDateTimeInstance().format(new Date());
        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        Message message = new Message();
        message.setFrom(ConnectClientTask.getConnection().getUser());
        message.setTo(sendTo);
        message.setType(Message.Type.chat);
        message.setBody(messTextDate);

        try {
            ConnectClientTask.getConnection().sendStanza(message);
            Log.d("chatLog", "Message sent : " + message.getBody());

            ChatMessage chatMessage = new ChatMessage(true, messageText);
            chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            messageET.setText("");
            displayMessage(chatMessage);

            ContentValues sendedMess = new ContentValues();
            sendedMess.put(ChatHistoryDBOpenHelper.KEY_MESSAGE_COLUMN, "Me!@#%%#@!" + messTextDate);
            Log.d("chatLog", "To db : " + sendedMess.toString());
            Log.d("chatLog", "To db split: " + sendedMess.toString().split("!@#%%#@!")[1]
                    + sendedMess.toString().split("!@#%%#@!")[2]);
            chatHistoryDB.insert(ChatHistoryDBOpenHelper.KEY_TABLE_NAME, null, sendedMess);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

}

