package com.example.mygps.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.example.mygps.R;
import com.example.mygps.service.TargetPosition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    public LatLng mOrigin;
    public LatLng mDestination;
    public Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    private Marker previousMarker = null;

    private int count = 0;

    float Bearing = 0;
    static Marker carMarker;
    Bitmap BitMapMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mMarkerPoints = new ArrayList<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

//        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.car_marker);
//        Bitmap b = bitmapdraw.getBitmap();
//        BitMapMarker = Bitmap.createScaledBitmap(b, 110, 60, false);

        //create tread care se apeleaza la fiecare 10 sec
        // tread ul contine pozitiile mDestination = targetService.getPosition()
//        TargetPosition targetPosition = generateDestinations();
//        Thread threadPositions = new Thread (
//                new Runnable() {
//                    public void run() {
//                        int i = 0;
//                        while (i<targetPosition.getPositions().size()) {
//                            mDestination = targetPosition.getPositions().get(i);
//                            i++;
//                        }
//                    }
//                });
//        threadPositions.start();

    }
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mOrigin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mDestination = new LatLng(44.322181088801834, 23.79738577643466);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mOrigin));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 15));
        ComputeLocation(mDestination);

        TargetPosition x = new TargetPosition();
        x.Iteration(this);

    }

    public void ComputeLocation(LatLng desiredDestination) {


//        TargetPosition targetPosition = generateDestinations();
//        for (int i=0;i<5;i++){
//            mDestination = targetPosition.getPositions().get(i);
//        }

        if (desiredDestination == null)
            return;

        MarkerOptions markerOptions = new MarkerOptions().position(mOrigin).title("I am here!");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addMarker(markerOptions);

                if (previousMarker != null && count > 0)
                    previousMarker.remove();

                MarkerOptions markerOptions2 = new MarkerOptions().position(desiredDestination).title("Destination");
                Marker currentMarker = mMap.addMarker(markerOptions2);
                previousMarker = currentMarker;
                count++;
                drawRoute(desiredDestination);
            }
        });


        // car marker added on the map
//        carMarker = mMap.addMarker(new MarkerOptions().position(mOrigin).
//                flat(true).icon(BitmapDescriptorFactory.fromBitmap(BitMapMarker)));
//        Bearing = currentLocation.getBearing();
//        changePositionSmoothly(carMarker, mDestination, Bearing);




    }

    private TargetPosition generateDestinations(){
        double targetLat;
        double targetLong;
        LatLng dest;

        TargetPosition targetPosition = new TargetPosition();
        List<LatLng> mockPositions = new ArrayList<>();
        mockPositions.add(new LatLng(44.323904, 23.794070));
        mockPositions.add(new LatLng(44.324815, 23.795849));
        mockPositions.add(new LatLng(44.325818, 23.797817));
        mockPositions.add(new LatLng(44.325160, 23.798395));
        mockPositions.add(new LatLng(44.324316, 23.799125));

        targetPosition.setPositions(mockPositions);

//        mockPositions = targetPosition.MockData();

//        targetLat = targetPosition.getLatitude();
//        targetLong = targetPosition.getLongitude();

//        dest = mockPositions.get(i);
        return targetPosition;
    }

//    void changePositionSmoothly(final Marker myMarker, final LatLng newLatLng, final Float bearing) {
//
//        final LatLng startPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        final LatLng finalPosition = newLatLng;
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
//        final float durationInMs = 10000;
//        final boolean hideMarker = false;
//
//        handler.post(new Runnable() {
//            long elapsed;
//            float t;
//            float v;
//
//            @Override
//            public void run() {
//                myMarker.setRotation(bearing);
//                // Calculate progress using interpolator
//                elapsed = SystemClock.uptimeMillis() - start;
//                t = elapsed / durationInMs;
//                v = interpolator.getInterpolation(t);
//
//                LatLng currentPosition = new LatLng(
//                        startPosition.latitude * (1 - t) + finalPosition.latitude * t,
//                        startPosition.longitude * (1 - t) + finalPosition.longitude * t);
//
//                myMarker.setPosition(currentPosition);
//
//                // Repeat till progress is complete.
//                if (t < 1) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                } else {
//                    if (hideMarker) {
//                        myMarker.setVisible(false);
//                    } else {
//                        myMarker.setVisible(true);
//                    }
//                }
//            }
//        });
//    }

    public void drawRoute(LatLng desiredDestination){
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, desiredDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }


}