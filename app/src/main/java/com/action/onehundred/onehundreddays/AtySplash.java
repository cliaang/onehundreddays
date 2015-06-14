package com.action.onehundred.onehundreddays;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class AtySplash extends Activity {

    //private SharedPreferences preferences;
    private static final int SPLASH_TIME = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_splash);
        final SharedPreferences preferences = getSharedPreferences("document",MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferences.getBoolean("first_start", true)) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("first_start", false);
                    editor.apply();
                    startActivity(new Intent(AtySplash.this, AtyGuide.class));
                    finish();
                } else {
                    startActivity(new Intent(AtySplash.this, AtyCurrentAction.class));
                    finish();
                }
            }
        }, SPLASH_TIME);
    }
}
