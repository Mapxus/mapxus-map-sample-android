package com.mapxus.mapxusmapandroiddemo.examples.annotations;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.annotation.Fill;
import com.mapbox.mapboxsdk.plugins.annotation.FillManager;
import com.mapbox.mapboxsdk.plugins.annotation.FillOptions;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.constant.LatLngConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mapxus.mapxusmapandroiddemo.examples.annotations.PolygonHolesActivity.Config.BLUE_COLOR;
import static com.mapxus.mapxusmapandroiddemo.examples.annotations.PolygonHolesActivity.Config.HOLE_COORDINATES;
import static com.mapxus.mapxusmapandroiddemo.examples.annotations.PolygonHolesActivity.Config.POLYGON_COORDINATES;

/**
 * Add holes to a polygon drawn on top of the map.
 */
public class PolygonHolesActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapxusMapReadyCallback {
    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapboxMap mMapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapboxMapOptions options = new MapboxMapOptions()
                .camera(new CameraPosition.Builder()
                        .target(LatLngConstant.ELEMENT_LATLON)
                        .zoom(17)
                        .build());

        mapView = new MapView(this, options);
        mapView.setId(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(this);
        setContentView(mapView);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        FillManager fillManager = new FillManager(mapView, mMapboxMap, Objects.requireNonNull(mMapboxMap.getStyle()));
        Fill fill = fillManager.create(new FillOptions()
                .withLatLngs(POLYGON_COORDINATES)
                .withFillColor(BLUE_COLOR));

        fill.setLatLngs(HOLE_COORDINATES);
        fillManager.update(fill);
    }

    static final class Config {
        static final String BLUE_COLOR = "#0E66B2";
        static final int RED_COLOR = Color.parseColor("#AF0000");

        static final List<List<LatLng>> POLYGON_COORDINATES = new ArrayList<List<LatLng>>() {
            {
                add(new ArrayList<LatLng>() {
                    {
                        add(new LatLng(22.371396, 114.111065));
                        add(new LatLng(22.371366, 114.110958));
                        add(new LatLng(22.371111, 114.110801));
                        add(new LatLng(22.370941, 114.111143));
                        add(new LatLng(22.371117, 114.111251));
                        add(new LatLng(22.371090, 114.111297));
                        add(new LatLng(22.371223, 114.111375));
                    }
                });
            }
        };

        static final List<List<LatLng>> HOLE_COORDINATES = new ArrayList<List<LatLng>>() {
            {

                add(POLYGON_COORDINATES.get(0));
                add(new ArrayList<>(new ArrayList<LatLng>() {
                    {
                        add(new LatLng(22.3712597, 114.1110223));
                        add(new LatLng(22.3712350, 114.1111283));
                        add(new LatLng(22.3712084, 114.1110966));
                    }
                }));

                add(new ArrayList<>(new ArrayList<LatLng>() {
                    {
                        add(new LatLng(22.3710839, 114.1110381));
                        add(new LatLng(22.3710234, 114.1110778));
                        add(new LatLng(22.3711031, 114.1111114));
                    }
                }));
            }
        };
    }
}
