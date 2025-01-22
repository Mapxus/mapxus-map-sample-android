package com.mapxus.mapxusmapandroiddemo.examples.mapinteraction

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapxus.map.mapxusmap.api.map.MapxusMap
import com.mapxus.map.mapxusmap.api.map.model.MapxusMapOptions
import com.mapxus.map.mapxusmap.api.services.RoutePlanning
import com.mapxus.map.mapxusmap.api.services.model.planning.PathDto
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningPoint
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningQueryRequest
import com.mapxus.map.mapxusmap.overlay.model.RoutePainterResource
import com.mapxus.map.mapxusmap.overlay.route.RoutePainter
import com.mapxus.mapxusmapandroiddemo.R
import com.mapxus.mapxusmapandroiddemo.compose.MapxusMap
import kotlinx.coroutines.launch

class ModifyRouteStyleActivity : AppCompatActivity() {
    private val markerCandidates = listOf("Default Marker", "Sample New Marker")
    private val patternCandidates = listOf("Default Pattern", "Sample New Pattern")

    private var markerSelectedIndex by mutableStateOf(0)
    private var patternSelectedIndex by mutableStateOf(0)
    private var mapboxMap: MapboxMap? by mutableStateOf(null)
    private var mapxusMap: MapxusMap? by mutableStateOf(null)
    private var painter: RoutePainter? by mutableStateOf(null)

    private val newStartIcon by lazy {
        AppCompatResources.getDrawable(this, R.drawable.new_start_marker)?.toBitmap()
    }
    private val newEndIcon by lazy {
        AppCompatResources.getDrawable(this, R.drawable.new_end_marker)?.toBitmap()
    }
    private val newWaypointIcon by lazy {
        AppCompatResources.getDrawable(this, R.drawable.new_waypoint_marker)?.toBitmap()
    }
    private val newPatternIcon by lazy {
        AppCompatResources.getDrawable(this, R.drawable.new_pattern_icon)?.toBitmap()
    }

    private var currentStartIcon: Bitmap? = null
    private var currentEndIcon: Bitmap? = null
    private var currentWaypointIcon: List<Bitmap>? = null
    private var currentPatternIcon: Bitmap? = null
    private var currentRouteOpacity = RoutePainterResource().inactiveRouteOpacity
    private var currentOutdoorLineOpacity = RoutePainterResource().outdoorLineOpacity
    private var currentIndoorColor = RoutePainterResource().indoorLineColor
    private var currentOutdoorColor = RoutePainterResource().outdoorLineColor
    private var currentDashColor = RoutePainterResource().dashedLineColor
    private var currentRouteWidth = 8
    private var currentDashWidth = 8

    private val lats by lazy { resources.getStringArray(R.array.route_style_points_lat) }
    private val lons by lazy { resources.getStringArray(R.array.route_style_points_lon) }
    private val floorIds by lazy { resources.getStringArray(R.array.route_style_points_floor_id) }

    private val routeQuery by lazy {
        RoutePlanningQueryRequest(
            listOf(
                RoutePlanningPoint(lons[0].toDouble(), lats[0].toDouble()),
                RoutePlanningPoint(
                    lons[1].toDouble(),
                    lats[1].toDouble(),
                    floorIds[0]
                ),
                RoutePlanningPoint(
                    lons[2].toDouble(),
                    lats[2].toDouble(),
                    floorIds[1]
                )
            )
        ).apply {
            vehicle = "foot"
            locale = "en"
        }
    }

