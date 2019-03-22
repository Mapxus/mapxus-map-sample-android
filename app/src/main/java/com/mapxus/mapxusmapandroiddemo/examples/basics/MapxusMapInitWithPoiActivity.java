package com.mapxus.mapxusmapandroiddemo.examples.basics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.model.LatLng;
import com.mapxus.map.model.MapxusMapOptions;
import com.mapxus.map.model.MapxusMarkerOptions;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.constant.LatLngConstant;
import com.mapxus.services.PoiSearch;
import com.mapxus.services.model.DetailSearchOption;
import com.mapxus.services.model.poi.PoiCategoryResult;
import com.mapxus.services.model.poi.PoiDetailResult;
import com.mapxus.services.model.poi.PoiInfo;
import com.mapxus.services.model.poi.PoiResult;

/**
 * Add a map view in a dynamically created layout
 */
public class MapxusMapInitWithPoiActivity extends AppCompatActivity implements OnMapxusMapReadyCallback {

    private MapViewProvider mapViewProvider;
    private MapView mapboxMapView;
    private MapxusMap mapxusMap;

    private static final String POI_ID = "74233";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_init_with_building);
        mapboxMapView = (MapView) findViewById(R.id.mapView);
        MapxusMapOptions mapxusMapOptions = new MapxusMapOptions().setPoiId(POI_ID);
        mapViewProvider = new MapboxMapViewProvider(this, mapboxMapView, mapxusMapOptions);

        mapViewProvider.getMapxusMapAsync(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        mapboxMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapboxMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapboxMapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapboxMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapboxMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapboxMapView.onDestroy();
        mapViewProvider.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapboxMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {

        this.mapxusMap = mapxusMap;

        PoiSearch poiSearch = PoiSearch.newInstance();

        DetailSearchOption detailSearchOption = new DetailSearchOption();
        detailSearchOption.id(POI_ID);

        poiSearch.setPoiSearchResultListener(poiSearchResultListener);

        poiSearch.searchPoiDetail(detailSearchOption);


    }

    private PoiSearch.PoiSearchResultListener poiSearchResultListener = new PoiSearch.PoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            if (poiDetailResult != null && poiDetailResult.getPoiInfo() != null) {
                PoiInfo poiInfo = poiDetailResult.getPoiInfo();
                MapxusMarkerOptions mapxusMarkerOptions = new MapxusMarkerOptions();
                mapxusMarkerOptions.setFloor(poiInfo.getFloor());
                mapxusMarkerOptions.setBuildingId(poiInfo.getBuildingId());
                mapxusMarkerOptions.setPosition(new LatLng(poiInfo.getLocation().getLat(), poiInfo.getLocation().getLon()));

                mapxusMap.addMarker(mapxusMarkerOptions);

            }
        }

        @Override
        public void onPoiCategoriesResult(PoiCategoryResult poiCategoryResult) {

        }
    };
}
