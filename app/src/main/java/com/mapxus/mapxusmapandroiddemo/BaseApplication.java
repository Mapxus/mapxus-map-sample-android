package com.mapxus.mapxusmapandroiddemo;

import android.support.multidex.MultiDexApplication;

import com.mapxus.map.MapxusMapContext;

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MapxusMapContext.init(getApplicationContext());
    }
}
