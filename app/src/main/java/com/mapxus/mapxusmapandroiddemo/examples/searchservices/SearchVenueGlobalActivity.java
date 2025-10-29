package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.services.VenueSearch;
import com.mapxus.map.mapxusmap.api.services.model.GlobalSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.venue.VenueResult;
import com.mapxus.map.mapxusmap.impl.MapLibreMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyVenueOverlay;

import org.jetbrains.annotations.NotNull;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.OnMapReadyCallback;

public class SearchVenueGlobalActivity extends BaseWithParamMenuActivity implements VenueSearch.VenueSearchResultListener, OnMapReadyCallback {

    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private MapxusMap mapxusMap;

    private VenueSearch venueSearch;
    private RelativeLayout progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_search_building_global);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapLibreMapViewProvider mapViewProvider = new MapLibreMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(mapxusMap -> this.mapxusMap = mapxusMap);
        progressBarView = findViewById(R.id.loding_view);

        venueSearch = VenueSearch.newInstance();
        venueSearch.setVenueSearchResultListener(this);
    }

    protected void doSearchQuery(String keyWord, int offset, int page) {
        GlobalSearchOption globalSearchOption = new GlobalSearchOption();
        globalSearchOption.keyword(keyWord);
        globalSearchOption.pageCapacity(offset);
        globalSearchOption.pageNum(page);
        venueSearch.searchInGlobal(globalSearchOption);
    }

    @Override
    public void onGetVenueResult(@NonNull VenueResult venueResult) {
        progressBarView.setVisibility(View.GONE);
        if (venueResult.status != 0) {
            Toast.makeText(this, venueResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (venueResult.getVenueInfoList() == null || venueResult.getVenueInfoList().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        MyVenueOverlay venueOverlay = new MyVenueOverlay(mapLibreMap, mapxusMap, venueResult.getVenueInfoList());
        venueOverlay.removeFromMap();
        venueOverlay.addToMap();
        venueOverlay.zoomToSpan(Double.parseDouble(getString(R.string.default_zoom_level_value)));

    }

    @Override
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
        if (venueSearch != null) {
            venueSearch.destroy();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}



