package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.services.PoiSearch;
import com.mapxus.map.mapxusmap.api.services.model.DetailSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiDetailResult;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyPoiOverlay;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchPoiDetailActivity extends BaseWithParamMenuActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;
    private PoiSearch poiSearch;
    private RelativeLayout progressBarView;
    private List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_search_poi_detail);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapboxMapViewProvider mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(mapxusMap -> this.mapxusMap = mapxusMap);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(searchResultListenerAdapter);

        progressBarView = findViewById(R.id.loding_view);
        views = new ArrayList<>();
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

    /**
     * 开始进行poi搜索, 根据ID查询详细POI信息
     */
    protected void doSearchQuery(List<String> poiIds) {
        DetailSearchOption detailSearchOption = new DetailSearchOption();
        detailSearchOption.ids(poiIds);
        poiSearch.searchPoiDetail(detailSearchOption);
    }

    private PoiSearch.PoiSearchResultListenerAdapter searchResultListenerAdapter = new PoiSearch.PoiSearchResultListenerAdapter() {
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            progressBarView.setVisibility(View.GONE);

            if (poiDetailResult.status != 0) {
                Toast.makeText(SearchPoiDetailActivity.this, poiDetailResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (poiDetailResult.getPoiInfo() == null) {
                Toast.makeText(SearchPoiDetailActivity.this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
                return;
            }

            MyPoiOverlay poiOverlay = new MyPoiOverlay(mapboxMap, mapxusMap, poiDetailResult.getPoiList());
            poiOverlay.removeFromMap();
            poiOverlay.addToMap();
            poiOverlay.zoomToSpan(Double.parseDouble(getString(R.string.default_zoom_level_value)));
        }
    };

    @SuppressLint("InflateParams")
    @Override
    protected void initBottomSheetDialog() {
        views.clear();
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_id_search_style, this);

        LinearLayout linearLayout = bottomSheetDialogView.findViewById(R.id.ll_buildingId);
        View startView = LayoutInflater.from(this).inflate(R.layout.swipe_item, null);
        TextView tv = startView.findViewById(R.id.tv_tips_id);
        tv.setText(R.string.poi_id);
        EditText editText = startView.findViewById(R.id.et_id);
        editText.setText(getString(R.string.default_poi_id));
        linearLayout.addView(startView);
        startView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            linearLayout.removeView(startView);
            views.remove(startView);
        });
        views.add(startView);

        bottomSheetDialogView.findViewById(R.id.create).setOnClickListener(v -> {
            List<String> ids = getValue();
            if (!ids.isEmpty()) {
                bottomSheetDialog.dismiss();
                progressBarView.setVisibility(View.VISIBLE);
                doSearchQuery(ids);
            } else {
                Toast.makeText(this, "Please input poiId at least one .", Toast.LENGTH_LONG).show();
            }
        });

        bottomSheetDialogView.findViewById(R.id.btn_add_id).setOnClickListener(v -> {
            View view = LayoutInflater.from(this).inflate(R.layout.swipe_item, null);
            TextView textView = view.findViewById(R.id.tv_tips_id);
            textView.setText(R.string.poi_id);
            view.findViewById(R.id.btnDelete).setOnClickListener(view1 -> {
                linearLayout.removeView(view);
                views.remove(view);
            });
            linearLayout.addView(view);
            views.add(view);
        });
    }

    private List<String> getValue() {
        List<String> venueIds = new ArrayList<>();
        for (View view : views) {
            EditText editText = view.findViewById(R.id.et_id);
            String buildingId = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(buildingId)) {
                venueIds.add(buildingId);
            }
        }
        return venueIds;
    }
}



