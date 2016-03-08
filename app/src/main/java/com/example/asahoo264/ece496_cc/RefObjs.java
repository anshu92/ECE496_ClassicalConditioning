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
    public static ArrayList<Double> alpha_val = new ArrayList<Double>();
    public static ArrayList<Double> beta_val = new ArrayList<Double>();
    public static ArrayList<Double> gamma_val = new ArrayList<Double>();
    public static ArrayList<Double> theta_val = new ArrayList<Double>();
    public static Double concentration = 0.0;

    private static RefObjs singleton;
    public static Muse muse = null;

    private static String dir_str = null;

    public void RefObjs(){
    }


    public static RefObjs getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        dir_str = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
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

    public static void updateConcentration(final ArrayList<Double> data){
        Double n = data.get(0);
        concentration = n;

    }
    public static void updateAlphaRelative(final ArrayList<Double> data) {
        Double n = average(data);
      //Log.d("alpha: ", n.toString());
        if (start_of_event && n != null && !n.isNaN()) {
            alpha_val.add(n);
        }

    }

    public static void updateBetaRelative(final ArrayList<Double> data) {
        Double n = average(data);
        if (start_of_event && n != null && !n.isNaN()) {
            beta_val.add(n);        }

    }

  public static void updateGammaRelative(final ArrayList<Double> data) {
      Double n = average(data);
      if (start_of_event && n != null && !n.isNaN()) {
            gamma_val.add(n);        }

    }

    public static void updateThetaRelative(final ArrayList<Double> data) {
        Double n = average(data);
        if (start_of_event && n != null && !n.isNaN()) {
            theta_val.add(n);        }

    }


    public static void register_event(final WeakReference<Activity> activityref, boolean is_train, boolean seekbar_progress) throws IOException {
        double alpha_sum = 0;
        double alpha_var_sum = 0;
        double alpha_mean;
        double alpha_var;
        for(int i = 0; i < alpha_val.size();i++){
            alpha_sum  =  alpha_sum +  alpha_val.get(i).doubleValue();
        }
        alpha_mean = alpha_sum/alpha_val.size();
        for(int i = 0; i < alpha_val.size();i++){
            alpha_var_sum  =  alpha_var_sum +  (alpha_val.get(i).doubleValue()-alpha_mean)*(alpha_val.get(i).doubleValue()-alpha_mean);
        }
        alpha_var = alpha_var_sum/alpha_val.size();
        if(alpha_val.size() == 0){
            Log.d("Size","Zero");
            alpha_var = 0;
        }

        alpha_val.clear();


        double beta_sum = 0;
        double beta_var_sum = 0;
        double beta_mean;
        double beta_var;
        for(int i = 0; i < beta_val.size();i++){
            beta_sum  =  beta_sum +  beta_val.get(i).doubleValue();
        }
        beta_mean = beta_sum/beta_val.size();
        for(int i = 0; i < beta_val.size();i++){
            beta_var_sum  =  beta_var_sum +  (beta_val.get(i).doubleValue()-beta_mean)*(beta_val.get(i).doubleValue()-beta_mean);
        }
        beta_var = beta_var_sum/beta_val.size();

        if(beta_val.size() == 0){
            Log.d("Size","Zero");
            beta_var = 0;
        }

        beta_val.clear();

        double gamma_sum = 0;
        double gamma_var_sum = 0;
        double gamma_mean;
        double gamma_var;
        for(int i = 0; i < gamma_val.size();i++){
            gamma_sum  =  gamma_sum +  gamma_val.get(i).doubleValue();
        }
        gamma_mean = gamma_sum/gamma_val.size();
        for(int i = 0; i < gamma_val.size();i++){
            gamma_var_sum  =  gamma_var_sum +  (gamma_val.get(i).doubleValue()-gamma_mean)*(gamma_val.get(i).doubleValue()-gamma_mean);
        }
        gamma_var = gamma_var_sum/gamma_val.size();
        if(gamma_val.size() == 0){
            Log.d("Size","Zero");
            gamma_var = 0;
        }
        gamma_val.clear();


        double theta_sum = 0;
        double theta_var_sum = 0;
        double theta_mean;
        double theta_var;
        for(int i = 0; i < theta_val.size();i++){
            theta_sum  =  theta_sum +  theta_val.get(i).doubleValue();
        }
        theta_mean = theta_sum/theta_val.size();
        for(int i = 0; i < theta_val.size();i++){
            theta_var_sum  =  theta_var_sum +  (theta_val.get(i).doubleValue()-theta_mean)*(theta_val.get(i).doubleValue()-theta_mean);
        }
        theta_var = theta_var_sum/theta_val.size();
        if(alpha_val.size() == 0){
            Log.d("Size","Zero");
            theta_var = 0;
        }
        theta_val.clear();

        int emotion_label;
        if(seekbar_progress)
            emotion_label = 1;
        else
            emotion_label = 0;

        String fname;
        final String fcontent;

        if(is_train)
            fname = "svminput";
        else
            fname = "svminput.t";

        File file = new File(dir_str, fname);
        // If file does not exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        if(alpha_var==0 && beta_var==0 && gamma_var==0 && theta_var==0)
            return;
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
        for(int i = 0; i < alpha_val.size();i++){
            alpha_sum  =  alpha_sum +  alpha_val.get(i).doubleValue();
        }
        alpha_mean = alpha_sum/alpha_val.size();
        for(int i = 0; i < alpha_val.size();i++){
            alpha_var_sum  =  alpha_var_sum +  (alpha_val.get(i).doubleValue()-alpha_mean)*(alpha_val.get(i).doubleValue()-alpha_mean);
        }
        alpha_var = alpha_var_sum/alpha_val.size();

        double beta_sum = 0;
        double beta_var_sum = 0;
        double beta_mean;
        double beta_var;
        for(int i = 0; i < beta_val.size();i++){
            beta_sum  =  beta_sum +  beta_val.get(i).doubleValue();
        }
        beta_mean = beta_sum/beta_val.size();
        for(int i = 0; i < beta_val.size();i++){
            beta_var_sum  =  beta_var_sum +  (beta_val.get(i).doubleValue()-beta_mean)*(beta_val.get(i).doubleValue()-beta_mean);
        }
        beta_var = beta_var_sum/beta_val.size();

        double gamma_sum = 0;
        double gamma_var_sum = 0;
        double gamma_mean;
        double gamma_var;
        for(int i = 0; i < gamma_val.size();i++){
            gamma_sum  =  gamma_sum +  gamma_val.get(i).doubleValue();
        }
        gamma_mean = gamma_sum/gamma_val.size();
        for(int i = 0; i < gamma_val.size();i++){
            gamma_var_sum  =  gamma_var_sum +  (gamma_val.get(i).doubleValue()-gamma_mean)*(gamma_val.get(i).doubleValue()-gamma_mean);
        }
        gamma_var = gamma_var_sum/gamma_val.size();


        double theta_sum = 0;
        double theta_var_sum = 0;
        double theta_mean;
        double theta_var;
        for(int i = 0; i < theta_val.size();i++){
            theta_sum  =  theta_sum +  theta_val.get(i).doubleValue();
        }
        theta_mean = theta_sum/theta_val.size();
        for(int i = 0; i < theta_val.size();i++){
            theta_var_sum  =  theta_var_sum +  (theta_val.get(i).doubleValue()-theta_mean)*(theta_val.get(i).doubleValue()-theta_mean);
        }
        theta_var = theta_var_sum/theta_val.size();

        String fname = "svmpredict";
        String fcontent;

        int emotion_label;
        if(emotion_val)
            emotion_label = 1;
        else
            emotion_label = 0;

        File file = new File(dir_str, fname);
        // If file does not exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

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

        String fname1 = "svmpredict.out";
        File file1 = new File(dir_str, fname1);
        String fpath1 = file1.toString();

        String fname2 = "svmpredict.model";
        File file2 = new File(dir_str, fname2);
        String fpath2 = file2.toString();
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

    private static Double average(ArrayList<Double> list) {
        // 'average' is undefined if there are no elements in the list.
        if (list == null || list.isEmpty())
            return 0.0;
        // Calculate the summation of the elements in the list
        Double sum = 0d;
        int n = list.size();
        // Iterating manually is faster than using an enhanced for loop.
        for (int i = 0; i < n; i++)
            sum += list.get(i);
        // We don't want to perform an integer division, so the cast is mandatory.
        return (sum) / n;
    }

}



