package com.mapxus.mapxusmapandroiddemo;

import android.app.Application;

import com.mapxus.map.mapxusmap.api.map.MapxusMapContext;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MapxusMapContext.init(getApplicationContext());
    }
}
