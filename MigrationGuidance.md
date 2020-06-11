# MapxusMap Android SDK Migration Guidance

##Introduction 

** This document is about how to migrate to MapxusMap Android SDK 4.0.0**

##Guidance 

### Step1: Set AndroidX Support

Please refer to Google about Migrating to AndroidX documentation and set AndroidX Support. https://developer.android.google.cn/jetpack/androidx/migrate

### Step2: Change version of MapxusMap Android SDK

Old version
```
"com.mapxus.map:mapxusmap:3.2.6" and below

```

New version
```
"com.mapxus.map:mapxusmap:4.0.0"

```

### Step3: Package Name Change

Old package name : 
```
com.mapxus.map , com.mapxus.services
```
New package name : 
```
com.mapxus.map.mapxusmap.api.map , com.mapxus.map.mapxusmap.api.services
```

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

##More

For more instructions , please check in the README.md.