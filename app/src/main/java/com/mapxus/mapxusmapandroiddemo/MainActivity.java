package com.mapxus.mapxusmapandroiddemo;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.perf.metrics.AddTrace;

import com.mapxus.mapxusmapandroiddemo.adapter.ExampleAdapter;
import com.mapxus.mapxusmapandroiddemo.examples.annotations.AnimatedMarkerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.annotations.DrawCustomMarkerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.annotations.DrawMarkerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.annotations.DrawPolygonActivity;
import com.mapxus.mapxusmapandroiddemo.examples.annotations.PolygonHolesActivity;
import com.mapxus.mapxusmapandroiddemo.examples.basics.MapxusMapInitWithBuildingActivity;
import com.mapxus.mapxusmapandroiddemo.examples.basics.MapxusMapInitWithPoiActivity;
import com.mapxus.mapxusmapandroiddemo.examples.basics.MapxusMapOptionActivity;
import com.mapxus.mapxusmapandroiddemo.examples.basics.MapxusMapWithoutOutdoorActivity;
import com.mapxus.mapxusmapandroiddemo.examples.basics.SimpleMapViewActivity;
import com.mapxus.mapxusmapandroiddemo.examples.basics.SupportMapFragmentActivity;
import com.mapxus.mapxusmapandroiddemo.examples.camera.AnimateMapCameraActivity;
import com.mapxus.mapxusmapandroiddemo.examples.camera.RestrictCameraActivity;
import com.mapxus.mapxusmapandroiddemo.examples.controllers.SelectorPositionActivity;
import com.mapxus.mapxusmapandroiddemo.examples.controllers.HiddenSelectorActivity;
import com.mapxus.mapxusmapandroiddemo.examples.displaylocation.LocationProviderActivity;
import com.mapxus.mapxusmapandroiddemo.examples.listener.BuildingAndFloorChangeListenerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.listener.CameraListenerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.listener.PoiClickListenerActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.RoutePlanningActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchBuildingDetailActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchBuildingGlobalActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchBuildingInboundActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchBuildingNearbyActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiDetailActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiInBuildingActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiInboundActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiNearbyActivity;
import com.mapxus.mapxusmapandroiddemo.examples.searchservices.SearchPoiWithOrientationActivity;
import com.mapxus.mapxusmapandroiddemo.examples.styles.DefaultStyleActivity;
import com.mapxus.mapxusmapandroiddemo.model.ExampleItemModel;
import com.mapxus.mapxusmapandroiddemo.model.views.CircularPagerIndicatorDecoration;
import com.mapxus.mapxusmapandroiddemo.utils.ItemClickSupport;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.mapxus.mapxusmapandroiddemo.utils.StringConstants.SKIPPED_KEY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Used to track internal navigation to the Snapshotter section
    public static String EXTRA_NAV = "EXTRA_NAV";

    private ArrayList<ExampleItemModel> exampleItemModels;
    private ExampleAdapter adapter;
    private int currentCategory = R.id.nav_basics;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private String categoryTitleForToolbar;
    private Timer timer = new Timer();

    @Override
    @AddTrace(name = "onCreateMainActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (savedInstanceState == null) {
            toolbar.setTitle(" ");
            setSupportActionBar(toolbar);

        }

        exampleItemModels = new ArrayList<>();

        // Create the adapter to convert the array to views
        adapter = new ExampleAdapter(this, exampleItemModels);
        // Attach the adapter to a ListView
        recyclerView = (RecyclerView) findViewById(R.id.details_list);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);

            recyclerView.addItemDecoration(new CircularPagerIndicatorDecoration());
        }
        if (savedInstanceState != null) {
            currentCategory = savedInstanceState.getInt("CURRENT_CATEGORY");
            categoryTitleForToolbar = savedInstanceState.getString("CURRENT_CATEGORY_TOOLBAR_TITLE");
//            toolbar.setTitle(categoryTitleForToolbar);
            listItems(currentCategory);
        } else {
            listItems(R.id.nav_basics);
        }

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            @AddTrace(name = "onItemClicked")
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {
                startActivity(exampleItemModels.get(position).getActivity());
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_basics);
        }
//
//        loggedIn = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
//                .getBoolean(TOKEN_SAVED_KEY, false);
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean(SKIPPED_KEY, true)
                .apply();

        TextView versionTv = findViewById(R.id.tv_version);
        versionTv.setText(BuildConfig.VERSION_NAME);
        versionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.checkUpgrade();
            }
        });

        methodRequiresPermission();

        checkUpgrade();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
