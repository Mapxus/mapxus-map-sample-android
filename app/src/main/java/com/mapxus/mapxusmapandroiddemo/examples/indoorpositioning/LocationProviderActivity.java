package com.mapxus.mapxusmapandroiddemo.examples.indoorpositioning;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.FollowUserMode;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.map.mapxusmap.positioning.ErrorInfo;
import com.mapxus.map.mapxusmap.positioning.IndoorLocation;
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProvider;
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProviderListener;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;

public class LocationProviderActivity extends AppCompatActivity implements OnMapxusMapReadyCallback, View.OnClickListener {

    private static final String TAG = LocationProviderActivity.class.getSimpleName();

    private MapView mapView;
    private MapxusMap mapxusMap;
    private MapViewProvider mapViewProvider;
    private IndoorLocationProvider mapxusPositioningProvider;

    private Dialog dialog;

    private TextView latTv, lonTv, floorTv, accuracyTv, timestampTv, compassTv;

    private Button btnPositioningMode;

    private ConnectivityManager connectivityManager;

    private boolean isPositioningFailed;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_provider);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        }
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
        mapxusPositioningProvider = new MapxusPositioningProvider(this, getApplicationContext());
        initView();
    }

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            if (isPositioningFailed && mapxusMap != null) {
                runOnUiThread(() -> setLocation());
                isPositioningFailed = false;
            }
        }

        @Override
        public void onLosing(@NonNull Network network, int maxMsToLive) {
            super.onLosing(network, maxMsToLive);
            isPositioningFailed = true;
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            isPositioningFailed = true;
        }

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            isPositioningFailed = true;
        }
    };

    private void initView() {
        latTv = findViewById(R.id.tv_lat);
        lonTv = findViewById(R.id.tv_lon);
        floorTv = findViewById(R.id.tv_floor);
        accuracyTv = findViewById(R.id.tv_accuracy);
        timestampTv = findViewById(R.id.tv_timestamp);
        compassTv = findViewById(R.id.tv_compass);
        btnPositioningMode = findViewById(R.id.btn_positioning_mode);
        btnPositioningMode.setText(getString(R.string.follow_me_none));
        btnPositioningMode.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (mapxusMap != null) {
            mapxusMap.onResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (mapxusMap != null) {
            mapxusMap.onPause();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mapViewProvider != null) {
            mapViewProvider.onDestroy();
        }
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
        mapxusMap.setFollowUserMode(FollowUserMode.NONE);
    }

    private void setLocation() {
        dialog = new MaterialDialog.Builder(LocationProviderActivity.this)
                .title(getString(R.string.location_dialog_title))
                .content(getString(R.string.location_dialog_message))
                .progress(true, 0).show();
        mapxusPositioningProvider.addListener(new IndoorLocationProviderListener() {
            @Override
            public void onProviderStarted() {

            }

            @Override
            public void onProviderStopped() {

            }

            @Override
            public void onProviderError(ErrorInfo error) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (error.getErrorCode() == ErrorInfo.WARNING || error.getErrorCode() == 109) {
                    Log.w(TAG, error.getErrorMessage());
                    return;
                }

                new MaterialDialog.Builder(LocationProviderActivity.this)
                        .content(error.getErrorMessage())
                        .positiveText(R.string.ok)
                        .onAny((dialog, which) -> dialog.dismiss()).show();

                isPositioningFailed = true;
            }

            @Override
            public void onIndoorLocationChange(IndoorLocation indoorLocation) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                showLocationInfo(indoorLocation);
            }

            @Override
            public void onCompassChanged(float angle, int sensorAccuracy) {
                compassTv.setText(String.format("%s%s", getString(R.string.compass), angle));
            }

        });
        mapxusMap.setLocationProvider(mapxusPositioningProvider);
    }


    @Override
    public void onClick(View v) {
        switch (mapxusMap.getFollowUserMode()) {
            case FollowUserMode.NONE:
                if (mapxusPositioningProvider != null && !mapxusPositioningProvider.isStarted()) {
                    setLocation();
                }
                btnPositioningMode.setText(getString(R.string.follow_me_follow_user));
                mapxusMap.setFollowUserMode(FollowUserMode.FOLLOW_USER);
                break;
            case FollowUserMode.FOLLOW_USER:
                btnPositioningMode.setText(getString(R.string.follow_me_follow_user_and_heading));
                mapxusMap.setFollowUserMode(FollowUserMode.FOLLOW_USER_AND_HEADING);
                break;
            case FollowUserMode.FOLLOW_USER_AND_HEADING:
                btnPositioningMode.setText(getString(R.string.follow_me_none));
                mapxusMap.setFollowUserMode(FollowUserMode.NONE);
        }
    }

    private void showLocationInfo(IndoorLocation indoorLocation) {
        latTv.setText(String.format("%s%s", getString(R.string.lat), indoorLocation.getLatitude()));
        lonTv.setText(String.format("%s%s", getString(R.string.lon), indoorLocation.getLongitude()));
        floorTv.setText(String.format("%s%s", getString(R.string.floor_tips), indoorLocation.getFloor()));
        accuracyTv.setText(String.format("%s%s", getString(R.string.accuracy), indoorLocation.getAccuracy()));
        timestampTv.setText(String.format("%s%s", getString(R.string.time_stamp), indoorLocation.getTime()));
    }
}