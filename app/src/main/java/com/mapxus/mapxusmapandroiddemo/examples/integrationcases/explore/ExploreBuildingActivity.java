package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.explore;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.MapxusMapZoomMode;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.map.model.SelectorPosition;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiInfo;
import com.mapxus.map.mapxusmap.impl.MapLibreMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyPoiOverlay;

import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;

import java.util.ArrayList;
import java.util.List;

public class ExploreBuildingActivity extends AppCompatActivity implements OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapxusMap mapxusMap;
    private MapLibreMap mapLibreMap;
    private OnBuildingChageListener onBuildingChageListener;
    private BottomSheetBehavior<?> bottomSheetBehavior;
    private MyPoiOverlay poiOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_building);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> this.mapLibreMap = mapboxMap);
        mapViewProvider = new MapLibreMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
        View view = findViewById(R.id.bottomSheetLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(view);
        view.setOnTouchListener((v, event) -> true);
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

        mapxusMap.getMapxusUiSettings().setSelectorPosition(SelectorPosition.TOP_RIGHT);
    }

    public void addMarker(PoiInfo poiInfo) {
        List<PoiInfo> poiInfos = new ArrayList<>();
        poiInfos.add(poiInfo);
        poiOverlay = new MyPoiOverlay(mapLibreMap, mapxusMap, poiInfos);
        poiOverlay.removeFromMap();
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan(Double.parseDouble(getString(R.string.default_zoom_level_value)));
        if (poiInfo.getFloorId() != null) {
            mapxusMap.selectFloorById(poiInfo.getFloorId(), MapxusMapZoomMode.ZoomDisable, null);
        }
        if (poiInfo.getSharedFloorId() != null) {
            mapxusMap.selectSharedFloorById(poiInfo.getSharedFloorId(), MapxusMapZoomMode.ZoomDisable, null);
        }
    }

    public void addMarkers(List<PoiInfo> poiInfos) {
        poiOverlay = new MyPoiOverlay(mapLibreMap, mapxusMap, poiInfos);
        poiOverlay.removeFromMap();
        poiOverlay.addToMap();
        PoiInfo firstPoi = poiInfos.get(0);
        if (firstPoi.getFloorId() != null) {
            mapxusMap.selectFloorById(firstPoi.getFloorId(), MapxusMapZoomMode.ZoomDisable, null);
        }
        if (firstPoi.getSharedFloorId() != null) {
            mapxusMap.selectSharedFloorById(firstPoi.getSharedFloorId(), MapxusMapZoomMode.ZoomDisable, null);
        }
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