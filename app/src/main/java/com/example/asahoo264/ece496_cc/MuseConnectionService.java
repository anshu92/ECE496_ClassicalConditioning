package com.example.asahoo264.ece496_cc;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.LibMuseVersion;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseFileFactory;
import com.interaxon.libmuse.MuseFileWriter;
import com.interaxon.libmuse.MusePreset;
import com.interaxon.libmuse.MuseVersion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MuseConnectionService extends IntentService {


    public static ConnectionState connection_state = null;
    public static ConnectionState previous_connection_state = null;
    public static String status = null;
    private static boolean ON_PHONE = true;
    public static  boolean start_recording = false;
    private static boolean send_ref = false;
    private static boolean done_conn = true;
    public static MuseConnectionService.ConnectionListener connectionListener = null;
    public static MuseConnectionService.DataListener dataListener = null;

    private static String dir_str = null;

    private MuseFileWriter fileWriter = null;





    public MuseConnectionService() {
        super("MuseConnectionService");
    }

    WeakReference<ConnectionListener> weakConn;
    WeakReference<DataListener> weakData;
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
        if(action == "setup") {
            WeakReference<IntentService> weakService =
                    new WeakReference<IntentService>(this);

            connectionListener = new ConnectionListener(weakService);
            dataListener = new DataListener(weakService);


            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            dir_str = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();

            weakConn = new WeakReference<ConnectionListener>(connectionListener);
            weakData = new WeakReference<DataListener>(dataListener);



            if (ON_PHONE) {
                fileWriter = MuseFileFactory.getMuseFileWriter(new File(dir, "datawrite.muse"));
                Log.i("Muse Headband", "libmuse version=" + LibMuseVersion.SDK_VERSION);
                Log.i("Muse Headband", dir.getPath());
                fileWriter.addAnnotationString(1, "ConnectActivity onCreate");
                dataListener.setFileWriter(fileWriter);

/*
                String fname = "svminput";
                File file = new File(dir_str, fname);

                if (file.exists()) {
                    file.delete();
                }

                fname = "svminput.t";
                File file1 = new File(dir_str, fname);

                if (file1.exists()) {
                    file1.delete();
                }

                fname = "svmpredict";
                File file2 = new File(dir_str, fname);

                if (file2.exists()) {
                    file2.delete();
                }
                fname = "range1";
                File file3 = new File(dir_str, fname);

                if (file3.exists()) {
                    file3.delete();
                }
                fname = "svminput.scale";
                File file4 = new File(dir_str, fname);

                if (file4.exists()) {
                    file4.delete();
                }
                fname = "svminput.t.scale";
                File file5 = new File(dir_str, fname);
                if (file5.exists()) {
                    file5.delete();
                }
                fname = "svminput.model";
                File file6 = new File(dir_str, fname);

                if (file6.exists()) {
                    file6.delete();
                }
                fname = "svminput.out";
                File file7  = new File(dir_str, fname);

                if (file7.exists()) {
                    file7.delete();
                }
                fname = "svmpredict.out";
                File file8  = new File(dir_str, fname);

                if (file8.exists()) {
                    file8.delete();
                }

                fname = "svm_predict";
                File file9  = new File(dir_str, fname);

                if (file9.exists()) {
                    file9.delete();
                }

                fileWriter.open();
                */
            }
        }
        }
    }



    /**
     * Connection listener updates UI with new connection status and logs it.
     */
    public class ConnectionListener extends MuseConnectionListener {

        final WeakReference<IntentService> activityRef;


        ConnectionListener(final WeakReference<IntentService> activityRef) {
            this.activityRef = activityRef;
        }

        @Override
        public void receiveMuseConnectionPacket(MuseConnectionPacket p) {


            final ConnectionState current = p.getCurrentConnectionState();
            connection_state = current;
            previous_connection_state = p.getPreviousConnectionState();
            status = p.getPreviousConnectionState().toString() +
                    " -> " + current;
            final String full = "Muse " + p.getSource().getMacAddress() +
                    " " + status;

            Log.i("Muse Headband", full);
            sendMessage(status);

        }


    }

    /**
     * Data listener will be registered to listen for: Accelerometer,
     * Eeg and Relative Alpha bandpower packets. In all cases we will
     * update UI with new values.
     * We also will log message if Artifact packets contains "blink" flag.
     * DataListener methods will be called from execution thread. If you are
     * implementing "serious" processing algorithms inside those listeners,
     * consider to create another thread.
     */

    public class DataListener extends MuseDataListener {

        final WeakReference<IntentService> activityRef;
        private MuseFileWriter fileWriter;


        DataListener(final WeakReference<IntentService> activityRef) {
            this.activityRef = activityRef;
        }


        @Override
        public void receiveMuseDataPacket(MuseDataPacket p) {


            if (ON_PHONE) {
                if (start_recording) {
                    fileWriter.addDataPacket(1, p);

                    // It's library client responsibility to flush the buffer,
                    if (fileWriter.getBufferedMessagesSize() > 8096)
                        fileWriter.flush();
                }
            }
            final MuseDataPacket temp = p;
            if(!temp.getValues().get(0).isNaN()) {
                switch (p.getPacketType()) {
                    case ALPHA_RELATIVE:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RefObjs.updateAlphaRelative(temp.getValues());

                            }
                        }).start();


                        break;
                    case BETA_RELATIVE:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RefObjs.updateBetaRelative(temp.getValues());

                            }
                        }).start();
                        break;
                    case GAMMA_RELATIVE:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RefObjs.updateGammaRelative(temp.getValues());

                            }
                        }).start();
                        break;
                    case GAMMA_ABSOLUTE:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RefObjs.updateGammaAbsolute(temp.getValues());

                            }
                        }).start();
                        break;
                    case THETA_RELATIVE:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RefObjs.updateThetaRelative(temp.getValues());

                            }
                        }).start();
                        break;
                    case HORSESHOE:
                        sendElectrodeStatus(temp.getValues());
                        break;
                    case CONCENTRATION:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RefObjs.updateConcentration(temp.getValues());

                            }
                        }).start();
                        break;
                    default:
                        break;
                }
            }
        }


        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket p) {


        }


        public void setFileWriter(MuseFileWriter fileWriter) {
            this.fileWriter = fileWriter;
        }

    }

    private void sendMessage(String status) {
        //Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", status);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendElectrodeStatus(ArrayList<Double> vals) {
        //Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("horseshoe_event");
        // You can also include some extra data.
        intent.putExtra("horseshoe", vals);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }




    @Override
    public void onDestroy() {
        Log.d("Service", "Destroyed");

        super.onDestroy();
    }
}
