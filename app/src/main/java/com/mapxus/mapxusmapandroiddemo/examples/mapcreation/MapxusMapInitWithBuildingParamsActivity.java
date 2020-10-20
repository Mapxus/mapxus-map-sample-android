package com.mapxus.mapxusmapandroiddemo.examples.mapcreation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mapxus.mapxusmapandroiddemo.R;

public class MapxusMapInitWithBuildingParamsActivity extends AppCompatActivity {

    Button btnCreate;
    EditText etBuildingId, etFloorName, etTop, etBottom, etLeft, etRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapxus_map_init_with_building_params);
        btnCreate = findViewById(R.id.create);
        etBuildingId = findViewById(R.id.et_id);
        etFloorName = findViewById(R.id.et_floor_name);
        etTop = findViewById(R.id.et_top);
        etBottom = findViewById(R.id.et_bottom);
        etLeft = findViewById(R.id.et_left);
        etRight = findViewById(R.id.et_right);
        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapxusMapInitWithBuildingActivity.class);
            intent.putExtra("building_id", etBuildingId.getText().toString().trim());
            intent.putExtra("floor_name", etFloorName.getText().toString().trim());
            intent.putExtra("top", etTop.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etTop.getText().toString().trim()));
            intent.putExtra("bottom", etBottom.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etBottom.getText().toString().trim()));
            intent.putExtra("left", etLeft.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etLeft.getText().toString().trim()));
            intent.putExtra("right", etRight.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etRight.getText().toString().trim()));
            startActivity(intent);
        });
    }
}