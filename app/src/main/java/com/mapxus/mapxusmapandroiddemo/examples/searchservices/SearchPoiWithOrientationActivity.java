package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.LatLng;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMarkerOptions;
import com.mapxus.map.mapxusmap.api.map.model.overlay.MapxusMarker;
import com.mapxus.map.mapxusmap.api.services.PoiSearch;
import com.mapxus.map.mapxusmap.api.services.constant.DistanceSearchType;
import com.mapxus.map.mapxusmap.api.services.model.IndoorLatLng;
import com.mapxus.map.mapxusmap.api.services.model.PoiOrientationSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiOrientationInfo;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiOrientationResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SearchPoiWithOrientationActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapxusMapReadyCallback {

    private static final String TAG = "SearchPoiByOrientationActivity";

    private MapView mapView;
    private MapboxMap mMapboxMap;
    private MapViewProvider mapViewProvider;
    private MapxusMap mMapxusMap;

    private EditText mSearchText;

    //搜索类型
    private String searchType = DistanceSearchType.POINT;

    //搜索对象
    private PoiSearch poiSearch;

    //中心点marker
    private MapxusMarker centerMarker;

    //中心点坐标
    private IndoorLatLng indoorLatLng;

    //搜索半径
    private int radius;

    //圆圈
    private Polyline circleLine;

    //查询结果poi列表
    private List<MapxusMarker> markerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_poi_with_orientation);

        // Setup the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(this);

        mSearchText = findViewById(R.id.input_edittext);

        updateRadius();


        TextView searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearchQuery();
            }
        });

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(adapter);
    }


    //radio 选择搜索类型
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.btn_point:
                if (checked)
                    searchType = DistanceSearchType.POINT;
                break;
            case R.id.btn_polygon:
                if (checked)
                    searchType = DistanceSearchType.POLYGON;
                break;
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        if (null == indoorLatLng) {
            Toast.makeText(SearchPoiWithOrientationActivity.this, "请点击地图进行选点", Toast.LENGTH_LONG).show();
            return;
        }
        updateRadius();
        PoiOrientationSearchOption option = new PoiOrientationSearchOption();

        //方向0度，正北方向
        option.orientation(0);
        option.indoorLatLng(indoorLatLng);
        option.meterRadius(radius);
        option.searchType(searchType);
        poiSearch.searchPoiByOrientation(option);
    }

    private PoiSearch.PoiSearchResultListenerAdapter adapter = new PoiSearch.PoiSearchResultListenerAdapter() {
        @Override
        public void onGetPoiByOrientationResult(PoiOrientationResult poiOrientationResult) {
            //通过朝向查找poi结果返回
            if (poiOrientationResult.status != 0) {
                Toast.makeText(getApplicationContext(), poiOrientationResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (poiOrientationResult.getPoiOrientationInfos() == null || poiOrientationResult.getPoiOrientationInfos().isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.no_result), Toast.LENGTH_LONG).show();
                return;
            }

            removeMarkerLayer();

            for (PoiOrientationInfo info : poiOrientationResult.getPoiOrientationInfos()) {
                drawPoiMarker(info);
            }
        }
    };

    /**
     * 清除图标
     */
    private void removeMarkerLayer() {
        for (MapxusMarker marker : markerList) {
            mMapxusMap.removeMarker(marker);
        }

        markerList.clear();
    }

    private void drawPoiMarker(PoiOrientationInfo poi) {
        markerList.add(mMapxusMap.addMarker(new MapxusMarkerOptions()
                .setBuildingId(poi.getBuildingId())
                .setFloor(poi.getFloor())
                .setPosition(new LatLng(poi.getLocation().getLat(), poi.getLocation().getLon()))
                .setTitle(poi.getName().get("default"))
                .setSnippet("source=from " + poi.getDistanceSource() + " ,distance=" + poi.getDistance() + " ,angle=" + poi.getAngle())
                .setIcon(R.drawable.purple_marker)));
    }


    private MapxusMap.OnMapClickListener choosePointMapClickListener = new MapxusMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng, String floor, String buildingId, String floorId) {
            updateRadius();

            indoorLatLng = new IndoorLatLng(latLng.getLatitude(), latLng.getLongitude(), floor, buildingId);
            if (centerMarker != null) {
                mMapxusMap.removeMarker(centerMarker);
                centerMarker = null;
            }
            MapxusMarkerOptions mapxusMarkerOptions = new MapxusMarkerOptions();
            mapxusMarkerOptions.setPosition(latLng);
            mapxusMarkerOptions.setBuildingId(buildingId);
            mapxusMarkerOptions.setFloor(floor);
            centerMarker = mMapxusMap.addMarker(mapxusMarkerOptions);

            if (null != circleLine) {
                mMapboxMap.removePolyline(circleLine);
                circleLine = null;
            }

            drawCircle();
        }
    };

    //更新搜索半径
    private void updateRadius() {
        radius = Integer.valueOf(mSearchText.getText().toString().trim());
    }

    //画圆圈
    public void drawCircle() {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(R.color.grey);
        polylineOptions.width(0.5f); // change the line width here
        polylineOptions.addAll(getCirclePoints());
        circleLine = mMapboxMap.addPolyline(polylineOptions);
    }

    //计算圆上的点
    private ArrayList<com.mapbox.mapboxsdk.geometry.LatLng> getCirclePoints() {
        int degreesBetweenPoints = 10; // change here for shape
        int numberOfPoints = (int) Math.floor(360 / degreesBetweenPoints);
        double distRadians = radius / 6371000.0; // earth radius in meters
        double centerLatRadians = indoorLatLng.getLat() * Math.PI / 180;
        double centerLonRadians = indoorLatLng.getLon() * Math.PI / 180;
        ArrayList<com.mapbox.mapboxsdk.geometry.LatLng> polygons = new ArrayList<>(); // array to hold all the points
        for (int index = 0; index < numberOfPoints; index++) {
            double degrees = index * degreesBetweenPoints;
            double degreeRadians = degrees * Math.PI / 180;
            double pointLatRadians = Math.asin(sin(centerLatRadians) * cos(distRadians)
                    + cos(centerLatRadians) * sin(distRadians) * cos(degreeRadians));
            double pointLonRadians = centerLonRadians + Math.atan2(sin(degreeRadians)
                            * sin(distRadians) * cos(centerLatRadians),
                    cos(distRadians) - sin(centerLatRadians) * sin(pointLatRadians));
            double pointLat = pointLatRadians * 180 / Math.PI;
            double pointLon = pointLonRadians * 180 / Math.PI;
            com.mapbox.mapboxsdk.geometry.LatLng point = new com.mapbox.mapboxsdk.geometry.LatLng(pointLat, pointLon);
            polygons.add(point);
        }
        // add first point at end to close circle
        polygons.add(polygons.get(0));
        return polygons;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        mMapxusMap = mapxusMap;
        mMapxusMap.addOnMapClickListener(choosePointMapClickListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
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
    }
}
