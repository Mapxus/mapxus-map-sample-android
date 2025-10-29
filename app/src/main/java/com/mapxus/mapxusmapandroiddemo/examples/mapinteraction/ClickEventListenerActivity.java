package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.map.model.Venue;
import com.mapxus.map.mapxusmap.api.services.model.floor.Floor;
import com.mapxus.map.mapxusmap.impl.MapLibreMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;
import org.maplibre.android.maps.MapView;

import java.util.Collection;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class ClickEventListenerActivity extends AppCompatActivity implements OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapViewProvider mapViewProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_click);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapLibreMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        mapxusMap.addOnMapClickedListener((latLng, mapxusSite) -> {
            Floor floor = mapxusSite.getFloor();
            IndoorBuilding indoorBuilding = mapxusSite.getIndoorBuilding();
            Venue venue = mapxusSite.getVenue();
            String buildingName = null;
            String venueName = null;
            if (indoorBuilding != null) {
                buildingName = indoorBuilding.getBuildingNameMap().getDefault();
            }
            if (venue != null) {
                venueName = venue.getNameMap().getDefault();
            }
            displayDialog("You have tap at coordinate " + latLng.latitude + "," + latLng.longitude + "," + (floor != null ? floor.getCode() : "") + "," + buildingName + "," + venueName);
        });
        mapxusMap.addOnMapLongClickedListener((latLng, mapxusSite) -> {
            Floor floor = mapxusSite.getFloor();
            IndoorBuilding indoorBuilding = mapxusSite.getIndoorBuilding();
            Venue venue = mapxusSite.getVenue();
            String buildingName = null;
            String venueName = null;
            if (indoorBuilding != null) {
                buildingName = indoorBuilding.getBuildingNameMap().getDefault();
            }
            if (venue != null) {
                venueName = venue.getNameMap().getDefault();
            }
            displayDialog("You have long press at coordinate " + latLng.latitude + "," + latLng.longitude + "," + (floor != null ? floor.getCode() : "") + "," + buildingName + "," + venueName);
        });


        mapxusMap.addOnIndoorPoiClickListener(poi -> {
            String buildingName = null;
            String venueName = null;
            String floor = null;
            String sectionName = null;
            IndoorBuilding poiBuilding = mapxusMap.getBuildings().get(poi.getBuildingId());
            if (poiBuilding != null) {
                buildingName = poiBuilding.getBuildingNameMap().getDefault();
                venueName = mapxusMap.getVenues().get(poiBuilding.getVenueId()).getNameMap().getDefault();
            }
            if (poi.getFloorName() != null) {
                floor = poi.getFloorName();
            }
            if (poi.getSharedFloorId() != null) {
                String sharedFloorId = poi.getSharedFloorId();
                floor = poi.getSharedFloorName();
                Collection<Venue> venues = mapxusMap.getVenues().values();
                for (Venue venue : venues) {
                    for (String id : venue.getSharedFloorIds()) {
                        if (id.equals(sharedFloorId)) {
                            venueName = venue.getNameMap().getDefault();
                            break;
                        }
                    }
                }
            }

            if (poi.getSections() != null && !poi.getSections().isEmpty()) {
                sectionName = poi.getSections().get(0).getSectionName().getDefault();
            }

            displayDialog("You have tap on POI " + poi.getNameMap().getDefault() + "," + floor + "," + sectionName + "," + buildingName + "," + venueName);
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