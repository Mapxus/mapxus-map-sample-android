# Mapxus Map sample app for Android

This is a sample project to demonstrate how to use mapxus map android sdk.

# Table of Contents
- [Installation](#installation)
- [Mapxus Map Android SDK Instruction](#Mapxus-Map-Android-SDK-Instruction)
  - [About Mapxus Map SDK](#About-Mapxus-Map-SDK)
  - [Install Mapxus Map SDK](#Install-Mapxus-Map-SDK)
    - [Create a Project](#Create-a-Project)
  - [Display Digital Map with Static Layout](#Display-Digital-Map-sith-Static-Layout)
    - [Display in Activity](#Display-in-Activity)
    - [Display in Fragment](#Display-in-Fragment)
  - [Display Digital Map Dynamically/Programatically](#Display-Digital-Map-Dynamically/Programatically)
    - [Display via lat/log and zoom level](#Display-via-lat/log-and-zoom-level)
    - [Display via specified building and floor](#Display-via-specified-building-and-floor)
    - [Display via specified POI](#Display-via-specified-POI)
  - [Digital Map Customizations](#Digital-Map-Customizations)
    - [Digital Map Controllers](#Digital-Map-Controllers)
    - [Digital Map Events](#Digital-Map-Events)
    - [Digital Map Drawing](#Digital-Map-Drawing)
    - [Digital Map Style](#Digital-Map-Style)
  - [Searching in Digital Map](#Searching-in-Digital-Map)
    - [Building Search](#Building-Search)
      - [Search nearby buildings](#Search-nearby-Buildings)
      - [Search Buildings by Area](#Search-Buildings-by-Area)
      - [Search by Building ID](#Search-by-Building-ID)
      - [Global Search](#Global-Search)
    - [POI Search](#POI-Search)
      - [Search nearby POIs](#Search-nearby-POIs)
      - [Search POIs by Area](#Search-POIs-by-Area)
      - [Search POI by ID](#Search-POI-by-ID)
      - [Search Indoor POIs by Keywords](#Search-Indoor-POIs-by-Keywords)
      - [Search Indoor POIs by Category](#Search-Indoor-POIs-by-Category)
      - [Search Indoor POIs with user orientation](#Search-Indoor-POIs-with-user-orientation)
    - [Route Planning](#Route-Planning)
  - [Display Location](#Display-Location)
  - [API Docs](#API-Docs)

# Installation

We highly recommend using the latest stable version of Android Studio (Current version: 4.0) to open this project.

Before running this project, please create the **secret.properties** file in the project root directory and fill in the application appid and secret in the following format:

	appid=
	secret=

Please contact us  <support@mapxus.com> to get appid and secret if you do not have them.

# Mapxus Map Android SDK Instruction

## About Mapxus Map SDK

Mapxus Map SDK is a set of developer-friendly tools to empower your android applications with our cutting edge city-based indoor location services, including digital map displaying, digital map customizations, indoor search, route planning, positioning, 360 visual view etc.

For more information about Mapxus Indoor Location Service, please check our website: [https://www.mapxus.com/](https://www.mapxus.com/).

## Install Mapxus Map SDK

### Create a Project

First of all, please create a project for your APP. Then, configure mapxus map SDK through the following steps:

#### Step1: Add jcenter repository

Add jcenter repository to your root project's **build.gradle** file:

```grovvy

allprojects {
    repositories {
        jcenter()
        ...
    }
}

```
#### Step2: Add Mapxus Map SDK Dependencies

Add dependencies in your **build.gradle** in app if you use


```grovvy

dependencies { 
 ...

 // MapxusMap
    implementation "com.mapxus.map:mapxusmap:4.0.0"

 ...
}	

```



#### Step3: Set Java 8 Support

Please refer to Google about Java 8 language documentation and set Java 8 Support.
[https://developer.android.com/studio/write/java8-support](https://developer.android.com/studio/write/java8-support)

#### Step4: Set AndroidX Support

Please refer to Google about Migrating to AndroidX documentation and set AndroidX Support.
[https://developer.android.google.cn/jetpack/androidx/migrate](https://developer.android.google.cn/jetpack/androidx/migrate)


#### Step5: Prevent Obfuscation

Please configurate these in ProGuard to avoid obfuscation:

```
-keep class com.mapxus.map.** {*;}
-dontwarn com.mapxus.map.**

```


#### Step6: Set Key and Secret

There are two options to set your api key and secret:

##### Option One:

1. Add them in onCreate() Method of BaseApplication

```java
	MapxusMapContext.init(getApplicationContext());
```

2. Configure the following codes in AndroidManifest.xml:

``` java
	<meta-data
	        android:name="com.mapxus.api.v1.appid"
	        android:value="acquiredkey" />
	<meta-data
	    android:name="com.mapxus.api.v1.secret"
	    android:value="acquiredsecret" />
```

##### Option Two:

Add them in onCreate() Method of BaseApplication

``` java
	MapxusMapContext.init(getApplicationContext()，Key，secret);
```

## Display Digital Map with Static Layout

### Display in Activity

First of all, add Mapbox Map controllers in the layout xml file:

```xml
      <com.mapbox.mapboxsdk.maps.MapView
        android:id="@+id/mapView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:mapbox_cameraTargetLat="22.3707866"
        app:mapbox_cameraTargetLng="114.1112654"
        app:mapbox_cameraZoom="17" />
```

Then, add map codes as follows in Activity file:

```java

    public class SimpleMapViewActivity extends AppCompatActivity {

    private MapView mapView;
    private MapViewProvider mapViewProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_simple_mapview);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mapViewProvider.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
	}
```

Please be aware that maps lifecycle requires reasonable management during using map in your project.

![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Getting_started_simple.png)

### Display in Fragment

Add SupportMapxusMapFragment in Activity file:

```java

    SupportMapxusMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_support_map_frag);
        if (savedInstanceState == null) {
            // Create fragment
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MapboxMapOptions options = new MapboxMapOptions();
            options.camera(new CameraPosition.Builder()
                    .target(LatLngConstant.ELEMENT_LATLON)
                    .zoom(17)
                    .build());

            MapView mapView = new MapView(SupportMapFragmentActivity.this, options);
            // Create map fragment
            mapFragment = SupportMapxusMapFragment.newInstance(mapView);
            // Add map fragment to parent container
            transaction.add(R.id.container, mapFragment, "com.mapxus.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapxusMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapxus.map");
        }
    }
```
![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Getting_started_fragment.png)

## Display Digital Map Dynamically/Programatically

### Display via lat/log and zoom level

```java
    private MapViewProvider mapViewProvider;
    private MapView mapboxMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create mapbox map
        MapboxMapOptions mapboxMapOptions = new MapboxMapOptions();
        mapboxMapOptions.camera(new CameraPosition.Builder().target(LatLngConstant.ELEMENT_LATLON).zoom(17).build());
        mapboxMapView = new MapView(this, mapboxMapOptions);
        mapboxMapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapboxMapView);
        setContentView(mapboxMapView);
    }
```
![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Getting_started_dynamic.png)

### Display via specified building and floor

Add MapView in Activity file:

```java

    private MapViewProvider mapViewProvider;
    private MapView mapboxMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_init_with_building);
        mapboxMapView = (MapView) findViewById(R.id.mapView);
        MapxusMapOptions mapxusMapOptions = new MapxusMapOptions().setBuildingId("tsuenwanplaza_hk_369d01").setFloor("L2");
        mapViewProvider = new MapboxMapViewProvider(this, mapboxMapView, mapxusMapOptions);
    }
```

### Display via specified POI

Add MapView in Activity file:

```java

    private MapViewProvider mapViewProvider;
    private MapView mapboxMapView;
    private static final String POI_ID = "12586";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_init_with_building);
        mapboxMapView = (MapView) findViewById(R.id.mapView);
        MapxusMapOptions mapxusMapOptions = new MapxusMapOptions().setPoiId(POI_ID);
        mapViewProvider = new MapboxMapViewProvider(this, mapboxMapView, mapxusMapOptions);
    }
```



## Digital Map Customizations

### Digital Map Controllers

Controllers refer to components displaying above the map with the function of operating maps, such as floor selector, etc. MapxusUiSettings class is used to manage these controllers so as to tailor your map view. MapxusUiSettings could be instantiated by MapxusMap.

``` java
     mapViewProvider.getMapxusMapAsync(new OnMapxusMapReadyCallback() {
            @Override
            public void onMapxusMapReady(MapxusMap mapxusMap) {
            	MapxusUiSettings mapxusUiSettings = mapxusMap.getMapxusUiSettings();
            }
    }
```

#### Selector

Selector is a button for APP users to alter displaying or hiding floor selecting list and building selecting list on the map. It is on (displaying) by default but you can hide it by the following interface:


```java
mapxusUiSettings.setSelectorEnabled(false);
```

#### Building Selector

Building selector allows APP users to decide whether displaying building selecting list on the map or not. It is on (displaying) by default but you can hide it by this interface:

```java
mapxusUiSettings.setBuildingSelectorEnabled(false);
```

### Digital Map Events

#### Change Building Listener

```java
mapxusMap.addOnBuildingChangeListener(new MapxusMap.OnBuildingChangeListener() {
            @Override
            public void onBuildingChange(IndoorBuilding indoorBuildingInfo) {

            }
        });
```
#### Change Floor Listener

```java

mapxusMap.addOnFloorChangeListener(new MapxusMap.OnFloorChangeListener() {
            @Override
            public void onFloorChange(IndoorBuilding indoorBuilding, String floor) {

            }
        });
    }
```

#### Click Indoor POI Listener

Click on the map and listen to the change of POI position:

```java
     @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {

        mapxusMap.addOnIndoorPoiClickListener(new MapxusMap.OnIndoorPoiClickListener() {
            @Override
            public void onIndoorPoiClick(Poi poi) {

                String message = String.format(getString(R.string.click_poi_message), poi.getName());

                poiClickTv.setText(message);
            }
        });
    }
```

### Digital Map Drawing

#### Draw a Marker

A marker can mark any site containing position information on the map, such as user position, car position, store position, etc.
When creating a marker, you can set its buildingID and floor. In this case, the marker is only displayed with corresponding building and floor.
To draw a marker:

```java
     mapViewProvider.getMapxusMapAsync(new OnMapxusMapReadyCallback() {
            @Override
            public void onMapxusMapReady(MapxusMap mapxusMap) {

                MapxusMarkerOptions mapxusMarkerOptions = new MapxusMarkerOptions();
                mapxusMarkerOptions.setPosition(new LatLng(LatLngConstant.ELEMENT_LATLON.getLatitude(), LatLngConstant.ELEMENT_LATLON.getLongitude()));
                mapxusMarkerOptions.setFloor("L3");
                mapxusMarkerOptions.setBuildingId("tsuenwanplaza_hk_369d01");


                MapxusMarkerOptions mapxusMarkerOptions2 = new MapxusMarkerOptions();
                mapxusMarkerOptions2.setPosition(new LatLng(22.370779, 114.111341)).setFloor("L2");
                mapxusMarkerOptions2.setBuildingId("tsuenwanplaza_hk_369d01");


                MapxusMarkerOptions mapxusMarkerOptions3 = new MapxusMarkerOptions();
                mapxusMarkerOptions3.setPosition(new LatLng(22.371144, 114.111062));

                mapxusMap.addMarker(mapxusMarkerOptions);
                mapxusMap.addMarker(mapxusMarkerOptions2);
                mapxusMap.addMarker(mapxusMarkerOptions3);
            }
        });
```
![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Annotations_marker.png)

#### Draw a Customized Marker

You can specify your marker as needed.

To create a marker with custom icon:

```java
      mapViewProvider.getMapxusMapAsync(new OnMapxusMapReadyCallback() {
            @Override
            public void onMapxusMapReady(MapxusMap mapxusMap) {
                mapxusMap.addMarker(new MapxusMarkerOptions()
                        .setBuildingId("tsuenwanplaza_hk_369d01")
                        .setFloor("L3")
                        .setPosition(new LatLng(LatLngConstant.ELEMENT_LATLON.getLatitude(),LatLngConstant.ELEMENT_LATLON.getLongitude()))
                        .setTitle(getString(R.string.draw_custom_marker_options_title))
                        .setSnippet(getString(R.string.draw_custom_marker_options_snippet))
                        .setIcon(R.drawable.purple_marker));
            }
        });
```
![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Annotations_customized_marker.png)

### Digital Map Style

You can change your map style by changing color, visibility of elements and characters of the basemap. Therefore, it will render differently to fit different APP styles.

Mapxus Map presents four styles now: Style.Mapxus, Style.MAPPYBEE, Style.HALLOWEEN, Style.CHRISTMAS, and Style.COMMON.
You can change your map style by this interface:

```java
mapViewProvider.setStyle(Style.COMMON);
```

![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Styles.png)

## Searching in Digital Map

### Building Search
Instantiate BuildingSearch Object

```java
        buildingSearch = BuildingSearch.newInstance();
```
Set Search Result Listener

```java
        buildingSearch.setBuildingSearchResultListener(this);
```

#### Search nearby buildings

##### Set Parameters

```java
        NearbySearchOption nearbySearchOption = new NearbySearchOption();
        nearbySearchOption.mRadius = 2;
        nearbySearchOption.location(new LatLng(LatLngConstant.ELEMENT_LATLON.getLatitude(), LatLngConstant.ELEMENT_LATLON.getLongitude()));
        nearbySearchOption.keyword(keyWord);

    }
```

##### Implement Searching

```java
buildingSearch.searchNearby(nearbySearchOption);
```
##### Get Search Result

```java
     @Override
    public void onGetBuildingResult(BuildingResult buildingResult) {

        if (buildingResult.status != 0) {
            Toast.makeText(this, buildingResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (buildingResult.getIndoorBuildingList() == null || buildingResult.getIndoorBuildingList().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        mapboxMap.clear();
        indoorBuildingOverlay = new MyIndoorBuildingOverlay(mapboxMap, buildingResult.getIndoorBuildingList());
        indoorBuildingOverlay.addToMap();
        indoorBuildingOverlay.zoomToSpan();

    }

    @Override
    public void onGetBuildingDetailResult(BuildingDetailResult buildingDetailResult) {

    }
```
![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_building_nearby.png)

#### Search Buildings by Area

##### Set Parameters

```java
        private LatLngBounds latLngBounds;
        com.mapxus.map.model.LatLng southweast = new com.mapxus.map.model.LatLng(22.2918962, 114.1353782);
        com.mapxus.map.model.LatLng northeast = new com.mapxus.map.model.LatLng(22.3418344, 114.2089048);
        latLngBounds = new LatLngBounds(southweast, northeast);
        BoundSearchOption boundSearchOption = new BoundSearchOption();

        boundSearchOption.bound(latLngBounds);
        boundSearchOption.keyword(keyWord);

```

##### Implement Searching

```java

        buildingSearch.searchInBound(boundSearchOption);

```

##### Get Search Result

```java
 @Override
    public void onGetBuildingResult(BuildingResult buildingResult) {

        if (buildingResult.status != 0) {
            Toast.makeText(this, buildingResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (buildingResult.getIndoorBuildingList() == null || buildingResult.getIndoorBuildingList().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        if (indoorBuildingOverlay != null) {
            indoorBuildingOverlay.removeFromMap();
            indoorBuildingOverlay = null;
        }
        indoorBuildingOverlay = new MyIndoorBuildingOverlay(mapboxMap, buildingResult.getIndoorBuildingList());
        indoorBuildingOverlay.addToMap();
        indoorBuildingOverlay.zoomToSpan();
    }

```



![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_building_inbound.png)

#### Search by Building ID

##### Set Parameters

```java
	DetailSearchOption detailSearchOption = new DetailSearchOption();
	detailSearchOption.id(keyWord);

```
##### Implement Searching

```java
	buildingSearch.searchBuildingDetail(detailSearchOption);
```
##### Get Searching Result

```java
	@Override
    public void onGetBuildingDetailResult(BuildingDetailResult buildingDetailResult) {
        if (buildingDetailResult.status != 0) {
            Toast.makeText(this, buildingDetailResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (buildingDetailResult.getIndoorBuildingInfo() == null) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        mapboxMap.clear();
        IndoorBuildingInfo indoorBuildingInfo = buildingDetailResult.getIndoorBuildingInfo();

        Marker marker = mapboxMap.addMarker(new ObjectMarkerOptions()
                .position(
                        new LatLng(indoorBuildingInfo.getLabelCenter()
                                .getLat(), indoorBuildingInfo
                                .getLabelCenter().getLon()))
                .title(indoorBuildingInfo.getName().get("default")).snippet(indoorBuildingInfo.getAddress().get("default").toShortAddress())
                .object(indoorBuildingInfo));
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
    }

```


![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_building_id.png)

#### Global Search

##### Set Parameters

```java

	GlobalSearchOption globalSearchOption = new GlobalSearchOption();
	globalSearchOption.keyword(keyWord);
```
##### Implement Searching

```java

	buildingSearch.searchInGlobal(globalSearchOption);
```
##### Get Searching Result
```java
@Override
    public void onGetBuildingResult(BuildingResult buildingResult) {

        if (buildingResult.status != 0) {
            Toast.makeText(this, buildingResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (buildingResult.getIndoorBuildingList() == null || buildingResult.getIndoorBuildingList().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

       mapboxMap.clear();
        indoorBuildingOverlay = new MyIndoorBuildingOverlay(mapboxMap, buildingResult.getIndoorBuildingList());
        indoorBuildingOverlay.addToMap();
        indoorBuildingOverlay.zoomToSpan();
    }

    @Override
    public void onGetBuildingDetailResult(BuildingDetailResult buildingDetailResult) {


    }

```


![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_building_global.png)

### POI Search

POI Search is implemented through PoiSearch. It could be instantiated through the following steps:

Instantiate BuildingSearch Object

```java
        poiSearch = PoiSearch.newInstance();
```
Set POI Search Result Listener

```java
        poiSearch.setPoiSearchResultListener(this);
```

#### Search nearby POIs

##### Set Parameters

```java
		NearbySearchOption nearbySearchOption = new NearbySearchOption();
		nearbySearchOption.mRadius = 2;
		nearbySearchOption.location(new LatLng(
		        LatLngConstant.ELEMENT_LATLON.getLatitude(),
		        LatLngConstant.ELEMENT_LATLON.getLongitude()));
		nearbySearchOption.keyword(keyWord);

```
##### Implement Searching

```java
poiSearch.searchNearby(nearbySearchOption);

```
##### Get Search Result

```java
public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult.status != 0) {
            Toast.makeText(this, poiResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (poiResult.getAllPoi() == null || poiResult.getAllPoi().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        mapboxMap.clear();
        poiOverlay = new MyPoiOverlay(mapboxMap, poiResult.getAllPoi());
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
    }

```
![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_poi_nearby.png)

#### Search POIs by Area


##### Set Parameters

```java

BoundSearchOption boundSearchOption = new BoundSearchOption();
boundSearchOption.bound(latLngBounds);
boundSearchOption.keyword(keyWord);

```
##### Implement Searching

```java
poiSearch.searchInBound(boundSearchOption);
```
##### Get Search Result

```java
public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult.status != 0) {
            Toast.makeText(this, poiResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (poiResult.getAllPoi() == null || poiResult.getAllPoi().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        mapboxMap.clear();
        poiOverlay = new MyPoiOverlay(mapboxMap, poiResult.getAllPoi());
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
    }

```

![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_poi_inbound.png)


#### Search POI by ID

##### Set Parameters

```java
DetailSearchOption detailSearchOption = new DetailSearchOption();
detailSearchOption.id(keyWord);

```
##### Implement Search

```java
poiSearch.searchPoiDetail(detailSearchOption);
```
##### Get Search Result

```java
public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.status != 0) {
            Toast.makeText(this, poiDetailResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (poiDetailResult.getPoiInfo() == null) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        mapboxMap.clear();
        PoiInfo poiInfo = poiDetailResult.getPoiInfo();

        Marker marker = mapboxMap.addMarker(new ObjectMarkerOptions()
                .position(
                        new LatLng(poiInfo.getLocation()
                                .getLat(), poiInfo
                                .getLocation().getLon()))
                .title(poiInfo.getName().get("default")).snippet("buildingId:" + poiInfo.getBuildingId())
                .object(poiInfo));
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 19));
    }

```
![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_poi_id.png)

#### Search Indoor POIs by Keywords

##### Set Parameters

```java
InBuildingSearchOption inBuildingSearchOption = new InBuildingSearchOption();
inBuildingSearchOption.buildingId(buildingId);
inBuildingSearchOption.keyword(keyWord);

```
##### Implement Searching

```java
poiSearch.searchInBuilding(inBuildingSearchOption);
```
##### Get Search Result

```java
@Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult.status != 0) {
            Toast.makeText(this, poiResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (poiResult.getAllPoi() == null || poiResult.getAllPoi().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        mapboxMap.clear();
        poiOverlay = new MyPoiOverlay(mapboxMap, poiResult.getAllPoi());
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
    }

```

![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_poi_inbuilding.png)

#### Search Indoor POIs by Category

##### Set Parameters

~~~java
PoiCategorySearchOption poiCategorySearchOption = new PoiCategorySearchOption();
poiCategorySearchOption.buildingId(buildingId);
poiCategorySearchOption.floor(floor);
~~~
##### Implement Searching

~~~java
poisearch.searchPoiCategoryInBuilding(poiCategorySearchOption);
~~~

##### Get Search Result

~~~java
@Override
public void onPoiCategoriesResult(PoiCategoryResult poiCategoryResult){
	poiCategoryResult.getResult();
}
~~~

#### Search Indoor POIs with user orientation

##### Set Parameters

~~~java
PoiOrientationSearchOption option = new PoiOrientationSearchOption();
option.orientation(mOrientation);
option.indoorLatLng(indoorLatLng);
option.meterRadius(distance);
~~~

##### Implement Searching

~~~java
 poiSearch.searchPoiByOrientation(option);
~~~

##### Get Search Result

~~~java
 @Override
public void onGetPoiByOrientationResult(PoiOrientationResult poiOrientationResult) {
}
~~~
![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_poi_orientation.png)

### Route Planning

Route planning can draw a route, including start point, end point and turning point, with WalkRouteOverlay according to the start point and end point.

#### Step 1: Instantiate RoutePlanning object

```java
        private RoutePlanning routePlanning;
        routePlanning = RoutePlanning.newInstance();
```
#### Step 2: Set route planning listener

```java
        routePlanning.setRoutePlanningListener(this);
```
#### Step 3: Set searching parameters

```java
     private RoutePlanningPoint origin = new RoutePlanningPoint("harbourcity_hk_8b580b", "G", 114.16802419399949, 22.298414559331476);
    private RoutePlanningPoint destination = new RoutePlanningPoint("harbourcity_hk_8b580b", "L3", 114.16835321304131, 22.298373814183165);
```

#### Step 4: Request route

```java
        routePlanning.route(origin, destination);
```
#### Step 5: Get route planning result

```java
   @Override
    public void onGetRoutePlanningResult(RoutePlanningResult routePlanningResult) {
        if (routePlanningResult.status != 0) {
            Toast.makeText(this, routePlanningResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (routePlanningResult.getRouteResponseDto() == null) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }
        RouteResponseDto routeResponseDto = routePlanningResult.getRouteResponseDto();
        drawRoute(routeResponseDto);
        mMapxusMap.switchFloor(origin.getFloor());
    }
```
#### Step 6: Draw the route

```java
    private void drawRoute(RouteResponseDto route) {
        // Convert LineString coordinates into LatLng[]

        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, mapboxMap, mMapxusMap, route, origin, destination);
        walkRouteOverlay.addToMap();
    }
```
![image](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/image/Search_services_rounte_planning.png)

## Display Location
First, Implement IndoorLocationProvider from mapxusmap dependency:
```java
public final class MapxusPositioningProvider extends IndoorLocationProvider {

    private static final String TAG = "MapxusPositioningProvider";

    private Context context;
    private MapxusPositioningClient positioningClient;
    LifecycleOwner lifecycleOwner;
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
```

Then set your indoor location provider to mapxus map:

```java
@Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        this.mapxusMap = mapxusMap;
        IndoorLocationProvider mapxusPositioningProvider = new MapxusPositioningProvider(this, getApplicationContext());
        mapxusPositioningProvider.addListener(new IndoorLocationProviderListener() {
            @Override
            public void onProviderStarted() {
            }
            @Override
            public void onProviderStopped() {
            }
            @Override
            public void onProviderError(ErrorInfo error) {
            }
            @Override
            public void onIndoorLocationChange(IndoorLocation indoorLocation) {
               // add your location change logic
            }
            @Override
            public void onCompassChanged(float angle, int sensorAccuracy) {
                compassTv.setText(String.valueOf(angle));
            }
        });
        mapxusMap.setLocationProvider(mapxusPositioningProvider);
    }
```

Please check [HERE](https://raw.githubusercontent.com/Mapxus/mapxus-map-sample-android/master/app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/displaylocation/LocationProviderActivity.java) for complete example.

## API Docs

Please click [HERE] to check our APIs.

[HERE]: https://service.mapxus.com/dpw/api/v1/api/digitalMap/android/4.0.0/index.html
