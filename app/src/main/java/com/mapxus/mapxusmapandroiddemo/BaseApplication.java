package com.mapxus.mapxusmapandroiddemo;

import android.support.multidex.MultiDexApplication;

import com.mapxus.map.MapxusMapContext;
import com.tencent.bugly.crashreport.CrashReport;

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "4baac78a3f", true);
        MapxusMapContext.init(getApplicationContext());
    }
}
