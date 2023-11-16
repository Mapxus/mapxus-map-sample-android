package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.route;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.services.model.building.FloorInfo;
import com.mapxus.map.mapxusmap.api.services.model.planning.PathDto;
import com.mapxus.map.mapxusmap.overlay.navi.NavigationPathDto;
import com.mapxus.map.mapxusmap.overlay.navi.RouteAdsorber;
import com.mapxus.map.mapxusmap.overlay.navi.RouteShortener;
import com.mapxus.map.mapxusmap.positioning.IndoorLocation;
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProvider;
import com.mapxus.positioning.positioning.api.ErrorInfo;
import com.mapxus.positioning.positioning.api.MapxusLocation;
import com.mapxus.positioning.positioning.api.MapxusPositioningClient;
import com.mapxus.positioning.positioning.api.MapxusPositioningListener;
import com.mapxus.positioning.positioning.api.PositioningState;

/**
 * Created by Edison on 2020/9/25.
 * Describe:
 */
public class MapxusNavigationPositioningProvider extends IndoorLocationProvider {
    private static final String TAG = "PositioningProvider";

    private Context context;
    private MapxusPositioningClient positioningClient;
    private LifecycleOwner lifecycleOwner;
    private boolean started;
    private RouteAdsorber routeAdsorber = null;
    private RouteShortener routeShortener = null;
    private MapboxMap mapboxMap;
    boolean isInHeadingMode = false;

    public MapxusNavigationPositioningProvider(LifecycleOwner lifecycleOwner, Context context) {
        this.lifecycleOwner = lifecycleOwner;
        this.context = context;

    }

    @Override
    public boolean supportsFloor() {
        return true;
    }

    @Override
    public void start() {
        positioningClient = MapxusPositioningClient.getInstance(lifecycleOwner, context.getApplicationContext());
        positioningClient.addPositioningListener(mapxusPositioningListener);
        positioningClient.start();
        started = true;

    }
    private MapxusPositioningListener mapxusPositioningListener = new MapxusPositioningListener() {
        @Override
        public void onStateChange(PositioningState positionerState) {
            switch (positionerState) {
                case STOPPED: {
                    dispatchOnProviderStopped();
                    break;
                }
                case RUNNING: {
                    dispatchOnProviderStarted();
                    break;
                }
                default:
                    break;
            }
        }

        @Override
        public void onError(ErrorInfo errorInfo) {
            Log.e(TAG, errorInfo.getErrorMessage());
            dispatchOnProviderError(new com.mapxus.map.mapxusmap.positioning.ErrorInfo(errorInfo.getErrorCode(), errorInfo.getErrorMessage()));
        }

        @Override
        public void onOrientationChange(float orientation, int sensorAccuracy) {
            if (isInHeadingMode) {
                if (Math.abs(orientation - getLastCompass()) > 10) {
                    dispatchCompassChange(orientation, sensorAccuracy);
                }
            } else {
                dispatchCompassChange(orientation, sensorAccuracy);
            }
        }

        @Override
        public void onLocationChange(MapxusLocation mapxusLocation) {
            if (mapxusLocation == null) {
                return;
            }
            Location location = new Location("MapxusPositioning");
            location.setLatitude(mapxusLocation.getLatitude());
            location.setLongitude(mapxusLocation.getLongitude());
            location.setTime(System.currentTimeMillis());

            String building = mapxusLocation.getBuildingId();
            FloorInfo floorInfo = mapxusLocation.getMapxusFloor() == null ? null : new FloorInfo(
                    mapxusLocation.getMapxusFloor().getId(), mapxusLocation.getMapxusFloor().getCode(), mapxusLocation.getMapxusFloor().getOrdinal()
            );
            IndoorLocation indoorLocation = new IndoorLocation(building, floorInfo, location);
            indoorLocation.setAccuracy(mapxusLocation.getAccuracy());

            if (null != routeAdsorber) {
                IndoorLocation indoorLatLon = routeAdsorber.calculateTheAdsorptionLocationFromActual(indoorLocation);
                if (indoorLocation.getLatitude() != indoorLatLon.getLatitude() || indoorLocation.getLongitude() != indoorLatLon.getLongitude()) {
                    Log.i(TAG,
                            "onLocationChange: " + indoorLatLon.getLatitude() + "," + indoorLatLon.getLongitude() + indoorLocation.getLatitude() + "," + indoorLocation.getLongitude());
                    routeShortener.cutFromTheLocationProjection(indoorLatLon, mapboxMap);
                    indoorLocation.setLatitude(indoorLatLon.getLatitude());
                    indoorLocation.setLongitude(indoorLatLon.getLongitude());
                }
            }

            dispatchIndoorLocationChange(indoorLocation);
        }
    };

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void stop() {
        if (positioningClient != null) {
//            positioningClient.removePositioningListener(mapxusPositioningListener);
            positioningClient.stop();
        }
        started = false;
    }

    public RouteAdsorber getRouteAdsorber() {
        return routeAdsorber;
    }

    public void setRouteAdsorber(RouteAdsorber routeAdsorber) {
        this.routeAdsorber = routeAdsorber;
    }

    public void updatePath(PathDto pathDto, MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        NavigationPathDto navigationPathDto = new NavigationPathDto(pathDto);
        routeAdsorber = new RouteAdsorber(navigationPathDto);
        routeShortener = new RouteShortener(navigationPathDto, pathDto, pathDto.getIndoorPoints());
        routeAdsorber.setOnDriftsNumberExceededListener(() -> {
            Log.i(TAG, "发生漂移了: ");
            Toast.makeText(context, "发生漂移了", Toast.LENGTH_SHORT).show();
        });
    }

    public void setOnPathChange(RouteShortener.OnPathChangeListener onPathChange) {
        routeShortener.setOnPathChangeListener(onPathChange);
    }

    public void setOnReachListener(RouteAdsorber.OnReachListener onReachListener) {
        routeAdsorber.setOnReachListener(onReachListener);
    }
}
