package com.example.st1ch.xmppclient.logic;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.contacts.list.activity.ContactsListActivity;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by st1ch on 20.10.15.
 */
public class ConnectClientTask extends AsyncTask<Void,Void,Void> {

    public static XMPPTCPConnection connection;
    private String username;
    private String service;
    private String password;
    private Context context;

    public ConnectClientTask(Context context, String username, String password){
        this.context = context;
        service = context.getString(R.string.serviceName);
        this.username = username;
        this.password = password;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("chatLog", "bg");
        Resources res = context.getResources();
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        builder.setServiceName(service);
        builder.setResource(context.getString(R.string.resource));
        builder.setPort(res.getInteger(R.integer.serverPort));
        builder.setHost(context.getString(R.string.serverIP));
        builder.setDebuggerEnabled(true);

        connection = new XMPPTCPConnection(builder.build());
        connection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connections) {
                Log.d("chatLog", "Connected to " + connection.getServiceName());

                try {
                    connection.login(username, password);
                } catch (IOException | SmackException | XMPPException e) {
                    e.printStackTrace();
                    Log.d("chatLog", e.toString());
                }

            }

            @Override
            public void authenticated(XMPPConnection connection, boolean resumed) {
                Log.d("chatLog", "Authenticated as: " + username + "@" + service);
                new ContactsToDBTask(context, username).execute();
                new ReceiveMessageTask(context).execute();
            }

            @Override
            public void connectionClosed() {
                Log.d("chatLog", "Connection closed!");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                Log.d("chatLog", "Connection closed on Error!");
            }

            @Override
            public void reconnectionSuccessful() {
                Log.d("chatLog", "Reconnected");
            }

            @Override
            public void reconnectingIn(int seconds) {
                Log.d("chatLog", "Trying to reconnect!");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                Log.d("chatLog", "Reconnection failed!");
            }
        });

        try {
            connection.setPacketReplyTimeout(3000);
            connection.connect();
        } catch (SmackException e) {
            e.printStackTrace();
            Log.d("chatLog", "SmackException: " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("chatLog", "IOException: " + e);
        } catch (XMPPException e) {
            e.printStackTrace();
            Log.d("chatLog", "XMPPException: " + e);

        }

        Intent intent = new Intent(context, ContactsListActivity.class);
        context.startActivity(intent);


        return null;
    }


    public static XMPPTCPConnection getConnection() {
        return connection;
    }

}
