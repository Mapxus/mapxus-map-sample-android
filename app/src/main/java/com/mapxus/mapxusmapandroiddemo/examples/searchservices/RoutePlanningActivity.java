package com.mapxus.mapxusmapandroiddemo.examples.searchservices;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.component.overlay.WalkRouteOverlay;
import com.mapxus.map.model.LatLng;
import com.mapxus.map.model.MapxusMarkerOptions;
import com.mapxus.map.model.overlay.MapxusMarker;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.services.RoutePlanning;
import com.mapxus.services.model.planning.RoutePlanningPoint;
import com.mapxus.services.model.planning.RoutePlanningResult;
import com.mapxus.services.model.planning.RouteResponseDto;

/**
 * Use MapxusMap Search Services to request directions
 */
public class RoutePlanningActivity extends AppCompatActivity implements RoutePlanning.RoutePlanningResultListener {

    private static final String TAG = "RoutePlanningActivity";

    private MapView mapView;
    private MapxusMap mMapxusMap;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;
    private RoutePlanning routePlanning;

    private RoutePlanningPoint origin = null;
    private RoutePlanningPoint destination = null;

    private Button planningBtn;

    private TextView pointStartTv;

    private TextView pointEndTv;


    private MapxusMarker startMarker;
    private MapxusMarker endMarker;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_route_planning);

        planningBtn = findViewById(R.id.btn_planning);
        pointStartTv = findViewById(R.id.point_start);
        pointEndTv = findViewById(R.id.point_end);

        // Setup the MapView
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(mapboxMap -> RoutePlanningActivity.this.mapboxMap = mapboxMap);

        mapViewProvider.getMapxusMapAsync(mapxusMap -> {
            RoutePlanningActivity.this.mMapxusMap = mapxusMap;
            routePlanning = RoutePlanning.newInstance();
            routePlanning.setRoutePlanningListener(RoutePlanningActivity.this);
            pointStartTv.setOnClickListener(pointClickListener);
            pointEndTv.setOnClickListener(pointClickListener);
            planningBtn.setOnClickListener(planningBtnClickListener);
        });

    }

    private void getRoute(RoutePlanningPoint origin, RoutePlanningPoint destination) {

        routePlanning.route(origin, destination);
    }

    private void drawRoute(RouteResponseDto route) {
       WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, mapboxMap, mMapxusMap, route, origin, destination);
        walkRouteOverlay.addToMap();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the directions API request
        if (routePlanning != null) {
            routePlanning.destroy();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onGetRoutePlanningResult(RoutePlanningResult routePlanningResult) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        if (routePlanningResult.status != 0) {
            Toast.makeText(this, routePlanningResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (routePlanningResult.getRouteResponseDto() == null) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }
        RouteResponseDto routeResponseDto = routePlanningResult.getRouteResponseDto();
        drawRoute(routeResponseDto);
        mMapxusMap.switchFloor(origin.getFloor());
    }


    private View.OnClickListener pointClickListener = v -> {
        switch (v.getId()) {
            case R.id.point_start: {
                pointStartClick();
                break;
            }

            case R.id.point_end: {
                pointEndClick();
                break;
            }
        }

    };

    private View.OnClickListener planningBtnClickListener = v -> {
        if (origin == null || destination == null) {
            return;
        }
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("wait");
        dialog.setMessage("get route");
        dialog.show();
        removeAllSelectedMapClickListener();
        getRoute(origin, destination);
    };


    private void pointStartClick() {

        pointStartTv.setText("please click map");
        removeAllSelectedMapClickListener();

        mMapxusMap.addOnMapClickListener(pointStartMapClickListener);
    }

    private void pointEndClick() {
        pointEndTv.setText("please click map");
        removeAllSelectedMapClickListener();
        mMapxusMap.addOnMapClickListener(pointEndMapClickListener);
    }

    private void removeAllSelectedMapClickListener() {
        mMapxusMap.removeOnMapClickListener(pointEndMapClickListener);
        mMapxusMap.removeOnMapClickListener(pointStartMapClickListener);
    }


    private MapxusMap.OnMapClickListener pointStartMapClickListener = new MapxusMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng, String floor, String buildingId) {
            origin = new RoutePlanningPoint(buildingId, floor, latLng.getLongitude(), latLng.getLatitude());
            pointStartTv.setText(String.format("%s,%s,%s", latLng.getLatitude(), latLng.getLongitude(),floor));
            if (startMarker != null) {
                mMapxusMap.removeMarker(startMarker);
                startMarker = null;
            }
            MapxusMarkerOptions mapxusMarkerOptions = new MapxusMarkerOptions();
            mapxusMarkerOptions.setPosition(latLng);
            mapxusMarkerOptions.setBuildingId(buildingId);
            mapxusMarkerOptions.setFloor(floor);
            startMarker = mMapxusMap.addMarker(mapxusMarkerOptions);
        }
    };

    private MapxusMap.OnMapClickListener pointEndMapClickListener = new MapxusMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng, String floor, String buildingId) {
            destination = new RoutePlanningPoint(buildingId, floor, latLng.getLongitude(), latLng.getLatitude());
            pointEndTv.setText(String.format("%s,%s,%s", latLng.getLatitude(), latLng.getLongitude(),floor));

            if (endMarker != null) {
                mMapxusMap.removeMarker(endMarker);
                endMarker = null;
            }
            MapxusMarkerOptions mapxusMarkerOptions = new MapxusMarkerOptions();
            mapxusMarkerOptions.setPosition(latLng);
            mapxusMarkerOptions.setBuildingId(buildingId);
            mapxusMarkerOptions.setFloor(floor);
            endMarker = mMapxusMap.addMarker(mapxusMarkerOptions);
        }
    };

}