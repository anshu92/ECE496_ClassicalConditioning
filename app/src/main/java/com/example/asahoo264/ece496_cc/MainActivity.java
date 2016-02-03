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

    Button connectbutton, calibbutton, plotbutton, verifybutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectbutton = (Button) findViewById(R.id.connectbutton);
        calibbutton = (Button) findViewById(R.id.calibbutton);
        plotbutton = (Button) findViewById(R.id.plotbutton);
        verifybutton = (Button) findViewById(R.id.verifybutton);

        connectbutton.setOnClickListener(this);
        calibbutton.setOnClickListener(this);
        plotbutton.setOnClickListener(this);
        verifybutton.setOnClickListener(this);


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

        } else if (v.getId() == R.id.calibbutton) {
            Intent i = new Intent(MainActivity.this, CalibActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);


        } else if (v.getId() == R.id.plotbutton) {
            Intent i = new Intent(MainActivity.this, ConnectActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (v.getId() == R.id.verifybutton) {
            Intent i = new Intent(MainActivity.this, ConnectActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }




}