package com.mapxus.mapxusmapandroiddemo.examples.integrationcases;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMapOptions;
import com.mapxus.map.mapxusmap.api.map.model.Venue;
import com.mapxus.map.mapxusmap.api.services.model.building.FloorInfo;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.visual.MapxusVisual;
import com.mapxus.visual.VisualEventListener;
import com.mapxus.visual.models.BuildingImage;
import com.mapxus.visual.models.Node;
import com.mapxus.visual.overlay.polyline.VisualPolylineOverlay;
import com.mapxus.visual.repository.image.VisualImageRepository;

import org.jetbrains.annotations.NotNull;

public class DisplayVisualActivity extends AppCompatActivity implements MapxusMap.OnBuildingChangeListener, MapxusMap.OnFloorChangedListener {

    private MapView mapView;
    private MapxusMap mapxusMap;
    private MapboxMap mapboxMap;
    private Button switchBtn;

    private CheckBox visualCheckbox;

    private MapxusVisual mapxusVisual;
    private VisualImageRepository visualImageRepository;

    private VisualPolylineOverlay visualPolylineOverlay;

    private RelativeLayout.LayoutParams bigViewLayoutParams;
    private RelativeLayout.LayoutParams smallViewLayoutParams;

    private boolean mapViewIsBig = true;

    private String lastShowVisualBuildingId = "";

    private double preMapZoomLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_visual);

        mapView = findViewById(R.id.mapView);
        mapxusVisual = findViewById(R.id.visual_view);
        switchBtn = findViewById(R.id.btn_switch);
        visualCheckbox = findViewById(R.id.cb_visual);
        mapView.onCreate(savedInstanceState);
        MapViewProvider mapViewProvider = new MapboxMapViewProvider(this, mapView, new MapxusMapOptions().setBuildingId(getString(R.string.default_visual_map_building_id)));
        mapViewProvider.getMapxusMapAsync(mapxusMap -> {
            this.mapxusMap = mapxusMap;
            initListener();
        });

        mapView.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;
            mapboxMap.getUiSettings().setCompassEnabled(false);
        });

        visualImageRepository = new VisualImageRepository(DisplayVisualActivity.this);

        bigViewLayoutParams = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
        smallViewLayoutParams = (RelativeLayout.LayoutParams) mapxusVisual.getLayoutParams();

    }

    /**
     * 添加监听
     */
    private void initListener() {

        mapxusMap.addOnFloorChangedListener(this);

        /*
          视觉地图显示变化时相应对地图显示路线做一些变化
         */
        mapxusVisual.addEventListener(new VisualEventListener() {
            @Override
            public void bearingChanged(double bearing) {
                if (visualPolylineOverlay != null) {
                    visualPolylineOverlay.updateMarkerRotate((float) bearing);
                }

            }

            @Override
            public void loadingChanged(boolean b) {

            }

            @Override
            public void nodeChanged(Node node) {
                if (visualPolylineOverlay != null) {
                    visualPolylineOverlay.setMapMarker(node.getKey());
                }

            }

            @Override
            public void renderComplete() {

            }
        });

        //切换显示大小屏监听
        switchBtn.setOnClickListener(v -> {

            if (mapViewIsBig) {
                mapxusVisual.setLayoutParams(bigViewLayoutParams);
                mapView.setLayoutParams(smallViewLayoutParams);
                mapxusMap.getMapxusUiSettings().setSelectorEnabled(false);
                preMapZoomLevel = mapxusMap.getCameraPosition().zoom;
                mapboxMap.animateCamera(CameraUpdateFactory.zoomTo(Double.parseDouble(getString(R.string.default_visual_zoom))));
                mapView.bringToFront();
                mapViewIsBig = false;
                mapxusVisual.resize();
                visualCheckbox.setVisibility(View.GONE);
            } else {
                mapView.setLayoutParams(bigViewLayoutParams);
                mapxusVisual.setLayoutParams(smallViewLayoutParams);
                mapxusMap.getMapxusUiSettings().setSelectorEnabled(true);
                mapboxMap.animateCamera(CameraUpdateFactory.zoomTo(preMapZoomLevel));
                mapxusVisual.bringToFront();
                mapxusVisual.resize();
                mapViewIsBig = true;
                visualCheckbox.setVisibility(View.VISIBLE);
                visualCheckbox.bringToFront();
            }
        });

        /*
          是否打开视觉地图
         */
        visualCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mapxusMap.addOnBuildingChangeListener(DisplayVisualActivity.this);
                String currentBuildingId = mapxusMap.getCameraPosition().buildingId;
                //查询当前建筑中的图片
                visualImageRepository.queryImages(currentBuildingId, visualMapImageQueryListener);
            } else {
                mapxusVisual.setVisibility(View.GONE);
                mapxusMap.removeOnBuildingChangeListener(DisplayVisualActivity.this);
                if (visualPolylineOverlay != null) {
                    visualPolylineOverlay.removeFromMap();
                }
                switchBtn.setVisibility(View.GONE);
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onBuildingChange(IndoorBuilding indoorBuilding) {
        //地图建筑变化，查询新建筑的图片
        if (indoorBuilding != null && !lastShowVisualBuildingId.equals(indoorBuilding.getBuildingId())) {
            visualImageRepository.queryImages(indoorBuilding.getBuildingId(), visualMapImageQueryListener);
        }

    }

    /**
     * 查询当前建筑的所有图片的监听返回，显示图片路径并设置路径点击事件
     */
    private VisualImageRepository.VisualMapImageQueryListener visualMapImageQueryListener = new VisualImageRepository.VisualMapImageQueryListener() {
        @Override
        public void onQueryImageSucceeded(BuildingImage buildingImage) {

            lastShowVisualBuildingId = buildingImage.getBuildingId();
            if (visualPolylineOverlay != null) {
                visualPolylineOverlay.removeFromMap();
                visualPolylineOverlay = null;
            }
            //显示图片的路径
            visualPolylineOverlay = new VisualPolylineOverlay(DisplayVisualActivity.this, mapboxMap, mapxusMap, buildingImage);
            if (!DisplayVisualActivity.this.isDestroyed()) visualPolylineOverlay.addToMap();
            visualPolylineOverlay.setOnPolylineClickListener(polylineClickListener);
        }

        @Override
        public void onQueryImageFailed(String error) {

        }
    };

    /**
     * 点击地图显示的路线时的监听，添加当前显示点、切换Visual显示的图
     */
    private VisualPolylineOverlay.OnPolylineClickListener polylineClickListener = new VisualPolylineOverlay.OnPolylineClickListener() {
        @Override
        public void onPolylineClick(String imageKey) {

            visualPolylineOverlay.setMapMarker(imageKey);

            if (mapxusVisual.getVisibility() == View.GONE) {
                mapxusVisual.setVisibility(View.VISIBLE);
                mapxusVisual.resize();
                switchBtn.setVisibility(View.VISIBLE);
            }
            mapxusVisual.moveToKey(imageKey);

        }
    };

    @Override
    public void onFloorChange(@Nullable Venue venue, @Nullable IndoorBuilding indoorBuilding, @Nullable FloorInfo floorInfo) {
        mapxusVisual.setVisibility(View.GONE);
        switchBtn.setVisibility(View.GONE);
    }

}
