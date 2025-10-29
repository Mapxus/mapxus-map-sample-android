package com.mapxus.mapxusmapandroiddemo

import android.app.Application
import com.mapxus.map.auth.CognitoContext
import com.mapxus.map.mapxusmap.api.map.MapxusMapContext

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CognitoContext.registerWithApiKey(
            applicationContext,
            BuildConfig.MAPXUS_APPID,
            BuildConfig.MAPXUS_SECRET
        )
        MapxusMapContext.initialize(applicationContext)
    }
}
