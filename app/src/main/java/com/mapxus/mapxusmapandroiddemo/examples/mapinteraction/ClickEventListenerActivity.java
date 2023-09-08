package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.map.model.Venue;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class ClickEventListenerActivity extends AppCompatActivity implements OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private IndoorBuilding indoorBuilding;
    private String venueName, buildingName, floorName;
    private Venue venue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_click);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        mapxusMap.addOnMapClickedListener((latLng, floorInfo, indoorBuilding, venue) -> {
            if (indoorBuilding != null) {
                buildingName = indoorBuilding.getBuildingName();
                venueName = venue.getVenueName();
            } else {
                buildingName = null;
                venueName = null;
            }
            displayDialog("You have tap at coordinate " + latLng.latitude + "," + latLng.longitude + "," + (floorInfo != null ? floorInfo.getCode() : "") + "," + buildingName + "," + venueName);
        });
        mapxusMap.addOnMapLongClickedListener((latLng, floorInfo, indoorBuilding, venue) -> {
            if (indoorBuilding != null) {
                buildingName = indoorBuilding.getBuildingName();
                venueName = venue.getVenueName();
            } else {
                buildingName = null;
                venueName = null;
            }
            displayDialog("You have long press at coordinate " + latLng.latitude + "," + latLng.longitude + "," + (floorInfo != null ? floorInfo.getCode() : "") + "," + buildingName + "," + venueName);
        });


        mapxusMap.addOnIndoorPoiClickListener(poi -> {
            setBuildingName(poi.getBuildingId());
            for (Map.Entry<String, String> entry : indoorBuilding.getFloorNameIdMap().entrySet()) {
                if (poi.getFloor().equals(entry.getValue())) {
                    floorName = entry.getKey();
                }
            }
            displayDialog("You have tap on POI " + poi.getName() + "," + floorName + "," + buildingName + "," + venueName);
        });


        mapxusMap.addOnFloorChangedListener((venue, indoorBuilding, floorInfo) -> {
            if (indoorBuilding != null) {
                ClickEventListenerActivity.this.indoorBuilding = indoorBuilding;
                ClickEventListenerActivity.this.venue = venue;
            }
        });

    }

    private void displayDialog(String message) {
        new MaterialDialog.Builder(this)
                .content(message)
                .positiveText(R.string.ok)
                .onAny((dialog, which) -> dialog.dismiss())
                .cancelable(false)
                .show();
    }

    private void setBuildingName(String buildingId) {
        if (indoorBuilding.getBuildingId().equals(buildingId)) {
            buildingName = indoorBuilding.getBuildingName();
            venueName = venue.getVenueName();
        } else {
            buildingName = null;
            venueName = null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
        mapViewProvider.onDestroy();
    }
}