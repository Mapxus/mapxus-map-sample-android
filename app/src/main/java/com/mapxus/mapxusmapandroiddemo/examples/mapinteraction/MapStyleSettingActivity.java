package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.Style;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;

public class MapStyleSettingActivity extends AppCompatActivity implements View.OnClickListener, PopupWindow.OnDismissListener {

    public static final int STYLE = 1;
    public static final int LANGUAGE = 2;
    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapxusMap mapxusMap;
    private PopupWindow popupWindow;
    private int navigationHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_style_default);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);

        SwitchCompat switchButton = findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> mapxusMap.setHiddenOutdoor(isChecked));

        mapViewProvider.getMapxusMapAsync(mapxusMap -> this.mapxusMap = mapxusMap);

        findViewById(R.id.btn_style).setOnClickListener(this);
        findViewById(R.id.btn_laguage).setOnClickListener(this);

        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = getResources().getDimensionPixelSize(resourceId);
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
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void openPopupWindow(View v, int type) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        int resource = type == STYLE ? R.layout.popup_window_view_style : R.layout.popup_window_view_language;
        View view = LayoutInflater.from(this).inflate(resource, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, navigationHeight);
        popupWindow.setOnDismissListener(this);
        setOnPopupViewClick(view, type);
        setBackgroundAlpha(0.8f);
    }

    private void setOnPopupViewClick(View view, int type) {
        view.findViewById(R.id.btn_cancel).setOnClickListener(popupWindowItemClickListener);
        if (type == STYLE) {
            view.findViewById(R.id.tv_common).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_chritmas).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_hallowmas).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_mappybee).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_mapxus).setOnClickListener(popupWindowItemClickListener);
        } else {
            view.findViewById(R.id.tv_default).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_en).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_zh_hant).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_zh_hans).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_ja).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_ko).setOnClickListener(popupWindowItemClickListener);
        }
    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    private View.OnClickListener popupWindowItemClickListener = v -> {
        switch (v.getId()) {
            case R.id.tv_common:
                mapViewProvider.setStyle(Style.COMMON);
                break;
            case R.id.tv_chritmas:
                mapViewProvider.setStyle(Style.CHRISTMAS);
                break;
            case R.id.tv_hallowmas:
                mapViewProvider.setStyle(Style.HALLOWMAS);
                break;
            case R.id.tv_mappybee:
                mapViewProvider.setStyle(Style.MAPPYBEE);
                break;
            case R.id.tv_mapxus:
                mapViewProvider.setStyle(Style.MAPXUS);
                break;
            case R.id.tv_default:
                mapViewProvider.setLanguage("default");
                break;
            case R.id.tv_en:
                mapViewProvider.setLanguage("en");
                break;
            case R.id.tv_zh_hant:
                mapViewProvider.setLanguage("zh-Hant");
                break;
            case R.id.tv_zh_hans:
                mapViewProvider.setLanguage("zh-Hans");
                break;
            case R.id.tv_ja:
                mapViewProvider.setLanguage("ja");
                break;
            case R.id.tv_ko:
                mapViewProvider.setLanguage("ko");
                break;
            case R.id.btn_cancel:
                break;
        }
        popupWindow.dismiss();
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_style:
                openPopupWindow(v, STYLE);
                break;
            case R.id.btn_laguage:
                openPopupWindow(v, LANGUAGE);
                break;
        }
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }
}