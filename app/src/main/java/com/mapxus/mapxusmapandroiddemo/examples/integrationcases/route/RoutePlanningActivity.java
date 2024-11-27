package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.route;


import static com.mapxus.mapxusmapandroiddemo.utils.LocalLanguageUtils.getLocalLanguage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.tabs.TabLayout;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.FollowUserMode;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.LatLng;
import com.mapxus.map.mapxusmap.api.map.model.MapxusPointAnnotationOptions;
import com.mapxus.map.mapxusmap.api.map.model.SelectorPosition;
import com.mapxus.map.mapxusmap.api.map.model.overlay.MapxusPointAnnotation;
import com.mapxus.map.mapxusmap.api.services.RoutePlanning;
import com.mapxus.map.mapxusmap.api.services.constant.RoutePlanningInstructionSign;
import com.mapxus.map.mapxusmap.api.services.constant.RoutePlanningVehicle;
import com.mapxus.map.mapxusmap.api.services.model.planning.InstructionDto;
import com.mapxus.map.mapxusmap.api.services.model.planning.PathDto;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningPoint;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningQueryRequest;
import com.mapxus.map.mapxusmap.api.services.model.planning.RouteResponseDto;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.map.mapxusmap.overlay.route.RoutePainter;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.adapter.InstructionsAdapter;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RoutePlanningActivity extends AppCompatActivity {

    private static final String TAG = RoutePlanningActivity.class.getSimpleName();

    //ui related
    private final Map<PointType, TextView> textViewMap = new EnumMap<>(PointType.class);
    private MapxusMap mMapxusMap;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;
    private final Map<PointType, RoutePlanningPoint> routePlanningPointMap = new EnumMap<>(PointType.class);
    private final Map<PointType, MapxusPointAnnotation> markers = new EnumMap<>(PointType.class);
    private final Map<String, String> floorIdToName = new HashMap<>();
    //map related
    private MapView mapView;
    //route planning search object
    private RoutePlanning routePlanning;
    private String vehicle = RoutePlanningVehicle.FOOT;
    private Button goBtn;
    private Button openInstructions;
    private RelativeLayout progressBarView;
    private RoutePainter routePainter;
    private InstructionsAdapter instructionsAdapter;
    private RouteResponseDto routeResponseDto = null;
    private PointType currentPointType = null;
    private final View.OnClickListener pointClickListener = v -> {
        switch (v.getId()) {
            case R.id.tv_start:
                currentPointType = PointType.Start;
                break;
            case R.id.tv_end:
                currentPointType = PointType.End;
                break;
            case R.id.tv_first_waypoint:
                currentPointType = PointType.First_Waypoint;
                break;
            case R.id.tv_second_waypoint:
                currentPointType = PointType.Second_Waypoint;
                break;
            case R.id.tv_third_waypoint:
                currentPointType = PointType.Third_Waypoint;
                break;
        }
        setUpPointTextView();
    };
    private final MapxusMap.OnIndoorPoiClickListener poiClickListener =
            poi -> responseMapClickEvent(
                    new LatLng(poi.getLatitude(), poi.getLongitude()),
                    poi.getFloor(),
                    poi.getFloorName()
            );
    private final MapxusMap.OnMapClickedListener mapClickListener =
            (latLng, floorInfo, indoorBuilding, venue) -> responseMapClickEvent(
                    latLng,
                    floorInfo != null ? floorInfo.getId() : null,
                    floorInfo != null ? floorInfo.getCode() : null
            );
    private final View.OnClickListener planningBtnClickListener = v -> {
        RoutePlanningPoint origin = routePlanningPointMap.get(PointType.Start);
        RoutePlanningPoint destination = routePlanningPointMap.get(PointType.End);

        if (origin == null || destination == null) {
            Toast.makeText(this, "Please select point", Toast.LENGTH_SHORT).show();
            return;
        } else {
            currentPointType = null;
            setUpPointTextView();
        }
        progressBarView.setVisibility(View.VISIBLE);

        RoutePlanningQueryRequest queryRequest = new RoutePlanningQueryRequest();
        queryRequest.setVehicle(vehicle);
        queryRequest.setLocale(getLocalLanguage());
        queryRequest.setPoints(new ArrayList<>(routePlanningPointMap.values()));

        routePlanning.route(queryRequest, routePlanningResult -> {
            progressBarView.setVisibility(View.GONE);
            if (routePlanningResult.status != 0 || routePlanningResult.getRouteResponseDto() == null) {
                Toast.makeText(this, routePlanningResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            routeResponseDto = routePlanningResult.getRouteResponseDto();
            drawRoute(routeResponseDto);
            instructionsAdapter = new InstructionsAdapter(routeResponseDto.getPaths().get(0).getInstructions());
            openInstructions.setEnabled(true);
        });
    };
    //positioning object
    private MapxusNavigationPositioningProvider mapxusPositioningProvider;
    private final View.OnClickListener goBtnClickListener = v -> {
        if (goBtn.getText().toString().equals(getString(R.string.go))) {
            goButtonClicked();
        } else {
            stopButtoncCicked();
        }
    };

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

    private void stopButtoncCicked() {
        goBtn.setText(R.string.go);
        mapxusPositioningProvider.routeAdsorber = null;
        mMapxusMap.addOnMapClickedListener(mapClickListener);
        mMapxusMap.addOnIndoorPoiClickListener(poiClickListener);
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

        for (PathDto pathDto : routeResponseDto.getPaths()) {
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

    private void goButtonClicked() {
        //if routeResponseDto is null, it means the route is not planned
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
        //update the path when you are moving
        mapxusPositioningProvider.updatePath(routeResponseDto.getPaths().get(0), mapboxMap);
        mapxusPositioningProvider.setOnPathChange(pathDto -> {
            //update progress when path changing
            if (pathDto != null) {
                if (pathDto.getDistance() <= 5.0) {
                    //reach the destination
                    mapxusPositioningProvider.setOnPathChange(null);
                    routePainter.cleanRoute();
                    Toast.makeText(RoutePlanningActivity.this, getString(R.string.reach_toast_text), Toast.LENGTH_SHORT).show();
                    instructionsAdapter.notifyCurrentPosition(routeResponseDto.getPaths().get(0).getInstructions().size() - 1);
                    mapxusPositioningProvider.routeAdsorber = null;
                } else {
                    InstructionDto currentInstruction = pathDto.getInstructions().get(0);
                    if (currentInstruction.getSign() == RoutePlanningInstructionSign.REACHED_VIA) {
                        Toast.makeText(this, currentInstruction.getText(), Toast.LENGTH_SHORT).show();
                    }

                    int full = routeResponseDto.getPaths().get(0).getInstructions().size();
                    int current = pathDto.getInstructions().size();
                    instructionsAdapter.notifyCurrentPosition(full - current);
                }
            }
        });
        mMapxusMap.removeOnMapClickedListener(mapClickListener);
        mMapxusMap.removeOnIndoorPoiClickListener(poiClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_route_planning);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(mapbox -> {
            RoutePlanningActivity.this.mapboxMap = mapbox;
            mapboxMap.getUiSettings().setCompassEnabled(false);
        });

        mapViewProvider.getMapxusMapAsync(mapxusMap -> {
            this.mMapxusMap = mapxusMap;
            mMapxusMap.getMapxusUiSettings().setSelectorPosition(SelectorPosition.CENTER_RIGHT);
            mMapxusMap.getMapxusUiSettings().setSelectorCollapse(true);
            mMapxusMap.setLocationProvider(mapxusPositioningProvider);
            mMapxusMap.setFollowUserMode(FollowUserMode.NONE);
            mMapxusMap.addOnFollowUserModeChangedListener(i -> mapxusPositioningProvider.isInHeadingMode = i == FollowUserMode.FOLLOW_USER_AND_HEADING);
            mMapxusMap.addOnMapClickedListener(mapClickListener);
            mMapxusMap.addOnIndoorPoiClickListener(poiClickListener);
        });
        progressBarView = findViewById(R.id.loding_view);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        vehicle = RoutePlanningVehicle.FOOT;
                        break;
                    case 1:
                        vehicle = RoutePlanningVehicle.WHEELCHAIR;
                        break;
                    default:
                        vehicle = RoutePlanningVehicle.EMERGENCY;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //ignore
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //ignore
            }
        });

        TextView tvStart = findViewById(R.id.tv_start);
        TextView tvEnd = findViewById(R.id.tv_end);
        TextView tvFirstWaypoint = findViewById(R.id.tv_first_waypoint);
        TextView tvSecondWayput = findViewById(R.id.tv_second_waypoint);
        TextView tvThirdWaypoint = findViewById(R.id.tv_third_waypoint);
        textViewMap.put(PointType.Start, tvStart);
        textViewMap.put(PointType.End, tvEnd);
        textViewMap.put(PointType.First_Waypoint, tvFirstWaypoint);
        textViewMap.put(PointType.Second_Waypoint, tvSecondWayput);
        textViewMap.put(PointType.Third_Waypoint, tvThirdWaypoint);

        tvStart.setOnClickListener(pointClickListener);
        tvEnd.setOnClickListener(pointClickListener);
        tvFirstWaypoint.setOnClickListener(pointClickListener);
        tvSecondWayput.setOnClickListener(pointClickListener);
        tvThirdWaypoint.setOnClickListener(pointClickListener);

        findViewById(R.id.btn_search).setOnClickListener(planningBtnClickListener);

        openInstructions = findViewById(R.id.open_instructions);
        openInstructions.setOnClickListener(v -> initBottomSheetDialog());

        goBtn = findViewById(R.id.btn_go);
        goBtn.setOnClickListener(goBtnClickListener);

        routePlanning = RoutePlanning.newInstance();

        mapxusPositioningProvider = new MapxusNavigationPositioningProvider(this, getApplicationContext());
    }

    //draw route result  on the map
    private void drawRoute(RouteResponseDto route) {
        mMapxusMap.removeMapxusPointAnnotations();
        markers.clear();

        routePainter = new RoutePainter(this, mapboxMap, mMapxusMap);
        routePainter.paintRouteUsingResult(route);
        Log.i(TAG, "drawRoute: " + routePainter.getPainterPathDto());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (routePainter != null) {
            routePainter.cleanRoute();
        }
        if (routePlanning != null) {
            routePlanning.destroy();
        }
        if (mapViewProvider != null) {
            mapViewProvider.onDestroy();
        }
    }

    private void responseMapClickEvent(
            LatLng latLng,
            @Nullable String floorId,
            String floorName
    ) {
        floorIdToName.put(floorId, floorName);
        if (currentPointType != null) {
            routePlanningPointMap.put(
                    currentPointType,
                    new RoutePlanningPoint(latLng.getLongitude(), latLng.getLatitude(), floorId)
            );

            markers.computeIfPresent(currentPointType, (k, v) -> {
                mMapxusMap.removeMapxusPointAnnotation(v);
                return null;
            });
            markers.put(
                    currentPointType,
                    mMapxusMap.addMapxusPointAnnotation(
                            getMapxusMarkerOptions(latLng, floorId)
                    ));
        }
    }

    private void setUpPointTextView() {
        textViewMap.forEach((pointType, textView) -> {
            String result;
            if (currentPointType != pointType) {
                RoutePlanningPoint routePlanningPoint = routePlanningPointMap.get(pointType);
                if (routePlanningPoint != null) {
                    result = formatPointText(routePlanningPoint);
                } else {
                    result = pointType.name().replace("_", "");
                }
            } else {
                result = getString(R.string.tap_screen_for, currentPointType.name().replace("_", ""));
            }
            textView.setText(result);
        });
    }

    private String formatPointText(RoutePlanningPoint point) {
        return String.format("%s,%s %s", doubleToString(point.getLat()), doubleToString(point.getLon()), floorIdToName.get(point.getFloorId()));
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

    private enum PointType {
        Start, First_Waypoint, Second_Waypoint, Third_Waypoint, End
    }
}