package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.services.VenueSearch;
import com.mapxus.map.mapxusmap.api.services.model.DetailSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.venue.VenueResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyVenueOverlay;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SearchVenueDetailActivity extends BaseWithParamMenuActivity implements VenueSearch.VenueSearchResultListener, OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;

    private RelativeLayout progressBarView;
    private VenueSearch venueSearch;

    private List<View> views = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_search_building_detail);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapboxMapViewProvider mapViewProvider = new MapboxMapViewProvider(this, mapView);

        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(mapxusMap -> this.mapxusMap = mapxusMap);

        venueSearch = VenueSearch.newInstance();
        venueSearch.setVenueSearchResultListener(this);

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

    protected void doSearchQuery(List<String> venueIds) {
        DetailSearchOption detailSearchOption = new DetailSearchOption();
        detailSearchOption.ids(venueIds);
        venueSearch.searchVenueDetail(detailSearchOption);
    }

    @Override
    public void onGetVenueResult(@NonNull VenueResult venueResult) {
        progressBarView.setVisibility(View.GONE);

        if (venueResult.status != 0) {
            Toast.makeText(this, venueResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (venueResult.getVenueInfoList() == null) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        MyVenueOverlay venueOverlay = new MyVenueOverlay(mapboxMap, mapxusMap, venueResult.getVenueInfoList());
        venueOverlay.removeFromMap();
        venueOverlay.addToMap();
        venueOverlay.zoomToSpan(15);
    }

    @SuppressLint("InflateParams")
    @Override
    protected void initBottomSheetDialog() {
        views.clear();
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_id_search_style, this);

        LinearLayout linearLayout = bottomSheetDialogView.findViewById(R.id.ll_buildingId);
        View startView = LayoutInflater.from(this).inflate(R.layout.swipe_item, null);
        EditText editText = startView.findViewById(R.id.et_id);
        editText.setText(getString(R.string.default_search_text_venue_id));
        linearLayout.addView(startView);
        startView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            linearLayout.removeView(startView);
            views.remove(startView);
        });
        views.add(startView);

        bottomSheetDialogView.findViewById(R.id.create).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            progressBarView.setVisibility(View.VISIBLE);
            getValueAndSearch();
        });

        bottomSheetDialogView.findViewById(R.id.btn_add_id).setOnClickListener(v -> {
            View view = LayoutInflater.from(this).inflate(R.layout.swipe_item, null);
            view.findViewById(R.id.btnDelete).setOnClickListener(view1 -> {
                linearLayout.removeView(view);
                views.remove(view);
            });
            linearLayout.addView(view);
            views.add(view);
        });
    }

    private void getValueAndSearch() {
        List<String> venueIds = new ArrayList<>();
        for (View view : views) {
            EditText editText = view.findViewById(R.id.et_id);
            String buildingId = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(buildingId)) {
                venueIds.add(buildingId);
            }
        }
        if (!venueIds.isEmpty()) {
            doSearchQuery(venueIds);
        } else {
            Toast.makeText(this, "Please input buildingId at least one .", Toast.LENGTH_LONG).show();
        }
    }
}





