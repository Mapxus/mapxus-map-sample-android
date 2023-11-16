package com.mapxus.mapxusmapandroiddemo.examples.mapcreation

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapxus.map.mapxusmap.api.map.MapViewProvider
import com.mapxus.map.mapxusmap.api.map.MapxusMap
import com.mapxus.map.mapxusmap.api.map.model.MapxusMapOptions
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider
import com.mapxus.mapxusmapandroiddemo.R


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

@Composable
private fun MapxusMap(
    modifier: Modifier = Modifier,
    mapboxMapOptions: MapboxMapOptions? = null,
    mapxusMapOptions: MapxusMapOptions = MapxusMapOptions(),
    onGetMapboxMap: (MapboxMap) -> Unit = {},
    onGetMapxusMap: (MapxusMap) -> Unit = {}
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context, mapboxMapOptions) }
    val mapViewProvider = remember {
        MapboxMapViewProvider(
            context,
            mapView,
            mapxusMapOptions
        )
    }

    var mapxusMap by remember { mutableStateOf<MapxusMap?>(null) }
    var mapboxMap by remember { mutableStateOf<MapboxMap?>(null) }

    MapLifecycle(mapView)

    mapxusMap?.let {
        MapxusLifeCycle(it, mapViewProvider)
    }

    AndroidView(
        factory = {
            mapView
        },
        modifier = modifier
    )

    LaunchedEffect(Unit) {
        mapView.getMapAsync {
            mapboxMap = it
            onGetMapboxMap(it)
        }

        mapViewProvider.getMapxusMapAsync {
            mapxusMap = it
            onGetMapxusMap(it)
        }
    }
}

@Composable
private fun MapLifecycle(mapView: MapView) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val previousState = remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }

    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver(previousState)
        val callbacks = mapView.componentCallbacks()

        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)
        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)
        }
    }
    DisposableEffect(mapView) {
        onDispose {
            Log.i(TAG, "MapView onDispose")
            mapView.onDestroy()
            mapView.removeAllViews()
        }
    }
}

@Composable
private fun MapxusLifeCycle(mapxusMap: MapxusMap, mapViewProvider: MapViewProvider) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(context, lifecycle, mapxusMap) {
        val mapLifecycleObserver = mapxusMap.lifecycleObserver()
        lifecycle.addObserver(mapLifecycleObserver)
        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
        }
    }

    DisposableEffect(mapViewProvider) {
        onDispose {
            Log.i(TAG, "MapViewProvider onDestroy")
            mapViewProvider.onDestroy()
        }
    }

}

private fun MapxusMap.lifecycleObserver(): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                Log.i(TAG, "MapxusMap onResume")
                this.onResume()
            }

            Lifecycle.Event.ON_PAUSE -> {
                Log.i(TAG, "MapxusMap onPause")
                this.onPause()
            }

            else -> {
                //ignore
            }
        }
    }

private fun MapView.lifecycleObserver(previousState: MutableState<Lifecycle.Event>): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.i(TAG, "MapView onCreate")
                if (previousState.value != Lifecycle.Event.ON_STOP) {
                    this.onCreate(Bundle())
                }
            }

            Lifecycle.Event.ON_START -> {
                Log.i(TAG, "MapView onStart")
                this.onStart()
            }

            Lifecycle.Event.ON_RESUME -> {
                Log.i(TAG, "MapView onResume")
                this.onResume()
            }

            Lifecycle.Event.ON_PAUSE -> {
                Log.i(TAG, "MapView onPause")
                this.onPause()
            }

            Lifecycle.Event.ON_STOP -> {
                Log.i(TAG, "MapView onStop")
                this.onStop()
            }

            Lifecycle.Event.ON_DESTROY -> {
                //handled in onDispose
            }

            else -> {
                //ignore
            }
        }
        previousState.value = event
    }

private fun MapView.componentCallbacks(): ComponentCallbacks =
    object : ComponentCallbacks {
        override fun onConfigurationChanged(config: Configuration) {
            //ignore
        }

        override fun onLowMemory() {
            Log.i(TAG, "MapView onLowMemory")
            this@componentCallbacks.onLowMemory()
        }
    }