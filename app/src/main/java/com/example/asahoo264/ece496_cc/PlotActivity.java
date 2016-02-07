package com.example.asahoo264.ece496_cc;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlotActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private LineGraphSeries<DataPoint> series;

    public static ArrayList<Double> alpha_curr = new ArrayList<Double>();
    public static ArrayList<Double> beta_curr = new ArrayList<Double>();
    public static ArrayList<Double> gamma_curr = new ArrayList<Double>();
    public static ArrayList<Double> theta_curr = new ArrayList<Double>();


    private double maxY = 0;
    private int lastX =0;
    private GraphView graph;
    private double graph2LastXValue = 5d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        graph = (GraphView) findViewById(R.id.graph1);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setScrollable(true);
    }






    @Override
    public void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 100; i++) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();


    }

    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        if(alpha_curr.size() != 0){
        series.appendData(new DataPoint(lastX++, alpha_curr.get(lastX)), true, alpha_curr.size());}

    }
}
