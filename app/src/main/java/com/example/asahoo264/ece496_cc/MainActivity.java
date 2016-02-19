package com.example.asahoo264.ece496_cc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button connectbutton,connectbutton1, authbutton, handlerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectbutton = (Button) findViewById(R.id.connectbutton);
        connectbutton1 = (Button) findViewById(R.id.connectbutton1);
        authbutton = (Button) findViewById(R.id.authbutton);
        handlerbutton = (Button) findViewById(R.id.handlerbutton);

        connectbutton.setOnClickListener(this);
        connectbutton1.setOnClickListener(this);
        authbutton.setOnClickListener(this);
        handlerbutton.setOnClickListener(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.connectbutton) {
            Intent i = new Intent(MainActivity.this, ConnectActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else if (v.getId() == R.id.connectbutton1) {
            Intent i = new Intent(MainActivity.this, WristbandActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }else if (v.getId() == R.id.authbutton) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);


        } else if (v.getId() == R.id.handlerbutton) {
            Intent i = new Intent(MainActivity.this, Handlers.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }




}