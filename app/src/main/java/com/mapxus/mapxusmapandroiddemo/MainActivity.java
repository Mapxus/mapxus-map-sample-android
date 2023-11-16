package com.mapxus.mapxusmapandroiddemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.navigation.NavigationView;
import com.mapxus.mapxusmapandroiddemo.adapter.ExampleAdapter;
import com.mapxus.mapxusmapandroiddemo.customizeview.CircularPagerIndicatorDecoration;
import com.mapxus.mapxusmapandroiddemo.examples.indoorpositioning.LocationProviderActivity;
import com.mapxus.mapxusmapandroiddemo.examples.integrationcases.DisplayVisualActivity;
import com.mapxus.mapxusmapandroiddemo.examples.integrationcases.SearchPoiWithOrientationActivity;
import com.mapxus.mapxusmapandroiddemo.examples.integrationcases.explore.ExploreBuildingActivity;
import com.mapxus.mapxusmapandroiddemo.examples.integrationcases.route.RoutePlanningActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapcreation.MapxusMapInitWithBuildingParamsActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapcreation.MapxusMapInitWithPoiParamsActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapcreation.SimpleComposeMapviewActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapcreation.SimpleMapViewActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapcreation.SupportMapFragmentActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapediting.DrawMarkerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapediting.DrawPolygonActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapinteraction.ClickEventListenerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapinteraction.FloorSwitchModeActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapinteraction.FocusOnIndoorSceneActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapinteraction.GestureInteractionSwitchBuildingActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapinteraction.IndoorMapControllerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapinteraction.IndoorSceneInAndOutListenerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapinteraction.IndoorSceneSwitchingListenerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.mapinteraction.MapStyleSettingActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchBuildingDetailActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchBuildingGlobalActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchBuildingInboundActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchBuildingNearbyActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiCategoriesInSiteActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiDetailActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiInSiteActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiInboundActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiNearbyActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchVenueDetailActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchVenueGlobalActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchVenueInboundActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchVenueNearbyActivity;
import com.mapxus.mapxusmapandroiddemo.model.ExampleItemModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<ExampleItemModel> exampleItemModels;
    private ExampleAdapter adapter;
    private int currentCategory = R.id.nav_map_creation;
    private RecyclerView recyclerView;
    private String categoryTitleForToolbar;
    private DrawerLayout drawer;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        exampleItemModels = new ArrayList<>();

        adapter = new ExampleAdapter(this, exampleItemModels);
        recyclerView = findViewById(R.id.details_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new CircularPagerIndicatorDecoration());
        if (savedInstanceState != null) {
            currentCategory = savedInstanceState.getInt("CURRENT_CATEGORY");
            categoryTitleForToolbar = savedInstanceState.getString("CURRENT_CATEGORY_TOOLBAR_TITLE");
            listItems(currentCategory);
        } else {
            toolbar.setTitle(" ");
            setSupportActionBar(toolbar);
            listItems(R.id.nav_map_creation);
        }

        adapter.setOnItemClickListener((position, view) -> startActivity(exampleItemModels.get(position).getActivity()));

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_map_creation);
        View headerView = navigationView.getHeaderView(0);
        TextView tv = headerView.findViewById(R.id.sub_title);
        tv.setText(String.format("%s（%s）", getString(R.string.examples), BuildConfig.VERSION_NAME));

        methodRequiresPermission();
    }

    @Override
    public void onBackPressed() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                moveTaskToBack(true);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != currentCategory) {
            listItems(id);
            categoryTitleForToolbar = item.getTitle().toString();
        }
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void listItems(int id) {
        exampleItemModels.clear();
        switch (id) {
            case R.id.nav_map_editing:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_annotation_marker_title,
                        R.string.activity_annotation_default_marker_description,
                        new Intent(MainActivity.this, DrawMarkerActivity.class),
                        R.drawable.indoor_marker));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_annotation_polygon_title,
                        R.string.activity_annotation_polygon_description,
                        new Intent(MainActivity.this, DrawPolygonActivity.class),
                        R.drawable.indoor_polygon));

                currentCategory = R.id.nav_map_editing;
                break;

            case R.id.nav_indoor_position:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_display_location_title,
                        R.string.activity_display_location_description,
                        new Intent(MainActivity.this, LocationProviderActivity.class),
                        R.drawable.display_location));

                currentCategory = R.id.nav_indoor_position;
                break;

            case R.id.nav_search_service:

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_venue_global_title,
                        R.string.activity_search_service_venue_global_description,
                        new Intent(MainActivity.this, SearchVenueGlobalActivity.class),
                        R.drawable.search_building_global));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_venue_in_bound_title,
                        R.string.activity_search_service_venue_in_bound_description,
                        new Intent(MainActivity.this, SearchVenueInboundActivity.class),
                        R.drawable.search_building_in_bound));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_venue_nearby_title,
                        R.string.activity_search_service_venue_nearby_description,
                        new Intent(MainActivity.this, SearchVenueNearbyActivity.class),
                        R.drawable.search_building_nearby));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_venue_by_id_title,
                        R.string.activity_search_service_venue_by_id_description,
                        new Intent(MainActivity.this, SearchVenueDetailActivity.class),
                        R.drawable.search_building_by_id));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_building_global_title,
                        R.string.activity_search_service_building_global_description,
                        new Intent(MainActivity.this, SearchBuildingGlobalActivity.class),
                        R.drawable.search_building_global));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_building_in_bound_title,
                        R.string.activity_search_service_building_in_bound_description,
                        new Intent(MainActivity.this, SearchBuildingInboundActivity.class),
                        R.drawable.search_building_in_bound));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_building_nearby_title,
                        R.string.activity_search_service_building_nearby_description,
                        new Intent(MainActivity.this, SearchBuildingNearbyActivity.class),
                        R.drawable.search_building_nearby));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_building_by_id_title,
                        R.string.activity_search_service_building_by_id_description,
                        new Intent(MainActivity.this, SearchBuildingDetailActivity.class),
                        R.drawable.search_building_by_id));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_categories_in_building_title,
                        R.string.activity_search_service_poi_category_in_building_description,
                        new Intent(MainActivity.this, SearchPoiCategoriesInSiteActivity.class),
                        R.drawable.category_include_in_scene));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_in_building_title,
                        R.string.activity_search_service_poi_in_building_description,
                        new Intent(MainActivity.this, SearchPoiInSiteActivity.class),
                        R.drawable.search_poi_in_scene));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_in_bound_title,
                        R.string.activity_search_service_poi_in_bound_description,
                        new Intent(MainActivity.this, SearchPoiInboundActivity.class),
                        R.drawable.search_poi_in_bound));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_nearby_title,
                        R.string.activity_search_service_poi_nearby_description,
                        new Intent(MainActivity.this, SearchPoiNearbyActivity.class),
                        R.drawable.search_poi_nearby));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_by_id_title,
                        R.string.activity_search_service_poi_by_id_description,
                        new Intent(MainActivity.this, SearchPoiDetailActivity.class),
                        R.drawable.search_poi_by_id));

                currentCategory = R.id.nav_search_service;
                break;

            case R.id.nav_integration_cases:

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_with_orientation_title,
                        R.string.activity_search_service_poi_with_orientation_description,
                        new Intent(MainActivity.this, SearchPoiWithOrientationActivity.class),
                        R.drawable.surrounding_identification));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_route_planning_title,
                        R.string.activity_search_service_route_planning_description,
                        new Intent(MainActivity.this, RoutePlanningActivity.class),
                        R.drawable.route));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_visual_map_title,
                        R.string.activity_visual_map_description,
                        new Intent(MainActivity.this, DisplayVisualActivity.class),
                        R.drawable.visualmap));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_explore_building_title,
                        R.string.activity_explore_building_description,
                        new Intent(MainActivity.this, ExploreBuildingActivity.class),
                        R.drawable.search_integrate));

                currentCategory = R.id.nav_integration_cases;
                break;

            case R.id.nav_map_interaction:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_indoor_map_controller_title,
                        R.string.activity_indoor_map_controller_description,
                        new Intent(MainActivity.this, IndoorMapControllerActivity.class),
                        R.drawable.indoor_map_controller));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_styles_default_title,
                        R.string.activity_styles_default_description,
                        new Intent(MainActivity.this, MapStyleSettingActivity.class),
                        R.drawable.map_style_setting));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_gesture_interaction_for_switching_building,
                        R.string.activity_gesture_interaction_for_switching_building_description,
                        new Intent(MainActivity.this, GestureInteractionSwitchBuildingActivity.class),
                        R.drawable.switching_building_gestures));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_floor_switch_mode_title,
                        R.string.activity_floor_switch_mode_description,
                        new Intent(MainActivity.this, FloorSwitchModeActivity.class),
                        R.drawable.focus_on_indoor_scene));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_camera_animate_title,
                        R.string.activity_camera_animate_description,
                        new Intent(MainActivity.this, FocusOnIndoorSceneActivity.class),
                        R.drawable.focus_on_indoor_scene));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_listener_click_title,
                        R.string.activity_listener_click_description,
                        new Intent(MainActivity.this, ClickEventListenerActivity.class),
                        R.drawable.click_event_listening));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_listener_building_floor_change_title,
                        R.string.activity_listener_building_floor_change_description,
                        new Intent(MainActivity.this, IndoorSceneSwitchingListenerActivity.class),
                        R.drawable.indoor_scene_switching_listener));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_listener_camera_change_listener_title,
                        R.string.activity_listener_camera_change_listener_description,
                        new Intent(MainActivity.this, IndoorSceneInAndOutListenerActivity.class),
                        R.drawable.indoor_scene_in_and_out_listener));

                currentCategory = R.id.nav_map_interaction;
                break;

            default:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_simple_mapview_title,
                        R.string.activity_basic_simple_mapview_description,
                        new Intent(MainActivity.this, SimpleMapViewActivity.class),
                        R.drawable.create_map_by_code));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_simple_compose_mapview_title,
                        R.string.activity_basic_simple_compose_mapview_description,
                        new Intent(MainActivity.this, SimpleComposeMapviewActivity.class),
                        R.drawable.create_map_by_code));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_support_map_frag_title,
                        R.string.activity_basic_support_map_frag_description,
                        new Intent(MainActivity.this, SupportMapFragmentActivity.class),
                        R.drawable.create_map_by_fragment));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_init_with_building_title,
                        R.string.activity_basic_init_with_building_description,
                        new Intent(MainActivity.this, MapxusMapInitWithBuildingParamsActivity.class),
                        R.drawable.create_map_with_scene));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_init_with_poi_title,
                        R.string.activity_basic_init_with_poi_description,
                        new Intent(MainActivity.this, MapxusMapInitWithPoiParamsActivity.class),
                        R.drawable.create_map_with_poi));

                currentCategory = R.id.nav_map_creation;
                break;
        }

        adapter.notifyDataSetChanged();

        // Scrolls recycler view back to top.
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPositionWithOffset(0, 0);
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CURRENT_CATEGORY", currentCategory);
        outState.putString("CURRENT_CATEGORY_TOOLBAR_TITLE", categoryTitleForToolbar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void methodRequiresPermission() {
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
        };
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "request permission", 1010, perms);
        }
    }
}
