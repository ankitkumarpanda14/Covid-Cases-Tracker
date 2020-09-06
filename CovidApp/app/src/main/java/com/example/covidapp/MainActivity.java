package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    TextView ViewActive;
    TextView ViewDeath;
    TextView ViewCured;
    TextView ViewTime;
    TextView ViewIncresaed;
    GraphView graph;
    ArrayList<Integer> listofValues = new ArrayList<Integer>();
    ArrayList<String> listofValuesActive = new ArrayList<String>();
    ArrayList<String> listofValuesDeath = new ArrayList<String>();
    ArrayList<String> listofValuesCured = new ArrayList<String>();
    ArrayList<String> listofValuesTime = new ArrayList<String>();
    private DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewActive = (TextView) findViewById(R.id.ActiveText);
        ViewDeath = (TextView) findViewById(R.id.deathText);
        ViewCured = (TextView) findViewById(R.id.curedText);
        ViewTime = (TextView) findViewById(R.id.TimeText);
        ViewIncresaed = (TextView) findViewById(R.id.IncreasedText);
        graph = (GraphView) findViewById(R.id.graph);
        dbr = FirebaseDatabase.getInstance().getReference().child("MyTestData");

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updataData(dataSnapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
      //  setIncreased();
    }

    private void updataData(DataSnapshot dataSnapshot) {
        String Active,Cured,Deaths,Migrated;
        String time;
        String[] timeDisp;
        String timeformatted;
        int sizeActive,sizeDeath,sizeCured,sizeTime;
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            Active = (String) ((DataSnapshot)i.next()).getValue();
            Cured = (String) ((DataSnapshot)i.next()).getValue();
            Deaths = (String)((DataSnapshot)i.next()).getValue();
            Migrated = (String) ((DataSnapshot)i.next()).getValue();
            time = (String) ((DataSnapshot)i.next()).getValue();
            timeDisp = time.split(":",2);
            timeformatted = timeDisp[1].replaceAll("2020","");
            timeformatted = timeformatted.substring(0,timeformatted.length()- 9);
            int integerActive = Integer.parseInt(Active);
            listofValues.add(integerActive);
            listofValuesActive.add(Active);
            listofValuesDeath.add((Deaths));
            listofValuesCured.add((Cured));
            listofValuesTime.add(timeformatted);
            sizeActive = listofValuesActive.size();
            sizeDeath = listofValuesDeath.size();
            sizeCured = listofValuesCured.size();
            sizeTime = listofValuesTime.size();
            ViewActive.setText(listofValuesActive.get(sizeActive-1));
            ViewDeath.setText(listofValuesDeath.get(sizeDeath-1));
            ViewCured.setText(listofValuesCured.get(sizeCured-1));
            ViewTime.setText(listofValuesTime.get(sizeTime-1));
            if(listofValues.size() >= 3){
            setIncreased();
            }
//            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                    new DataPoint(0, 4312),
//                    new DataPoint(1, 4315)
//            });
//            graph.addSeries(series);

            DataPoint[] dataPoints = new DataPoint[listofValues.size()];
            for (int j = 0; j < listofValues.size(); j++) {

                dataPoints[j] = new DataPoint(j+1, listofValues.get(j));
            }

            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>(dataPoints);
            graph.addSeries(series);
            DataPoint[] dataPoints1 = new DataPoint[listofValues.size()];
            for (int j = 0; j < listofValues.size(); j++) {

                dataPoints1[j] = new DataPoint(j+1, listofValues.get(j));
            }

            LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(dataPoints);
            graph.addSeries(series1);
            Integer size = listofValues.size();
            GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
            gridLabel.setVerticalAxisTitle("Active Cases");
            gridLabel.setNumHorizontalLabels(size + 1);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScalableY(true);
            //graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setScrollable(true);

            graph.getViewport().setMaxX(size+1);
        }
    }



    private void setIncreased() {
        int sizeActive;
        sizeActive = listofValues.size();
        int integerActive = listofValues.get(sizeActive - 1);
        int previntegerActive = listofValues.get(sizeActive - 3);
        int increase = integerActive - previntegerActive;
        ViewIncresaed.setText(Integer.toString(increase));
    }
}
