package com.example.st1ch.xmppclient.logic;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.db.LoginDBOpenHelper;
import com.example.st1ch.xmppclient.user.activity.LoginActivity;

/**
 * Created by st1ch on 10.12.15.
 */
public class LoginTask extends AsyncTask<Void, Void, Void> {

    private SQLiteDatabase loginDB;
    private LoginDBOpenHelper loginDBOpenHelper;
    private Cursor cursor;
    private Context context;
    private String username;
    private String password;
    private static final int port = 5222;

    private static final int SPLASH_SHOW_TIME = 3000;

    public LoginTask(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            Thread.sleep(SPLASH_SHOW_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loginDBOpenHelper = new LoginDBOpenHelper(context.getApplicationContext());
        loginDB = loginDBOpenHelper.getReadableDatabase();

        String[] resultCol = new String[]{LoginDBOpenHelper.KEY_JID_COLUMN,
                LoginDBOpenHelper.KEY_PASSWORD_COLUMN};
        cursor = loginDB.query(LoginDBOpenHelper.DB_TABLE_NAME, resultCol,
                null, null, null, null, null);

        check();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        cancel(true);
    }

    private void check(){
        if(cursor.getCount() > 0){
            int colJIDIndex = cursor.getColumnIndexOrThrow(LoginDBOpenHelper.KEY_JID_COLUMN);
            int colPassIndex = cursor.getColumnIndexOrThrow(LoginDBOpenHelper.KEY_PASSWORD_COLUMN);
            cursor.moveToFirst();
            username = cursor.getString(colJIDIndex);
            Log.d("chatLog", "username from DB = " + username);
            password = cursor.getString(colPassIndex);
            Log.d("chatLog", "password from DB = " + password);
            cursor.close();
            login(username, password);
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }

    private void login(String username, String pass){
       /* Client.getInstance().setHostIP(context.getString(R.string.serverIP));
        Client.getInstance().setServiceName(context.getString(R.string.serviceName));
        Client.getInstance().setPort(port);
        Client.getInstance().setUsername(username);
        Client.getInstance().setPassword(pass); */

        ConnectClientTask connectClientTask = new ConnectClientTask(context, username, pass);
        connectClientTask.execute();
    }
}
