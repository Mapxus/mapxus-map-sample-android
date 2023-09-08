package com.mapxus.mapxusmapandroiddemo.examples.mapcreation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.mapxus.mapxusmapandroiddemo.R;

public class MapxusMapInitWithBuildingParamsActivity extends AppCompatActivity {

    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapxus_map_init_with_building_params);
        btnCreate = findViewById(R.id.create);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        EditText etId = findViewById(R.id.et_id);
        EditText etTop = findViewById(R.id.et_top);
        EditText etBottom = findViewById(R.id.et_bottom);
        EditText etLeft = findViewById(R.id.et_left);
        EditText etRight = findViewById(R.id.et_right);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        etId.setText(getString(R.string.default_floor_id));
                        break;
                    case 1:
                        etId.setText(getString(R.string.default_search_text_building_id));
                        break;
                    default:
                        etId.setText(getString(R.string.default_venue_id));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapxusMapInitWithBuildingActivity.class);
            intent.putExtra("id", etId.getText().toString().trim());
            intent.putExtra("selectedTab", tabLayout.getSelectedTabPosition());
            intent.putExtra("top", etTop.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etTop.getText().toString().trim()));
            intent.putExtra("bottom", etBottom.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etBottom.getText().toString().trim()));
            intent.putExtra("left", etLeft.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etLeft.getText().toString().trim()));
            intent.putExtra("right", etRight.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(etRight.getText().toString().trim()));
            startActivity(intent);
        });
    }
}