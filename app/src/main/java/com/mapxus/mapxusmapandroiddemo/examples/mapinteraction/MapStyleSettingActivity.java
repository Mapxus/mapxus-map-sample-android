package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.BuildingBorderStyle;
import com.mapxus.map.mapxusmap.api.map.model.MapLanguage;
import com.mapxus.map.mapxusmap.api.map.model.Style;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;

import org.jetbrains.annotations.NotNull;

public class MapStyleSettingActivity extends AppCompatActivity implements View.OnClickListener, PopupWindow.OnDismissListener {

    public static final int STYLE = 1;
    public static final int LANGUAGE = 2;
    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapxusMap mapxusMap;
    private PopupWindow popupWindow;
    private int navigationHeight;

    private final String[] languages = {MapLanguage.DEFAULT, MapLanguage.EN, MapLanguage.ZH_HK, MapLanguage.ZH_TW, MapLanguage.ZH_CN, MapLanguage.JA, MapLanguage.KO, MapLanguage.AR, MapLanguage.FIL, MapLanguage.ID, MapLanguage.PT, MapLanguage.TH, MapLanguage.VI};

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
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.text_list_item, languages);
        AutoCompleteTextView autoCompleteTextView = ((AutoCompleteTextView) ((TextInputLayout) findViewById(R.id.language_field)).getEditText());
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            mapViewProvider.setLanguage(languages[position]);
        });
        findViewById(R.id.btn_building_outline_style).setOnClickListener(this);

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
            view.findViewById(R.id.tv_mapxus_section).setOnClickListener(popupWindowItemClickListener);
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
            case R.id.tv_mapxus_section:
                mapViewProvider.setCustomStyle("mapxus_v7_with_section");
                break;
            case R.id.tv_default:
                mapViewProvider.setLanguage(MapLanguage.DEFAULT);
                break;
            case R.id.tv_en:
                mapViewProvider.setLanguage(MapLanguage.EN);
                break;
            case R.id.tv_zh_hant:
                mapViewProvider.setLanguage(MapLanguage.ZH_HK);
                break;
            case R.id.tv_zh_hans:
                mapViewProvider.setLanguage(MapLanguage.ZH_CN);
                break;
            case R.id.tv_ja:
                mapViewProvider.setLanguage(MapLanguage.JA);
                break;
            case R.id.tv_ko:
                mapViewProvider.setLanguage(MapLanguage.KO);
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
            case R.id.btn_building_outline_style:
                openBottomSheetDialog();
                break;
        }
    }

    private void openBottomSheetDialog() {
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog(this);
        View bottomSheetDialogView = bottomSheetDialog.setStyle(R.layout.bottomsheet_dialog_building_outline_style, this);
        bottomSheetDialogView.findViewById(R.id.create).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            EditText etOpacity = bottomSheetDialogView.findViewById(R.id.et_building_outline_opacity);
            EditText etLineColor = bottomSheetDialogView.findViewById(R.id.et_building_outline_color);
            EditText etLineWidth = bottomSheetDialogView.findViewById(R.id.et_building_outline_line_width);

            BuildingBorderStyle buildingBorderStyle = new BuildingBorderStyle();
            try {
                buildingBorderStyle.setLineOpacity(Expression.literal(Float.parseFloat(etOpacity.getText().toString())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                buildingBorderStyle.setLineColor(Expression.color(Color.parseColor(String.format("#%s", etLineColor.getText().toString()))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                buildingBorderStyle.setLineWidth(Expression.literal(Float.parseFloat(etLineWidth.getText().toString())));
            } catch (Exception e) {
                e.printStackTrace();
            }

            mapxusMap.getMapxusUiSettings().setSelectedBuildingBorderStyle(buildingBorderStyle);
        });
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }
}