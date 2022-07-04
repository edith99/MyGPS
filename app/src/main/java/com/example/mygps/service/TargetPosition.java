package com.example.mygps.service;

import com.example.mygps.activities.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TargetPosition {

    List<LatLng> positions = new ArrayList<>();
    double v, v1;

    public TargetPosition(double v, double v1) {
        this.v = v; this.v1 = v1;
    }

    public TargetPosition(List<LatLng> positions) {
        this.positions = positions;
    }

    public TargetPosition() {
        MockData();
    }

    @Override
    public String toString() {
        return "TargetPosition{" +
                "v=" + v +
                ", v1=" + v1 +
                '}';
    }

   public List<LatLng> mockPositions = new ArrayList<>();

    public void MockData(){
        mockPositions.add(new LatLng(44.310699, 23.834865));
        mockPositions.add(new LatLng(44.311635, 23.835055));
        mockPositions.add(new LatLng(44.311941, 23.834438));
        mockPositions.add(new LatLng(44.312007, 23.833932));
        mockPositions.add(new LatLng(44.312572, 23.834064));
        mockPositions.add(new LatLng(44.313101, 23.833821));
        mockPositions.add(new LatLng(44.313398, 23.833497));
        mockPositions.add(new LatLng(44.313861, 23.833021));
        mockPositions.add(new LatLng(44.313977, 23.832099));
        mockPositions.add(new LatLng(44.314600, 23.832229));
        mockPositions.add(new LatLng(44.314951, 23.832322));
        mockPositions.add(new LatLng(44.314860, 23.832872));
    }
    public void Iteration(MapsActivity map){
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private int index = 0;
            public void run() {
                map.ComputeLocation(mockPositions.get(index));
                index++;
                if (index >= mockPositions.size())
                    timer.cancel();
            }
        }, 10000L, 10000L);
    }
}

