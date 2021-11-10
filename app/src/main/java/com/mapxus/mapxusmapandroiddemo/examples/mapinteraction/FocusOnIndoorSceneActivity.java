package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.graphics.Insets;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.MapxusMapZoomMode;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.services.BuildingSearch;
import com.mapxus.map.mapxusmap.api.services.model.DetailSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.building.BuildingDetailResult;
import com.mapxus.map.mapxusmap.api.services.model.building.BuildingResult;
import com.mapxus.map.mapxusmap.api.services.model.building.IndoorBuildingInfo;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;

import org.jetbrains.annotations.NotNull;

/**
 * Animate the map's camera position, tilt, bearing, and zoom.
 */
public class FocusOnIndoorSceneActivity extends BaseWithParamMenuActivity implements OnMapReadyCallback, OnMapxusMapReadyCallback, BuildingSearch.BuildingSearchResultListener {

    public static final String ZOOM_DISABLE = "ZoomDisable";
    public static final String ZOOM_ANIMATED = "ZoomAnimated";
    public static final String ZOOM_DIRECT = "ZoomDirect";

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;
    private BuildingSearch buildingSearch;
    private String buildingId, floorName;
    private int paddingTop, paddingBottom, paddingLeft, paddingRight;
    private String zoomMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_animate);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapViewProvider mapViewProvider = new MapboxMapViewProvider(FocusOnIndoorSceneActivity.this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(this);

        buildingSearch = BuildingSearch.newInstance();
        buildingSearch.setBuildingSearchResultListener(this);
    }

    @Override
    protected void initBottomSheetDialog() {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_zoom_mode_style, this);

        Button btnZoomMode = bottomSheetDialogView.findViewById(R.id.btn_zoom_mode);
        zoomMode = ZOOM_DISABLE;
        btnZoomMode.setText(zoomMode);
        btnZoomMode.setOnClickListener(v -> {
            switch (zoomMode) {
                case ZOOM_DISABLE:
                    zoomMode = ZOOM_ANIMATED;
                    break;
                case ZOOM_ANIMATED:
                    zoomMode = ZOOM_DIRECT;
                    break;
                case ZOOM_DIRECT:
                    zoomMode = ZOOM_DISABLE;
                    break;
            }
            btnZoomMode.setText(zoomMode);
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
                            doSearchQuery();
                        }
                        dialog.dismiss();
                    })
                    .cancelable(false)
                    .show();
        });
    }

    private void getValue(View bottomSheetDialogView) {
        EditText etBuildingId = bottomSheetDialogView.findViewById(R.id.et_id);
        EditText etFloorName = bottomSheetDialogView.findViewById(R.id.et_floor_name);
        EditText etTop = bottomSheetDialogView.findViewById(R.id.et_top);
        EditText etBottom = bottomSheetDialogView.findViewById(R.id.et_bottom);
        EditText etLeft = bottomSheetDialogView.findViewById(R.id.et_left);
        EditText etRight = bottomSheetDialogView.findViewById(R.id.et_right);

        buildingId = etBuildingId.getText().toString().trim();
        floorName = etFloorName.getText().toString().trim();
        paddingTop = etTop.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etTop.getText().toString().trim());
        paddingBottom = etBottom.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etBottom.getText().toString().trim());
        paddingLeft = etLeft.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etLeft.getText().toString().trim());
        paddingRight = etRight.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etRight.getText().toString().trim());
    }

    @Override
    public void onMapReady(@NotNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
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

    /**
     * 根据building id 搜索building位置
     */
    protected void doSearchQuery() {
        DetailSearchOption detailSearchOption = new DetailSearchOption();
        detailSearchOption.id(buildingId);
        buildingSearch.searchBuildingDetail(detailSearchOption);
    }

    @Override
    public void onGetBuildingResult(BuildingResult buildingResult) {

    }

    @Override
    public void onGetBuildingDetailResult(BuildingDetailResult buildingDetailResult) {
        if (buildingDetailResult.status != 0) {
            Toast.makeText(this, buildingDetailResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (buildingDetailResult.getIndoorBuildingInfo() == null) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        IndoorBuildingInfo indoorBuildingInfo = buildingDetailResult.getIndoorBuildingInfo();

        switch (zoomMode) {
            case ZOOM_DISABLE:
                mapxusMap.selectBuilding(indoorBuildingInfo.getBuildingId(), floorName , MapxusMapZoomMode.ZoomDisable, Insets.NONE);
                break;
            case ZOOM_ANIMATED:
                mapxusMap.selectBuilding(indoorBuildingInfo.getBuildingId(), floorName ,MapxusMapZoomMode.ZoomAnimated, Insets.of(paddingLeft, paddingTop, paddingRight, paddingBottom));
                break;
            case ZOOM_DIRECT:
                mapxusMap.selectBuilding(indoorBuildingInfo.getBuildingId(), floorName ,MapxusMapZoomMode.ZoomDirect, Insets.of(paddingLeft, paddingTop, paddingRight, paddingBottom));
                break;
        }
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
    }
}