package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.model.LatLngBounds;
import com.mapxus.map.mapxusmap.api.services.BuildingSearch;
import com.mapxus.map.mapxusmap.api.services.model.BoundSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.building.BuildingDetailResult;
import com.mapxus.map.mapxusmap.api.services.model.building.BuildingResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyIndoorBuildingOverlay;

/**
 * Use MapxusMap Search Services to request directions
 */
public class SearchBuildingInboundActivity extends AppCompatActivity implements OnMapReadyCallback, BuildingSearch.BuildingSearchResultListener {

    private static final String TAG = "SearchBuildingInboundActivity";

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;

    private String keyWord = "";
    private EditText mSearchText;

    private LatLng startPoint = new LatLng(22.0360235, 113.18262645);
    private int currentPage;
    private BuildingSearch buildingSearch;
    private MyIndoorBuildingOverlay indoorBuildingOverlay;
    private LatLngBounds latLngBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchservices_search_building_inbound);

        // Setup the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);


        TextView searchButton = (TextView) findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearchQuery();
            }
        });

        mSearchText = (EditText) findViewById(R.id.input_edittext);

        buildingSearch = BuildingSearch.newInstance();
        buildingSearch.setBuildingSearchResultListener(this);

        com.mapxus.map.mapxusmap.api.map.model.LatLng southweast = new com.mapxus.map.mapxusmap.api.map.model.LatLng(22.2918962, 114.1353782);
        com.mapxus.map.mapxusmap.api.map.model.LatLng northeast = new com.mapxus.map.mapxusmap.api.map.model.LatLng(22.3418344, 114.2089048);

        latLngBounds = new LatLngBounds(southweast, northeast);

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        showBoundsArea();
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
        currentPage = 0;

        BoundSearchOption boundSearchOption = new BoundSearchOption();

        boundSearchOption.bound(latLngBounds);
        boundSearchOption.keyword(keyWord);

        buildingSearch.searchInBound(boundSearchOption);
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

//        mBeemap.clexzar();
        if (indoorBuildingOverlay != null) {
            indoorBuildingOverlay.removeFromMap();
            indoorBuildingOverlay = null;
        }
        indoorBuildingOverlay = new MyIndoorBuildingOverlay(mapboxMap, buildingResult.getIndoorBuildingList());
        indoorBuildingOverlay.addToMap();
        indoorBuildingOverlay.zoomToSpan();
    }

    @Override
    public void onGetBuildingDetailResult(BuildingDetailResult buildingDetailResult) {

    }

    private void showBoundsArea() {
        PolygonOptions boundsArea = new PolygonOptions();

        boundsArea.add(new LatLng(latLngBounds.northeast.latitude, latLngBounds.southwest.longitude));
        boundsArea.add(new LatLng(latLngBounds.northeast.latitude, latLngBounds.northeast.longitude));
        boundsArea.add(new LatLng(latLngBounds.southwest.latitude, latLngBounds.northeast.longitude));
        boundsArea.add(new LatLng(latLngBounds.southwest.latitude, latLngBounds.southwest.longitude));

        boundsArea.fillColor(getResources().getColor(R.color.bound_polygon_color));
        mapboxMap.addPolygon(boundsArea);
    }


}



