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

/**
 * Created by YanyanZ on 2/10/16.
 */
public class Handlers extends AppCompatActivity implements View.OnClickListener {
    Button calibbutton, verifybutton;
    String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");

        calibbutton = (Button) findViewById(R.id.calibbutton);
        verifybutton = (Button) findViewById(R.id.verifybutton);

        calibbutton.setOnClickListener(this);
        verifybutton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.calibbutton) {
            Intent i = new Intent(Handlers.this, CalibIntro.class);

            i.putExtra("Name", name);
            if(name != null) {
                Log.d("In Handler. Name", name);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);


        } else if (v.getId() == R.id.verifybutton) {
            Intent i = new Intent(Handlers.this, CalibVerificationHandler.class);
            i.putExtra("Name", name);
            if(name != null) {
                Log.d("In Handler. Name", name);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }




}