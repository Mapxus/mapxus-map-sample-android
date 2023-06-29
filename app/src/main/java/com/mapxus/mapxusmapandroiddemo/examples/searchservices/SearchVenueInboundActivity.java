package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.LatLngBounds;
import com.mapxus.map.mapxusmap.api.services.VenueSearch;
import com.mapxus.map.mapxusmap.api.services.model.BoundSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.venue.VenueResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyVenueOverlay;

import org.jetbrains.annotations.NotNull;

public class SearchVenueInboundActivity extends BaseWithParamMenuActivity implements OnMapReadyCallback, VenueSearch.VenueSearchResultListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;
    private MapViewProvider mapViewProvider;

    private RelativeLayout progressBarView;

    private VenueSearch venueSearch;
    private MyVenueOverlay venueOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchservices_search_building_inbound);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(mapxusMap -> this.mapxusMap = mapxusMap);
        progressBarView = findViewById(R.id.loding_view);

        venueSearch = VenueSearch.newInstance();
        venueSearch.setVenueSearchResultListener(this);

    }

    protected void doSearchQuery(LatLngBounds latLngBounds, String keyWord, int offset, int page) {

        BoundSearchOption boundSearchOption = new BoundSearchOption();
        boundSearchOption.bound(latLngBounds);
        boundSearchOption.keyword(keyWord);
        boundSearchOption.pageCapacity(offset);
        boundSearchOption.pageNum(page);
        venueSearch.searchInBound(boundSearchOption);
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

        if (venueOverlay != null) {
            venueOverlay.removeFromMap();
            venueOverlay = null;
        }

        venueOverlay = new MyVenueOverlay(mapboxMap, mapxusMap, venueResult.getVenueInfoList());
        venueOverlay.removeFromMap();
        venueOverlay.addToMap();
        venueOverlay.zoomToSpan(Double.parseDouble(getString(R.string.default_zoom_level_value)));

    }

    private void showBoundsArea(LatLngBounds latLngBounds) {
        PolygonOptions boundsArea = new PolygonOptions();

        boundsArea.add(new LatLng(latLngBounds.northeast.latitude, latLngBounds.southwest.longitude));
        boundsArea.add(new LatLng(latLngBounds.northeast.latitude, latLngBounds.northeast.longitude));
        boundsArea.add(new LatLng(latLngBounds.southwest.latitude, latLngBounds.northeast.longitude));
        boundsArea.add(new LatLng(latLngBounds.southwest.latitude, latLngBounds.southwest.longitude));

        boundsArea.fillColor(getResources().getColor(R.color.bound_polygon_color_gray));
        mapboxMap.addPolygon(boundsArea);
    }


    @Override
    protected void initBottomSheetDialog() {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_bound_search_style, this);
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

        EditText etMaxLat = bottomSheetDialogView.findViewById(R.id.et_max_lat);
        EditText etMaxLon = bottomSheetDialogView.findViewById(R.id.et_max_lon);
        EditText etMinLat = bottomSheetDialogView.findViewById(R.id.et_min_lat);
        EditText etMinLon = bottomSheetDialogView.findViewById(R.id.et_min_lon);

        com.mapxus.map.mapxusmap.api.map.model.LatLng southweast = new com.mapxus.map.mapxusmap.api.map.model.LatLng(
                etMinLat.getText().toString().isEmpty() ? 0 : Double.parseDouble(etMinLat.getText().toString().trim()),
                etMinLon.getText().toString().isEmpty() ? 0 : Double.parseDouble(etMinLon.getText().toString().trim()));
        com.mapxus.map.mapxusmap.api.map.model.LatLng northeast = new com.mapxus.map.mapxusmap.api.map.model.LatLng(
                etMaxLat.getText().toString().isEmpty() ? 0 : Double.parseDouble(etMaxLat.getText().toString().trim()),
                etMaxLon.getText().toString().isEmpty() ? 0 : Double.parseDouble(etMaxLon.getText().toString().trim()));

        LatLngBounds latLngBounds = new LatLngBounds(southweast, northeast);

        showBoundsArea(latLngBounds);

        doSearchQuery(latLngBounds,
                etKeywords.getText().toString().trim(),
                etOffset.getText().toString().isEmpty() ? 0 : Integer.parseInt(etOffset.getText().toString().trim()),
                etPage.getText().toString().isEmpty() ? 0 : Integer.parseInt(etPage.getText().toString().trim()));
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
        if (venueSearch != null) {
            venueSearch.destroy();
        }
        mapView.onDestroy();
        mapViewProvider.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}



