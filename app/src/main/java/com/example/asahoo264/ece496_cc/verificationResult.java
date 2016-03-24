package com.example.asahoo264.ece496_cc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by YanyanZ on 2/10/16.
 */
public class verificationResult extends AppCompatActivity  implements View.OnClickListener{
    Button yes, no;
    String result = null;
    private ImageView sw;
    private static final int SHOW_IMAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibverify_result);

        yes = (Button) findViewById(R.id.button);
        no = (Button) findViewById(R.id.button2);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        Intent intent = getIntent();
        result = intent.getStringExtra("Result");
        sw = (ImageView) findViewById(R.id.imageView);
        Bitmap bm = null;
        if(result.equals("happy")) {
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.happyicon);
        }
        else if(result.equals("sad")){
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.sadicon);
        }
        sw.setImageBitmap(bm);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button) {

            finish();

        } else if (v.getId() == R.id.button2) {
            finish();
        }

    }
}