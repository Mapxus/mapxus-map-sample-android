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
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.services.PoiSearch;
import com.mapxus.map.mapxusmap.api.services.model.PoiInSiteSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiCategoryResult;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiDetailResult;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiOrientationResult;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyPoiOverlay;

import org.jetbrains.annotations.NotNull;


public class SearchPoiInSiteActivity extends BaseWithParamMenuActivity implements OnMapReadyCallback, PoiSearch.PoiSearchResultListener, OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;

    private PoiSearch poiSearch;
    private RelativeLayout progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchservices_search_poi_in_site);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapboxMapViewProvider mapboxMapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapboxMapViewProvider.getMapxusMapAsync(this);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(this);

        progressBarView = findViewById(R.id.loding_view);
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

    protected void doSearchQuery(String keyWord, String category, String venueId, String buildingId, String floorId, int offset, int page) {
        PoiInSiteSearchOption inSiteSearchOption = new PoiInSiteSearchOption();
        inSiteSearchOption.keyword(keyWord);
        inSiteSearchOption.category(category);
        inSiteSearchOption.venueId(venueId);
        inSiteSearchOption.buildingId(buildingId);
        inSiteSearchOption.floorId(floorId);
        inSiteSearchOption.pageCapacity(offset);
        inSiteSearchOption.pageNum(page);

        poiSearch.searchInSite(inSiteSearchOption);

    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        progressBarView.setVisibility(View.GONE);
        if (poiResult.status != 0) {
            Toast.makeText(this, poiResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (poiResult.getAllPoi() == null || poiResult.getAllPoi().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        MyPoiOverlay poiOverlay = new MyPoiOverlay(mapboxMap, mapxusMap, poiResult.getAllPoi());
        poiOverlay.removeFromMap();
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan(Double.parseDouble(getString(R.string.default_zoom_level_value)));
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiByOrientationResult(PoiOrientationResult poiOrientationResult) {

    }

    @Override
    public void onPoiCategoriesResult(PoiCategoryResult poiCategoryResult) {
    }


    @Override
    protected void initBottomSheetDialog() {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_poi_in_site_search_style, this);
        bottomSheetDialogView.findViewById(R.id.create).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            progressBarView.setVisibility(View.VISIBLE);
            getValueAndSearch(bottomSheetDialogView);
        });
    }

    private void getValueAndSearch(View bottomSheetDialogView) {
        EditText etKeywords = bottomSheetDialogView.findViewById(R.id.et_keywords);
        EditText etCategory = bottomSheetDialogView.findViewById(R.id.et_category);
        EditText etVenueId = bottomSheetDialogView.findViewById(R.id.et_venue_id);
        EditText etBuildingId = bottomSheetDialogView.findViewById(R.id.et_id);
        EditText etFloorId = bottomSheetDialogView.findViewById(R.id.et_floor_id);
        EditText etOffset = bottomSheetDialogView.findViewById(R.id.et_offset);
        EditText etPage = bottomSheetDialogView.findViewById(R.id.et_page);

        String venueId = etVenueId.getText().toString().trim();
        String buildingId = etBuildingId.getText().toString().trim();
        String floorId = etFloorId.getText().toString().trim();

        doSearchQuery(
                etKeywords.getText().toString().trim(),
                etCategory.getText().toString().trim(),
                venueId,
                buildingId,
                floorId,
                etOffset.getText().toString().isEmpty() ? 0 : Integer.parseInt(etOffset.getText().toString().trim()),
                etPage.getText().toString().isEmpty() ? 0 : Integer.parseInt(etPage.getText().toString().trim()));
        if (!floorId.isEmpty()) {
            mapxusMap.selectFloorById(floorId);
        } else if (!buildingId.isEmpty()) {
            mapxusMap.selectBuildingById(buildingId);
        }
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
    }
}



