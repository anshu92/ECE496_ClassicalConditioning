package com.example.asahoo264.ece496_cc;


import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.interaxon.libmuse.Accelerometer;
import com.interaxon.libmuse.AnnotationData;
import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Eeg;
import com.interaxon.libmuse.LibMuseVersion;
import com.interaxon.libmuse.MessageType;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.MuseConfiguration;
import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseFileFactory;
import com.interaxon.libmuse.MuseFileReader;
import com.interaxon.libmuse.MuseFileWriter;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.libmuse.MusePreset;
import com.interaxon.libmuse.MuseVersion;

import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectActivity extends Activity implements OnClickListener{


    private boolean dataTransmission = true;
    private int clench_count = 0;
    public static String status = null;
    public static ArrayAdapter<String> adapterArray_status = null;
    public static long[] event_timestamps = new long[10];
    public static int event_counter = 0;
    public static  boolean start_recording = false;
    public static boolean start_of_event = false;
    public static  boolean updating_data = false;
    public static  boolean updating_conn = false;
    public static ConnectionState connection_state = null;
    public static ConnectionState previous_connection_state = null;
    public static int[] epoch_num = new int[10];

    public static final boolean ON_PHONE = true;


    public Intent mServiceIntent;
    private WeakReference<Activity> weakActivity;
    public static WeakReference<MuseConnectionService.ConnectionListener> weakconn;
    public static WeakReference<MuseConnectionService.DataListener> weakdat;

    public ConnectActivity() {

        weakActivity = new WeakReference<Activity>(this);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);


        mServiceIntent = new Intent(this, MuseConnectionService.class);
        mServiceIntent.setAction("setup");
        startService(mServiceIntent);

                Button disconnectbutton, connectbutton, refreshbutton;

        disconnectbutton = (Button) findViewById(R.id.disconnect);
        connectbutton = (Button) findViewById(R.id.connect);
        refreshbutton = (Button) findViewById(R.id.refresh);
        disconnectbutton.setOnClickListener(this);
        connectbutton.setOnClickListener(this);
        refreshbutton.setOnClickListener(this);


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mElectrodeReceiver,
                new IntentFilter("horseshoe_event"));


    }



    @Override
    public void onClick(View v) {
        Spinner musesSpinner = (Spinner) findViewById(R.id.muses_spinner);
        if (v.getId() == R.id.refresh) {
            MuseManager.refreshPairedMuses();
            List<Muse> pairedMuses = MuseManager.getPairedMuses();
            List<String> spinnerItems = new ArrayList<String>();
            for (Muse m: pairedMuses) {
                String dev_id = m.getName() + "-" + m.getMacAddress();
                Log.i("Muse Headband", dev_id);
                spinnerItems.add(dev_id);
            }
            ArrayAdapter<String> adapterArray = new ArrayAdapter<String> (
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       this, android.R.layout.simple_spinner_item, spinnerItems);
            adapterArray_status = adapterArray;
            musesSpinner.setAdapter(adapterArray);
        }
        else if (v.getId() == R.id.connect) {
            List<Muse> pairedMuses = MuseManager.getPairedMuses();
            if (pairedMuses.size() < 1 ||
                    musesSpinner.getAdapter().getCount() < 1) {
                Log.w("Muse Headband", "There is nothing to connect to");
            }
            else {

                RefObjs.muse = pairedMuses.get(musesSpinner.getSelectedItemPosition());
                ConnectionState state = RefObjs.muse.getConnectionState();
                if (state == ConnectionState.CONNECTED ||
                        state == ConnectionState.CONNECTING) {
                    Log.w("Muse Headband",
                            "doesn't make sense to connect second time to the same muse");
                    return;
                }
//                if(ON_PHONE) {
//                    fileWriter.open();
//                    fileWriter.addAnnotationString(1, "Connect clicked");
//                }
                /**
                 * In most cases libmuse native library takes care about
                 * exceptions and recovery mechanism, but native code still
                 * may throw in some unexpected situations (like bad bluetooth
                 * connection). Print all exceptions here.
                 */

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        configureLibrary();


                    } catch (Exception e) {
                        Log.e("Muse Headband", e.toString());
                    }

                }
            });


//Set the schedule function and rate



//                            try {
//                                muse.runAsynchronously();
//                            } catch (Exception e) {
//                                Log.e("Muse Headband", e.toString());
//                            }
//                        }
//                    });
//                }
            }
        }
        else if (v.getId() == R.id.disconnect) {
            if (RefObjs.muse != null) {
                /**
                 * true flag will force libmuse to unregister all listeners,
                 * BUT AFTER disconnecting and sending disconnection event.
                 * If you don't want to receive disconnection event (for ex.
                 * you call disconnect when application is closed), then
                 * unregister listeners first and then call disconnect:
                 * muse.unregisterAllListeners();
                 * muse.disconnect(false);
                 */
                RefObjs.muse.disconnect(true);
//                if(ON_PHONE) {
//                    fileWriter.addAnnotationString(1, "Disconnect clicked");
//                    fileWriter.flush();
//                    fileWriter.close();
//                }
            }
        }

    }





    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            final String message = intent.getStringExtra("message");
            status = message;
            Log.d("receiver", "Got message: " + message);
            Activity activity = weakActivity.get();
            // UI thread is used here only because we need to update
            // TextView values. You don't have to use another thread, unless
            // you want to run disconnect() or connect() from connection packet
            // handler. In this case creating another thread is required.
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView statusText =
                                (TextView) findViewById(R.id.con_status);

                            statusText.setText(message);


                    }
                });
            }

        }
    };


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
                });
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        TextView statusText =
                (TextView) findViewById(R.id.con_status);

        statusText.setText(status);


    }

    public void configureLibrary() {

        RefObjs.muse.registerConnectionListener(MuseConnectionService.connectionListener);
        RefObjs.muse.registerDataListener(MuseConnectionService.dataListener,
                MuseDataPacketType.ALPHA_RELATIVE);
        RefObjs.muse.registerDataListener(MuseConnectionService.dataListener,
                MuseDataPacketType.BETA_RELATIVE);
        RefObjs.muse.registerDataListener(MuseConnectionService.dataListener,
                MuseDataPacketType.GAMMA_RELATIVE);
        RefObjs.muse.registerDataListener(MuseConnectionService.dataListener,
                MuseDataPacketType.THETA_RELATIVE);
        RefObjs.muse.registerDataListener(MuseConnectionService.dataListener,
                MuseDataPacketType.HORSESHOE);
        RefObjs.muse.setPreset(MusePreset.PRESET_14);
        RefObjs.muse.enableDataTransmission(true);
        RefObjs.muse.runAsynchronously();


    }



}
