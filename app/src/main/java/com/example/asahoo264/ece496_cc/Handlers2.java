package com.example.asahoo264.ece496_cc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by YanyanZ on 2/10/16.
 */
public class Handlers2 extends AppCompatActivity implements View.OnClickListener {
    Button plotbutton, verifybutton;
    String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");

        plotbutton = (Button) findViewById(R.id.plotbutton);
        verifybutton = (Button) findViewById(R.id.verifybutton);

        plotbutton.setOnClickListener(this);
        verifybutton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.plotbutton) {
            Intent i = new Intent(Handlers2.this, ConnectActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (v.getId() == R.id.verifybutton) {
            Intent i = new Intent(Handlers2.this, ConnectActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }




}