package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.LatLng;
import com.mapxus.map.mapxusmap.api.services.PoiSearch;
import com.mapxus.map.mapxusmap.api.services.model.PoiNearbySearchOption;
import com.mapxus.map.mapxusmap.api.services.model.PoiSearchSortWay;
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

public class SearchPoiNearbyActivity extends BaseWithParamMenuActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private MapxusMap mapxusMap;

    private PoiSearch poiSearch;

    private RelativeLayout progressBarView;

    private EditText etOrdinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_search_poi_nearby);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapLibreMapViewProvider mapViewProvider = new MapLibreMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(mapxusMap -> this.mapxusMap = mapxusMap);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(poiSearchResultListenerAdapter);

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

    protected void doSearchQuery(
            String sortWay,
            String keyWord,
            String category,
            String excludeCategory,
            int ordinal, LatLng latLng,
            int meterDistance,
            int offset,
            int page) {
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.keyword(keyWord);
        nearbySearchOption.sort(sortWay);
        nearbySearchOption.ordinal(ordinal);
        nearbySearchOption.category(category);
        nearbySearchOption.meterRadius(meterDistance);
        nearbySearchOption.location(latLng);
        nearbySearchOption.excludeCategories(Arrays.asList(excludeCategory.split(",")));
        nearbySearchOption.pageCapacity(offset);
        nearbySearchOption.pageNum(page);
        poiSearch.searchNearby(nearbySearchOption);
    }

    PoiSearch.PoiSearchResultListenerAdapter poiSearchResultListenerAdapter = new PoiSearch.PoiSearchResultListenerAdapter() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            progressBarView.setVisibility(View.GONE);

            if (poiResult.status != 0) {
                Toast.makeText(SearchPoiNearbyActivity.this, poiResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (poiResult.getAllPoi() == null || poiResult.getAllPoi().isEmpty()) {
                Toast.makeText(SearchPoiNearbyActivity.this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
                return;
            }

            MyPoiOverlay poiOverlay = new MyPoiOverlay(mapLibreMap, mapxusMap, poiResult.getAllPoi());
            poiOverlay.removeFromMap();
            poiOverlay.addToMap();
            poiOverlay.zoomToSpan(Double.parseDouble(getString(R.string.default_zoom_level_value)));
        }
    };

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void initBottomSheetDialog() {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_poi_nearby_style, this);
        bottomSheetDialogView.findViewById(R.id.create).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            progressBarView.setVisibility(View.VISIBLE);
            getValueAndSearch(bottomSheetDialogView);
        });

        etOrdinal = bottomSheetDialogView.findViewById(R.id.et_ordinal);

        TextView tvOrdinal = bottomSheetDialogView.findViewById(R.id.tv_ordinal);

        Button btnSoft = bottomSheetDialogView.findViewById(R.id.btn_soft_mode);

        btnSoft.setOnClickListener(v -> {
            if (btnSoft.getText().toString().equals(getString(R.string.actual_distance))) {
                btnSoft.setText(getString(R.string.absoulute_distance));
                etOrdinal.setEnabled(false);
                etOrdinal.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
                tvOrdinal.setEnabled(false);
                tvOrdinal.setTextColor(getResources().getColor(R.color.lighter_gray));
            } else {
                btnSoft.setText(getString(R.string.actual_distance));
                etOrdinal.setEnabled(true);
                etOrdinal.setBackground(getDrawable(android.R.drawable.editbox_background_normal));
                tvOrdinal.setEnabled(true);
                tvOrdinal.setTextColor(getResources().getColor(R.color.black));
            }
        });
    }

    private void getValueAndSearch(View bottomSheetDialogView) {
        EditText etKeywords = bottomSheetDialogView.findViewById(R.id.et_keywords);
        EditText etCategory = bottomSheetDialogView.findViewById(R.id.et_category);
        EditText etExcludeCategory = bottomSheetDialogView.findViewById(R.id.et_exclude_category);
        EditText etOffset = bottomSheetDialogView.findViewById(R.id.et_offset);
        EditText etPage = bottomSheetDialogView.findViewById(R.id.et_page);
        Button btnSoft = bottomSheetDialogView.findViewById(R.id.btn_soft_mode);

        EditText etDistance = bottomSheetDialogView.findViewById(R.id.et_distance);
        EditText etLat = bottomSheetDialogView.findViewById(R.id.et_lat);
        EditText etLon = bottomSheetDialogView.findViewById(R.id.et_lon);

        LatLng latLng = new LatLng(
                etLat.getText().toString().isEmpty() ? 0 : Double.parseDouble(etLat.getText().toString().trim()),
                etLon.getText().toString().isEmpty() ? 0 : Double.parseDouble(etLon.getText().toString().trim())
        );

        String sortWay;
        if (btnSoft.getText().toString().equals(getString(R.string.actual_distance))) {
            sortWay = PoiSearchSortWay.ACTUAL_DISTANCE;
        } else {
            sortWay = PoiSearchSortWay.ABSOLUTE_DISTANCE;
        }

        doSearchQuery(
                sortWay,
                etKeywords.getText().toString().trim(),
                etCategory.getText().toString().trim(),
                etExcludeCategory.getText().toString().trim(),
                etDistance.getText().toString().isEmpty() ? 0 : Integer.parseInt(etDistance.getText().toString().trim()),
                latLng,
                etDistance.getText().toString().isEmpty() ? 0 : Integer.parseInt(etDistance.getText().toString().trim()),
                etOffset.getText().toString().isEmpty() ? 0 : Integer.parseInt(etOffset.getText().toString().trim()),
                etPage.getText().toString().isEmpty() ? 0 : Integer.parseInt(etPage.getText().toString().trim()));
    }
}



