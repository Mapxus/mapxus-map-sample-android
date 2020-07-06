package com.mapxus.mapxusmapandroiddemo.examples.displaylocation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

/**
 * The most basic example of adding a map to an activity.
 */
public class LocationProviderActivity extends AppCompatActivity implements OnMapxusMapReadyCallback, View.OnClickListener {

    private static final String TAG = "LocationProvider";


    private MapView mapView;
    private MapxusMap mapxusMap;
    private MapViewProvider mapViewProvider;

    private Dialog dialog;

    private TextView latTv, lonTv, floorTv, accuracyTv, buildingTv, timestampTv, compassTv;

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
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
        initButtons();
        initTextView();
        dialog = ProgressDialog.show(LocationProviderActivity.this, getString(R.string.location_dialog_title), getString(R.string.location_dialog_message));
        dialog.show();
    }

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            if (isPositioningFailed && mapxusMap != null) {
                runOnUiThread(() -> setLocation(mapxusMap));
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

    private void initButtons() {
        Button followMeNone = findViewById(R.id.follow_me_none);
        Button followUser = findViewById(R.id.follow_me_follow_user);
        Button followUserAndHeading = findViewById(R.id.follow_me_follow_user_and_heading);
        followMeNone.setOnClickListener(this);
        followUser.setOnClickListener(this);
        followUserAndHeading.setOnClickListener(this);
    }

    private void initTextView() {
        latTv = findViewById(R.id.tv_lat);
        lonTv = findViewById(R.id.tv_lon);
        floorTv = findViewById(R.id.tv_floor);
        accuracyTv = findViewById(R.id.tv_accuracy);
        buildingTv = findViewById(R.id.tv_building);
        timestampTv = findViewById(R.id.tv_timestamp);
        compassTv = findViewById(R.id.tv_compass);
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
        setLocation(mapxusMap);
        mapxusMap.setFollowUserMode(FollowUserMode.FOLLOW_USER_AND_HEADING);
    }

    private void setLocation(MapxusMap mapxusMap) {
        IndoorLocationProvider mapxusPositioningProvider = new MapxusPositioningProvider(this, getApplicationContext());
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
                AlertDialog alertDialog = new AlertDialog.Builder(LocationProviderActivity.this).setMessage(error.getErrorMessage()).setPositiveButton(R.string.location_positioning_button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                alertDialog.show();

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
                compassTv.setText(String.valueOf(angle));
            }

        });
        mapxusMap.setLocationProvider(mapxusPositioningProvider);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_me_none:
                mapxusMap.setFollowUserMode(FollowUserMode.NONE);
                break;
            case R.id.follow_me_follow_user:
                mapxusMap.setFollowUserMode(FollowUserMode.FOLLOW_USER);
                break;
            case R.id.follow_me_follow_user_and_heading:
                mapxusMap.setFollowUserMode(FollowUserMode.FOLLOW_USER_AND_HEADING);
                break;
            default:
                mapxusMap.setFollowUserMode(FollowUserMode.NONE);
        }
    }

    private void showLocationInfo(IndoorLocation indoorLocation) {
        latTv.setText(String.valueOf(indoorLocation.getLatitude()));
        lonTv.setText(String.valueOf(indoorLocation.getLongitude()));
        floorTv.setText(indoorLocation.getFloor());
        accuracyTv.setText(String.valueOf(indoorLocation.getAccuracy()));
        buildingTv.setText(indoorLocation.getBuilding());
        timestampTv.setText(String.valueOf(indoorLocation.getTime()));
    }
}