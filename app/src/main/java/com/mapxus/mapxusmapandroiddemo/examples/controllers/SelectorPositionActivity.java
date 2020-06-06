package com.mapxus.mapxusmapandroiddemo.examples.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.MapxusUiSettings;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.SelectorPosition;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

/**
 * The most basic example of adding a map to an activity.
 */
public class SelectorPositionActivity extends AppCompatActivity implements OnMapxusMapReadyCallback, View.OnClickListener  {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapxusMap mapxusMap;
    private MapxusUiSettings uiSettings;

    private Button button1, button2, button3, button4, button5, button6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_position);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
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
    public void onSaveInstanceState(Bundle outState) {
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
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        MapxusUiSettings uiSettings = mapxusMap.getMapxusUiSettings();
        this.mapxusMap = mapxusMap;
        this.uiSettings = uiSettings;


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button1: {
                uiSettings.setSelectorPosition(SelectorPosition.CENTER_LEFT);
                break;
            }
            case R.id.button2: {
                uiSettings.setSelectorPosition(SelectorPosition.CENTER_RIGHT);
                break;
            }
            case R.id.button3: {
                uiSettings.setSelectorPosition(SelectorPosition.BOTTOM_LEFT);
                break;
            }
            case R.id.button4: {
                uiSettings.setSelectorPosition(SelectorPosition.BOTTOM_RIGHT);
                break;
            }
            case R.id.button5: {
                uiSettings.setSelectorPosition(SelectorPosition.TOP_LEFT);
                break;
            }
            case R.id.button6: {
                uiSettings.setSelectorPosition(SelectorPosition.TOP_RIGHT);
                break;
            }

        }
    }
}