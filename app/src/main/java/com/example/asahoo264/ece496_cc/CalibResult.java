package com.example.asahoo264.ece496_cc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by YanyanZ on 2/10/16.
 */
public class CalibResult extends AppCompatActivity  implements View.OnClickListener{
    private TextView tv;
    private static final int SHOW_IMAGE = 0;
    String name = "";
    String dir_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calib_result);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");

        dir_str = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
        if(name!=null) {
            dir_str = dir_str + File.separator + name + File.separator;
        }

        tv = (TextView) findViewById(R.id.textView2);

        run_svm();

        tv.setText("" + svm_predict.acc);
        //view.invalidate();

    }

    private void run_svm(){
        try {
            String fname1 = "range1";
            File file1 = new File(dir_str, fname1);
            if (!file1.exists()) {
                file1.createNewFile();
            }
            String fpath1 = file1.toString();

            String fname2 = "svminput";
            File file2 = new File(dir_str, fname2);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            String fpath2 = file2.toString();

            String fname3 = "svminput.scale";
            File file3 = new File(dir_str, fname3);
            if (!file3.exists()) {
                file3.createNewFile();
            }
            String fpath3 = file3.toString();
            //ConnectActivity.start_recording = false;
            String[] scaling1 = {"-l", "-1", "-u", "1", "-s", fpath1, fpath2/*, ">"*/, fpath3};

            fname1 = "range1";
            file1 = new File(dir_str, fname1);
            if (!file1.exists()) {
                file1.createNewFile();
            }
            fpath1 = file1.toString();

            fname2 = "svminput.t";
            file2 = new File(dir_str, fname2);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            fpath2 = file2.toString();

            fname3 = "svminput.t.scale";
            file3 = new File(dir_str, fname3);
            if (!file3.exists()) {
                file3.createNewFile();
            }
            fpath3 = file3.toString();
            String[] scaling2 = {"-r", fpath1, fpath2/*, ">"*/, fpath3};

            fname1 = "svminput";
            file1 = new File(dir_str, fname1);
            if (!file1.exists()) {
                file1.createNewFile();
            }
            fpath1 = file1.toString();

            fname2 = "svminput.model";
            file2 = new File(dir_str, fname2);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            fpath2 = file2.toString();
            String[] training1 = {fpath1, fpath2};
            Log.d("Trianing1", fpath2);

            fname1 = "svminput.scale";
            file1 = new File(dir_str, fname1);
            if (!file1.exists()) {
                file1.createNewFile();
            }
            fpath1 = file1.toString();

            fname2 = "svminput.scale.model";
            file2 = new File(dir_str, fname2);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            fpath2 = file2.toString();
            String[] training2 = {"-c", "2048", "-g", "0.00048828125", "-b", "1", "-v", "5", fpath1, fpath2};
            //String[] training3 = {"-b", "1", fpath1, fpath2};

            fname1 = "svminput.t";
            file1 = new File(dir_str, fname1);
            if (!file1.exists()) {
                file1.createNewFile();
            }
            fpath1 = file1.toString();

            fname2 = "svminput.model";
            file2 = new File(dir_str, fname2);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            fpath2 = file2.toString();

            fname3 = "svminput.out";
            file3 = new File(dir_str, fname3);
            if (!file3.exists()) {
                file3.createNewFile();
            }
            fpath3 = file3.toString();
            String[] testing1 = {fpath1, fpath2, fpath3, dir_str};

            fname1 = "svminput.t.scale";
            file1 = new File(dir_str, fname1);
            if (!file1.exists()) {
                file1.createNewFile();
            }
            fpath1 = file1.toString();

            fname2 = "svminput.scale.model";
            file2 = new File(dir_str, fname2);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            fpath2 = file2.toString();

            fname3 = "svminput.scale.out";
            file3 = new File(dir_str, fname3);
            if (!file3.exists()) {
                file3.createNewFile();
            }
            fpath3 = file3.toString();

            String[] testing2 = {"-b", "1", fpath1, fpath2, fpath3, dir_str};

            svm_scale.main(scaling1);

            svm_scale.main(scaling2);


            svm_train.main(training2);
            //svm_train.main(training3);

            svm_predict.main(testing2);
        } catch (IOException e) {
            e.printStackTrace();
        }
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