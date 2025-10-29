package com.mapxus.mapxusmapandroiddemo.examples.indoorpositioning;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.mapxus.map.mapxusmap.api.services.model.building.FloorInfo;
import com.mapxus.map.mapxusmap.api.services.model.floor.Floor;
import com.mapxus.map.mapxusmap.api.services.model.floor.SharedFloor;
import com.mapxus.map.mapxusmap.positioning.IndoorLocation;
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProvider;
import com.mapxus.mapxusmapandroiddemo.BuildConfig;
import com.mapxus.positioning.positioning.api.ErrorInfo;
import com.mapxus.positioning.positioning.api.MapxusFloor;
import com.mapxus.positioning.positioning.api.MapxusLocation;
import com.mapxus.positioning.positioning.api.MapxusPositioningClient;
import com.mapxus.positioning.positioning.api.MapxusPositioningListener;
import com.mapxus.positioning.positioning.api.PositioningState;

public final class MapxusPositioningProvider extends IndoorLocationProvider {

    private static final String TAG = MapxusPositioningProvider.class.getSimpleName();

    private final Context context;
    private MapxusPositioningClient positioningClient;
    private LifecycleOwner lifecycleOwner;
    private boolean started;
    boolean isInHeadingMode = false;

    public MapxusPositioningProvider(LifecycleOwner lifecycleOwner, Context context) {
        this.lifecycleOwner = lifecycleOwner;
        this.context = context;
    }

    @Override
    public boolean supportsFloor() {
        return true;
    }

    @Override
    public void start() {
        positioningClient = MapxusPositioningClient.getInstance(lifecycleOwner, context.getApplicationContext(),
                BuildConfig.MAPXUS_APPID,
                BuildConfig.MAPXUS_SECRET
        );
        positioningClient.addPositioningListener(mapxusPositioningListener);
        positioningClient.start();
        started = true;

    }

    @Override
    public void stop() {
        if (positioningClient != null) {
            positioningClient.stop();
//            positioningClient.removePositioningListener(mapxusPositioningListener);
        }
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }


    private final MapxusPositioningListener mapxusPositioningListener = new MapxusPositioningListener() {
        @Override
        public void onStateChange(PositioningState positioningState) {
            switch (positioningState) {
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
            Floor floor = null;
            MapxusFloor mapxusFloor = mapxusLocation.getMapxusFloor();
            if (mapxusFloor != null) {
                switch (mapxusFloor.getType()) {
                    case SHARED_FLOOR: {
                        floor = new SharedFloor(mapxusFloor.getId(), mapxusFloor.getCode(), mapxusFloor.getOrdinal());
                        break;
                    }
                    case FLOOR: {
                        floor = new FloorInfo(mapxusFloor.getId(), mapxusFloor.getCode(), mapxusFloor.getOrdinal());
                        break;
                    }
                }
            }

            IndoorLocation indoorLocation = new IndoorLocation(building, floor, location);
            indoorLocation.setAccuracy(mapxusLocation.getAccuracy());

            dispatchIndoorLocationChange(indoorLocation);
        }
    };
}
