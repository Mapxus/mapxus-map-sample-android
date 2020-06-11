# MapxusMap Android SDK Migration Guidance

## Introduction 

** This document is about how to migrate to MapxusMap Android SDK 4.0.0 **

## Guidance 

### Step1: Set AndroidX Support

Please refer to Google about Migrating to AndroidX documentation and set AndroidX Support. https://developer.android.google.cn/jetpack/androidx/migrate

### Step2: Change version of MapxusMap Android SDK

Notice:

** In the past , you are reminded to add mapbox dependency . Since version 4.0.0 ，it is not recommend that add mapbox dependency in your application . If you want to use some functions of mapbox, we have included the mapbox API in the SDK .**

Old version
```
//mapbox
"com.mapbox.mapboxsdk:mapbox-android-sdk:7.32"
//mapxus
"com.mapxus.map:mapxusmap:3.2.6" and below

```

New version
```
//mapxus
"com.mapxus.map:mapxusmap:4.0.0"

```

### Step3: Package Name Change

Notice 1:

** In the past , if you want to draw navigation line when you are planning a route , you need to add mapxus map component dependency （com.mapxus.map:mapxusmap-component）.Since version 4.0.0 , we have included component API in SDK, so you can remove this dependency in build.gradle and use component functions as usual when you modify package name according to the following mapping table . **

Notice 2:

** In the past , when you are doing indoor positioning , you need to add mapxus positioning provider dependency (com.mapxus.map:mapxus-positioning-provider) . Since version 4.0.0 , we have included this module but not MapxusPositioningProvider in Mapxus Map SDK. So you can remove this dependency in build.gradle and use API as usual when you modify package name according to the following mapping table . More importantly , you should add Positioning SDK dependency(com.mapxus.positioning:positioning) to use indoor positioning functions . More positioning details you can check in the MapxusSample-mapxus-positioning-sample-android project. **


Here is the Mapping table between old package name and new package name 

Old package name  |  New package name | Example class 
:-: | :-: | :-:
 com.mapxus.map  | com.mapxus.map.mapxusmap.api.map |  MapViewProvider ,IndoorBuilding
 com.mapxus.services  | com.mapxus.map.mapxusmap.api.services  | RoutePlanning , BuildingSearch
 com.mapxus.map.component.overlay  | com.mapxus.map.mapxusmap.overlay  | WalkRouteOverlay
  com.mapxus.positioning  | com.mapxus.map.mapxusmap.positioning  | ErrorInfo , IndoorLocation
  com.mapxus.map.impl  | com.mapxus.map.mapxusmap.impl  | MapboxMapViewProvider
 

### Step4: Confusion Change

Old confusion : 
```
-keep class com.mapxus.map.** {*;}
-keep class com.mapxus.services.** {*;}
-dontwarn com.mapxus.map.**
-dontwarn com.mapxus.services.**

```

New confusion : 
```
-keep class com.mapxus.map.** {*;}
-dontwarn com.mapxus.map.**
```

## More

For more instructions , please check in the README.md.