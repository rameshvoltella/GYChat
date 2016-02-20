package com.example.st1ch.xmppclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ServiceConnect extends Service {
    public ServiceConnect() {
    }

    final String LOG_TAG = "chatLog";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "ServiceConnect onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "ServiceConnect onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "ServiceConnect onStartCommand");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class ConnectionBinder extends Binder {
        public ConnectionBinder getService(){
            return ConnectionBinder.this;
        }
    }
}
