package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.route;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.FollowUserMode;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.LatLng;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMarkerOptions;
import com.mapxus.map.mapxusmap.api.map.model.SelectorPosition;
import com.mapxus.map.mapxusmap.api.map.model.overlay.MapxusMarker;
import com.mapxus.map.mapxusmap.api.services.RoutePlanning;
import com.mapxus.map.mapxusmap.api.services.constant.RoutePlanningVehicle;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningPoint;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningRequest;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningResult;
import com.mapxus.map.mapxusmap.api.services.model.planning.RouteResponseDto;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.map.mapxusmap.overlay.route.WalkRouteOverlay;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.customizeview.SwitchView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class RoutePlanningActivity extends AppCompatActivity implements RoutePlanning.RoutePlanningResultListener, OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapxusMap mMapxusMap;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;
    private RoutePlanning routePlanning;

    private RoutePlanningPoint origin, destination = null;

    private MapxusMarker startMarker, endMarker;

    private TextView tvStart, tvEnd;
    private Button goBtn;

    private String vehicle = RoutePlanningVehicle.FOOT;
    private boolean toDoor = false;
    private RelativeLayout progressBarView;

    private MapxusNavigationPositioningProvider mapxusPositioningProvider;
    private WalkRouteOverlay walkRouteOverlay;

    private RouteResponseDto routeResponseDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_route_planning);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(mapboxMap -> RoutePlanningActivity.this.mapboxMap = mapboxMap);


        mapViewProvider.getMapxusMapAsync(this);

        progressBarView = findViewById(R.id.loding_view);

        SwitchView switchView = findViewById(R.id.switch_view);
        switchView.setOnCheckChangedListener(checked -> vehicle = checked ? RoutePlanningVehicle.WHEELCHAIR : RoutePlanningVehicle.FOOT);

        SwitchCompat switchButton = findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> toDoor = isChecked);

        tvStart = findViewById(R.id.tv_start);
        tvEnd = findViewById(R.id.tv_end);
        tvStart.setOnClickListener(pointClickListener);
        tvEnd.setOnClickListener(pointClickListener);
        findViewById(R.id.btn_search).setOnClickListener(planningBtnClickListener);
        goBtn = findViewById(R.id.btn_go);
        goBtn.setOnClickListener(goBtnClickListener);

        routePlanning = RoutePlanning.newInstance();
        routePlanning.setRoutePlanningListener(RoutePlanningActivity.this);

        mapxusPositioningProvider = new MapxusNavigationPositioningProvider(this, getApplicationContext());
    }

    private void getRoute() {
        RoutePlanningRequest request = new RoutePlanningRequest(origin, destination);
        request.setVehicle(vehicle);
        request.setToDoor(toDoor);
        routePlanning.route(request);
    }

    private void drawRoute(RouteResponseDto route) {
        if (startMarker != null) {
            mMapxusMap.removeMarker(startMarker);
            startMarker = null;
        }
        if (endMarker != null) {
            mMapxusMap.removeMarker(endMarker);
            endMarker = null;
        }
        mapboxMap.setMaxZoomPreference(22);
        walkRouteOverlay = new WalkRouteOverlay(this, mapboxMap, mMapxusMap, route, origin, destination, false);
        walkRouteOverlay.addToMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (mMapxusMap != null) {
            mMapxusMap.onResume();
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
        if (mMapxusMap != null) {
            mMapxusMap.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (routePlanning != null) {
            routePlanning.destroy();
        }
        if (mapViewProvider != null) {
            mapViewProvider.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onGetRoutePlanningResult(RoutePlanningResult routePlanningResult) {
        progressBarView.setVisibility(View.GONE);
        if (routePlanningResult.status != 0) {
            Toast.makeText(this, routePlanningResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (routePlanningResult.getRouteResponseDto() == null) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }
        routeResponseDto = routePlanningResult.getRouteResponseDto();
        drawRoute(routeResponseDto);
        mMapxusMap.switchFloor(origin.getFloor());
    }

    private View.OnClickListener pointClickListener = v -> {
        switch (v.getId()) {
            case R.id.tv_start:
                pointStartClick();
                break;
            case R.id.tv_end:
                pointEndClick();
                break;
        }
    };

    private void pointStartClick() {
        removeAllSelectedMapClickListener();
        mMapxusMap.addOnMapClickListener(pointStartMapClickListener);
        if (tvStart.getText().toString().equals(getString(R.string.start))) {
            tvStart.setText(R.string.tap_screen_for_start);
        } else {
            tvStart.setText(R.string.start);
        }

        if (destination != null) {
            tvEnd.setText(String.format("%s,%s %s", doubleToString(destination.getLat()), doubleToString(destination.getLon()), destination.getFloor()));
        } else {
            tvEnd.setText(R.string.end);
        }
    }

    private void pointEndClick() {
        removeAllSelectedMapClickListener();
        mMapxusMap.addOnMapClickListener(pointEndMapClickListener);
        if (tvEnd.getText().toString().equals(getString(R.string.end))) {
            tvEnd.setText(R.string.tap_screen_for_end);
        } else {
            tvEnd.setText(R.string.end);
        }

        if (origin != null) {
            tvStart.setText(String.format("%s,%s %s", doubleToString(origin.getLat()), doubleToString(origin.getLon()), origin.getFloor()));
        } else {
            tvStart.setText(R.string.start);
        }
    }

    private View.OnClickListener planningBtnClickListener = v -> {
        if (origin == null || destination == null) {
            Toast.makeText(this, "Please select point", Toast.LENGTH_SHORT).show();
            return;
        } else {
            tvStart.setText(String.format("%s,%s %s", doubleToString(origin.getLat()), doubleToString(origin.getLon()), origin.getFloor()));
            tvEnd.setText(String.format("%s,%s %s", doubleToString(destination.getLat()), doubleToString(destination.getLon()), destination.getFloor()));
        }
        progressBarView.setVisibility(View.VISIBLE);
        removeAllSelectedMapClickListener();
        getRoute();
    };

    private View.OnClickListener goBtnClickListener = v -> {
        if (goBtn.getText().toString().equals(getString(R.string.go))) {
            if (routeResponseDto == null) {
                new MaterialDialog.Builder(this)
                        .title(getString(R.string.warning))
                        .content(getString(R.string.search_first))
                        .positiveText(getString(R.string.ok))
                        .onPositive((dialog, which) -> dialog.dismiss()).show();
                return;
            }
            goBtn.setText(R.string.stop);
            mMapxusMap.setFollowUserMode(FollowUserMode.FOLLOW_USER_AND_HEADING);
            mapxusPositioningProvider.updatePath(routeResponseDto.getPaths().get(0),mapboxMap);
            mapxusPositioningProvider.setOnReachListener(() -> {
                walkRouteOverlay.removeFromMap();
                Toast.makeText(RoutePlanningActivity.this, getString(R.string.reach_toast_text), Toast.LENGTH_SHORT).show();
                mapxusPositioningProvider.setNavigation(null);
            });
        } else {
            goBtn.setText(R.string.go);
            mapxusPositioningProvider.setNavigation(null);
        }

    };

    private void removeAllSelectedMapClickListener() {
        mMapxusMap.removeOnMapClickListener(pointEndMapClickListener);
        mMapxusMap.removeOnMapClickListener(pointStartMapClickListener);
    }

    private MapxusMap.OnMapClickListener pointStartMapClickListener = new MapxusMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng, String floor, String buildingId, String floorId) {
            origin = new RoutePlanningPoint(buildingId, floor, latLng.getLongitude(), latLng.getLatitude());
            if (startMarker != null) {
                mMapxusMap.removeMarker(startMarker);
                startMarker = null;
            }
            startMarker = mMapxusMap.addMarker(getMapxusMarkerOptions(latLng, floor, buildingId));
        }
    };

    private MapxusMap.OnMapClickListener pointEndMapClickListener = new MapxusMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng, String floor, String buildingId, String floorId) {
            destination = new RoutePlanningPoint(buildingId, floor, latLng.getLongitude(), latLng.getLatitude());

            if (endMarker != null) {
                mMapxusMap.removeMarker(endMarker);
                endMarker = null;
            }
            endMarker = mMapxusMap.addMarker(getMapxusMarkerOptions(latLng, floor, buildingId));
        }
    };

    @NotNull
    private MapxusMarkerOptions getMapxusMarkerOptions(LatLng latLng, String floor, String buildingId) {
        MapxusMarkerOptions mapxusMarkerOptions = new MapxusMarkerOptions();
        mapxusMarkerOptions.setPosition(latLng);
        mapxusMarkerOptions.setBuildingId(buildingId);
        mapxusMarkerOptions.setFloor(floor);
        return mapxusMarkerOptions;
    }

    private static String doubleToString(double num) {
        return new DecimalFormat("0.0000").format(num);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        RoutePlanningActivity.this.mMapxusMap = mapxusMap;
        mMapxusMap.getMapxusUiSettings().setSelectorPosition(SelectorPosition.CENTER_RIGHT);
        setLocation();
    }

    private void setLocation() {
        mMapxusMap.setLocationProvider(mapxusPositioningProvider);
        mMapxusMap.setFollowUserMode(FollowUserMode.NONE);
    }
}