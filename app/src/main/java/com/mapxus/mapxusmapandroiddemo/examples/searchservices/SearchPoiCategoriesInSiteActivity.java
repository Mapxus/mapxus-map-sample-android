package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.services.CategorySearch;
import com.mapxus.map.mapxusmap.api.services.model.CategoryInSiteSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.building.FloorInfo;
import com.mapxus.map.mapxusmap.api.services.model.category.CategoryGroup;
import com.mapxus.map.mapxusmap.api.services.model.category.CategoryResult;
import com.mapxus.map.mapxusmap.api.services.model.floor.Floor;
import com.mapxus.map.mapxusmap.api.services.model.floor.SharedFloor;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.adapter.CategoriesNameInfoAdapter;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchPoiCategoriesInSiteActivity extends AppCompatActivity implements OnMapxusMapReadyCallback, CategorySearch.CategoryResponseListener, View.OnClickListener {

    private MapView mapView;
    private CategorySearch categorySearch;
    private String buildingId, venueId = "";
    private Floor floor;
    private RelativeLayout progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchservices_search_poi_categories_in_site);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapViewProvider mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapViewProvider.getMapxusMapAsync(this);

        categorySearch = CategorySearch.newInstance();

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
        if (categorySearch != null) {
            categorySearch.destroy();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    protected void searchAllPoiCategory(String venueId, String buildingId, Floor floor) {
        progressBarView.setVisibility(View.VISIBLE);
        CategoryInSiteSearchOption categoryInSiteSearchOption = new CategoryInSiteSearchOption();
        categoryInSiteSearchOption.venueId(venueId);
        categoryInSiteSearchOption.buildingId(buildingId);
        if (floor != null) {
            if (floor instanceof FloorInfo) {
                categoryInSiteSearchOption.floorId(floor.getId());
            }
            if (floor instanceof SharedFloor) {
                categoryInSiteSearchOption.sharedFloorId(floor.getId());
            }
        }
        categorySearch.searchCategoriesInSite(categoryInSiteSearchOption, this);
    }

    private void initBottomSheetDialog(List<CategoryGroup> categoryInfos) {
        if (isDestroyed() || isFinishing()) return;
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
        mapxusMap.addOnFloorChangedListener((venue, indoorBuilding, floor) -> {
            this.floor = floor;
            buildingId = indoorBuilding == null ? "" : indoorBuilding.getBuildingId();
            venueId = venue == null ? "" : venue.getId();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_in_venue: {
                if (venueId == null || venueId.isEmpty()) {
                    Toast.makeText(this, "Please select a venue", Toast.LENGTH_SHORT).show();
                } else {
                    searchAllPoiCategory(venueId, "", null);
                }
                break;
            }
            case R.id.btn_search_in_building: {
                if (buildingId == null || buildingId.isEmpty()) {
                    Toast.makeText(this, "Please select a building", Toast.LENGTH_SHORT).show();
                } else {
                    searchAllPoiCategory("", buildingId, null);
                }
                break;
            }
            case R.id.btn_search_on_floor: {
                if (floor == null) {
                    Toast.makeText(this, "Please select a floor", Toast.LENGTH_SHORT).show();
                } else {
                    searchAllPoiCategory("", buildingId, floor);
                }
                break;
            }
        }
    }

    @Override
    public void onGetCategoryResult(@NonNull CategoryResult categoryResult) {
        progressBarView.setVisibility(View.GONE);
        if (categoryResult.status != 0) {
            Toast.makeText(this, categoryResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        List<CategoryGroup> categoryGroups = categoryResult.getCategoryGroups();
        if (categoryGroups.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        initBottomSheetDialog(categoryGroups);
    }
}



