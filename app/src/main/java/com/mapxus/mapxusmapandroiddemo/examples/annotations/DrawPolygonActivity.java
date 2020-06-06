package com.mapxus.mapxusmapandroiddemo.examples.annotations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
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

/**
 * Draw a vector polygon on a map with the Beemap Android SDK.
 */
public class DrawPolygonActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapboxMap mMapboxMap;

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

    private void drawPolygon(MapboxMap mapboxMap) {
        FillManager fillManager = new FillManager(mapView, mapboxMap, Objects.requireNonNull(mapboxMap.getStyle()));
        List<LatLng> innerLatLngs = new ArrayList<>();
        innerLatLngs.add(new LatLng(22.371396, 114.111065));
        innerLatLngs.add(new LatLng(22.371366, 114.110958));
        innerLatLngs.add(new LatLng(22.371111, 114.110801));
        innerLatLngs.add(new LatLng(22.370941, 114.111143));
        innerLatLngs.add(new LatLng(22.371117, 114.111251));
        innerLatLngs.add(new LatLng(22.371090, 114.111297));
        innerLatLngs.add(new LatLng(22.371223, 114.111375));

        List<List<LatLng>> latLngs = new ArrayList<>();
        latLngs.add(innerLatLngs);

        FillOptions fillOptions = new FillOptions()
                .withLatLngs(latLngs)
                .withFillColor("#0E66B2");
        fillManager.create(fillOptions);
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

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        drawPolygon(mMapboxMap);
    }
}