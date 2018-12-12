package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.constant.LatLngConstant;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyIndoorBuildingOverlay;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.map.model.LatLng;
import com.mapxus.services.BuildingSearch;
import com.mapxus.services.model.NearbySearchOption;
import com.mapxus.services.model.building.BuildingDetailResult;
import com.mapxus.services.model.building.BuildingResult;

/**
 * Use MapxusMap Search Services to request directions
 */
public class SearchBuildingNearbyActivity extends AppCompatActivity implements OnMapReadyCallback, BuildingSearch.BuildingSearchResultListener {

    private static final String TAG = "SearchBuildingNearbyActivity";

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;

    private RelativeLayout mPoiDetail;
    private TextView mPoiName, mPoiAddress;
    private String keyWord = "";
    private EditText mSearchText;

    private BuildingSearch buildingSearch;
    private MyIndoorBuildingOverlay indoorBuildingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_search_building_nearby);
        // Setup the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);


        TextView searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearchQuery();
            }
        });


        mPoiDetail = findViewById(R.id.poi_detail);
        mPoiName = findViewById(R.id.poi_name);
        mPoiAddress = findViewById(R.id.poi_address);
        mSearchText = findViewById(R.id.input_edittext);

        buildingSearch = BuildingSearch.newInstance();
        buildingSearch.setBuildingSearchResultListener(this);

        Toast.makeText(this, getString(R.string.building_nearby_toast), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the directions API request
        if (buildingSearch != null) {
            buildingSearch.destroy();
        }
        mapView.onDestroy();
        mapViewProvider.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * 开始进行poi搜索
     */
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        keyWord = mSearchText.getText().toString().trim();
        NearbySearchOption nearbySearchOption = new NearbySearchOption();
        nearbySearchOption.mRadius = 2;
        com.mapbox.mapboxsdk.geometry.LatLng mapCenter = mapboxMap.getCameraPosition().target;
        nearbySearchOption.location(new LatLng(
                mapCenter.getLatitude(),
                mapCenter.getLongitude()));
        nearbySearchOption.keyword(keyWord);
        buildingSearch.searchNearby(nearbySearchOption);
    }

    @Override
    public void onGetBuildingResult(BuildingResult buildingResult) {

        if (buildingResult.status != 0) {
            Toast.makeText(this, buildingResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (buildingResult.getIndoorBuildingList() == null || buildingResult.getIndoorBuildingList().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        mapboxMap.clear();
        indoorBuildingOverlay = new MyIndoorBuildingOverlay(mapboxMap, buildingResult.getIndoorBuildingList());
        indoorBuildingOverlay.addToMap();
        indoorBuildingOverlay.zoomToSpan();

    }

    @Override
    public void onGetBuildingDetailResult(BuildingDetailResult buildingDetailResult) {

    }


}



