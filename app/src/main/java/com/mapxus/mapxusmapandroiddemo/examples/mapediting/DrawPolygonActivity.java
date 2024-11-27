package com.mapxus.mapxusmapandroiddemo.examples.mapediting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.annotation.Fill;
import com.mapbox.mapboxsdk.plugins.annotation.FillManager;
import com.mapbox.mapboxsdk.plugins.annotation.FillOptions;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DrawPolygonActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;
    private FillManager fillManager;
    private Fill fill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_annotation_polygon);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        mapxusMap.addOnFloorChangedListener((venue, indoorBuilding, floorInfo) -> {
            if (indoorBuilding != null && floorInfo != null && indoorBuilding.getBuildingId().equals(getString(R.string.default_search_text_building_id)) && (Objects.equals(indoorBuilding.getDefaultDisplayFloorId(), floorInfo.getId()) || Objects.equals(indoorBuilding.getFloors().get(0).getId(), floorInfo.getId()))) {
                drawPolygon(mapboxMap);
            } else {
                deletePolygon();
            }
        });
    }

    private void drawPolygon(MapboxMap mapboxMap) {
        if (fill == null) {
            fillManager = new FillManager(mapView, mapboxMap, Objects.requireNonNull(mapboxMap.getStyle()));

            List<LatLng> innerLatLngs = new ArrayList<>();
            String[] polygonLat = getResources().getStringArray(R.array.default_draw_polygon_lat);
            String[] polygonLng = getResources().getStringArray(R.array.default_draw_polygon_lng);
            for (int i = 0; i < polygonLat.length; i++) {
                innerLatLngs.add(new LatLng(Double.parseDouble(polygonLat[i]), Double.parseDouble(polygonLng[i])));
            }

            List<List<LatLng>> latLngs = new ArrayList<>();
            latLngs.add(innerLatLngs);

            FillOptions fillOptions = new FillOptions()
                    .withLatLngs(latLngs)
                    .withFillColor("#0E66B2");
            fill = fillManager.create(fillOptions);
        }
    }

    private void deletePolygon() {
        if (fill != null) {
            fillManager.delete(fill);
            fill = null;
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
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