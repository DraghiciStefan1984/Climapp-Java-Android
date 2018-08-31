package com.climapp.stefandraghici.climapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    final int REQUEST_CODE=123;
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    final String APP_ID = "89b0ae42e657b385a22a095351c6c08c";
    final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;

    TextView cityLabel;
    ImageView weatherImage;
    TextView temperatureLabel;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityLabel = (TextView) findViewById(R.id.locationTV);
        weatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        temperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("Clima", "onPostResume() called.");
        Log.d("Clima", "Getting weather from for location.");

        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                Log.d("Clima", "onLocationChanged() called.");
                String longitude=String.valueOf(location.getLongitude());
                String latitude=String.valueOf(location.getLatitude());

                Log.d("Clima", longitude);
                Log.d("Clima", latitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {
                Log.d("Clima", "onProviderDisabled() called.");
            }
        };

        if (ActivityCompat.checkSelfPermission(this,
                                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Log.d("Clima", "onRequestPermissionsResult() granted.");
                getWeatherForCurrentLocation();
            }
            else
            {
                Log.d("Clima", "onRequestPermissionsResult() denied.");
            }
        }
    }
}
