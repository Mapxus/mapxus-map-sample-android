package com.mapxus.mapxusmapandroiddemo.examples.displaylocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.map.FollowUserMode;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.positioning.provider.MapxusPositioningProvider;

/**
 * The most basic example of adding a map to an activity.
 */
public class LocationProviderActivity extends AppCompatActivity implements OnMapxusMapReadyCallback, View.OnClickListener {

    private MapView mapView;
    private MapxusMap mapxusMap;
    private MapViewProvider mapViewProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_provider);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
        initButtons();
    }

    private void initButtons() {
        Button followMeNone = findViewById(R.id.follow_me_none);
        Button followUser = findViewById(R.id.follow_me_follow_user);
        Button followUserAndHeading = findViewById(R.id.follow_me_follow_user_and_heading);
        followMeNone.setOnClickListener(this);
        followUser.setOnClickListener(this);
        followUserAndHeading.setOnClickListener(this);
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
        mapxusMap.setLocationProvider(new MapxusPositioningProvider(this));
        mapxusMap.setFollowUserMode(FollowUserMode.FOLLOW_USER_AND_HEADING);
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
}