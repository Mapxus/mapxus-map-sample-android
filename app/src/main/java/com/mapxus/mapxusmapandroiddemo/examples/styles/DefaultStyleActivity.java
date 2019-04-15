package com.mapxus.mapxusmapandroiddemo.examples.styles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.model.Style;

/**
 * Use a variety of professionally designed styles with the Beemap Android SDK.
 */
public class DefaultStyleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapboxMap mapboxMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_style_default);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);

        mapViewProvider.getMapxusMapAsync(new OnMapxusMapReadyCallback() {
            @Override
            public void onMapxusMapReady(MapxusMap mapxusMap) {
                DefaultStyleActivity.this.mapboxMap = mapboxMap;
                Toast.makeText(DefaultStyleActivity.this, getString(R.string.click_toobar_switch_style), Toast.LENGTH_LONG).show();
            }
        });
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.style_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getMenuInflater().inflate(R.menu.menu_map_style, menu);

        Spinner spinner = (Spinner) menu.findItem(R.id.spinner).getActionView();
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(DefaultStyleActivity.this);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mapViewProvider.setStyle(Style.MAPXUS);
                return;
            case 1:
                mapViewProvider.setStyle(Style.COMMON);
                return;
            case 2:
                mapViewProvider.setStyle(Style.MAPPYBEE);
                return;
            case 3:
                mapViewProvider.setStyle(Style.HALLOWMAS);
                return;
            case 4:
                mapViewProvider.setStyle(Style.CHRISTMAS);
                return;
            default:
                mapViewProvider.setStyle(Style.MAPXUS);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}