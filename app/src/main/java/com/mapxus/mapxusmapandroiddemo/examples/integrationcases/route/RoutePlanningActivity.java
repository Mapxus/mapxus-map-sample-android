package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.route;


import static com.mapxus.mapxusmapandroiddemo.utils.LocalLanguageUtils.getLocalLanguage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.FollowUserMode;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.map.model.LatLng;
import com.mapxus.map.mapxusmap.api.map.model.MapxusPointAnnotationOptions;
import com.mapxus.map.mapxusmap.api.map.model.Poi;
import com.mapxus.map.mapxusmap.api.map.model.SelectorPosition;
import com.mapxus.map.mapxusmap.api.map.model.Venue;
import com.mapxus.map.mapxusmap.api.map.model.overlay.MapxusPointAnnotation;
import com.mapxus.map.mapxusmap.api.services.RoutePlanning;
import com.mapxus.map.mapxusmap.api.services.constant.RoutePlanningVehicle;
import com.mapxus.map.mapxusmap.api.services.model.building.FloorInfo;
import com.mapxus.map.mapxusmap.api.services.model.planning.InstructionDto;
import com.mapxus.map.mapxusmap.api.services.model.planning.PathDto;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningPoint;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningRequest;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningResult;
import com.mapxus.map.mapxusmap.api.services.model.planning.RouteResponseDto;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.map.mapxusmap.overlay.route.RoutePainter;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.adapter.InstructionsAdapter;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.customizeview.SwitchView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RoutePlanningActivity extends AppCompatActivity implements RoutePlanning.RoutePlanningResultListener, OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapxusMap mMapxusMap;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;
    private RoutePlanning routePlanning;

    private RoutePlanningPoint origin, destination = null;

    private MapxusPointAnnotation startMarker, endMarker;

    private TextView tvStart, tvEnd;
    private final MapxusMap.OnMapClickedListener pointEndMapClickListener = new MapxusMap.OnMapClickedListener() {
        @Override
        public void onMapClick(@NonNull LatLng latLng, @Nullable FloorInfo floorInfo, @Nullable IndoorBuilding indoorBuilding, @Nullable Venue venue) {
            if (floorInfo != null) {
                endFloor = floorInfo.getCode();
            }
            destination = new RoutePlanningPoint(latLng.getLongitude(), latLng.getLatitude(), indoorBuilding != null ? indoorBuilding.getBuildingId() : null, floorInfo != null ? floorInfo.getId() : null);

            if (endMarker != null) {
                mMapxusMap.removeMapxusPointAnnotation(endMarker);
                endMarker = null;
            }
            endMarker = mMapxusMap.addMapxusPointAnnotation(getMapxusMarkerOptions(latLng, floorInfo != null ? floorInfo.getId() : null));
        }
    };
    private Button goBtn;
    private Button openInstructions;

    private String vehicle = RoutePlanningVehicle.FOOT;
    private boolean toDoor = false;
    private RelativeLayout progressBarView;

    private MapxusNavigationPositioningProvider mapxusPositioningProvider;
    private RoutePainter routePainter;
    private InstructionsAdapter instructionsAdapter;

    private RouteResponseDto routeResponseDto = null;
    private final View.OnClickListener goBtnClickListener = v -> {
        if (goBtn.getText().toString().equals(getString(R.string.go))) {
            if (routeResponseDto == null) {
                new MaterialDialog.Builder(this)
                        .title(getString(R.string.warning))
                        .content(getString(R.string.search_first))
                        .positiveText(getString(R.string.ok))
                        .onPositive((dialog, which) -> dialog.dismiss()).show();
                return;
            }
            goBtn.setText(R.string.stop);
            mMapxusMap.setFollowUserMode(FollowUserMode.FOLLOW_USER_AND_HEADING);
            mapxusPositioningProvider.updatePath(routeResponseDto.getPaths().get(0), mapboxMap);
            mapxusPositioningProvider.setOnReachListener(() -> {
                mapxusPositioningProvider.setOnPathChange(null);
                routePainter.cleanRoute();
                Toast.makeText(RoutePlanningActivity.this, getString(R.string.reach_toast_text), Toast.LENGTH_SHORT).show();
                instructionsAdapter.notifyCurrentPosition(routeResponseDto.getPaths().get(0).getInstructions().size() - 1);
                mapxusPositioningProvider.setRouteAdsorber(null);
            });
            mapxusPositioningProvider.setOnPathChange(pathDto -> {
                int size = routeResponseDto.getPaths().get(0).getInstructions().size();
                int size1 = pathDto.getInstructions().size();
                instructionsAdapter.notifyCurrentPosition(size - size1);
            });
        } else {
            goBtn.setText(R.string.go);
            mapxusPositioningProvider.setRouteAdsorber(null);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_route_planning);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(mapboxMap -> RoutePlanningActivity.this.mapboxMap = mapboxMap);


        mapViewProvider.getMapxusMapAsync(this);

        progressBarView = findViewById(R.id.loding_view);


        SwitchView switchView = findViewById(R.id.switch_view);
        switchView.setOnCheckChangedListener(checked -> vehicle = checked ? RoutePlanningVehicle.WHEELCHAIR : RoutePlanningVehicle.FOOT);

        SwitchCompat switchButton = findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> toDoor = isChecked);

        tvStart = findViewById(R.id.tv_start);
        tvEnd = findViewById(R.id.tv_end);
        tvStart.setOnClickListener(pointClickListener);
        tvEnd.setOnClickListener(pointClickListener);
        findViewById(R.id.btn_search).setOnClickListener(planningBtnClickListener);
        openInstructions = findViewById(R.id.open_instructions);
        openInstructions.setOnClickListener(v -> initBottomSheetDialog());
        goBtn = findViewById(R.id.btn_go);
        goBtn.setOnClickListener(goBtnClickListener);

        routePlanning = RoutePlanning.newInstance();
        routePlanning.setRoutePlanningListener(RoutePlanningActivity.this);

        mapxusPositioningProvider = new MapxusNavigationPositioningProvider(this, getApplicationContext());
    }

    private void getRoute() {
        RoutePlanningRequest request = new RoutePlanningRequest(origin, destination);
        request.setVehicle(vehicle);
        request.setToDoor(toDoor);
        request.setLocale(getLocalLanguage());
        routePlanning.route(request);
    }

    private void drawRoute(RouteResponseDto route) {
        if (startMarker != null) {
            mMapxusMap.removeMapxusPointAnnotation(startMarker);
            startMarker = null;
        }
        if (endMarker != null) {
            mMapxusMap.removeMapxusPointAnnotation(endMarker);
            endMarker = null;
        }
        routePainter = new RoutePainter(this, mapboxMap, mMapxusMap);
        routePainter.paintRouteUsingResult(route);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (mMapxusMap != null) {
            mMapxusMap.onResume();
        }
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
        if (mMapxusMap != null) {
            mMapxusMap.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (routePlanning != null) {
            routePlanning.destroy();
        }
        if (mapViewProvider != null) {
            mapViewProvider.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void initBottomSheetDialog() {

        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        bottomSheetDialog.getBehavior().setDraggable(false);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_instructions_style, this);
        bottomSheetDialogView.findViewById(R.id.iv_close).setOnClickListener(v -> bottomSheetDialog.dismiss());
        TextView tvTotalDistance = bottomSheetDialogView.findViewById(R.id.tv_total_distance);
        TextView tvTotalTime = bottomSheetDialogView.findViewById(R.id.tv_total_time);

        double _totalDistance = 0;
        long _totaltime = 0;

        List<InstructionDto> instructionDtos = new ArrayList<>();
        for (PathDto pathDto : routeResponseDto.getPaths()) {
            instructionDtos.addAll(pathDto.getInstructions());
            _totalDistance += pathDto.getDistance();
            _totaltime += pathDto.getTime();
        }


        tvTotalDistance.setText(String.format("%s m", (int) Math.floor(_totalDistance)));
        tvTotalTime.setText(String.format("%s min", TimeUnit.MILLISECONDS.toMinutes(_totaltime)));

        RecyclerView recyclerView = bottomSheetDialogView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(instructionsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener pointClickListener = v -> {
        switch (v.getId()) {
            case R.id.tv_start:
                pointStartClick();
                break;
            case R.id.tv_end:
                pointEndClick();
                break;
        }
    };

    //    private final MapxusMap.OnMapClickListener pointStartMapClickListener = new MapxusMap.OnMapClickListener() {
//        @Override
//        public void onMapClick(LatLng latLng, String floor, String buildingId, String floorId) {
//
//        }
//
//    };
    private final MapxusMap.OnIndoorPoiClickListener pointEndPoiClickListener = new MapxusMap.OnIndoorPoiClickListener() {
        @Override
        public void onIndoorPoiClick(Poi poi) {
            endFloor = poi.getFloorName();
            destination = new RoutePlanningPoint(poi.getLongitude(), poi.getLatitude(), poi.getBuildingId(), poi.getFloor());

            if (endMarker != null) {
                mMapxusMap.removeMapxusPointAnnotation(endMarker);
                endMarker = null;
            }
            endMarker = mMapxusMap.addMapxusPointAnnotation(getMapxusMarkerOptions(new LatLng(poi.getLatitude(), poi.getLongitude()), poi.getFloor()));
        }
    };
    private String startFloor, endFloor;
    private final MapxusMap.OnMapClickedListener pointStartMapClickListener = new MapxusMap.OnMapClickedListener() {

        @Override
        public void onMapClick(@NonNull LatLng latLng, @Nullable FloorInfo floorInfo, @Nullable IndoorBuilding indoorBuilding, @Nullable Venue venue) {
            if (floorInfo != null) {
                startFloor = floorInfo.getCode();
            }
            origin = new RoutePlanningPoint(latLng.getLongitude(), latLng.getLatitude(), indoorBuilding != null ? indoorBuilding.getBuildingId() : null, floorInfo != null ? floorInfo.getId() : null);
            if (startMarker != null) {
                mMapxusMap.removeMapxusPointAnnotation(startMarker);
                startMarker = null;
            }
            startMarker = mMapxusMap.addMapxusPointAnnotation(getMapxusMarkerOptions(latLng, floorInfo != null ? floorInfo.getId() : null));
        }

    };

    @Override
    public void onGetRoutePlanningResult(RoutePlanningResult routePlanningResult) {
        progressBarView.setVisibility(View.GONE);
        if (routePlanningResult.status != 0) {
            Toast.makeText(this, routePlanningResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (routePlanningResult.getRouteResponseDto() == null) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }
        routeResponseDto = routePlanningResult.getRouteResponseDto();
        drawRoute(routeResponseDto);
        instructionsAdapter = new InstructionsAdapter(routeResponseDto.getPaths().get(0).getInstructions());

        openInstructions.setEnabled(true);
    }

    private void removeAllSelectedMapClickListener() {
        mMapxusMap.removeOnMapClickedListener(pointEndMapClickListener);
        mMapxusMap.removeOnMapClickedListener(pointStartMapClickListener);
        mMapxusMap.removeOnIndoorPoiClickListener(pointStartPoiClickListener);
        mMapxusMap.removeOnIndoorPoiClickListener(pointEndPoiClickListener);
    }

    private final MapxusMap.OnIndoorPoiClickListener pointStartPoiClickListener = new MapxusMap.OnIndoorPoiClickListener() {
        @Override
        public void onIndoorPoiClick(Poi poi) {
            startFloor = poi.getFloorName();
            origin = new RoutePlanningPoint(poi.getLongitude(), poi.getLatitude(), poi.getBuildingId(), poi.getFloor());
            if (startMarker != null) {
                mMapxusMap.removeMapxusPointAnnotation(startMarker);
                startMarker = null;
            }
            startMarker = mMapxusMap.addMapxusPointAnnotation(getMapxusMarkerOptions(new LatLng(poi.getLatitude(), poi.getLongitude()), poi.getFloor()));
        }
    };
    private final View.OnClickListener planningBtnClickListener = v -> {
        if (origin == null || destination == null) {
            Toast.makeText(this, "Please select point", Toast.LENGTH_SHORT).show();
            return;
        } else {
            tvStart.setText(String.format("%s,%s %s", doubleToString(origin.getLat()), doubleToString(origin.getLon()), startFloor));
            tvEnd.setText(String.format("%s,%s %s", doubleToString(destination.getLat()), doubleToString(destination.getLon()), endFloor));
        }
        progressBarView.setVisibility(View.VISIBLE);
        removeAllSelectedMapClickListener();
        getRoute();
    };

    private void pointStartClick() {
        removeAllSelectedMapClickListener();
        mMapxusMap.addOnMapClickedListener(pointStartMapClickListener);
        mMapxusMap.addOnIndoorPoiClickListener(pointStartPoiClickListener);
        if (tvStart.getText().toString().equals(getString(R.string.start))) {
            tvStart.setText(R.string.tap_screen_for_start);
        } else {
            tvStart.setText(R.string.start);
        }

        if (destination != null) {
            tvEnd.setText(String.format("%s,%s %s", doubleToString(destination.getLat()), doubleToString(destination.getLon()), endFloor));
        } else {
            tvEnd.setText(R.string.end);
        }
    }

    private void pointEndClick() {
        removeAllSelectedMapClickListener();
        mMapxusMap.addOnMapClickedListener(pointEndMapClickListener);
        mMapxusMap.addOnIndoorPoiClickListener(pointEndPoiClickListener);
        if (tvEnd.getText().toString().equals(getString(R.string.end))) {
            tvEnd.setText(R.string.tap_screen_for_end);
        } else {
            tvEnd.setText(R.string.end);
        }

        if (origin != null) {
            tvStart.setText(String.format("%s,%s %s", doubleToString(origin.getLat()), doubleToString(origin.getLon()), startFloor));
        } else {
            tvStart.setText(R.string.start);
        }
    }

    @NotNull
    private MapxusPointAnnotationOptions getMapxusMarkerOptions(LatLng latLng, String floorId) {
        MapxusPointAnnotationOptions mapxusMarkerOptions = new MapxusPointAnnotationOptions();
        mapxusMarkerOptions.setPosition(latLng);
        mapxusMarkerOptions.setFloorId(floorId);
        return mapxusMarkerOptions;
    }

    private static String doubleToString(double num) {
        return new DecimalFormat("0.0000").format(num);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mMapxusMap = mapxusMap;
        mMapxusMap.getMapxusUiSettings().setSelectorPosition(SelectorPosition.CENTER_RIGHT);
        setLocation();
        mMapxusMap.addOnFollowUserModeChangedListener(i -> {
            if (i == FollowUserMode.FOLLOW_USER_AND_HEADING) {
                mapxusPositioningProvider.isInHeadingMode = true;
            } else {
                mapxusPositioningProvider.isInHeadingMode = false;
            }
        });
    }

    private void setLocation() {
        mMapxusMap.setLocationProvider(mapxusPositioningProvider);
        mMapxusMap.setFollowUserMode(FollowUserMode.NONE);
    }
}