//            toolbar.setTitle(categoryTitleForToolbar);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @AddTrace(name = "listItems")
    private void listItems(int id) {
        exampleItemModels.clear();
        switch (id) {
            case R.id.nav_styles:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_styles_default_title,
                        R.string.activity_styles_default_description,
                        new Intent(MainActivity.this, DefaultStyleActivity.class),
                        R.drawable.styles_2_1, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_beemap_without_outdoor_title,
                        R.string.activity_basic_beemap_without_outdoor_description,
                        new Intent(MainActivity.this, MapxusMapWithoutOutdoorActivity.class),
                        R.drawable.styles_2_2, false, BuildConfig.MIN_SDK_VERSION));

                currentCategory = R.id.nav_styles;
                break;



            case R.id.nav_annotations:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_annotation_marker_title,
                        R.string.activity_annotation_custom_marker_description,
                        new Intent(MainActivity.this, DrawMarkerActivity.class),
                        R.drawable.annotations_3_1, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_annotation_custom_marker_title,
                        R.string.activity_annotation_custom_marker_description,
                        new Intent(MainActivity.this, DrawCustomMarkerActivity.class),
                        R.drawable.annotations_3_2, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_annotation_polygon_title,
                        R.string.activity_annotation_polygon_description,
                        new Intent(MainActivity.this, DrawPolygonActivity.class),
                        R.drawable.annotations_3_3, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_annotation_polygon_holes_title,
                        R.string.activity_annotation_polygon_holes_description,
                        new Intent(MainActivity.this, PolygonHolesActivity.class),
                        R.drawable.annotations_3_4, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_annotation_animated_marker_title,
                        R.string.activity_annotation_animated_marker_description,
                        new Intent(MainActivity.this, AnimatedMarkerActivity.class),
                        R.drawable.annotations_3_5, false, BuildConfig.MIN_SDK_VERSION));

                currentCategory = R.id.nav_annotations;
                break;

            case R.id.nav_camera:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_camera_animate_title,
                        R.string.activity_camera_animate_description,
                        new Intent(MainActivity.this, AnimateMapCameraActivity.class),
                        R.drawable.camera_4_1, false, BuildConfig.MIN_SDK_VERSION));

