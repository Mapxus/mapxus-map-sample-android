package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.services.BuildingSearch;
import com.mapxus.map.mapxusmap.api.services.model.DetailSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.building.Address;
import com.mapxus.map.mapxusmap.api.services.model.building.BuildingDetailResult;
import com.mapxus.map.mapxusmap.api.services.model.building.BuildingResult;
import com.mapxus.map.mapxusmap.api.services.model.building.IndoorBuildingInfo;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.model.overlay.ObjectMarker;
import com.mapxus.mapxusmapandroiddemo.model.overlay.ObjectMarkerOptions;

/**
 * Use MapxusMap Search Services to request directions
 */
public class SearchBuildingDetailActivity extends AppCompatActivity implements OnMapxusMapReadyCallback, BuildingSearch.BuildingSearchResultListener, MapboxMap.OnMarkerClickListener, OnMapReadyCallback {

    private static final String TAG = "SearchBuildingDetailActivity";

    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapboxMap mapboxMap;


    private RelativeLayout mBuildingDetail;
    private TextView mBuildingName, mBuildingAddress;
    private String keyWord = "";
    private EditText mSearchText;

    //    private LatLng startPoint = new LatLng(22.0360235, 113.18262645);
    private BuildingSearch buildingSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_search_building_detail);

        // Setup the MapView
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);

        mapViewProvider.getMapxusMapAsync(this);
        mapView.getMapAsync(this);

        TextView searchButton = (TextView) findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearchQuery();
            }
        });


        mBuildingDetail = (RelativeLayout) findViewById(R.id.poi_detail);
        mBuildingName = (TextView) findViewById(R.id.poi_name);
        mBuildingAddress = (TextView) findViewById(R.id.poi_address);
        mSearchText = (EditText) findViewById(R.id.input_edittext);

        buildingSearch = BuildingSearch.newInstance();
        buildingSearch.setBuildingSearchResultListener(this);


    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {

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
        if (buildingSearch != null) {
            buildingSearch.destroy();
        }
        mapView.onDestroy();
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

        DetailSearchOption detailSearchOption = new DetailSearchOption();
        detailSearchOption.id(keyWord);


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

        mapboxMap.clear();
        IndoorBuildingInfo indoorBuildingInfo = buildingDetailResult.getIndoorBuildingInfo();

        Marker marker = mapboxMap.addMarker(new ObjectMarkerOptions()
                .position(
                        new LatLng(indoorBuildingInfo.getLabelCenter()
                                .getLat(), indoorBuildingInfo
                                .getLabelCenter().getLon()))
                .title(indoorBuildingInfo.getName().get("default")).snippet(indoorBuildingInfo.getAddress().get("default").toShortAddress())
                .object(indoorBuildingInfo));
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ObjectMarker objectMarker = (ObjectMarker) marker;
        IndoorBuildingInfo indoorBuildingInfo = (IndoorBuildingInfo) objectMarker.getObject();
        mBuildingName.setText(indoorBuildingInfo.getName().get("default"));
        Address address = indoorBuildingInfo.getAddress().get("default");
        mBuildingAddress.setText(address.getStreet() + address.getHousenumber());
        mBuildingDetail.setVisibility(View.VISIBLE);
        return true;
    }
}





