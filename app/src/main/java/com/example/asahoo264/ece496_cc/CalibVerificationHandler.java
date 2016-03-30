package com.example.asahoo264.ece496_cc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by YanyanZ on 2/10/16.
 */
public class CalibVerificationHandler extends AppCompatActivity  implements View.OnClickListener{
    Button start;
    String name = "";
    private static final int SHOW_IMAGE = 0;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibverify_handler);

        start = (Button) findViewById(R.id.start);

        start.setOnClickListener(this);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.start) {
            Intent i = new Intent(CalibVerificationHandler.this, CalibVerification.class);
            if(name!=null) {
                i.putExtra("Name", name);
                Log.d("Name", name);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            String dbg = "Number started" + count;
            count++;
            Log.d("Call verification", dbg);
            startActivityForResult(i, SHOW_IMAGE);


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_IMAGE) {
            if (resultCode == RESULT_OK) {

                // Successful signup logic here
                Bundle res = data.getExtras();
                String result = res.getString("Result: ");

                Intent i1 = new Intent(CalibVerificationHandler.this, CalibVerificationResult.class);
                if(result!=null) {
                    i1.putExtra("Result", result);
                    Log.d("Verification Result", result);
                }
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
/*
                Intent i2 = new Intent(CalibVerificationHandler.this, CalibVerification.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                if(name!=null) {
                    i2.putExtra("Name", name);
                    Log.d("Name", name);
                }
                i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String dbg = "Number started" + count;
                count++;
                Log.d("Call verification", dbg);
                startActivityForResult(i2, SHOW_IMAGE);
                */
            }
        }
    }




}