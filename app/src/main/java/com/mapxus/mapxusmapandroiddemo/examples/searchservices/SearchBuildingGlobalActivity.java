package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.services.BuildingSearch;
import com.mapxus.map.mapxusmap.api.services.model.GlobalSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.building.BuildingDetailResult;
import com.mapxus.map.mapxusmap.api.services.model.building.BuildingResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyIndoorBuildingOverlay;

import org.jetbrains.annotations.NotNull;

public class SearchBuildingGlobalActivity extends BaseWithParamMenuActivity implements BuildingSearch.BuildingSearchResultListener, OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;

    private BuildingSearch buildingSearch;
    private RelativeLayout progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_search_building_global);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapboxMapViewProvider mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(mapxusMap -> this.mapxusMap = mapxusMap);
        progressBarView = findViewById(R.id.loding_view);

        buildingSearch = BuildingSearch.newInstance();
        buildingSearch.setBuildingSearchResultListener(this);
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
    protected void onSaveInstanceState(@NotNull Bundle outState) {
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

    protected void doSearchQuery(String keyWord, int offset, int page) {
        GlobalSearchOption globalSearchOption = new GlobalSearchOption();
        globalSearchOption.keyword(keyWord);
        globalSearchOption.pageCapacity(offset);
        globalSearchOption.pageNum(page);
        buildingSearch.searchInGlobal(globalSearchOption);
    }

    @Override
    public void onGetBuildingResult(BuildingResult buildingResult) {

        progressBarView.setVisibility(View.GONE);
        if (buildingResult.status != 0) {
            Toast.makeText(this, buildingResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (buildingResult.getIndoorBuildingList() == null || buildingResult.getIndoorBuildingList().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        MyIndoorBuildingOverlay indoorBuildingOverlay = new MyIndoorBuildingOverlay(mapboxMap, mapxusMap, buildingResult.getIndoorBuildingList());
        indoorBuildingOverlay.removeFromMap();
        indoorBuildingOverlay.addToMap();
        indoorBuildingOverlay.zoomToSpan();
    }

    @Override
    public void onGetBuildingDetailResult(BuildingDetailResult buildingDetailResult) {

    }

    protected void initBottomSheetDialog() {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_global_search_style, this);
        bottomSheetDialogView.findViewById(R.id.create).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            progressBarView.setVisibility(View.VISIBLE);
            getValueAndSearch(bottomSheetDialogView);
        });
    }

    private void getValueAndSearch(View bottomSheetDialogView) {
        EditText etKeywords = bottomSheetDialogView.findViewById(R.id.et_keywords);
        EditText etOffset = bottomSheetDialogView.findViewById(R.id.et_offset);
        EditText etPage = bottomSheetDialogView.findViewById(R.id.et_page);

        doSearchQuery(etKeywords.getText().toString().trim(),
                etOffset.getText().toString().isEmpty() ? 0 : Integer.parseInt(etOffset.getText().toString().trim()),
                etPage.getText().toString().isEmpty() ? 0 : Integer.parseInt(etPage.getText().toString().trim()));
    }
}



