package com.example.cylonz.awarenessapi;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.cylonz.awarenessapi.databinding.ActivityMainBinding;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient mGoogleApiClient;
    ActivityMainBinding Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();

        Binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        initSnapshots();
    }

    private void initSnapshots() {
        final String TAG="WWW:";
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    12345
            );
        }
        Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                .setResultCallback(new ResultCallback<WeatherResult>() {
                    @Override
                    public void onResult(@NonNull WeatherResult weatherResult) {
                        if (!weatherResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not get weather.");
                            return;
                        }
                        Weather weather = weatherResult.getWeather();
                        Log.i(TAG, "Temperature: " + weather.getTemperature(2));
                        Log.i(TAG, "FeelsLikeTemperature: " + weather.getFeelsLikeTemperature(2));
                        Log.i(TAG, "getHumidity: " + weather.getHumidity());

                        Binding.txt1.setText("Temperature:"+weather.getTemperature(2));
                        Binding.txt2.setText("FeelsLikeTemperature:"+weather.getFeelsLikeTemperature(2));
                        Binding.txt3.setText("getHumidity:"+weather.getHumidity());
                    }
                });

    }

}
