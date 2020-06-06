package com.mapxus.mapxusmapandroiddemo.examples.annotations;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Create a default marker with an InfoWindow
 */
public class DrawLineActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapboxMap mapboxMap;
    private Button drawLineButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_annotation_line);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapViewProvider = new MapboxMapViewProvider(this, mapView);

        mapView.getMapAsync(this);

        drawLineButton = findViewById(R.id.line);
        drawLineButton.setOnClickListener(view -> drawLine());
    }

    private void drawLine() {
        if (mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                FeatureCollection featureCollection = createLine();
                if (Objects.requireNonNull(featureCollection.features()).size() > 0) {
                    style.addSource(new GeoJsonSource("line-source", featureCollection));

                    style.addLayer(new LineLayer("linelayer", "line-source")
                            .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                    PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                    PropertyFactory.lineOpacity(1f),
                                    PropertyFactory.lineWidth(7f),
                                    PropertyFactory.lineColor(Color.parseColor("#0E66B2"))));
                }
            }
        }
        drawLineButton.setClickable(false);
    }

    public FeatureCollection createLine() {
        List<Point> routeCoordinates = new ArrayList<>();
        routeCoordinates.add(Point.fromLngLat(114.110958, 22.371366));
        routeCoordinates.add(Point.fromLngLat(114.111065, 22.371396));
        routeCoordinates.add(Point.fromLngLat(114.111375, 22.371223));
        routeCoordinates.add(Point.fromLngLat(114.111297, 22.371090));
        routeCoordinates.add(Point.fromLngLat(114.111251, 22.371117));
        routeCoordinates.add(Point.fromLngLat(114.111143, 22.370941));
        routeCoordinates.add(Point.fromLngLat(114.110801, 22.371111));

        LineString lineString = LineString.fromLngLats(routeCoordinates);
        return FeatureCollection.fromFeature(Feature.fromGeometry(lineString));
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mapViewProvider.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }
}