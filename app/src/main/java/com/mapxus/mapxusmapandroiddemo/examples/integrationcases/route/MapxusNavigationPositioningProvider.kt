package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.route

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapxus.map.mapxusmap.api.services.model.building.FloorInfo
import com.mapxus.map.mapxusmap.api.services.model.floor.SharedFloor
import com.mapxus.map.mapxusmap.api.services.model.planning.PathDto
import com.mapxus.map.mapxusmap.overlay.navi.NavigationPathDto
import com.mapxus.map.mapxusmap.overlay.navi.RouteAdsorber
import com.mapxus.map.mapxusmap.overlay.navi.RouteAdsorber.OnDriftsNumberExceededListener
import com.mapxus.map.mapxusmap.overlay.navi.RouteShortener
import com.mapxus.map.mapxusmap.positioning.IndoorLocation
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProvider
import com.mapxus.positioning.positioning.api.ErrorInfo
import com.mapxus.positioning.positioning.api.FloorType
import com.mapxus.positioning.positioning.api.MapxusLocation
import com.mapxus.positioning.positioning.api.MapxusPositioningClient
import com.mapxus.positioning.positioning.api.MapxusPositioningListener
import com.mapxus.positioning.positioning.api.PositioningState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

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

    private var fakePositioningJob: Job? = null

    private val fakeLocations = listOf(
        listOf(114.17434634578406, 22.294415525409875, (-1).toDouble()),
        listOf(114.17434930729888, 22.294416210695720, (-1).toDouble()),
        listOf(114.17434924232869, 22.294416974054414, (-1).toDouble()),
        listOf(114.17434916680120, 22.294417426921136, (-1).toDouble()),
        listOf(114.17435041568386, 22.294417354535636, (-1).toDouble()),
        listOf(114.17434963195260, 22.294417192109140, (-1).toDouble()),
        listOf(114.17434938661647, 22.294417018136812, (-1).toDouble()),
        listOf(114.17434867413125, 22.294416540278082, (-1).toDouble()),
        listOf(114.17434652959473, 22.294415246391750, (-1).toDouble()),
        listOf(114.17434858402919, 22.294413772794194, (-1).toDouble()),
        listOf(114.17434874889439, 22.294412206425662, (-1).toDouble()),
        listOf(114.17435178273466, 22.294409461053476, (-1).toDouble()),
        listOf(114.17435738436181, 22.294407535422570, (-1).toDouble()),
        listOf(114.17436458866787, 22.294406001266356, (-1).toDouble()),
        listOf(114.17437684836291, 22.294405475441433, (-1).toDouble()),
        listOf(114.17438084696980, 22.294422372222350, (-1).toDouble()),
        listOf(114.17438702344678, 22.294431074559533, (-1).toDouble()),
        listOf(114.17438842364939, 22.294436109946492, (-1).toDouble()),
        listOf(114.17438867621867, 22.294440281834480, (-1).toDouble()),
        listOf(114.17438988431338, 22.294443715283762, (-1).toDouble()),
        listOf(114.17439073800276, 22.294446732053370, (-1).toDouble()),
        listOf(114.17439111378143, 22.294447901667215, (-1).toDouble()),
        listOf(114.17439473735374, 22.294450137946427, (-1).toDouble()),
        listOf(114.17439768365644, 22.294451794860066, (-1).toDouble()),
        listOf(114.17440148792522, 22.294452648784560, (-1).toDouble()),
        listOf(114.17440279943494, 22.294453185797686, (-1).toDouble()),
        listOf(114.17440623989508, 22.294452868053373, (-1).toDouble()),
        listOf(114.17440826016103, 22.294452808330590, (-1).toDouble()),
        listOf(114.17441700868521, 22.294451962384727, (-1).toDouble()),
        listOf(114.17442728231494, 22.294450002380874, (-1).toDouble()),
        listOf(114.17443101214887, 22.294448775025412, (-1).toDouble()),
        listOf(114.17443056354506, 22.294448320753794, (-1).toDouble()),
        listOf(114.17443224914592, 22.294447678796587, (-1).toDouble()),
        listOf(114.17443304765084, 22.294447152052280, (-1).toDouble()),
        listOf(114.17444302222990, 22.294443826248010, (-1).toDouble()),
        listOf(114.17443417099909, 22.294446577322528, (-1).toDouble()),
        listOf(114.17443112445018, 22.294447715239418, (-1).toDouble()),
        listOf(114.17442981614010, 22.294447795456720, (-1).toDouble()),
        listOf(114.17442794080313, 22.294441198079067, (-2).toDouble()),
        listOf(114.17443196145034, 22.294449695826483, (-2).toDouble()),
        listOf(114.17443954138113, 22.294455339576448, (-2).toDouble()),
        listOf(114.17444787702613, 22.294460665648458, (-2).toDouble()),
        listOf(114.17453458629890, 22.294425541644088, (-2).toDouble()),
        listOf(114.17454564693699, 22.294423623440185, (-2).toDouble()),
        listOf(114.17455688579045, 22.294415941909460, (-2).toDouble()),
        listOf(114.17456927590140, 22.294417999649470, (-2).toDouble()),
        listOf(114.17458412752298, 22.294417175195380, (-2).toDouble()),
        listOf(114.17459799944632, 22.294416825932450, (-2).toDouble()),
        listOf(114.17461078088596, 22.294419945516780, (-2).toDouble()),
        listOf(114.17462248954179, 22.294420587403570, (-2).toDouble()),
        listOf(114.17463309047079, 22.294418127143995, (-2).toDouble()),
        listOf(114.17463896961686, 22.294416701397427, (-2).toDouble()),
        listOf(114.17464249596955, 22.294409257696362, (-2).toDouble()),
        listOf(114.17464629981662, 22.294400222742162, (-2).toDouble()),
        listOf(114.17464567952025, 22.294392897703364, (-2).toDouble()),
        listOf(114.17465449249775, 22.294386710338433, (-2).toDouble()),
        listOf(114.17466625258861, 22.294383807817706, (-2).toDouble()),
        listOf(114.17468059782652, 22.294383292623298, (-2).toDouble()),
        listOf(114.17469564211500, 22.294384164189957, (-2).toDouble()),
        listOf(114.17471041734136, 22.294382700725365, (-2).toDouble()),
        listOf(114.17472071841189, 22.294382371916168, (-2).toDouble()),
        listOf(114.17472638039702, 22.294376320457050, (-2).toDouble()),
        listOf(114.17474479887228, 22.294381624391020, (-2).toDouble()),
        listOf(114.17472820669029, 22.294359733792156, (-2).toDouble()),
        listOf(114.17472498520539, 22.294361890275006, (-2).toDouble()),
        listOf(114.17472880002379, 22.294369441206065, (-2).toDouble()),
        listOf(114.17473068860123, 22.294375920679713, (-2).toDouble()),
        listOf(114.17473462688154, 22.294379956071648, (-2).toDouble()),
        listOf(114.17475968749368, 22.294388228348947, (-2).toDouble()),
        listOf(114.17477228871743, 22.294389636599632, (-2).toDouble()),
        listOf(114.17478350619345, 22.294389589144988, (-2).toDouble()),
        listOf(114.17479564272509, 22.294389625508373, (-2).toDouble()),
        listOf(114.17481947043947, 22.294379930925057, (-2).toDouble()),
        listOf(114.17483394759321, 22.294376076215176, (-2).toDouble()),
        listOf(114.17484210611734, 22.294378876243640, (-2).toDouble()),
        listOf(114.17485004991707, 22.294378242816860, (-2).toDouble()),
        listOf(114.17486159400298, 22.294380939448560, (-2).toDouble()),
        listOf(114.17487096148457, 22.294382307427817, (-2).toDouble()),
        listOf(114.17488390345785, 22.294386464132305, (-2).toDouble()),
        listOf(114.17488782633562, 22.294388826823514, (-2).toDouble()),
        listOf(114.17489252458428, 22.294389993512755, (-2).toDouble()),
        listOf(114.17490204020400, 22.294391648531864, (-2).toDouble()),
        listOf(114.17491395791806, 22.294393378169087, (-2).toDouble()),
        listOf(114.17492204855246, 22.294397468778950, (-2).toDouble()),
        listOf(114.17493045738135, 22.294399743658440, (-2).toDouble()),
        listOf(114.17492620087916, 22.294401117717168, (-2).toDouble()),
        listOf(114.17493303723428, 22.294400927152324, (-2).toDouble()),
        listOf(114.17492774381564, 22.294401117708766, (-2).toDouble()),
        listOf(114.17494774381564, 22.294451117708766, (-2).toDouble()),
        listOf(114.17493510030337, 22.294399544988227, (-2).toDouble()),
    )

    private var fakeLocationIterator = fakeLocations.iterator()

    private fun getNextFakeLocation(): IndoorLocation {
        if (!fakeLocationIterator.hasNext()) fakeLocationIterator = fakeLocations.iterator()
        val next = fakeLocationIterator.next()
        val location = Location("fake").apply {
            time = System.currentTimeMillis()
            longitude = next[0]
            latitude = next[1]
        }
        return IndoorLocation(
            "ed8353f3-6b85-45ad-86a1-fba7b324c80d",
            FloorInfo(
                if (next[2].toInt() == -1) "ad1255f8-4d64-49b6-9dbc-361a3ccdbd64" else "1854696a-dbe8-483b-a44e-af152e07dc3e",
                if (next[2].toInt() == -1) "B1" else "B2",
                next[2].toInt()
            ),
            location
        )
    }

    override fun start() {
        positioningClient =
            MapxusPositioningClient.getInstance(lifecycleOwner, context.applicationContext)
        positioningClient?.addPositioningListener(mapxusPositioningListener)
        positioningClient?.start()
        started = true

//        fakePositioningJob?.cancel()
//        fakePositioningJob = coroutineScope.launch {
//            withContext(Dispatchers.Main) {
//                dispatchIndoorLocationChange(getNextFakeLocation())
//            }
//            while (isActive) {
//                delay(500)
//                if (null != routeAdsorber) {
//                    val indoorLocation = getNextFakeLocation()
//                    val indoorLatLon =
//                        routeAdsorber!!.calculateTheAdsorptionLocation(indoorLocation)
//                    if (indoorLocation.latitude != indoorLatLon!!.latitude || indoorLocation.longitude != indoorLatLon.longitude) {
//                        Log.i(
//                            TAG,
//                            "onLocationChange: " + indoorLatLon.latitude + "," + indoorLatLon.longitude + indoorLocation.latitude + "," + indoorLocation.longitude
//                        )
//                        withContext(Dispatchers.Main) {
//                            routeShortener!!.cutFromTheLocationProjection(
//                                indoorLatLon,
//                                mapboxMap
//                            )
//                        }
//                        indoorLocation.latitude = indoorLatLon.latitude
//                        indoorLocation.longitude = indoorLatLon.longitude
//                    }
//                    withContext(Dispatchers.Main) {
//                        dispatchIndoorLocationChange(indoorLocation)
//                    }
//                }
//            }
//        }
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
                    val floorInfo = mapxusLocation.mapxusFloor?.run {
                        when (type) {
                            null -> null
                            FloorType.FLOOR -> FloorInfo(id, code, ordinal)
                            FloorType.SHARED_FLOOR -> SharedFloor(id, code, ordinal)
                        }
                    }
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

    companion object {
        private const val TAG = "PositioningProvider"
    }
}
