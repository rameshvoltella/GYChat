package com.example.st1ch.xmppclient.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.st1ch.xmppclient.R;
import com.example.st1ch.xmppclient.logic.LoginTask;

/**
 * Created by st1ch on 10.12.15.
 */
public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new LoginTask(this).execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
