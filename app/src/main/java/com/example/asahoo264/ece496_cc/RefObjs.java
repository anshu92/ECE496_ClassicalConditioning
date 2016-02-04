package com.example.asahoo264.ece496_cc;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.interaxon.libmuse.Muse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by asahoo264 on 2/2/2016.
 */

public class RefObjs extends Application{

    public static WeakReference<MuseConnectionService.ConnectionListener> weakConn;
    public static WeakReference<MuseConnectionService.DataListener> weakData;
    public static boolean start_of_event=false;
    public static double[] alpha_val = new double[2200];
    public static double[] beta_val = new double[2200];
    public static double[] gamma_val = new double[2200];
    public static double[] theta_val = new double[2200];
    public static int alpha_cnt = 0;
    public static int beta_cnt = 0;
    public static int gamma_cnt = 0;
    public static int theta_cnt = 0;

    private static RefObjs singleton;
    public static Muse muse = null;



    public void RefObjs(){
    }


    public static RefObjs getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public WeakReference<MuseConnectionService.ConnectionListener> getWeakConn() {
        return weakConn;
    }

    public WeakReference<MuseConnectionService.DataListener> getWeakData() {
        return weakData;
    }

    public static void setWeakConn(WeakReference<MuseConnectionService.ConnectionListener> weakConn) {
        weakConn = weakConn;
    }

    public static void setWeakData(WeakReference<MuseConnectionService.DataListener> weakData) {
       weakData = weakData;
    }


    public static void updateAlphaAbsolute(final ArrayList<Double> data) {
        double n = data.get(0);
        if (start_of_event ) {
            alpha_val[alpha_cnt++] = n;
        }
        else {
            alpha_cnt = 0;

        }
    }

    public static void updateBetaAbsolute(final ArrayList<Double> data) {
        double n = data.get(0);
        if (start_of_event ) {
            beta_val[beta_cnt++] = n;
        }
        else {
            beta_cnt = 0;

        }
    }

  public static void updateGammaAbsolute(final ArrayList<Double> data) {
        double n = data.get(0);
        if (start_of_event) {
            gamma_val[gamma_cnt++] = n;
        }
        else {
            gamma_cnt = 0;

        }
    }

    public static void updateThetaAbsolute(final ArrayList<Double> data) {
        double n = data.get(0);
        if (start_of_event ) {
            theta_val[theta_cnt++] = n;
        }
        else {
            theta_cnt = 0;

        }
    }


