package com.mapxus.mapxusmapandroiddemo.examples.indoorpositioning;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.mapxus.map.mapxusmap.positioning.IndoorLocation;
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProvider;
import com.mapxus.positioning.positioning.api.ErrorInfo;
import com.mapxus.positioning.positioning.api.MapxusLocation;
import com.mapxus.positioning.positioning.api.MapxusPositioningClient;
import com.mapxus.positioning.positioning.api.MapxusPositioningListener;
import com.mapxus.positioning.positioning.api.PositioningState;

public final class MapxusPositioningProvider extends IndoorLocationProvider {

    private static final String TAG = MapxusPositioningProvider.class.getSimpleName();

    private Context context;
    private MapxusPositioningClient positioningClient;
    private LifecycleOwner lifecycleOwner;
    private boolean started;

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
        positioningClient = MapxusPositioningClient.getInstance(lifecycleOwner, context.getApplicationContext());
        positioningClient.addPositioningListener(mapxusPositioningListener);
        positioningClient.start();
        started = true;

    }

    @Override
    public void stop() {
        if (positioningClient != null) {
            positioningClient.stop();
        }
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
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
            dispatchCompassChange(orientation,sensorAccuracy);
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
            String floor = mapxusLocation.getMapxusFloor() == null ? null : mapxusLocation.getMapxusFloor().getCode();
            String building = mapxusLocation.getBuildingId();
            IndoorLocation indoorLocation = new IndoorLocation(location, building, floor);
            indoorLocation.setAccuracy(mapxusLocation.getAccuracy());

            dispatchIndoorLocationChange(indoorLocation);
        }
    };
}
