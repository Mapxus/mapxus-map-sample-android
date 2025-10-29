package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.services.PoiSearch;
import com.mapxus.map.mapxusmap.api.services.model.PoiInSiteSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.PoiSearchOrderBy;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiCategoryResult;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiDetailResult;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiOrientationResult;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiResult;
import com.mapxus.map.mapxusmap.impl.MapLibreMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyPoiOverlay;

import org.jetbrains.annotations.NotNull;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.OnMapReadyCallback;

import java.util.Arrays;
import java.util.List;


public class SearchPoiInSiteActivity extends BaseWithParamMenuActivity implements OnMapReadyCallback, PoiSearch.PoiSearchResultListener, OnMapxusMapReadyCallback {

    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private MapxusMap mapxusMap;

    private PoiSearch poiSearch;
    private RelativeLayout progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchservices_search_poi_in_site);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapLibreMapViewProvider MapLibreMapViewProvider = new MapLibreMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        MapLibreMapViewProvider.getMapxusMapAsync(this);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(this);

        progressBarView = findViewById(R.id.loding_view);
    }

    @Override
    public void onMapReady(@NotNull MapLibreMap mapLibreMap) {
        this.mapLibreMap = mapLibreMap;
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

    protected void doSearchQuery(String keyWord, String orderBy,
                                 String category, String excludeCategory,
                                 String venueId, String buildingId, List<String> floorIds, List<String> sharedFloorIds, int offset, int page) {
        PoiInSiteSearchOption inSiteSearchOption = new PoiInSiteSearchOption();
        inSiteSearchOption.keyword(keyWord);
        inSiteSearchOption.orderBy(orderBy);

        inSiteSearchOption.category(category);
        inSiteSearchOption.excludeCategories(Arrays.asList(excludeCategory.split(",")));
        inSiteSearchOption.venueId(venueId);
        inSiteSearchOption.buildingId(buildingId);
        if (floorIds != null && !floorIds.isEmpty()) {
            inSiteSearchOption.floorIds(floorIds);
        }
        if (sharedFloorIds != null && !sharedFloorIds.isEmpty()) {
            inSiteSearchOption.sharedFloorIds(sharedFloorIds);
        }
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

        MyPoiOverlay poiOverlay = new MyPoiOverlay(mapLibreMap, mapxusMap, poiResult.getAllPoi());
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

        Button btnOrderBy = bottomSheetDialogView.findViewById(R.id.btn_order_by);
        TextView tvKeyword = bottomSheetDialogView.findViewById(R.id.tv_keywords);
        EditText etKeywords = bottomSheetDialogView.findViewById(R.id.et_keywords);
        btnOrderBy.setOnClickListener(v -> {
            if (btnOrderBy.getText().toString().equals(getString(R.string.order_by_default_name))) {
                btnOrderBy.setText(getString(R.string.order_by_none));
                etKeywords.setEnabled(true);
                etKeywords.setBackground(getDrawable(android.R.drawable.editbox_background_normal));
                tvKeyword.setEnabled(true);
                tvKeyword.setTextColor(getResources().getColor(R.color.black));
            } else {
                btnOrderBy.setText(getString(R.string.order_by_default_name));
                etKeywords.setEnabled(false);
                etKeywords.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
                tvKeyword.setEnabled(false);
                tvKeyword.setTextColor(getResources().getColor(R.color.lighter_gray));
            }
        });
    }

    private void getValueAndSearch(View bottomSheetDialogView) {
        EditText etKeywords = bottomSheetDialogView.findViewById(R.id.et_keywords);
        EditText etCategory = bottomSheetDialogView.findViewById(R.id.et_category);
        EditText etExcludeCategory = bottomSheetDialogView.findViewById(R.id.et_exclude_category);
        EditText etVenueId = bottomSheetDialogView.findViewById(R.id.et_venue_id);
        EditText etBuildingId = bottomSheetDialogView.findViewById(R.id.et_id);
        EditText etFloorId = bottomSheetDialogView.findViewById(R.id.et_floor_id);
        EditText etSharedFloorId = bottomSheetDialogView.findViewById(R.id.et_shared_floor_id);
        EditText etOffset = bottomSheetDialogView.findViewById(R.id.et_offset);
        EditText etPage = bottomSheetDialogView.findViewById(R.id.et_page);

        String venueId = etVenueId.getText().toString().trim();
        String buildingId = etBuildingId.getText().toString().trim();
        String floorId = etFloorId.getText().toString().trim();
        String sharedFloorId = etSharedFloorId.getText().toString().trim();
        Button btnOrderBy = bottomSheetDialogView.findViewById(R.id.btn_order_by);
        String orderBy = null;
        if (btnOrderBy.getText().toString().equals(getString(R.string.order_by_default_name))) {
            orderBy = PoiSearchOrderBy.DEFAULT_NAME;
        }

        List<String> floorIds = null;
        if (!floorId.isEmpty()) floorIds = Arrays.asList(floorId.split(","));
        List<String> sharedFloorIds = null;
        if (!sharedFloorId.isEmpty()) sharedFloorIds = Arrays.asList(sharedFloorId.split(","));

        doSearchQuery(
                etKeywords.getText().toString().trim(),
                orderBy,
                etCategory.getText().toString().trim(),
                etExcludeCategory.getText().toString().trim(),
                venueId,
                buildingId,
                floorIds,
                sharedFloorIds,
                etOffset.getText().toString().isEmpty() ? 0 : Integer.parseInt(etOffset.getText().toString().trim()),
                etPage.getText().toString().isEmpty() ? 0 : Integer.parseInt(etPage.getText().toString().trim()));
        if (!floorId.isEmpty()) {
            mapxusMap.selectFloorById(floorId);
        } else if (!sharedFloorId.isEmpty()) {
            mapxusMap.selectSharedFloorById(sharedFloorId);
        } else if (!buildingId.isEmpty()) {
            mapxusMap.selectBuildingById(buildingId);
        }
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
    }
}



