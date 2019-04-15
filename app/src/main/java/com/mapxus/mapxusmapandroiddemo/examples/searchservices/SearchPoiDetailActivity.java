package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.model.overlay.ObjectMarker;
import com.mapxus.mapxusmapandroiddemo.model.overlay.ObjectMarkerOptions;
import com.mapxus.services.PoiSearch;
import com.mapxus.services.model.DetailSearchOption;
import com.mapxus.services.model.poi.PoiCategoryResult;
import com.mapxus.services.model.poi.PoiDetailResult;
import com.mapxus.services.model.poi.PoiInfo;
import com.mapxus.services.model.poi.PoiOrientationResult;
import com.mapxus.services.model.poi.PoiResult;

/**
 * Use MapxusMap Search Services to request directions
 */
public class SearchPoiDetailActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMarkerClickListener {

    private static final String TAG = "SearchPoiDetailActivity";

    private MapView mapView;
    private MapboxMap mapboxMap;

    private MapViewProvider mapViewProvider;
    private RelativeLayout mBuildingDetail;
    private TextView mBuildingName, mBuildingAddress;
    private String keyWord = "";
    private EditText mSearchText;

    private PoiSearch poiSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_search_poi_detail);


        // Setup the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);


        TextView searchButton = (TextView) findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_search:
                        doSearchQuery();
                        break;

                    default:
                        break;
                }
            }
        });


        mBuildingDetail = (RelativeLayout) findViewById(R.id.poi_detail);
        mBuildingName = (TextView) findViewById(R.id.poi_name);
        mBuildingAddress = (TextView) findViewById(R.id.poi_address);
        mSearchText = (EditText) findViewById(R.id.input_edittext);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(searchResultListenerAdapter);

    }

    @Override
    public void onMapReady(MapboxMap beeMap) {
        mapboxMap = beeMap;
        mapboxMap.setOnMarkerClickListener(this);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the directions API request
        if (poiSearch != null) {
            poiSearch.destroy();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * 开始进行poi搜索, 根据ID查询详细POI信息
     */
    protected void doSearchQuery() {
        keyWord = mSearchText.getText().toString().trim();
        DetailSearchOption detailSearchOption = new DetailSearchOption();
        detailSearchOption.id(keyWord);
        poiSearch.searchPoiDetail(detailSearchOption);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        ObjectMarker objectMarker = (ObjectMarker) marker;
        PoiInfo poiInfo = (PoiInfo) objectMarker.getObject();
        mBuildingName.setText(poiInfo.getName().get("default"));
        mBuildingAddress.setText(poiInfo.getBuildingId() + "," + poiInfo.getFloor());
        mBuildingDetail.setVisibility(View.VISIBLE);
        return true;
    }

    private PoiSearch.PoiSearchResultListenerAdapter searchResultListenerAdapter = new PoiSearch.PoiSearchResultListenerAdapter() {
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            if (poiDetailResult.status != 0) {
                Toast.makeText(SearchPoiDetailActivity.this, poiDetailResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (poiDetailResult.getPoiInfo() == null) {
                Toast.makeText(SearchPoiDetailActivity.this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
                return;
            }

            mapboxMap.clear();
            PoiInfo poiInfo = poiDetailResult.getPoiInfo();

            Marker marker = mapboxMap.addMarker(new ObjectMarkerOptions()
                    .position(
                            new LatLng(poiInfo.getLocation()
                                    .getLat(), poiInfo
                                    .getLocation().getLon()))
                    .title(poiInfo.getName().get("default")).snippet("buildingId:" + poiInfo.getBuildingId())
                    .object(poiInfo));
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 19));
        }
    };

//    @Override
//    public void onGetPoiResult(PoiResult poiResult) {
//
//    }
//
//    @Override
//    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//        if (poiDetailResult.status != 0) {
//            Toast.makeText(this, poiDetailResult.error.toString(), Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (poiDetailResult.getPoiInfo() == null) {
//            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        mapboxMap.clear();
//        PoiInfo poiInfo = poiDetailResult.getPoiInfo();
//
//        Marker marker = mapboxMap.addMarker(new ObjectMarkerOptions()
//                .position(
//                        new LatLng(poiInfo.getLocation()
//                                .getLat(), poiInfo
//                                .getLocation().getLon()))
//                .title(poiInfo.getName().get("default")).snippet("buildingId:" + poiInfo.getBuildingId())
//                .object(poiInfo));
//        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 19));
//    }
//
//    @Override
//    public void onGetPoiByOrientationResult(PoiOrientationResult poiOrientationResult) {
//
//    }
//
//    @Override
//    public void onPoiCategoriesResult(PoiCategoryResult poiCategoryResult) {
//
//    }
}



