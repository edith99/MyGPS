package com.example.mygps.service;

import android.widget.Toast;

import com.example.mygps.activities.MapsActivity;
import com.example.mygps.activities.RegisterActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

    public List<LatLng> getPositions() {
        return positions;
    }

    public void setPositions(List<LatLng> positions) {
        this.positions = positions;
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

    //    positions.add(new LatLng(44.325818, 23.797817));
//    positions.add(new LatLng(44.325160, 23.798395));
//    positions.add(new LatLng(44.324316, 23.799125));

//    double latitude;
//    double longitude;
//
//    public TargetPosition() {
//    }
//
//    public TargetPosition(double latitude, double longitude) {
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }
//
//    public double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }

    //lista statica de pozitii initializate

   //public List<TargetPosition> mockPositions = new ArrayList<>();
   public List<LatLng> mockPositions = new ArrayList<>();

    public void /*List<LatLng>*/ MockData(){
/*        mockPositions.add(new TargetPosition(44.323904, 23.794070));
        mockPositions.add(new TargetPosition(44.324815, 23.795849));
        mockPositions.add(new TargetPosition(44.325818, 23.797817));
        mockPositions.add(new TargetPosition(44.325160, 23.798395));
        mockPositions.add(new TargetPosition(44.324316, 23.799125));*/
//        List<LatLng> mockPositions = new ArrayList<>();
        mockPositions.add(new LatLng(44.323904, 23.794070));
        mockPositions.add(new LatLng(44.324815, 23.795849));
        mockPositions.add(new LatLng(44.325818, 23.797817));
        mockPositions.add(new LatLng(44.325160, 23.798395));
        mockPositions.add(new LatLng(44.324316, 23.799125));
//        return mockPositions;
    }

    public void Iteration(MapsActivity map){
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private int index = 0;
//            @Override
            public void run() {
//                System.out.println(mockPositions.get(index));
                //Toast.makeText(null, String.format("%s", mockPositions.get(index).toString()), Toast.LENGTH_LONG).show();
                map.ComputeLocation(mockPositions.get(index));
                index++;
                if (index >= mockPositions.size())
                    timer.cancel();
            }
        }, 5000L, 5000L);
    }

//    public getPositions(){
//        for (int i=0; i < mockPositions.size(); i++){
//            return mockPositions.get(i);
//        }
//    }

//    static int index = 0;
//    LatLng getPosition(){
//        index++;
//
//        return mockPosition.get(index);
//    }
}
