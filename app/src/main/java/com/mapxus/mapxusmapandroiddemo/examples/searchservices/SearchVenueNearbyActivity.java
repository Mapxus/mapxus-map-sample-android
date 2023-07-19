package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.LatLng;
import com.mapxus.map.mapxusmap.api.services.VenueSearch;
import com.mapxus.map.mapxusmap.api.services.model.NearbySearchOption;
import com.mapxus.map.mapxusmap.api.services.model.venue.VenueResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyVenueOverlay;

import org.jetbrains.annotations.NotNull;

public class SearchVenueNearbyActivity extends BaseWithParamMenuActivity implements OnMapReadyCallback, VenueSearch.VenueSearchResultListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;
    private MapViewProvider mapViewProvider;

    private RelativeLayout progressBarView;

    private VenueSearch venueSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_search_building_nearby);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(mapxusMap -> this.mapxusMap = mapxusMap);

        progressBarView = findViewById(R.id.loding_view);

        venueSearch = VenueSearch.newInstance();
        venueSearch.setVenueSearchResultListener(this);
    }

    protected void doSearchQuery(LatLng location, double radius, String keyWord, int offset, int page) {
        NearbySearchOption nearbySearchOption = new NearbySearchOption();
        nearbySearchOption.distance(radius);
        nearbySearchOption.location(location);
        nearbySearchOption.keyword(keyWord);
        nearbySearchOption.pageCapacity(offset);
        nearbySearchOption.pageNum(page);
        venueSearch.searchNearby(nearbySearchOption);
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

        MyVenueOverlay venueOverlay = new MyVenueOverlay(mapboxMap, mapxusMap, venueResult.getVenueInfoList());
        venueOverlay.removeFromMap();
        venueOverlay.addToMap();
        venueOverlay.zoomToSpan(Double.parseDouble(getString(R.string.default_zoom_level_value)));

    }


    @Override
    protected void initBottomSheetDialog() {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_nearby_search_style, this);
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

        EditText etDistance = bottomSheetDialogView.findViewById(R.id.et_distance);
        EditText etLat = bottomSheetDialogView.findViewById(R.id.et_lat);
        EditText etLon = bottomSheetDialogView.findViewById(R.id.et_lon);

        LatLng latLng = new LatLng(
                etLat.getText().toString().isEmpty() ? 0 : Double.parseDouble(etLat.getText().toString().trim()),
                etLon.getText().toString().isEmpty() ? 0 : Double.parseDouble(etLon.getText().toString().trim())
        );

        doSearchQuery(latLng,
                etDistance.getText().toString().isEmpty() ? 0 : Double.parseDouble(etDistance.getText().toString().trim()),
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



