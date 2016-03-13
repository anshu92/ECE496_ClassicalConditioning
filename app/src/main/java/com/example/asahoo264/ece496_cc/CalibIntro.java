package com.example.asahoo264.ece496_cc;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.Random;

public class CalibIntro extends AppIntro {

    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(first_fragment);
//        addSlide(second_fragment);
//        addSlide(third_fragment);
//        addSlide(fourth_fragment);
        //SLIDE 1
       String title = "Welcome to the Calibration module";
       String description = "This module trains the brain to react to vibration patterns in positive and negative ways";
       int image = R.drawable.iconcalib;
       int background_colour = Color.parseColor("#00BFA5");
       addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));
       // setBarColor(Color.parseColor("#00BFA5"));
        //ssetSeparatorColor(Color.parseColor("#00BFA5"));
        //SLIDE 2
        title = "CONCENTRATE";
        description = "For this module, we need you to move into a quiet environment without any disturbances. Good EEG data will ensure good results.";
        image = R.drawable.iconmeditation;
        background_colour = Color.parseColor("#0091EA");
        addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        //SLIDE 3
        Random r = new Random();
        int i1 = r.nextInt(100 - 50) + 50;
        int i2 = r.nextInt(10 - 0);
        description = "Let's focus by doing math!";
        title = "Multiply " + String.valueOf(i1)+ " X " + String.valueOf(i2) + " = ?";
        image = R.drawable.iconmath;
        background_colour = Color.parseColor("#FF6D00");
        addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));
        // OPTIONAL METHODS
        // Override bar/separator color.
       // setBarColor(Color.parseColor("#0091EA"));
        //setSeparatorColor(Color.parseColor("#0091EA"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed() {
        Intent i = new Intent(CalibIntro.this, CalibActivity.class);
        //i.putExtra("Name", name);
        //Log.d("In Handler. Name", name);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);    }

    @Override
    public void onDonePressed() {
        Intent i = new Intent(CalibIntro.this, CalibActivity.class);
        //i.putExtra("Name", name);
        //Log.d("In Handler. Name", name);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

}
