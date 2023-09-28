package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.services.PoiSearch;
import com.mapxus.map.mapxusmap.api.services.model.PoiCategorySearchOption;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiCategoryInfo;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiCategoryResult;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiDetailResult;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiOrientationResult;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.adapter.CategoriesNameInfoAdapter;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchPoiCategoriesInSiteActivity extends AppCompatActivity implements OnMapxusMapReadyCallback, PoiSearch.PoiSearchResultListener, View.OnClickListener {

    private MapView mapView;
    private PoiSearch poiSearch;
    private String floor, buildingId, venueId = "";
    private RelativeLayout progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchservices_search_poi_categories_in_site);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapViewProvider mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(this);

        progressBarView = findViewById(R.id.loding_view);
        findViewById(R.id.btn_search_in_venue).setOnClickListener(this);
        findViewById(R.id.btn_search_in_building).setOnClickListener(this);
        findViewById(R.id.btn_search_on_floor).setOnClickListener(this);

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

    protected void searchAllPoiCategory(String venueId, String buildingId, String floor) {
        PoiCategorySearchOption poiCategorySearchOption = new PoiCategorySearchOption();
        poiCategorySearchOption.venueId(venueId);
        poiCategorySearchOption.buildingId(buildingId);
        poiCategorySearchOption.floorId(floor);
        poiSearch.searchPoiCategoryInSite(poiCategorySearchOption);
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiByOrientationResult(PoiOrientationResult poiOrientationResult) {

    }

    @Override
    public void onPoiCategoriesResult(PoiCategoryResult poiCategoryResult) {
        progressBarView.setVisibility(View.GONE);
        if (poiCategoryResult.status != 0) {
            Toast.makeText(this, poiCategoryResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (poiCategoryResult.getPoiCategoryInfos() == null || poiCategoryResult.getPoiCategoryInfos().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        initBottomSheetDialog(poiCategoryResult.getPoiCategoryInfos());
    }

    private void initBottomSheetDialog(List<PoiCategoryInfo> categoryInfos) {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_category_search_style, this);
        bottomSheetDialogView.findViewById(R.id.btn_close).setOnClickListener(v -> bottomSheetDialog.dismiss());

        RecyclerView recyclerView = bottomSheetDialogView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CategoriesNameInfoAdapter(categoryInfos));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        mapxusMap.addOnFloorChangedListener((venue, indoorBuilding, floorInfo) -> {
            if (floorInfo != null) {
                SearchPoiCategoriesInSiteActivity.this.floor = floorInfo.getId();
                buildingId = indoorBuilding.getBuildingId();
                venueId = venue.getId();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_in_venue:
                searchAllPoiCategory(venueId, "", "");
                break;
            case R.id.btn_search_in_building:
                searchAllPoiCategory("", buildingId, "");
                break;
            case R.id.btn_search_on_floor:
                searchAllPoiCategory("", buildingId, floor);
                break;
        }
        progressBarView.setVisibility(View.VISIBLE);
    }
}



