package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapxus.map.MapViewProvider;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.impl.MapboxMapViewProvider;
import com.mapxus.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.model.LatLng;
import com.mapxus.map.model.MapxusMarkerOptions;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.services.PoiSearch;
import com.mapxus.services.model.IndoorLatLng;
import com.mapxus.services.model.PoiOrientationSearchOption;
import com.mapxus.services.model.poi.PoiOrientationInfo;
import com.mapxus.services.model.poi.PoiOrientationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchPoiWithOrientationActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapxusMapReadyCallback {

    private static final String TAG = "SearchPoiWithOrientationActivity";

    private MapView mapView;
    private MapboxMap mMapboxMap;
    private MapViewProvider mapViewProvider;
    private MapxusMap mMapxusMap;

    private EditText mSearchText;
    private EditText mDistanceText;
    private int mOrientation;
    private int mDistance;

    private PoiSearch poiSearch;
    private IndoorLatLng indoorLatLng;

    private String MARKER_SOURCE = "orientation-result-marker-source";
    private String MARKER_LAYER = "orientation-result-marker-layer";
    private String MARKER_IMAGE = "orientation-result-marker-image";
    private String IMAGE_KEY = "orientation-result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_poi_with_orientation);

        // Setup the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(this);
        mapViewProvider.getMapxusMapAsync(this);

        mSearchText = findViewById(R.id.input_edittext);
        mDistanceText = findViewById(R.id.input_distance);
        TextView searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearchQuery();
            }
        });

        indoorLatLng = new IndoorLatLng();
        indoorLatLng.setLat(23.03566177875929);
        indoorLatLng.setLon(113.18252357250441);
        indoorLatLng.setBuildingId("vivocity_foshan_d3fmv9");
        indoorLatLng.setFloor("1");

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(adapter);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;
    }

    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        mMapxusMap = mapxusMap;
        MapxusMarkerOptions markerOptions = new MapxusMarkerOptions();
        markerOptions.setPosition(new LatLng(indoorLatLng.getLat(), indoorLatLng.getLon()));
        markerOptions.setFloor(indoorLatLng.getFloor());
        markerOptions.setBuildingId(indoorLatLng.getBuildingId());
        mMapxusMap.addMarker(markerOptions);
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        mOrientation = Integer.valueOf(mSearchText.getText().toString().trim());
        mDistance = Integer.valueOf(mDistanceText.getText().toString().trim());
        PoiOrientationSearchOption option = new PoiOrientationSearchOption();
        //当前手机朝向
        option.orientation(mOrientation);
        option.indoorLatLng(indoorLatLng);
        //搜索范围
        option.meterRadius(mDistance);
        poiSearch.searchPoiByOrientation(option);
    }

    private PoiSearch.PoiSearchResultListenerAdapter adapter = new PoiSearch.PoiSearchResultListenerAdapter() {
        @Override
        public void onGetPoiByOrientationResult(PoiOrientationResult poiOrientationResult) {
            //通过朝向查找poi结果返回
            if (poiOrientationResult.status != 0) {
                Toast.makeText(getApplicationContext(), poiOrientationResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (poiOrientationResult.getPoiOrientationInfos() == null || poiOrientationResult.getPoiOrientationInfos().isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.no_result), Toast.LENGTH_LONG).show();
                return;
            }

            List<Feature> markerFeatureList = new ArrayList<>();
            for (PoiOrientationInfo info : poiOrientationResult.getPoiOrientationInfos()) {
                Feature feature = Feature.fromGeometry(Point.fromLngLat(info.getLocation().getLon(),
                        info.getLocation().getLat()));
                feature.addStringProperty(IMAGE_KEY, MARKER_IMAGE + info.getAngle());
                markerFeatureList.add(feature);
                drawImage(info.getAngle());
            }
            removeMarkerLayer();
            FeatureCollection markerFeatureCollection = FeatureCollection.fromFeatures(markerFeatureList);
            Source markerSource = new GeoJsonSource(MARKER_SOURCE, markerFeatureCollection);
            mMapboxMap.getStyle().addSource(markerSource);


            SymbolLayer symbolLayer = new SymbolLayer(MARKER_LAYER, MARKER_SOURCE)
                    .withProperties(PropertyFactory.iconImage(Expression.get(IMAGE_KEY)));
            mMapboxMap.getStyle().addLayer(symbolLayer);

        }
    };

    /**
     * 清除图标
     */
    private void removeMarkerLayer() {
        Objects.requireNonNull(mMapboxMap.getStyle()).removeLayer(MARKER_LAYER);
        mMapboxMap.getStyle().removeSource(MARKER_SOURCE);
        mMapboxMap.getStyle().removeImage(MARKER_IMAGE);
    }

    private void drawImage(int angle) {
        Bitmap bitmap = Bitmap.createBitmap(60, 60, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //画圆
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 25, paint);

        //写字体
        Paint textPaint = new Paint();
        int size;
        if (angle < 10) {
            size = 40;
        } else if (angle < 100) {
            size = 30;
        } else {
            size = 20;
        }
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(size);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHigh = fontMetrics.descent - fontMetrics.ascent;
        canvas.drawText(angle + "", canvas.getWidth() / 2, canvas.getHeight() / 2 + textHigh / 2, textPaint);

        Objects.requireNonNull(mMapboxMap.getStyle()).addImage(MARKER_IMAGE + angle, bitmap);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
