package com.mapxus.mapxusmapandroiddemo.examples.annotations;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.constant.LatLngConstant;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.impl.MapboxMapViewProvider;

import java.util.ArrayList;
import java.util.List;

import static com.mapxus.mapxusmapandroiddemo.examples.annotations.PolygonHolesActivity.Config.BLUE_COLOR;
import static com.mapxus.mapxusmapandroiddemo.examples.annotations.PolygonHolesActivity.Config.HOLE_COORDINATES;
import static com.mapxus.mapxusmapandroiddemo.examples.annotations.PolygonHolesActivity.Config.POLYGON_COORDINATES;

/**
 * Add holes to a polygon drawn on top of the map.
 */
public class PolygonHolesActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private Polygon polygon;
    private MapViewProvider mapViewProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapboxMapOptions options = new MapboxMapOptions();
        options.camera(new CameraPosition.Builder()
                .target(LatLngConstant.ELEMENT_LATLON)
                .zoom(17)
                .build());

        mapView = new MapView(this, options);
        mapView.setId(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        setContentView(mapView);
    }

    @Override
    public void onMapReady(MapboxMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngConstant.ELEMENT_LATLON, 17));//

        polygon = map.addPolygon(new PolygonOptions()
                .addAll(POLYGON_COORDINATES)
                .addAllHoles(HOLE_COORDINATES)
                .fillColor(BLUE_COLOR));
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
    protected void onSaveInstanceState(Bundle outState) {
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

    static final class Config {
        static final int BLUE_COLOR = Color.parseColor("#3bb2d0");
        static final int RED_COLOR = Color.parseColor("#AF0000");

        static final List<LatLng> POLYGON_COORDINATES = new ArrayList<LatLng>() {
            {

                add(new LatLng(22.3062, 114.163445));
                add(new LatLng(22.3064002, 114.1613069));
                add(new LatLng(22.3051301, 114.1614686));
                add(new LatLng(22.3051462, 114.1629946));
            }
        };

        static final List<List<LatLng>> HOLE_COORDINATES = new ArrayList<List<LatLng>>() {
            {
                add(new ArrayList<>(new ArrayList<LatLng>() {
                    {
                        add(new LatLng(22.3057336, 114.1615695));
                        add(new LatLng(22.3054094, 114.1615344));
                        add(new LatLng(22.3057985, 114.1619725));
                    }
                }));
            }
        };
    }
}
