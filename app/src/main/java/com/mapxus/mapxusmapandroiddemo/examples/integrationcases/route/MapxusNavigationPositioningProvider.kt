package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.route

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapxus.map.mapxusmap.api.services.model.building.FloorInfo
import com.mapxus.map.mapxusmap.api.services.model.planning.PathDto
import com.mapxus.map.mapxusmap.overlay.navi.NavigationPathDto
import com.mapxus.map.mapxusmap.overlay.navi.RouteAdsorber
import com.mapxus.map.mapxusmap.overlay.navi.RouteAdsorber.OnDriftsNumberExceededListener
import com.mapxus.map.mapxusmap.overlay.navi.RouteShortener
import com.mapxus.map.mapxusmap.positioning.IndoorLocation
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProvider
import com.mapxus.positioning.positioning.api.ErrorInfo
import com.mapxus.positioning.positioning.api.MapxusLocation
import com.mapxus.positioning.positioning.api.MapxusPositioningClient
import com.mapxus.positioning.positioning.api.MapxusPositioningListener
import com.mapxus.positioning.positioning.api.PositioningState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.*

/**
 * Created by Edison on 2020/9/25.
 * Describe:
 */
class MapxusNavigationPositioningProvider(
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context
) :
    IndoorLocationProvider() {
    private var positioningClient: MapxusPositioningClient? = null
    private var started = false

    @JvmField
    var routeAdsorber: RouteAdsorber? = null
    private var routeShortener: RouteShortener? = null
    private var mapboxMap: MapboxMap? = null

    @JvmField
    var isInHeadingMode = false
    private val coroutineScope: CoroutineScope =
        CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())

    override fun supportsFloor(): Boolean {
        return true
    }

    override fun start() {
        positioningClient =
            MapxusPositioningClient.getInstance(lifecycleOwner, context.applicationContext)
        positioningClient?.addPositioningListener(mapxusPositioningListener)
        positioningClient?.start()
        started = true
    }

    override fun stop() {
        if (positioningClient != null) {
//            positioningClient.removePositioningListener(mapxusPositioningListener);
            positioningClient!!.stop()
        }
        started = false
        coroutineScope.coroutineContext.cancelChildren()
    }

    override fun isStarted(): Boolean {
        return started
    }

    private val mapxusPositioningListener: MapxusPositioningListener =
        object : MapxusPositioningListener {
            override fun onStateChange(positionerState: PositioningState) {
                when (positionerState) {
                    PositioningState.STOPPED -> {
                        dispatchOnProviderStopped()
                    }

                    PositioningState.RUNNING -> {
                        dispatchOnProviderStarted()
                    }

                    else -> {
                        //ignore
                    }
                }
            }

            override fun onError(errorInfo: ErrorInfo) {
                Log.e(TAG, errorInfo.errorMessage)
                dispatchOnProviderError(
                    com.mapxus.map.mapxusmap.positioning.ErrorInfo(
                        errorInfo.errorCode,
                        errorInfo.errorMessage
                    )
                )
            }

            override fun onOrientationChange(orientation: Float, sensorAccuracy: Int) {
                dispatchCompassChange(orientation, sensorAccuracy)
            }

            override fun onLocationChange(mapxusLocation: MapxusLocation?) {
                if (mapxusLocation == null) {
                    return
                }
                coroutineScope.launch {
                    val location = Location("MapxusPositioning")
                    location.latitude = mapxusLocation.latitude
                    location.longitude = mapxusLocation.longitude
                    location.time = System.currentTimeMillis()
                    val building = mapxusLocation.buildingId
                    val floorInfo = if (mapxusLocation.mapxusFloor == null) null else FloorInfo(
                        mapxusLocation.mapxusFloor.id,
                        mapxusLocation.mapxusFloor.code,
                        mapxusLocation.mapxusFloor.ordinal
                    )
                    val indoorLocation = IndoorLocation(building, floorInfo, location)
                    indoorLocation.accuracy = mapxusLocation.accuracy
                    if (null != routeAdsorber) {
                        val indoorLatLon =
                            routeAdsorber!!.calculateTheAdsorptionLocation(indoorLocation)
                        if (indoorLocation.latitude != indoorLatLon!!.latitude || indoorLocation.longitude != indoorLatLon.longitude) {
                            Log.i(
                                TAG,
                                "onLocationChange: " + indoorLatLon.latitude + "," + indoorLatLon.longitude + indoorLocation.latitude + "," + indoorLocation.longitude
                            )
                            withContext(Dispatchers.Main) {
                                routeShortener!!.cutFromTheLocationProjection(
                                    indoorLatLon,
                                    mapboxMap
                                )
                            }
                            indoorLocation.latitude = indoorLatLon.latitude
                            indoorLocation.longitude = indoorLatLon.longitude
                        }
                    }
                    withContext(Dispatchers.Main) {
                        dispatchIndoorLocationChange(indoorLocation)
                    }
                }
            }
        }

    fun updatePath(pathDto: PathDto, mapboxMap: MapboxMap?) {
        this.mapboxMap = mapboxMap
        val navigationPathDto = NavigationPathDto(pathDto)
        routeAdsorber = RouteAdsorber(navigationPathDto)
        routeShortener = RouteShortener(navigationPathDto, pathDto, pathDto.indoorPoints)
        routeAdsorber?.setOnDriftsNumberExceededListener(object : OnDriftsNumberExceededListener {
            override fun onExceeded() {
                Log.i(TAG, "发生漂移了: ")
                Toast.makeText(context, "发生漂移了", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setOnPathChange(onPathChange: RouteShortener.OnPathChangeListener?) {
        routeShortener?.setOnPathChangeListener(onPathChange)
    }

    fun setOnReachListener(onReachListener: RouteAdsorber.OnReachListener?) {
        routeAdsorber?.setOnReachListener(onReachListener)
    }

    companion object {
        private const val TAG = "PositioningProvider"
    }
}
