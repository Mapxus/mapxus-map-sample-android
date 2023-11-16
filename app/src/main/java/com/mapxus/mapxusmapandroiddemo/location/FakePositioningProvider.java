package com.mapxus.mapxusmapandroiddemo.location;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.mapxus.map.mapxusmap.positioning.IndoorLocation;
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProvider;
import com.mapxus.positioning.positioning.api.ErrorInfo;
import com.mapxus.positioning.positioning.api.MapxusLocation;
import com.mapxus.positioning.positioning.api.MapxusPositioningClient;
import com.mapxus.positioning.positioning.api.MapxusPositioningListener;
import com.mapxus.positioning.positioning.api.PositioningState;

/**
 * @author frank
 * @date 21/5/2020
 */
public final class FakePositioningProvider extends IndoorLocationProvider {

    private static final String TAG = "FakePositioningProvider";

    private Context context;
    private MapxusPositioningClient positioningClient;
    private LifecycleOwner lifecycleOwner;
    private boolean started;
    private IndoorLocation indoorLocation;

    public FakePositioningProvider(LifecycleOwner lifecycleOwner, Context context) {
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
            dispatchCompassChange(orientation, sensorAccuracy);
        }

        @Override
        public void onLocationChange(MapxusLocation mapxusLocation) {
//            if (indoorLocation != null) {
//                dispatchIndoorLocationChange(indoorLocation);
//            }
        }
    };

    public void setIndoorLocation(IndoorLocation indoorLocation) {
        this.indoorLocation = indoorLocation;
        dispatchIndoorLocationChange(indoorLocation);
    }
}
