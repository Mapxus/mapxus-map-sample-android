package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.SwitchFloorScope;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.impl.MapLibreMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.OnMapReadyCallback;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class FloorSwitchModeActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapxusMapReadyCallback, PopupWindow.OnDismissListener {


    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private MapxusMap mapxusMap;
    private PopupWindow popupWindow;
    private int navigationHeight;
    private View.OnClickListener popupWindowItemClickListener = v -> {
        switch (v.getId()) {
            case R.id.tv_venue_switch_scope:
                mapxusMap.setSwitchFloorScope(SwitchFloorScope.VENUE);
                break;
            case R.id.tv_global_switch_scope:
                mapxusMap.setSwitchFloorScope(SwitchFloorScope.GLOBAL);
                break;
            case R.id.btn_cancel:
                break;
        }
        popupWindow.dismiss();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_floor_switch_mode);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapViewProvider mapViewProvider = new MapLibreMapViewProvider(FloorSwitchModeActivity.this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(this);
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = getResources().getDimensionPixelSize(resourceId);
        findViewById(R.id.btn_floor_switch_mode).setOnClickListener(this::openPopupWindow);
        ((SwitchCompat) findViewById(R.id.switch_button)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mapxusMap != null) {
                mapxusMap.isMaskNonSelectedSite(isChecked);
            }
        });
    }

    @Override
    public void onMapReady(@NotNull MapLibreMap mapLibreMap) {
        this.mapLibreMap = mapLibreMap;
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
    }

    private void openPopupWindow(View v) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        int resource = R.layout.popup_window_view_floor_switch_mode;
        View view = LayoutInflater.from(this).inflate(resource, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, navigationHeight);
        popupWindow.setOnDismissListener(this);
        setBackgroundAlpha(0.8f);

        view.findViewById(R.id.btn_cancel).setOnClickListener(popupWindowItemClickListener);
        view.findViewById(R.id.tv_venue_switch_scope).setOnClickListener(popupWindowItemClickListener);
        view.findViewById(R.id.tv_global_switch_scope).setOnClickListener(popupWindowItemClickListener);
    }

    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }
}