package com.example.asahoo264.ece496_cc;

/**
 * Created by asahoo264 on 1/30/2016.
 */
import android.app.Activity;
import android.os.AsyncTask;

import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


class ReadFile extends AsyncTask<String, String, Integer> {
    Activity main;
    String name;
    Integer result;


    public ReadFile(Activity main, String name) {
        this.main = main;
        this.name = name;
    }

    @Override
    protected Integer doInBackground(String... args) {

        File file1 = new File(name);

        try {
            FileInputStream fIn = new FileInputStream(file1);
            result = fIn.read();
            fIn.close();


        } catch (IOException e) {
            e.printStackTrace();

        }

        return result;

    }

    protected void onProgressUpdate(String... progress) {
        //called when the background task makes any progress
    }

    protected void onPreExecute() {
        //called before doInBackground() is started

    }

    //called after doInBackground() has finished
    protected void onPostExecute() {


        Toast.makeText(main, result.toString(), Toast.LENGTH_SHORT).show();

    }
}
