package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.MapxusUiSettings;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.SelectorPosition;
import com.mapxus.map.mapxusmap.impl.MapLibreMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;

import org.jetbrains.annotations.NotNull;
import org.maplibre.android.maps.MapView;

/**
 * The most basic example of adding a map to an activity.
 */
public class IndoorMapControllerActivity extends AppCompatActivity implements OnMapxusMapReadyCallback, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapxusUiSettings uiSettings;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_map_controller);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapLibreMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
        SwitchCompat switchButton = findViewById(R.id.switch_button);
        switchButton.setChecked(false);
        switchButton.setOnCheckedChangeListener(this);
        currentPosition = SelectorPosition.CENTER_LEFT;
        findViewById(R.id.btn_position).setOnClickListener(this);
        findViewById(R.id.btn_box_length).setOnClickListener(this);
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
    public void onSaveInstanceState(@NotNull Bundle outState) {
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
        this.uiSettings = mapxusMap.getMapxusUiSettings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_position:
                switch (currentPosition) {
                    case SelectorPosition.CENTER_LEFT:
                        uiSettings.setSelectorPosition(SelectorPosition.CENTER_RIGHT);
                        currentPosition = SelectorPosition.CENTER_RIGHT;
                        break;
                    case SelectorPosition.CENTER_RIGHT:
                        uiSettings.setSelectorPosition(SelectorPosition.BOTTOM_LEFT);
                        currentPosition = SelectorPosition.BOTTOM_LEFT;
                        break;
                    case SelectorPosition.BOTTOM_LEFT:
                        uiSettings.setSelectorPosition(SelectorPosition.BOTTOM_RIGHT);
                        currentPosition = SelectorPosition.BOTTOM_RIGHT;
                        break;
                    case SelectorPosition.BOTTOM_RIGHT:
                        uiSettings.setSelectorPosition(SelectorPosition.TOP_LEFT);
                        currentPosition = SelectorPosition.TOP_LEFT;
                        break;
                    case SelectorPosition.TOP_LEFT:
                        uiSettings.setSelectorPosition(SelectorPosition.TOP_RIGHT);
                        currentPosition = SelectorPosition.TOP_RIGHT;
                        break;
                    case SelectorPosition.TOP_RIGHT:
                        uiSettings.setSelectorPosition(SelectorPosition.CENTER_LEFT);
                        currentPosition = SelectorPosition.CENTER_LEFT;
                        break;
                }
                break;
            case R.id.btn_box_length:
                openBottomSheetDialog();
                break;
        }
    }

    private void openBottomSheetDialog() {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_box_length_style, this);
        EditText etVisibleFloors = bottomSheetDialogView.findViewById(R.id.et_visible_item);
        bottomSheetDialogView.findViewById(R.id.create).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            uiSettings.setSelectorVisibleItem(Integer.parseInt(etVisibleFloors.getText().toString()));

        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        uiSettings.setSelectorEnabled(!isChecked);
    }
}