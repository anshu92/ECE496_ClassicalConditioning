package com.example.asahoo264.ece496_cc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Handler;

public class verification extends AppCompatActivity {
        String name = "";
        private ImageView sw;
        private final Random r = new Random();

        private static String dir_str = null;
        private double result = 0;
        Intent data = new Intent();

        private boolean disconnected = false;

        private TextView timertext;
        private CalibCountDownTimer timer;
        private final long startTime = 10000;
        private final long interval = 1000;
        private long timeElapsed;
        private boolean timerHasStarted = false;
        private boolean alert = false;
        private boolean alert2 = false;
        private int counter=1;
        private int counter1=1;
        private int ctr = 0;

        // private LoadImage loader;
        private int loader;
        private Handler handler;


        private LruCache<String, Bitmap> mMemoryCache;
        public Intent mServiceIntent,mEventIntent, mEventIntent1;
        private WeakReference<MuseConnectionService.ConnectionListener> weakconn;
        private WeakReference<MuseConnectionService.DataListener> weakdat;
        private WeakReference<Activity> weakActivity;


        private ImageButton happy, sad;
        private AlertDialog alertDialog;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_verify);

                Intent intent = getIntent();
                name = intent.getStringExtra("Name");

                timertext = (TextView)findViewById(R.id.timer);
                timer = new CalibCountDownTimer(startTime,interval);

                dir_str = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
                if(name!=null) {
                        dir_str = dir_str + File.separator + name + File.separator;
                }
                Log.d("In Verification", dir_str);
                weakActivity = new WeakReference<Activity>(this);

                LocalBroadcastManager.getInstance(this).registerReceiver(mElectrodeReceiver,
                        new IntentFilter("horseshoe_event"));

                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                        new IntentFilter("custom-event-name"));
                //   mServiceIntent = new Intent(getApplicationContext(), MuseConnectionService.class);
                // mServiceIntent.setAction("getref");
                // startService(mServiceIntent);

                Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                                counter =  1;

                                Random random = new Random();
                                final boolean switch_state =  random.nextBoolean();

                                new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                                RefObjs.start_of_event = true;
                                        }
                                }).start();

                                timer.start();

                                try {
                                        Thread.sleep(1000); // Waits for 1 second (1000 milliseconds)
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }

                                Log.d("Ready to send vibration", "" + switch_state);
                                String data;
                                if (switch_state) {
                                        data = "!C" + 1 + "1";
                                } else {
                                        data = "!B" + 1 + "1";
                                }
                                ByteBuffer buffer = ByteBuffer.allocate(data.length()).order(java.nio.ByteOrder.LITTLE_ENDIAN);
                                buffer.put(data.getBytes());
                                UartInterfaceActivity.sendDataWithCRC(buffer.array());

                                try {
                                        Thread.sleep(9000); // Waits for 1 second (1000 milliseconds)
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }

                                new  Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                                RefObjs.start_of_event = false;

                                                try {
                                                        RefObjs.predict_event(switch_state, dir_str);
                                                        alert = true;
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                }

                                        }
                                }).start();

                                while(!alert) {
                                        try {
                                                Thread.sleep(100);
                                        }catch (InterruptedException e) {
                                                e.printStackTrace();
                                        }
                                }
                                        try{
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

                                                fname2 = "svm_predict";
                                                file2 = new File(dir_str, fname2);
                                                if (!file2.exists()) {
                                                        file2.createNewFile();
                                                }
                                                fpath2 = file2.toString();

                                                fname3 = "svm_predict.scale";
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
                                                String[] training2 = {"-v", "5", fpath1, fpath2};
                                                String[] training3 = {fpath1, fpath2};

                                                fname1 = "svm_predict";
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

                                                fname3 = "svm_predict.out";
                                                file3 = new File(dir_str, fname3);
                                                if (!file3.exists()) {
                                                        file3.createNewFile();
                                                }
                                                fpath3 = file3.toString();
                                                String[] testing1 = {fpath1, fpath2, fpath3, dir_str};

                                                fname1 = "svm_predict.scale";
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

                                                fname3 = "svm_predict.out";
                                                file3 = new File(dir_str, fname3);
                                                if (!file3.exists()) {
                                                        file3.createNewFile();
                                                }
                                                fpath3 = file3.toString();

                                                String[] testing2 = {fpath1, fpath2, fpath3, dir_str};
                                                //timer.start();

                                                svm_scale.main(scaling1);

                                                svm_scale.main(scaling2);


                                                svm_train.main(training3);
                                                svm_train.main(training2);

                                                svm_predict.main(testing2);


                                        BufferedReader var1 = new BufferedReader(new FileReader(fpath3));
                                        String res = var1.readLine();
                                                Log.d("result", res);
                                        result = Double.valueOf(res).doubleValue();
                                                Log.d("result", " in double" + result);

                                                alert2 = true;
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }




                        }

                };

                Thread myThread = new Thread(myRunnable);
                myThread.start();


        }


        //COUNTDOWN TIMER
        public class CalibCountDownTimer extends CountDownTimer
        {

                public CalibCountDownTimer(long startTime, long interval)
                {
                        super(startTime, interval);
                }

                @Override
                public void onFinish()
                {
                        timertext.setText("Time's up!");
                        while(!alert2){
                                try {
                                        Thread.sleep(1000); // Waits for 1 second (1000 milliseconds)
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                        }
                        Bundle conData = new Bundle();
                        String res = null;
                        if(result > 0){
                                res = "happy";
                        }
                        else{
                                res = "sad";
                        }
                        Log.d("I feel" , res);
                        conData.putString("Result: ", res);
                        data.putExtras(conData);
                        setResult(RESULT_OK, data);
                        if (getParent() == null) {
                                setResult(Activity.RESULT_OK, data);
                        } else {
                                getParent().setResult(Activity.RESULT_OK, data);
                        }
                        finish();
                }

                @Override
                public void onTick(long millisUntilFinished)
                {

                        TextView conc = (TextView) findViewById(R.id.concentration);
                        conc.setText(String.format( "Concentration: %.1f", RefObjs.concentration*100 )+" %");

                        if(RefObjs.concentration*100 > 50 && alertDialog != null){
                                alertDialog.dismiss();
                        }
                        timertext.setText(millisUntilFinished / 1000 + " secs");
                        timeElapsed = startTime - millisUntilFinished;
                        //average_conc = (average_conc + RefObjs.concentration*100)/(timeElapsed/1000);


                }
        }




        @Override
        public void onStop(){

                super.onStop();

                timer.cancel();



        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
                ImageView bmImage;

                public DownloadImageTask(ImageView bmImage) {
                        this.bmImage = bmImage;
                }

                protected Bitmap doInBackground(String... urls) {
                        String urldisplay = urls[0];
                        Bitmap mIcon11 = null;
                        try {
                                InputStream in = new URL(urldisplay).openStream();
                                mIcon11 = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                                Log.e("Error", e.getMessage());
                                e.printStackTrace();
                        }
                        return mIcon11;
                }

                protected void onPostExecute(Bitmap result) {
                        bmImage.setImageBitmap(result);
                }
        }

        private BroadcastReceiver mElectrodeReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                        // Get extra data included in the Intent

                        final ArrayList<Double> vals = (ArrayList<Double>)intent.getSerializableExtra("horseshoe");
                        Activity activity = weakActivity.get();
                        // UI thread is used here only because we need to update
                        // TextView values. You don't have to use another thread, unless
                        // you want to run disconnect() or connect() from connection packet
                        // handler. In this case creating another thread is required.

                        if (activity != null) {
                                activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                                TextView left = (TextView) findViewById(R.id.left_electrode);
                                                TextView lc = (TextView) findViewById(R.id.lc_electrode);
                                                TextView rc = (TextView) findViewById(R.id.rc_electrode);
                                                TextView right = (TextView) findViewById(R.id.right_electrode);
                                                if(!disconnected){
                                                        if (vals.get(0) == 1) {
                                                                left.setBackgroundColor(Color.GREEN);
                                                        } else if (vals.get(0) == 2) {
                                                                left.setBackgroundColor(Color.BLUE);
                                                        } else{
                                                                left.setBackgroundColor(Color.RED);
                                                        }

                                                        if (vals.get(1) == 1) {
                                                                lc.setBackgroundColor(Color.GREEN);
                                                        } else if (vals.get(1) == 2) {
                                                                lc.setBackgroundColor(Color.BLUE);
                                                        } else{
                                                                lc.setBackgroundColor(Color.RED);
                                                        }

                                                        if (vals.get(2) == 1) {
                                                                rc.setBackgroundColor(Color.GREEN);
                                                        } else if (vals.get(2) == 2) {
                                                                rc.setBackgroundColor(Color.BLUE);
                                                        } else{
                                                                rc.setBackgroundColor(Color.RED);
                                                        }

                                                        if (vals.get(3) == 1) {
                                                                right.setBackgroundColor(Color.GREEN);
                                                        } else if (vals.get(3) == 2) {
                                                                right.setBackgroundColor(Color.BLUE);
                                                        } else{
                                                                right.setBackgroundColor(Color.RED);
                                                        }
                                                }
                                        }
                                });
                        }

                }
        };


        private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                        // Get extra data included in the Intent
                        final String message = intent.getStringExtra("message");

                        final CharSequence cs = "DISCONNECTED";
                        Activity activity = weakActivity.get();
                        // UI thread is used here only because we need to update
                        // TextView values. You don't have to use another thread, unless
                        // you want to run disconnect() or connect() from connection packet
                        // handler. In this case creating another thread is required.
                        if (activity != null) {
                                activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                                TextView left = (TextView) findViewById(R.id.left_electrode);
                                                TextView lc = (TextView) findViewById(R.id.lc_electrode);
                                                TextView rc = (TextView) findViewById(R.id.rc_electrode);
                                                TextView right = (TextView) findViewById(R.id.right_electrode);

                                                if (message.contains(cs)) {
                                                        left.setBackgroundColor(Color.RED);
                                                        lc.setBackgroundColor(Color.RED);
                                                        rc.setBackgroundColor(Color.RED);
                                                        right.setBackgroundColor(Color.RED);
                                                        disconnected = true;
                                                }

                                        }
                                });
                        }
                }
        };

}