    private var path: PathDto? by mutableStateOf(null)

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GetPainter()
            FetchRoute()
            DrawRoute()
            val scope = rememberCoroutineScope()
            val bottomSheetState = rememberBottomSheetScaffoldState()
            BackHandler(bottomSheetState.bottomSheetState.currentValue == BottomSheetValue.Expanded) {
                scope.launch { bottomSheetState.bottomSheetState.collapse() }
            }
            BottomSheetScaffold(
                sheetContent = {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        DetailSettings(
                            onConfirm = { routeOpacity, outdoorLineOpacity, indoorColor, outdoorColor, dashColor, routeWidth, dashWidth ->
                                currentRouteOpacity = routeOpacity
                                currentOutdoorLineOpacity = outdoorLineOpacity
                                currentIndoorColor = indoorColor
                                currentOutdoorColor = outdoorColor
                                currentDashColor = dashColor
                                currentRouteWidth = routeWidth
                                currentDashWidth = dashWidth
                                painter?.setRoutePainterResource(getRoutePainterResource())
                                scope.launch { bottomSheetState.bottomSheetState.collapse() }
                            },
                            onError = {
                                scope.launch { bottomSheetState.snackbarHostState.showSnackbar("Error: $it") }
                            }
                        )
                    }
                },
                scaffoldState = bottomSheetState,
                sheetPeekHeight = 0.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    MapxusMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        mapboxMapOptions = MapboxMapOptions.createFromAttributes(this@ModifyRouteStyleActivity)
                            .maxZoomPreference(22.0)
                            .minZoomPreference(15.0),
                        mapxusMapOptions = MapxusMapOptions()
                            .setBuildingId(getString(R.string.default_search_text_building_id)),
                        onGetMapxusMap = { mapxusMap = it },
                        onGetMapboxMap = { mapboxMap = it }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        SingleChoice(markerCandidates, markerSelectedIndex) {
                            markerSelectedIndex = it

                            currentEndIcon = if (it == 0) null else newEndIcon
                            currentStartIcon = if (it == 0) null else newStartIcon
                            currentWaypointIcon = if (it == 0) null else listOf(newWaypointIcon!!)

                            painter?.setRoutePainterResource(getRoutePainterResource())
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        SingleChoice(patternCandidates, patternSelectedIndex) {
                            patternSelectedIndex = it

                            currentPatternIcon = if (it == 0) null else newPatternIcon
                            painter?.setRoutePainterResource(getRoutePainterResource())
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val mainBlue = colorResource(id = android.R.color.holo_blue_light)
                        Button(
                            onClick = {
                                scope.launch {
                                    bottomSheetState.bottomSheetState.expand()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = mainBlue),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Route Display Setting",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getRoutePainterResource() = RoutePainterResource(
        startIcon = currentStartIcon,
        endIcon = currentEndIcon,
        waypointIconList = currentWaypointIcon,
        arrowIcon = currentPatternIcon,
        indoorLineColor = currentIndoorColor,
        outdoorLineColor = currentOutdoorColor,
        dashLineColor = currentDashColor,
        lineWidth = Expression.literal(currentRouteWidth),
        dashedLineWidth = Expression.literal(currentDashWidth),
        outdoorLineOpacity = currentOutdoorLineOpacity,
        inactiveRouteOpacity = currentRouteOpacity
    )

    @Composable
    private fun DetailSettings(
        onConfirm: (Float, Float, Int, Int, Int, Int, Int) -> Unit,
        onError: (String) -> Unit
    ) {
        var settingRouteOpacity by remember { mutableStateOf("0.5") }
        var settingOutdoorLineOpacity by remember { mutableStateOf("1.0") }
        var settingIndoorColor by remember { mutableStateOf("#F9D60D") }
        var settingOutdoorColor by remember { mutableStateOf("#2181D0") }
        var settingDashColor by remember { mutableStateOf("#000000") }
        var settingRouteWidth by remember { mutableStateOf("5") }
        var settingDashWidth by remember { mutableStateOf("5") }
        OutlinedTextField(
            value = settingRouteOpacity,
            onValueChange = {
                settingRouteOpacity = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal
            ),
            label = { Text("Inactive Route Opacity（From 0 to 1）") }
        )

        OutlinedTextField(
            value = settingOutdoorLineOpacity,
            onValueChange = {
                settingOutdoorLineOpacity = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal
            ),
            label = { Text("Outdoor Line Opacity（From 0 to 1）") }
        )

        OutlinedTextField(
            value = settingIndoorColor,
            onValueChange = {
                settingIndoorColor = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            label = { Text("Indoor Route Color（HEX）") }
        )

        OutlinedTextField(
            value = settingOutdoorColor,
            onValueChange = { settingOutdoorColor = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            label = { Text("Outdoor Route Color（HEX）") }
        )

        OutlinedTextField(
            value = settingDashColor,
            onValueChange = { settingDashColor = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            label = { Text("Dash Route Color（HEX）") }
        )

        OutlinedTextField(
            value = settingRouteWidth,
            onValueChange = { settingRouteWidth = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.NumberPassword
            ),
            label = { Text("Route Line Width（Pixel）") }
        )

        OutlinedTextField(
            value = settingDashWidth,
            onValueChange = { settingDashWidth = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.NumberPassword
            ),
            label = { Text("Dash Route Line Width（Pixel）") }
        )

        val mainBlue = colorResource(id = android.R.color.holo_blue_light)
        Button(
            onClick = {
                try {
                    val routeOpacity = settingRouteOpacity.toFloat()
                    if (routeOpacity > 1 || routeOpacity < 0)
                        throw Exception("Inactive Route Opacity should be between 0 and 1")
                    val outdoorLineOpacity = settingOutdoorLineOpacity.toFloat()
                    if (outdoorLineOpacity > 1 || outdoorLineOpacity < 0)
                        throw Exception("Outdoor Line Opacity should be between 0 and 1")
                    onConfirm(
                        routeOpacity,
                        outdoorLineOpacity,
                        android.graphics.Color.parseColor(settingIndoorColor),
                        android.graphics.Color.parseColor(settingOutdoorColor),
                        android.graphics.Color.parseColor(settingDashColor),
                        settingRouteWidth.toInt(),
                        settingDashWidth.toInt()
                    )
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown Error")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = mainBlue),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Create",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }

    @Composable
    private fun SingleChoice(
        candidates: List<String>,
        selectedIndex: Int,
        onSelected: (Int) -> Unit
    ) {
        val mainBlue = colorResource(id = android.R.color.holo_blue_light)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(CircleShape)
                .border(1.dp, mainBlue, shape = CircleShape)
        ) {
            Row(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        if (selectedIndex == 0) {
                            mainBlue
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable { onSelected(0) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (selectedIndex == 0)
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                Text(
                    text = candidates[0],
                    fontSize = 14.sp,
                    color = if (selectedIndex == 0) Color.White else Color.Black
                )
            }

            Row(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        if (selectedIndex == 1) {
                            mainBlue
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable { onSelected(1) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (selectedIndex == 1)
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                Text(
                    text = candidates[1],
                    fontSize = 14.sp,
                    color = if (selectedIndex == 1) Color.White else Color.Black
                )
            }
        }
    }


    @Composable
    private fun GetPainter() = LaunchedEffect(mapxusMap, mapboxMap) {
        if (mapxusMap != null && mapboxMap != null) {
            painter = RoutePainter(this@ModifyRouteStyleActivity, mapboxMap!!, mapxusMap!!)
        }
    }

    @Composable
    private fun DrawRoute() = LaunchedEffect(painter, path) {
        val thePath = path
        if (painter != null && thePath != null) {
            painter?.paintRouteUsingResult(thePath, thePath.indoorPoints, false)
        }
    }

    @Composable
    private fun FetchRoute() = LaunchedEffect(Unit) {
        RoutePlanning.newInstance().route(routeQuery) {
            path = try {
                it.routeResponseDto.paths[0]
            } catch (e: Exception) {
                Toast.makeText(this@ModifyRouteStyleActivity, e.message, Toast.LENGTH_SHORT).show()
                Log.e("TAG", "FetchRoute: failed", e)
                null
            }
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            SingleChoice(candidates = markerCandidates, selectedIndex = markerSelectedIndex) {
                markerSelectedIndex = it
            }
        }
    }
}