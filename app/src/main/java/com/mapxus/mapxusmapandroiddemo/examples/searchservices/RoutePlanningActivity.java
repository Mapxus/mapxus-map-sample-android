package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.model.overlay.WalkRouteOverlay;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.map.interfaces.OnMapxusMapReadyCallback;
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
    private RouteResponseDto currentRoute;
    private RoutePlanning routePlanning;
    private RoutePlanningPoint origin = new RoutePlanningPoint("elements_hk_dc005f", "L1", 114.16130, 22.30585);
    private RoutePlanningPoint destination = new RoutePlanningPoint("elements_hk_dc005f", "L3", 114.16185, 22.30405);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_route_planning);

        // Setup the MapView
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                RoutePlanningActivity.this.mapboxMap = mapboxMap;
            }
        });

        mapViewProvider.getMapxusMapAsync(new OnMapxusMapReadyCallback() {
            @Override
            public void onMapxusMapReady(MapxusMap mapxusMap) {
                RoutePlanningActivity.this.mMapxusMap = mapxusMap;
                routePlanning = RoutePlanning.newInstance();
                routePlanning.setRoutePlanningListener(RoutePlanningActivity.this);
                getRoute(origin, destination);
            }
        });

    }

    private void getRoute(RoutePlanningPoint origin, RoutePlanningPoint destination) {

        routePlanning.route(origin, destination);
    }

    private void drawRoute(RouteResponseDto route) {
        // Convert LineString coordinates into LatLng[]

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
}