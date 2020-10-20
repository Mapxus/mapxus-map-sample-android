package com.mapxus.mapxusmapandroiddemo.examples.mapcreation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mapxus.mapxusmapandroiddemo.R;

public class MapxusMapInitWithPoiParamsActivity extends AppCompatActivity {
    EditText etPoiId, etZoomLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapxus_map_init_with_poi_params);
        Button btnCreate = findViewById(R.id.create);
        etPoiId = findViewById(R.id.et_poi_id);
        etZoomLevel = findViewById(R.id.et_zoom_level);
        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapxusMapInitWithPoiActivity.class);
            intent.putExtra("poi_id", etPoiId.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etPoiId.getText().toString().trim()));
            intent.putExtra("zoom_level", etZoomLevel.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etZoomLevel.getText().toString().trim()));
            startActivity(intent);
        });
    }
}