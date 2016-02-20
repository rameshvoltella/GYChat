package com.example.st1ch.xmppclient.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.service.ServiceConnect;

/**
 * Created by st1ch on 13.02.16.
 */
public class ContactListActivity extends Activity {

    ServiceConnect.ConnectionBinder m_serviceConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_to);

        Intent intent = new Intent(this, ServiceConnect.class);
        bindService(intent, m_serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection m_serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            m_serviceConnect = ((ServiceConnect.ConnectionBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            m_serviceConnect = null;
        }
    };
}
