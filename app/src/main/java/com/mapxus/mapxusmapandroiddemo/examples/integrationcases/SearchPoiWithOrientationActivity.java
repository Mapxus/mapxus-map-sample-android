package com.mapxus.mapxusmapandroiddemo.examples.integrationcases;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.FollowUserMode;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.MapxusMapZoomMode;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.services.PoiSearch;
import com.mapxus.map.mapxusmap.api.services.constant.DistanceSearchType;
import com.mapxus.map.mapxusmap.api.services.model.IndoorLatLng;
import com.mapxus.map.mapxusmap.api.services.model.PoiOrientationSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.building.FloorInfo;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiOrientationResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.map.mapxusmap.positioning.ErrorInfo;
import com.mapxus.map.mapxusmap.positioning.IndoorLocation;
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProviderListener;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.location.FakePositioningProvider;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyPoiOrientationOverlay;

import org.jetbrains.annotations.NotNull;

public class SearchPoiWithOrientationActivity extends BaseWithParamMenuActivity implements OnMapReadyCallback, View.OnClickListener, OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapboxMap mMapboxMap;
    private MapxusMap mapxusMap;
    private MapboxMapViewProvider mapViewProvider;

    private PoiSearch poiSearch;
    private RelativeLayout progressBarView;

    private int angle, radius;
    private Integer ordinal;
    private IndoorLatLng indoorLatLng;
    private String searchType;
    private Button btnSearchType;

    private IndoorBuilding indoorBuilding;

    private FakePositioningProvider indoorLocationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_poi_with_orientation);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(this);
        progressBarView = findViewById(R.id.loding_view);

        findViewById(R.id.btn_search).setOnClickListener(this);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(poiSearchResultListenerAdapter);

        indoorLocationProvider = new FakePositioningProvider(this, this);
    }

    protected void doSearchQuery(int angle, Integer ordinal, IndoorLatLng indoorLatLng, int radius,
                                 String searchType) {
        PoiOrientationSearchOption option = new PoiOrientationSearchOption();
        option.orientation(angle);
        option.orientation(angle);
        option.mOrdinal = ordinal;
        option.indoorLatLng(indoorLatLng);
        option.meterRadius(radius);
        option.searchType(searchType);
        poiSearch.searchPoiByOrientation(option);
    }

    private PoiSearch.PoiSearchResultListenerAdapter poiSearchResultListenerAdapter = new PoiSearch.PoiSearchResultListenerAdapter() {
        @Override
        public void onGetPoiByOrientationResult(PoiOrientationResult poiOrientationResult) {
            progressBarView.setVisibility(View.GONE);
            if (poiOrientationResult.status != 0) {
                Toast.makeText(getApplicationContext(), poiOrientationResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (poiOrientationResult.getPoiOrientationInfos() == null || poiOrientationResult.getPoiOrientationInfos().isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.no_result), Toast.LENGTH_LONG).show();
                return;
            }
            MyPoiOrientationOverlay poiOverlay = new MyPoiOrientationOverlay(mMapboxMap, mapxusMap, poiOrientationResult.getPoiOrientationInfos());
            poiOverlay.removeFromMap();
            poiOverlay.addToMap();
            poiOverlay.zoomToSpan(Double.parseDouble(getString(R.string.default_zoom_level_value)));
        }
    };


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
        if (mapxusMap != null) {
            mapxusMap.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (mapxusMap != null) {
            mapxusMap.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
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
        if (mapViewProvider != null) {
            mapViewProvider.onDestroy();
        }
    }

    @Override
    protected void initBottomSheetDialog() {
        ordinal = null;

        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_orientation_search_style, this);
        btnSearchType = bottomSheetDialogView.findViewById(R.id.btn_search_type);
        btnSearchType.setOnClickListener(v -> {
            if (btnSearchType.getText().toString().equals(getString(R.string.point))) {
                btnSearchType.setText(getString(R.string.polygon));
                searchType = DistanceSearchType.POLYGON;
            } else if (btnSearchType.getText().toString().equals(getString(R.string.polygon))) {
                btnSearchType.setText(getString(R.string.gate));
                searchType = DistanceSearchType.GATE;
            } else {
                btnSearchType.setText(getString(R.string.point));
                searchType = DistanceSearchType.POINT;
            }
        });
        bottomSheetDialogView.findViewById(R.id.create).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            getValue(bottomSheetDialogView);
        });
    }

    private void getValue(View bottomSheetDialogView) {
        if (indoorBuilding == null) {
            Toast.makeText(this, "can not get building info.", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText etLat = bottomSheetDialogView.findViewById(R.id.et_lat);
        EditText etLon = bottomSheetDialogView.findViewById(R.id.et_lon);
        EditText etBuildingId = bottomSheetDialogView.findViewById(R.id.et_building_id);
        EditText etFloorId = bottomSheetDialogView.findViewById(R.id.et_floor_id);
        EditText etDistance = bottomSheetDialogView.findViewById(R.id.et_distance);
        EditText etOrdinal = bottomSheetDialogView.findViewById(R.id.et_ordinal);

        String floorId = etFloorId.getText().toString().trim();
        String buildingId = etBuildingId.getText().toString().trim();

        if (!TextUtils.isEmpty(etOrdinal.getText().toString())) {
            try {
                ordinal = Integer.parseInt(etOrdinal.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Ordinal Not a Number", Toast.LENGTH_SHORT).show();
            }
        }

        indoorLatLng = new IndoorLatLng();
        indoorLatLng.setBuildingId(buildingId);
        indoorLatLng.setFloorId(floorId);
        indoorLatLng.setLat(etLat.getText().toString().isEmpty() ? 0 : Double.parseDouble(etLat.getText().toString().trim()));
        indoorLatLng.setLon(etLon.getText().toString().isEmpty() ? 0 : Double.parseDouble(etLon.getText().toString().trim()));
        radius = etDistance.getText().toString().isEmpty() ? 0 : Integer.parseInt(etDistance.getText().toString().trim());

        mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(indoorLatLng.getLat(),
                indoorLatLng.getLon()), 17), new MapboxMap.CancelableCallback() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish() {
                new Handler().postDelayed(() -> {
                    if (!buildingId.isEmpty()) {
                        mapxusMap.selectBuildingById(buildingId, MapxusMapZoomMode.ZoomDisable, null);
                    }
                    FloorInfo floorInfoValue = null;

                    if (ordinal != null) {
                        IndoorBuilding building = mapxusMap.getBuildings().get(buildingId);
                        if (building != null){
                            for (FloorInfo floorInfo : building.getFloors()) {
                                if (ordinal == floorInfo.getOrdinal()) {
                                    floorInfoValue = floorInfo;
                                }
                            }
                        }
                    } else if (!TextUtils.isEmpty(floorId)) {
                        String floorName = "";
                        int ordinalValue = 0;

                        for (FloorInfo floorInfo : indoorBuilding.getFloors()) {
                            if (floorId.equals(floorInfo.getId())) {
                                floorName = floorInfo.getCode();
                                ordinalValue = floorInfo.getOrdinal();
                            }
                        }
                        floorInfoValue = new FloorInfo(floorId, floorName, ordinalValue);
                    }

                    String buildingIdValue = buildingId;
                    if (TextUtils.isEmpty(buildingIdValue) && indoorBuilding != null) {
                        buildingIdValue = indoorBuilding.getBuildingId();
                    }

                    IndoorLocation indoorLocation = new IndoorLocation("Fake", floorInfoValue,
                            floorInfoValue == null ? null : buildingIdValue,
                            indoorLatLng.getLat(), indoorLatLng.getLon(), System.currentTimeMillis());
                    mapxusMap.setLocationProvider(indoorLocationProvider);
                    mapxusMap.setFollowUserMode(FollowUserMode.FOLLOW_USER);
                    indoorLocationProvider.setIndoorLocation(indoorLocation);

                }, 2000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (indoorLatLng != null) {
            progressBarView.setVisibility(View.VISIBLE);
            doSearchQuery(angle, ordinal, indoorLatLng, radius, searchType);
        } else {
            Toast.makeText(this, "Pleas set params first.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
        indoorLocationProvider.addListener(new IndoorLocationProviderListener() {
            @Override
            public void onProviderStarted() {
            }

            @Override
            public void onProviderStopped() {
            }

            @Override
            public void onProviderError(ErrorInfo error) {
                Toast.makeText(SearchPoiWithOrientationActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIndoorLocationChange(IndoorLocation indoorLocation) {
            }

            @Override
            public void onCompassChanged(float angle, int sensorAccuracy) {
                SearchPoiWithOrientationActivity.this.angle = (int) angle;
            }
        });

        mapxusMap.addOnFloorChangedListener((venue, indoorBuilding, floorInfo) -> SearchPoiWithOrientationActivity.this.indoorBuilding = indoorBuilding);
    }
}
