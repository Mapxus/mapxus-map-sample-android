# MapxusMap Android SDK Migration Guidance

## Introduction

This document is about how to migrate to latest Mapxus Map Android SDKs.

## Guidance

### Step1: Set AndroidX Support

Please refer to Google about Migrating to AndroidX documentation and set AndroidX Support. [https://developer.android.google.cn/jetpack/androidx/migrate](https://developer.android.google.cn/jetpack/androidx/migrate)

### Step2: Change version of mapxusmap dependency declaration

Notice:

**In older versions, you need to explicitly add mapbox dependency in order to use mapxusmap sdk properly. Since version 4.0.0, we have already defined mapbox dependencies as [transitive dependencies](https://docs.gradle.org/current/userguide/dependency_management_terminology.html#sub:terminology_transitive_dependency). As a result, you only need to declare mapxusmap dependency and use mapbox sdk as usual.**

Old version dependencies declaration:
```
//mapbox
"com.mapbox.mapboxsdk:mapbox-android-sdk:7.32"
//mapxus
"com.mapxus.map:mapxusmap:3.2.6" and below

```

New version dependencies declaration:
```
//mapxus
"com.mapxus.map:mapxusmap:4.0.3"

```

### Step3: Package Name Change

Notice 1:

**In older verion, if you want to draw navigation line when you are planning a route, you need to add mapxus map component dependency (com.mapxus.map:mapxusmap-component). Since version 4.0.0, we have merged component logic to mapxusmap, so please remove com.mapxus.map:mapxusmap-component dependency in build.gradle.**

Notice 2:

**In older version, when you are doing indoor positioning , you need to add mapxus positioning provider dependency (com.mapxus.map:mapxus-positioning-provider). Since version 4.0.0, we no longer use com.mapxus.map:mapxus-positioning-provider, instead we use com.mapxus.positioning:positioning. More positioning details you can check Display Location section in README.md.**


Here is the Mapping table between old package name and new package name

Old package name  |  New package name | Example class
:-: | :-: | :-:
 com.mapxus.map  | com.mapxus.map.mapxusmap.api.map |  MapViewProvider ,IndoorBuilding
 com.mapxus.services  | com.mapxus.map.mapxusmap.api.services  | RoutePlanning , BuildingSearch
 com.mapxus.map.component.overlay  | com.mapxus.map.mapxusmap.overlay  | WalkRouteOverlay
  com.mapxus.positioning  | com.mapxus.map.mapxusmap.positioning  | ErrorInfo , IndoorLocation
  com.mapxus.map.impl  | com.mapxus.map.mapxusmap.impl  | MapboxMapViewProvider


### Step4: Obfuscation Rules Change

Old obfuscation rules:
```
-keep class com.mapxus.map.** {*;}
-keep class com.mapxus.services.** {*;}
-dontwarn com.mapxus.map.**
-dontwarn com.mapxus.services.**

```

New obfuscation rules:
```
-keep class com.mapxus.map.** {*;}
-dontwarn com.mapxus.map.**
```

## More

For more instructions , please check in the README.md.
