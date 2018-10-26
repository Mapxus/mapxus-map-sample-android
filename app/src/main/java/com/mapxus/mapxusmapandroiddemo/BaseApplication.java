package com.mapxus.mapxusmapandroiddemo;

import android.support.multidex.MultiDexApplication;

import com.mapxus.map.MapxusMapContext;
import com.tencent.bugly.Bugly;

import timber.log.Timber;

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "4baac78a3f", true);
        MapxusMapContext.init(getApplicationContext());
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }
}
