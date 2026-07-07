package com.mapxus.mapxusmapandroiddemo.examples.integrationcases;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMapOptions;
import com.mapxus.map.mapxusmap.impl.MapLibreMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.visual.MapxusVisual;
import com.mapxus.visual.VisualEventListener;
import com.mapxus.visual.models.BuildingImage;
import com.mapxus.visual.models.Node;
import com.mapxus.visual.overlay.polyline.VisualPolylineOverlay;
import com.mapxus.visual.repository.image.VisualImageRepository;

import org.jetbrains.annotations.NotNull;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;

public class DisplayVisualActivity extends AppCompatActivity implements MapxusMap.OnBuildingChangeListener {

    private MapView mapView;
    private MapxusMap mapxusMap;
    private MapLibreMap mapLibreMap;
    private Button switchBtn;

    private CheckBox visualCheckbox;

    private MapViewProvider mapViewProvider;
    private MapxusVisual mapxusVisual;

    private VisualImageRepository visualImageRepository;

    private VisualPolylineOverlay visualPolylineOverlay;

    private RelativeLayout.LayoutParams bigViewLayoutParams;
    private RelativeLayout.LayoutParams smallViewLayoutParams;

    private boolean mapViewIsBig = true;

    private String lastShowVisualBuildingId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_visual);

        mapView = findViewById(R.id.mapView);
        mapxusVisual = findViewById(R.id.visual_view);
        switchBtn = findViewById(R.id.btn_switch);
        visualCheckbox = findViewById(R.id.cb_visual);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapLibreMapViewProvider(this, mapView, new MapxusMapOptions().setBuildingId(getString(R.string.default_visual_map_building_id)));
        mapViewProvider.getMapxusMapAsync(mapxusMap -> {
            this.mapxusMap = mapxusMap;
            mapxusMap.getMapxusUiSettings().setBuildingSelectorEnabled(false);
            initListener();
        });

        mapView.getMapAsync(mapboxMap -> {
            this.mapLibreMap = mapboxMap;
            mapboxMap.getUiSettings().setCompassEnabled(false);
        });

        visualImageRepository = new VisualImageRepository(DisplayVisualActivity.this);

        bigViewLayoutParams = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
        smallViewLayoutParams = (RelativeLayout.LayoutParams) mapxusVisual.getLayoutParams();

    }

    /**
     * Initialize listeners.
     */
    private void initListener() {
        /*
           When visual map display changes, update map route display accordingly.
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

        // Listener for switching between large/small display
        switchBtn.setOnClickListener(v -> {
            if (mapViewIsBig) {
                mapxusVisual.setLayoutParams(bigViewLayoutParams);
                mapView.setLayoutParams(smallViewLayoutParams);
                mapxusMap.getMapxusUiSettings().setSelectorEnabled(false);
                mapView.bringToFront();
                mapViewIsBig = false;
                mapxusVisual.resize();
                visualCheckbox.setVisibility(View.GONE);
            } else {
                setMpaViewBig();
            }
        });

        /*
          Whether to enable visual map
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

    private void setMpaViewBig() {
        mapView.setLayoutParams(bigViewLayoutParams);
        mapxusVisual.setLayoutParams(smallViewLayoutParams);
        mapxusMap.getMapxusUiSettings().setSelectorEnabled(true);
        mapxusVisual.bringToFront();
        mapxusVisual.resize();
        mapViewIsBig = true;
        visualCheckbox.setVisibility(View.VISIBLE);
        visualCheckbox.bringToFront();
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
        // Map building changed: query images for the new building
        if (indoorBuilding != null && !lastShowVisualBuildingId.equals(indoorBuilding.getBuildingId())) {
            mapxusVisual.setVisibility(View.GONE);
            switchBtn.setVisibility(View.GONE);
            visualImageRepository.queryImages(indoorBuilding.getBuildingId(), visualMapImageQueryListener);

            if (!mapViewIsBig) {
                setMpaViewBig();
                visualCheckbox.setChecked(false);
            }
        }
    }

    /**
     * Listener for current building images: show image paths and set click handlers.
     */
    private VisualImageRepository.VisualMapImageQueryListener visualMapImageQueryListener = new VisualImageRepository.VisualMapImageQueryListener() {
        @Override
        public void onQueryImageSucceeded(BuildingImage buildingImage) {

            lastShowVisualBuildingId = buildingImage.getBuildingId();
            if (visualPolylineOverlay != null) {
                visualPolylineOverlay.removeFromMap();
                visualPolylineOverlay = null;
            }
            //show image paths
            visualPolylineOverlay = new VisualPolylineOverlay(DisplayVisualActivity.this, mapLibreMap, mapxusMap, buildingImage);
            if (!DisplayVisualActivity.this.isDestroyed()) visualPolylineOverlay.addToMap();
            visualPolylineOverlay.setOnPolylineClickListener(polylineClickListener);
        }

        @Override
        public void onQueryImageFailed(String error) {

        }
    };

    /**
     * Listener for clicking displayed route: add current display point, switch Visual display image.
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
}