    public static void register_event(final WeakReference<Activity> activityref, boolean is_train, boolean seekbar_progress) throws IOException {
        double alpha_sum = 0;
        double alpha_var_sum = 0;
        double alpha_mean;
        double alpha_var;
        for(int i = 0; i < alpha_val.length;i++){
            alpha_sum  =  alpha_sum +  alpha_val[i];
        }
        alpha_mean = alpha_sum/alpha_cnt;
        for(int i = 0; i < alpha_val.length;i++){
            alpha_var_sum  =  alpha_var_sum +  (alpha_val[i]-alpha_mean)*(alpha_val[i]-alpha_mean);
        }
        alpha_var = alpha_var_sum/alpha_cnt;

        double beta_sum = 0;
        double beta_var_sum = 0;
        double beta_mean;
        double beta_var;
        for(int i = 0; i < beta_val.length;i++){
            beta_sum  =  beta_sum +  beta_val[i];
        }
        beta_mean = beta_sum/beta_cnt;
        for(int i = 0; i < beta_val.length;i++){
            beta_var_sum  =  beta_var_sum +  (beta_val[i]-beta_mean)*(beta_val[i]-beta_mean);
        }
        beta_var = beta_var_sum/beta_cnt;

        double gamma_sum = 0;
        double gamma_var_sum = 0;
        double gamma_mean;
        double gamma_var;
        for(int i = 0; i < gamma_val.length;i++){
            gamma_sum  =  gamma_sum +  gamma_val[i];
        }
        gamma_mean = gamma_sum/gamma_cnt;
        for(int i = 0; i < gamma_val.length;i++){
            gamma_var_sum  =  gamma_var_sum +  (gamma_val[i]-gamma_mean)*(gamma_val[i]-gamma_mean);
        }
        gamma_var = gamma_var_sum/gamma_cnt;


        double theta_sum = 0;
        double theta_var_sum = 0;
        double theta_mean;
        double theta_var;
        for(int i = 0; i < theta_val.length;i++){
            theta_sum  =  theta_sum +  theta_val[i];
        }
        theta_mean = theta_sum/theta_cnt;
        for(int i = 0; i < theta_val.length;i++){
            theta_var_sum  =  theta_var_sum +  (theta_val[i]-theta_mean)*(theta_val[i]-theta_mean);
        }
        theta_var = theta_var_sum/theta_cnt;

        int emotion_label;
        if(seekbar_progress)
            emotion_label = 1;
        else
            emotion_label = 0;

        String fname;
        final String fcontent;

        if(is_train)
            fname = "/svminput";
        else
            fname = "/svminput.t";

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + fname);
        file.mkdirs();
        try {
            fcontent = String.valueOf(emotion_label) + " 1:" + String.valueOf(alpha_var) + " 2:" + String.valueOf(beta_var)  + " 3:" + String.valueOf(gamma_var) + " 4:"  +  String.valueOf(theta_var) + "\n";

            Log.d("FCONTENT: ", fcontent);
            FileOutputStream fOut = new FileOutputStream(file,true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(fcontent);
            myOutWriter.flush();
            myOutWriter.close();
            fOut.close();



        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    public static void predict_event(boolean emotion_val) throws IOException {


        double alpha_sum = 0;
        double alpha_var_sum = 0;
        double alpha_mean;
        double alpha_var;
        for(int i = 0; i < alpha_val.length;i++){
            alpha_sum  =  alpha_sum +  alpha_val[i];
        }
        alpha_mean = alpha_sum/alpha_cnt;
        for(int i = 0; i < alpha_val.length;i++){
            alpha_var_sum  =  alpha_var_sum +  (alpha_val[i]-alpha_mean)*(alpha_val[i]-alpha_mean);
        }
        alpha_var = alpha_var_sum/alpha_cnt;

        double beta_sum = 0;
        double beta_var_sum = 0;
        double beta_mean;
        double beta_var;
        for(int i = 0; i < beta_val.length;i++){
            beta_sum  =  beta_sum +  beta_val[i];
        }
        beta_mean = beta_sum/beta_cnt;
        for(int i = 0; i < beta_val.length;i++){
            beta_var_sum  =  beta_var_sum +  (beta_val[i]-beta_mean)*(beta_val[i]-beta_mean);
        }
        beta_var = beta_var_sum/beta_cnt;

        double gamma_sum = 0;
        double gamma_var_sum = 0;
        double gamma_mean;
        double gamma_var;
        for(int i = 0; i < gamma_val.length;i++){
            gamma_sum  =  gamma_sum +  gamma_val[i];
        }
        gamma_mean = gamma_sum/gamma_cnt;
        for(int i = 0; i < gamma_val.length;i++){
            gamma_var_sum  =  gamma_var_sum +  (gamma_val[i]-gamma_mean)*(gamma_val[i]-gamma_mean);
        }
        gamma_var = gamma_var_sum/gamma_cnt;


        double theta_sum = 0;
        double theta_var_sum = 0;
        double theta_mean;
        double theta_var;
        for(int i = 0; i < theta_val.length;i++){
            theta_sum  =  theta_sum +  theta_val[i];
        }
        theta_mean = theta_sum/theta_cnt;
        for(int i = 0; i < theta_val.length;i++){
            theta_var_sum  =  theta_var_sum +  (theta_val[i]-theta_mean)*(theta_val[i]-theta_mean);
        }
        theta_var = theta_var_sum/theta_cnt;

        String fname = "/svmpredict";
        String fcontent;

        int emotion_label;
        if(emotion_val)
            emotion_label = 1;
        else
            emotion_label = 0;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + fname);
        file.mkdirs();
        try {
            fcontent = String.valueOf(emotion_label) + " 1:" + String.valueOf(alpha_var) + " 2:" + String.valueOf(beta_var)  + " 3:" + String.valueOf(gamma_var) + " 4:"  +  String.valueOf(theta_var) + "\n";
            //Toast.makeText(this, fcontent, Toast.LENGTH_SHORT).show();

            FileOutputStream fOut = new FileOutputStream(file,false);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(fcontent);
            myOutWriter.flush();
            myOutWriter.close();
            fOut.close();



        } catch (IOException e) {
            e.printStackTrace();

        }

        String fname1 = "/svmpredict.out";
        File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + fname1);
        String fpath1 = file1.toString();

        String fname2 = "/svmpredict.model";
        File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + fname2);
        String fpath2 = file1.toString();
        String fpath = file.toString();
        int prediction;
        String[] testing1 = {fpath, fpath2, fpath1};

        try {
            svm_predict.main(testing1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer result;

        FileInputStream fIn = new FileInputStream(file1);
        result = fIn.read();
        fIn.close();
        // Toast.makeText(this, "prediction: " + result.toString(), Toast.LENGTH_SHORT).show();



    }


}