//                exampleItemModels.add(new ExampleItemModel(
//                        R.string.activity_camera_bounding_box_title,
//                        R.string.activity_camera_bounding_box_description,
//                        new Intent(MainActivity.this, BoundingBoxCameraActivity.class),
//                        R.string.activity_camera_bounding_box_url, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_camera_restrict_title,
                        R.string.activity_camera_restrict_description,
                        new Intent(MainActivity.this, RestrictCameraActivity.class),
                        R.drawable.camera_4_2, false, BuildConfig.MIN_SDK_VERSION));

                currentCategory = R.id.nav_camera;
                break;

            case R.id.nav_listener:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_listener_building_floor_change_title,
                        R.string.activity_listener_building_floor_change_description,
                        new Intent(MainActivity.this, BuildingAndFloorChangeListenerActivity.class),
                        R.drawable.listener_1_1, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_listener_poi_click_title,
                        R.string.activity_listener_poi_click_description,
                        new Intent(MainActivity.this, PoiClickListenerActivity.class),
                        R.drawable.listener_2_1, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_listener_camera_change_listener_title,
                        R.string.activity_listener_camera_change_listener_description,
                        new Intent(MainActivity.this, CameraListenerActivity.class),
                        R.drawable.listener_3_1, false, BuildConfig.MIN_SDK_VERSION));

                currentCategory = R.id.nav_listener;
                break;

            case R.id.nav_search_services:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_route_planning_title,
                        R.string.activity_search_service_route_planning_description,
                        new Intent(MainActivity.this, RoutePlanningActivity.class),
                        R.drawable.searchservice_06_1, false, BuildConfig.MIN_SDK_VERSION));
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_building_nearby_title,
                        R.string.activity_search_service_building_nearby_description,
                        new Intent(MainActivity.this, SearchBuildingNearbyActivity.class),
                        R.drawable.searchservice_06_2, false, BuildConfig.MIN_SDK_VERSION));
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_building_in_bound_title,
                        R.string.activity_search_service_building_in_bound_description,
                        new Intent(MainActivity.this, SearchBuildingInboundActivity.class),
                        R.drawable.searchservice_06_3, false, BuildConfig.MIN_SDK_VERSION));
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_building_by_id_title,
                        R.string.activity_search_service_building_by_id_description,
                        new Intent(MainActivity.this, SearchBuildingDetailActivity.class),
                        R.drawable.searchservice_06_4, false, BuildConfig.MIN_SDK_VERSION));
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_building_global_title,
                        R.string.activity_search_service_building_global_description,
                        new Intent(MainActivity.this, SearchBuildingGlobalActivity.class),
                        R.drawable.searchservice_06_5, false, BuildConfig.MIN_SDK_VERSION));
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_nearby_title,
                        R.string.activity_search_service_poi_nearby_description,
                        new Intent(MainActivity.this, SearchPoiNearbyActivity.class),
                        R.drawable.searchservice_06_6, false, BuildConfig.MIN_SDK_VERSION));
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_in_bound_title,
                        R.string.activity_search_service_poi_in_bound_description,
                        new Intent(MainActivity.this, SearchPoiInboundActivity.class),
                        R.drawable.searchservice_06_7, false, BuildConfig.MIN_SDK_VERSION));
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_by_id_title,
                        R.string.activity_search_service_poi_by_id_description,
                        new Intent(MainActivity.this, SearchPoiDetailActivity.class),
                        R.drawable.searchservice_06_8, false, BuildConfig.MIN_SDK_VERSION));
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_in_building_title,
                        R.string.activity_search_service_poi_in_building_description,
                        new Intent(MainActivity.this, SearchPoiInBuildingActivity.class),
                        R.drawable.searchservice_06_9, false, BuildConfig.MIN_SDK_VERSION));
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_search_service_poi_with_orientation,
                        R.string.activity_search_service_poi_with_orientation_description,
                        new Intent(MainActivity.this, SearchPoiWithOrientationActivity.class),
                        R.drawable.searchservice_06_9, false, BuildConfig.MIN_SDK_VERSION));
                currentCategory = R.id.nav_search_services;
                break;

            case R.id.nav_dispay_location:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_display_location_title,
                        R.string.activity_display_location_description,
                        new Intent(MainActivity.this, LocationProviderActivity.class),
                        R.drawable.location_07_1, false, BuildConfig.MIN_SDK_VERSION));

                currentCategory = R.id.nav_dispay_location;
                break;

            case R.id.nav_controllers:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_selector_position_title,
                        R.string.activity_selector_position_description,
                        new Intent(MainActivity.this, SelectorPositionActivity.class),
                        R.drawable.controller_2, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_hidden_selector_title,
                        R.string.activity_hidden_selector_description,
                        new Intent(MainActivity.this, HiddenSelectorActivity.class),
                        R.drawable.controller_1, false, BuildConfig.MIN_SDK_VERSION));

                currentCategory = R.id.nav_controllers;
                break;

            default:
                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_simple_mapview_title,
                        R.string.activity_basic_simple_mapview_description,
                        new Intent(MainActivity.this, SimpleMapViewActivity.class),
                        R.drawable.getstarted_1_1, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_support_map_frag_title,
                        R.string.activity_basic_support_map_frag_description,
                        new Intent(MainActivity.this, SupportMapFragmentActivity.class),
                        R.drawable.getstarted_1_2, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_beemap_options_title,
                        R.string.activity_basic_beemap_options_description,
                        new Intent(MainActivity.this, MapxusMapOptionActivity.class),
                        R.drawable.getstarted_1_3, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_beemap_init_with_building_title,
                        R.string.activity_basic_beemap_init_with_building_description,
                        new Intent(MainActivity.this, MapxusMapInitWithBuildingActivity.class),
                        R.drawable.getstarted_1_4, false, BuildConfig.MIN_SDK_VERSION));

                exampleItemModels.add(new ExampleItemModel(
                        R.string.activity_basic_beemap_init_with_poi_title,
                        R.string.activity_basic_beemap_init_with_poi_description,
                        new Intent(MainActivity.this, MapxusMapInitWithPoiActivity.class),
                        R.drawable.getstarted_1_5, false, BuildConfig.MIN_SDK_VERSION));


                currentCategory = R.id.nav_basics;
                break;
        }

        verifySdkVersion();
        adapter.notifyDataSetChanged();

        // Scrolls recycler view back to top.
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    private void verifySdkVersion() {
        for (Iterator<ExampleItemModel> iterator = exampleItemModels.iterator(); iterator.hasNext(); ) {
            ExampleItemModel model = iterator.next();
            if (model != null && Build.VERSION.SDK_INT < model.getMinSdkVersion()) {
                iterator.remove();
            }
        }
    }

    public int getCurrentCategory() {
        return currentCategory;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate toolbar items
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CURRENT_CATEGORY", currentCategory);
        outState.putString("CURRENT_CATEGORY_TOOLBAR_TITLE", categoryTitleForToolbar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(1010)
    private void methodRequiresPermission() {
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "request permission", 1010, perms);
        }
    }


    private void checkUpgrade() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Beta.checkUpgrade(false, false);
            }
        }, 4000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
