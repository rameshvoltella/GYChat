package com.example.st1ch.xmppclient.user.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.st1ch.xmppclient.db.LoginDBOpenHelper;
import com.example.st1ch.xmppclient.logic.Client;
import com.example.st1ch.xmppclient.logic.ConnectClientTask;
import com.example.st1ch.xmppclient.R;


public class LoginActivity extends Activity implements View.OnClickListener {

    private static String username;
    private static String pass;
  //  private static final int port = 5222;

    private EditText login;
    private EditText password;
    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        login = (EditText) findViewById(R.id.etLogin);
        password = (EditText) findViewById(R.id.etPassword);
        Button btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConnect:
                username = login.getText().toString();
                pass = password.getText().toString();
                if(!TextUtils.isEmpty(username)&& !TextUtils.isEmpty(pass)){
                    // TODO: перенести запись в "атентификацию"
                    LoginDBOpenHelper loginDBOpenHelper = new LoginDBOpenHelper(this);
                    SQLiteDatabase loginDB = loginDBOpenHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(LoginDBOpenHelper.KEY_JID_COLUMN, username);
                    cv.put(LoginDBOpenHelper.KEY_PASSWORD_COLUMN, pass);
                    loginDB.insert(LoginDBOpenHelper.DB_TABLE_NAME, null, cv);
                    Log.d("chatLog", "inserted in DB: " + username + " " + pass);
                    login(username, pass);
                } else {
                    Toast.makeText(this, "Enter username and password", Toast.LENGTH_LONG).show();
                }
                break;

        }

    }

    private void createProgressDialog(){
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Connecting to server. Please wait..");
        mProgressDialog.show();
    }

    private void login(String username, String pass){
       /* Client.getInstance().setHostIP(getString(R.string.serverIP));
        Client.getInstance().setServiceName(getString(R.string.serviceName));
        Client.getInstance().setPort(port);
        Client.getInstance().setUsername(username);
        Client.getInstance().setPassword(pass);
        */

        ConnectClientTask connectClientTask = new ConnectClientTask(this, username, pass);
        connectClientTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    public static String getUsername() {
        return username;
    }

    public static ProgressDialog getmProgressDialog() {
        return mProgressDialog;
    }
}
