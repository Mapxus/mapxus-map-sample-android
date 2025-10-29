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
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.BuildingBorderStyle;
import com.mapxus.map.mapxusmap.api.map.model.MapLanguage;
import com.mapxus.map.mapxusmap.api.map.model.Style;
import com.mapxus.map.mapxusmap.impl.MapLibreMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.customizeview.MyBottomSheetDialog;

import org.jetbrains.annotations.NotNull;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.style.expressions.Expression;

public class MapStyleSettingActivity extends AppCompatActivity implements View.OnClickListener, PopupWindow.OnDismissListener, MapView.OnDidFinishLoadingStyleListener {

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
        mapViewProvider = new MapLibreMapViewProvider(this, mapView);

        SwitchCompat switchButton = findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> mapxusMap.setHiddenOutdoor(isChecked));

        mapViewProvider.getMapxusMapAsync(mapxusMap -> this.mapxusMap = mapxusMap);

        findViewById(R.id.btn_style).setOnClickListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.text_list_item, languages);
        AutoCompleteTextView autoCompleteTextView = ((AutoCompleteTextView) ((TextInputLayout) findViewById(R.id.language_field)).getEditText());
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> setLanguage(languages[position]));
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
        mapView.removeOnDidFinishLoadingStyleListener(this);
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
            view.findViewById(R.id.tv_mocha_mousse).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_city_walk).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_pear_sorbet).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_rose_tea).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_mapxus).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_mapxus_section).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_mapxus_section_by_color).setOnClickListener(popupWindowItemClickListener);
            view.findViewById(R.id.tv_mapxus_section_by_category).setOnClickListener(popupWindowItemClickListener);
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
            case R.id.tv_mocha_mousse:
                setStyle(Style.MOCHA_MOUSSE);
                break;
            case R.id.tv_city_walk:
                setStyle(Style.CITY_WALK);
                break;
            case R.id.tv_pear_sorbet:
                setStyle(Style.PEAR_SORBET);
                break;
            case R.id.tv_rose_tea:
                setStyle(Style.ROSE_TEA);
                break;
            case R.id.tv_mapxus:
                setStyle(Style.MAPXUS);
                break;
            case R.id.tv_mapxus_section:
                setCustomStyle("mapxus_v7_with_section");
                break;
            case R.id.tv_mapxus_section_by_color:
                setStyle(Style.SECTION_DISPLAY_BY_COLOR);
                break;
            case R.id.tv_mapxus_section_by_category:
                setStyle(Style.SECTION_DISPLAY_BY_CATEGORY);
                break;
            case R.id.tv_default:
                setLanguage(MapLanguage.DEFAULT);
                break;
            case R.id.tv_en:
                setLanguage(MapLanguage.EN);
                break;
            case R.id.tv_zh_hant:
                setLanguage(MapLanguage.ZH_HK);
                break;
            case R.id.tv_zh_hans:
                setLanguage(MapLanguage.ZH_CN);
                break;
            case R.id.tv_ja:
                setLanguage(MapLanguage.JA);
                break;
            case R.id.tv_ko:
                setLanguage(MapLanguage.KO);
                break;
            case R.id.btn_cancel:
                break;
        }
        popupWindow.dismiss();
    };

    private String language;

    private void setLanguage(String language) {
        this.language = language;
        mapViewProvider.setLanguage(language);
    }

    private void setStyle(int style) {
        mapView.addOnDidFinishLoadingStyleListener(this);
        mapViewProvider.setStyle(style);
    }

    private void setCustomStyle(String style) {
        mapView.addOnDidFinishLoadingStyleListener(this);
        mapViewProvider.setCustomStyle(style);
    }

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

    @Override
    public void onDidFinishLoadingStyle() {
        if (language != null) {
            mapViewProvider.setLanguage(language);
        }
        mapView.removeOnDidFinishLoadingStyleListener(this);
    }
}