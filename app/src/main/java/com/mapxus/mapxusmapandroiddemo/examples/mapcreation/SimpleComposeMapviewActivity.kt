package com.mapxus.mapxusmapandroiddemo.examples.mapcreation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapxus.map.mapxusmap.api.map.model.MapxusMapOptions
import com.mapxus.mapxusmapandroiddemo.R
import com.mapxus.mapxusmapandroiddemo.compose.MapxusMap


private const val TAG = "SimpleComposeMapviewActivity"

class SimpleComposeMapviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapxusMap(
                        mapboxMapOptions = MapboxMapOptions.createFromAttributes(this)
                            .maxZoomPreference(22.0)
                            .minZoomPreference(15.0),
                        mapxusMapOptions = MapxusMapOptions()
                            .setBuildingId(getString(R.string.default_search_text_building_id))
                    )
                }
            }
        }
    }
}