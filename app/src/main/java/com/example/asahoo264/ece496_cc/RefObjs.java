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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by asahoo264 on 2/2/2016.
 */

public class RefObjs extends Application{

    public static WeakReference<MuseConnectionService.ConnectionListener> weakConn;
    public static WeakReference<MuseConnectionService.DataListener> weakData;
    public static boolean start_of_event=false;
    public static ArrayList<Double> alpha_val = new ArrayList<Double>();
    public static ArrayList<Double> beta_val = new ArrayList<Double>();
    public static ArrayList<ArrayList<Double>> gamma_val = new ArrayList<ArrayList<Double>>();
    public static ArrayList<Double> gamma_val_rel = new ArrayList<Double>();
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
      if(n != null&& !n.isNaN()){
          Socket socket = null;
          try {
              socket = new Socket("192.168.0.16",1755);
          } catch (IOException e) {
              e.printStackTrace();
          }
          DataOutputStream DOS = null;
          try {
              DOS = new DataOutputStream(socket.getOutputStream());
          } catch (IOException e) {
              e.printStackTrace();
          }
          try {
              DOS.writeUTF(String.valueOf(n));
          } catch (IOException e) {
              e.printStackTrace();
          }
          try {
              socket.close();
          } catch (IOException e) {
              e.printStackTrace();
          }

      }
      if (start_of_event && n != null && !n.isNaN()) {
          gamma_val_rel.add(n);
      }

    }

    public static void updateGammaAbsolute(final ArrayList<Double> data) {
        Double n = average(data);
        //String size = "Size: " + data.size();
        //Log.d("Data: ", size);
        if (start_of_event && n != null && !n.isNaN()) {
            ArrayList<Double> gamma_val_inner = new ArrayList<Double>(data);
            gamma_val.add(gamma_val_inner);
            /*for(int i=0; i< data.size(); i++){
                gamma_val_inner.add(data.get(i));
                gamma_val.get(i).add(gamma_val_inner);
            }*/
        }

    }

    public static void updateThetaRelative(final ArrayList<Double> data) {
        Double n = average(data);
        if (start_of_event && n != null && !n.isNaN()) {
            theta_val.add(n);        }

    }


    public static void register_event(final WeakReference<Activity> activityref, boolean is_train, boolean seekbar_progress, String dir) throws IOException {
        dir_str = dir;
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

        double[] gamma_sum = new double[4];
        double[] gamma_var_sum = new double[4];
        double[] gamma_mean = new double[4];
        double[] gamma_var = new double[4];
        double[] gamma_data = new double[4];

        String gm = "Size: " + gamma_val.size();
        if(gamma_val.size() != 0) {
            String gm_in = "Inner Size: " + gamma_val.get(0).size();
            Log.d(gm, gm_in);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < gamma_val.size(); j++) {
                    gamma_sum[i] += gamma_val.get(j).get(i).doubleValue();
                }
                gamma_mean[i] = gamma_sum[i] / gamma_val.size();
                gamma_data[i] = gamma_mean[i];
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < gamma_val.size(); j++) {
                    gamma_var_sum[i] = gamma_var_sum[i] + (gamma_val.get(j).get(i).doubleValue() - gamma_mean[i]) * (gamma_val.get(j).get(i).doubleValue() - gamma_mean[i]);
                }
                gamma_var[i] = gamma_var_sum[i] / gamma_val.size();
            }
        }
        if(gamma_val.size() == 0){
            Log.d("Size","Zero");
            for(int i = 0; i < gamma_data.length;i++) {
                gamma_data[i] = 0;
            }
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
            emotion_label = -1;

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
        //if(alpha_var==0 && beta_var==0 && gamma_var==0 && theta_var==0)
        if(gamma_data.length == 0 || (gamma_data[0]==0 && gamma_data[1]==0 && gamma_data[2]==0 && gamma_data[3]==0))
            return;
        try {
            //fcontent = String.valueOf(emotion_label) + " 1:" + String.valueOf(alpha_var) + " 2:" + String.valueOf(beta_var)  + " 3:" + String.valueOf(gamma_var) + " 4:"  +  String.valueOf(theta_var) + "\n";
            fcontent = String.valueOf(emotion_label) + " 1:" + String.valueOf(gamma_data[0]) + " 2:" + String.valueOf(gamma_data[1])  + " 3:" + String.valueOf(gamma_data[2]) + " 4:"  +  String.valueOf(gamma_data[3]) + "\n";
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


    public static void predict_event(boolean emotion_val, String dir) throws IOException {

        dir_str = dir;

        double[] gamma_sum = new double[4];
        double[] gamma_var_sum = new double[4];
        double[] gamma_mean = new double[4];
        double[] gamma_var = new double[4];
        double[] gamma_data = new double[4];

        String gm = "Size: " + gamma_val.size();
        if(gamma_val.size() != 0) {
            String gm_in = "Inner Size: " + gamma_val.get(0).size();
            Log.d(gm, gm_in);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < gamma_val.size(); j++) {
                    gamma_sum[i] += gamma_val.get(j).get(i).doubleValue();
                }
                gamma_mean[i] = gamma_sum[i] / gamma_val.size();
                gamma_data[i] = gamma_mean[i];
                if(gamma_data[i] == 0){
                    //gamma_data[i] = 0.0001;
                }
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < gamma_val.size(); j++) {
                    gamma_var_sum[i] = gamma_var_sum[i] + (gamma_val.get(j).get(i).doubleValue() - gamma_mean[i]) * (gamma_val.get(j).get(i).doubleValue() - gamma_mean[i]);
                }
                gamma_var[i] = gamma_var_sum[i] / gamma_val.size();
            }
        }
        if(gamma_val.size() == 0){
            Log.d("Size","Zero");
            for(int i = 0; i < gamma_data.length;i++) {
                gamma_data[i] = 0;
            }
        }
        gamma_val.clear();

        int emotion_label;
        if(emotion_val)
            emotion_label = 1;
        else
            emotion_label = -1;

        String fname = "svm_predict";
        File file = new File(dir_str, fname);

        Log.d("File: ", file.toString());
        // If file does not exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        final String fcontent;
        //if(alpha_var==0 && beta_var==0 && gamma_var==0 && theta_var==0)
        if(gamma_data.length == 0 || (gamma_data[0]==0 && gamma_data[1]==0 && gamma_data[2]==0 && gamma_data[3]==0))
            return;
        try {
            //fcontent = String.valueOf(emotion_label) + " 1:" + String.valueOf(alpha_var) + " 2:" + String.valueOf(beta_var)  + " 3:" + String.valueOf(gamma_var) + " 4:"  +  String.valueOf(theta_var) + "\n";
            fcontent = String.valueOf(emotion_label) + " 1:" + String.valueOf(gamma_data[0]) + " 2:" + String.valueOf(gamma_data[1])  + " 3:" + String.valueOf(gamma_data[2]) + " 4:"  +  String.valueOf(gamma_data[3]) + "\n";
            Log.d("FCONTENT: ", fcontent);
            FileOutputStream fOut = new FileOutputStream(file,false);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(fcontent);
            myOutWriter.flush();
            myOutWriter.close();
            fOut.close();



        } catch (IOException e) {
            e.printStackTrace();

        }

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



