package com.example.st1ch.xmppclient.logic;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.st1ch.xmppclient.chat.activity.ChatActivity;
import com.example.st1ch.xmppclient.VPN.activity.VPNActivity;
import com.example.st1ch.xmppclient.chat.logic.ChatMessage;
import com.example.st1ch.xmppclient.db.ChatHistoryDBOpenHelper;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by st1ch on 20.10.15.
 */
public class ReceiveMessageTask extends AsyncTask<String,String,Void> {

    Context context;

    private SQLiteDatabase chatHistoryDB;
    private ChatHistoryDBOpenHelper chatHistoryDBOpenHelper;

    public ReceiveMessageTask(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(final String... mess) {

        if(ConnectClientTask.getConnection().isConnected()) {
            StanzaFilter filter = new StanzaTypeFilter(Message.class);
            ConnectClientTask.getConnection().addAsyncStanzaListener(new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    Message message = (Message) packet;
                    String type = String.valueOf(message.getType());
                    String from = message.getFrom().split("/")[0];
                    Log.d("chatLog", "message FROM : " + from + " type : " + message.getType()
                            + " to : " + message.getTo());



                    if (type.equals("chat")) {
                        if (message.getBody().equals("onvpn")) {
                            Intent intent = new Intent(context, VPNActivity.class);
                            context.startActivity(intent);
                        } else if (message.getBody().substring(0, 2).equals("&%")) {
                            Log.d("chatLog", "SYSTEM MESSAGE");
                            publishProgress("SYSTEM MESSAGE");
                        } else {
                            Log.d("chatLog", "Received message from " + from
                                    + " : " + message.getBody());
                            publishProgress(from
                                    + "!@#%%#@!" + message.getBody());

                            chatHistoryDBOpenHelper = new ChatHistoryDBOpenHelper(context, from);
                            chatHistoryDB = chatHistoryDBOpenHelper.getWritableDatabase();

                            writeToDb(from, message);

                            //  NotificatonClass notificatonClass = new NotificatonClass(context);
                          //  notificatonClass.getNotify(NotificatonClass.NEW_MESSAGE_ID);
                        }
                    }
                }
            }, filter);
        }
        return null;
    }


    private void writeToDb(String from, Message message){

        try {
            ContentValues messVal = new ContentValues();
            messVal.put(ChatHistoryDBOpenHelper.KEY_MESSAGE_COLUMN,
                    from + "!@#%%#@!" + message.getBody());
            chatHistoryDB.insert(ChatHistoryDBOpenHelper.KEY_TABLE_NAME, null, messVal);
            Log.d("chatLog", "Inserted new mess row in table "
                    + ChatHistoryDBOpenHelper.KEY_TABLE_NAME);
        } catch (Exception ex){
            chatHistoryDBOpenHelper.onCreate(chatHistoryDB);
            return;
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        ChatMessage chatMessage = new ChatMessage(false, values[0].trim().split("!@#%%#@!")[1]);
        chatMessage.setDate(values[0].trim().split("!@#%%#@!")[2]);
        try{
            ChatActivity.displayMessage(chatMessage);
        }catch (Exception ex){

        }
    }

}
