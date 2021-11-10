package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.explore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.MapxusMapZoomMode;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.map.model.SelectorPosition;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiInfo;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyPoiOverlay;

import java.util.ArrayList;
import java.util.List;

public class ExploreBuildingActivity extends AppCompatActivity implements OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapxusMap mapxusMap;
    private MapboxMap mapboxMap;
    private OnBuildingChageListener onBuildingChageListener;
    private BottomSheetBehavior<?> bottomSheetBehavior;
    private MyPoiOverlay poiOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_building);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> this.mapboxMap = mapboxMap);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
        mapxusMap.addOnBuildingChangeListener(indoorBuilding -> onBuildingChageListener.onBuildingChange(indoorBuilding));

        mapxusMap.getMapxusUiSettings().setSelectorPosition(SelectorPosition.CENTER_RIGHT);
    }

    public void addMarker(PoiInfo poiInfo) {
        List<PoiInfo> poiInfos = new ArrayList<>();
        poiInfos.add(poiInfo);
        poiOverlay = new MyPoiOverlay(mapboxMap, mapxusMap, poiInfos);
        poiOverlay.removeFromMap();
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
        mapxusMap.selectFloor(poiInfo.getFloor(), MapxusMapZoomMode.ZoomDisable, null);
    }

    public void addMarkers(List<PoiInfo> poiInfos) {
        poiOverlay = new MyPoiOverlay(mapboxMap, mapxusMap, poiInfos);
        poiOverlay.removeFromMap();
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
        mapxusMap.selectFloor(poiInfos.get(0).getFloor(), MapxusMapZoomMode.ZoomDisable, null);
    }

    public void removeMarker() {
        if (poiOverlay != null) {
            poiOverlay.removeFromMap();
        }
    }

    public interface OnBuildingChageListener {
        void onBuildingChange(IndoorBuilding indoorBuilding);
    }

    public void setOnBuildingChageListener(OnBuildingChageListener onBuildingChageListener) {
        this.onBuildingChageListener = onBuildingChageListener;
    }

    public BottomSheetBehavior<?> getBottomSheetBehavior() {
        return bottomSheetBehavior;
    }
}