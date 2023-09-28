package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.graphics.Insets;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.tabs.TabLayout;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.MapxusMapZoomMode;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;

import org.jetbrains.annotations.NotNull;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class FocusOnIndoorSceneActivity extends BaseWithParamMenuActivity implements OnMapxusMapReadyCallback {

    public static final String ZOOM_DISABLE = "ZoomDisable";
    public static final String ZOOM_ANIMATED = "ZoomAnimated";
    public static final String ZOOM_DIRECT = "ZoomDirect";

    private MapView mapView;
    private MapxusMap mapxusMap;
    private String id;
    private Insets insets;
    private int zoomMode = MapxusMapZoomMode.ZoomDisable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_animate);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapViewProvider mapViewProvider = new MapboxMapViewProvider(FocusOnIndoorSceneActivity.this, mapView);
        mapViewProvider.getMapxusMapAsync(this);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
    }

    @Override
    protected void initBottomSheetDialog() {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_zoom_mode_style, this);
        zoomMode = MapxusMapZoomMode.ZoomDisable;
        Button btnZoomMode = bottomSheetDialogView.findViewById(R.id.btn_zoom_mode);
        btnZoomMode.setText(ZOOM_DISABLE);
        btnZoomMode.setOnClickListener(v -> {
            String btnZoomModeText = ZOOM_DISABLE;
            switch (btnZoomMode.getText().toString()) {
                case ZOOM_DISABLE:
                    btnZoomModeText = ZOOM_ANIMATED;
                    zoomMode = MapxusMapZoomMode.ZoomAnimated;
                    break;
                case ZOOM_ANIMATED:
                    btnZoomModeText = ZOOM_DIRECT;
                    zoomMode = MapxusMapZoomMode.ZoomDirect;
                    break;
                case ZOOM_DIRECT:
                    btnZoomModeText = ZOOM_DISABLE;
                    zoomMode = MapxusMapZoomMode.ZoomDisable;
                    break;
            }
            btnZoomMode.setText(btnZoomModeText);
        });
        TabLayout tabLayout = bottomSheetDialogView.findViewById(R.id.tab_layout);
        EditText etId = bottomSheetDialogView.findViewById(R.id.et_id);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        etId.setText(getString(R.string.default_switch_indoor_floor_id));
                        break;
                    case 1:
                        etId.setText(getString(R.string.default_search_text_building_id));
                        break;
                    default:
                        etId.setText(getString(R.string.default_venue_id));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        bottomSheetDialogView.findViewById(R.id.create).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            getValue(bottomSheetDialogView);
            new MaterialDialog.Builder(this)
                    .content("Focus on the scene with the params now?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .onAny((dialog, which) -> {
                        if (which == DialogAction.POSITIVE) {
                            if (mapxusMap != null) {
                                switch (tabLayout.getSelectedTabPosition()) {
                                    case 0:
                                        mapxusMap.selectFloorById(id, zoomMode, insets);
                                        break;
                                    case 1:
                                        mapxusMap.selectBuildingById(id, zoomMode, insets);
                                        break;
                                    default:
                                        mapxusMap.selectVenueById(id, zoomMode, insets);
                                        break;
                                }
                            }
                        }
                        dialog.dismiss();
                    })
                    .cancelable(false)
                    .show();
        });
    }

    private void getValue(View bottomSheetDialogView) {
        EditText etId = bottomSheetDialogView.findViewById(R.id.et_id);
        EditText etTop = bottomSheetDialogView.findViewById(R.id.et_top);
        EditText etBottom = bottomSheetDialogView.findViewById(R.id.et_bottom);
        EditText etLeft = bottomSheetDialogView.findViewById(R.id.et_left);
        EditText etRight = bottomSheetDialogView.findViewById(R.id.et_right);

        id = etId.getText().toString().trim();
        int paddingTop = etTop.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etTop.getText().toString().trim());
        int paddingBottom = etBottom.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etBottom.getText().toString().trim());
        int paddingLeft = etLeft.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etLeft.getText().toString().trim());
        int paddingRight = etRight.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etRight.getText().toString().trim());

        insets = Insets.of(paddingLeft, paddingTop, paddingRight, paddingBottom);
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
}