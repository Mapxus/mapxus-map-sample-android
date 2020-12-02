# Mapxus Map sample app for Android

This is a sample project to demonstrate how to use mapxus map android sdk.

[中文说明](./README_CN.md)

# Installation

We highly recommend using the latest stable version of Android Studio to open this project.

Before running this project, please create the **secret.properties** file in the project root directory and fill in the application appid and secret in the following format:

	appid=
	secret=

Please contact us  <support@mapxus.com> to get appid and secret if you do not have them.

## About Mapxus Map SDK

All the follwing sample is based on [the latest Mapxus Map SDK version](./CHANGELOG.md) , please add sdk dependency to your own project's builid.gradle file:
```groovy
dependencies {
    	implementation 'com.mapxus.map:mapxusmap: latest version'
	}
```

For more information about Mapxus Indoor Map and Location Service, please check our website: [https://www.mapxus.com/](https://www.mapxus.com/).


## Map Creation

### Create map with code

* File name：[SimpleMapViewActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/SimpleMapViewActivity.java)

* Summary：Create map with code.

* Detail：

  * Create a map using code and set the map's central geographic coordinates and zoom level in xml.


### Create map with fragment


* File name：[SupportMapFragmentActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/SupportMapFragmentActivity.java)

* Summary：Create map with fragment.

* Detail：

  * Create a map using fragment and set the map's central geographic coordinates and zoom level.


### Create map (Specify the initial building, floor and building adaptive margin)


* File name：[MapxusMapInitWithBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/MapxusMapInitWithBuildingActivity.java)

* Summary：To Maximize the specified building indoor map by the setting margin range and switch to the setting floor.

* Detail：

   * Create a parameter class [MapxusMapInitWithBuildingParamsActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/MapxusMapInitWithBuildingParamsActivity.java) instance with the specified building ID, floor and building adaptive margins.
   
   * Initialize the map with the generated MapxusMapInitWithBuildingParamsActivity instance.

### Create map (Specify the initial POI and zoom level)


* File name：[MapxusMapInitWithPoiActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/MapxusMapInitWithPoiActivity.java)

* Summary：To show the specified POI in the centre of the map and show the map by the setting zoom level when creating map.

* Detail：

     * Create a parameter class [MapxusMapInitWithPoiParamsActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/MapxusMapInitWithPoiParamsActivity.java) instance using the specified POI, map zoom level.
     
     * Initialize the map with the generated MapxusMapInitWithPoiParamsActivity instance.
     

## Map Interaction

### Interaction of indoor map controller

* File name：[IndoorMapControllerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/IndoorMapControllerActivity.java)

* Summary：Location of indoor map controllers.

* Detail：

  * Set whether the indoor map control is always hidden.
  
  * Set indoor map control positions: left, right, top-left, top-right, bottom-left, bottom-right.

### Map style setting

* File name：[MapStyleSettingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/MapStyleSettingActivity.java)

* Summary：Modify map style, mark language and control outdoor map hiding.

* Detail：

  * Set whether to hide the outdoor map.
  * Change map style.
  * Change mark language.

### Gesture interaction for switching buildings

* File name：[GestureInteractionSwitchBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/GestureInteractionSwitchBuildingActivity.java)

* Summary：Setting gestures for switching buildings.

* Detail：

  * Set whether or not to support tapping the screen to switch buildings.
  * Set whether or not the indoor building moves to the center of the screen to automatically switch between buildings.

### Method interaction (Switching indoor scenes)

* File name：[FocusOnIndoorSceneActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/FocusOnIndoorSceneActivity.java)

* Summary：Use code settings to focus on indoor scenes.

* Detail：

  * Set the building and floor the map focuses on
  * Set focus effect: no zoom, zoom by animation, zoom without animation.
  * Setting Architectural Adaptive Margins.
  
### Click event listener

* File name：[ClickEventListenerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/ClickEventListenerActivity.java)

* Summary：Listener for click or long on the map, and click POI event.

* Detail：

	* Click on the POI to trigger the `- onIndoorPoiClick(Poi poi)` callback method.
    * Click on a map blank to trigger the `-onMapClick(LatLng latLng, String floor, String buildingId, String floorId)` callback method.
    * Press and hold the map to trigger the `-onMapLongClick(LatLng latLng, String floor, String buildingId, String floorId)` callback method.

### Indoor scene switching event listener

* File name：[IndoorSceneSwitchingListenerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/IndoorSceneSwitchingListenerActivity.java)

* Summary：Listener for indoor scene switching events.

* Detail：

   * Triggers the `-onFloorChange(IndoorBuilding indoorBuilding, String floor)` callback method when switching to an indoor scene.
   
### Get in or leave indoor scene event listener

* File name：[IndoorSceneInAndOutListenerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/IndoorSceneInAndOutListenerActivity.java)

* Summary：Listener for get in indoor or leave indoor scene.

* Detail：

   * Triggers the `-onBuildingChange(IndoorBuilding indoorBuilding)` callback method when entering or exiting the indoor scene.
   

## Map Editing

### Drawing markers by floor

* File name：[DrawMarkerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapediting/DrawMarkerActivity.java)

* Summary：Only display the markers on current floor.

* Detail：

   * Create Mapxus Marker with special floors and add them to the map.

### Drawing polygons by floor

* File name：[DrawPolygonActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapediting/DrawPolygonActivity.java)

* Summary：Only display the polygon on current floor.

* Detail：

   * Creating a Layer Instance.
   * Add layer to mapview.
   * Listen to floor switch, filter layer data.
   
## Indoor Positioning

### Indoor Positioning

* File name：[LocationProviderActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/indoorpositioning/LocationProviderActivity.java)

* Summary：Show the positioning location and different following mode.

* Detail：

  * Real-time display of current position latitude, longitude, floor and level accuracy.
  * Switching between different position-following modes.

* Before using

   *  Add positioning sdk dependency to your project's build.gradle file:
```groovy
dependencies {
    	implementation 'com.mapxus.positioning:positioning:2.0.4'
	}
```


## Search Service

### Search building globally

* File name：[SearchBuildingGlobalActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchBuildingGlobalActivity.java)

* Summary：Search building globally.

* Detail：

     * Creating an instance of the search parameter class GlobalSearchOption.
     * Create an instance of the search class BuildingSearch.
     * Get the search result by using the `-onGetBuildingResult(BuildingResult buildingResult)` callback method.
   
### Search building in the specified area

* File name：[SearchBuildingInboundActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchBuildingInboundActivity.java)

* Summary：Search building in the specified rectangular area.

* Detail：

     * Creating an instance of the search parameter class BoundSearchOption.
     * Create an instance of the search class BuildingSearch.
     * Get the search result by using the `-onGetBuildingResult(BuildingResult buildingResult)` callback method.


### Search building nearby

* File name：[SearchBuildingInboundActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchBuildingInboundActivity.java)

* Summary：Search building in the specified circular area.

* Detail：

     * Creating an instance of the search parameter class NearbySearchOption.
     * Create an instance of the search class BuildingSearch.
     * Get the search result by using the `-onGetBuildingResult(BuildingResult buildingResult)` callback method.

### Search building by building ID

* File name：[SearchBuildingDetailActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchBuildingDetailActivity.java)

* Summary：Search building by building ID.

* Detail：

     * Creating an instance of the search parameter class DetailSearchOption.
     * Create an instance of the search class BuildingSearch.
     * Get the search result by using the `-onGetBuildingDetailResult(BuildingDetailResult buildingDetailResult)` callback method.

### Get the POI categories by building or floor

* File name：[SearchPoiCategoriesInBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoiCategoriesInBuildingActivity.java)

* Summary：Get all the POI categories of the specified building or floor.

* Detail：

     * Creating an instance of the search parameter class PoiCategorySearchOption.
     * Create an instance of the search class PoiSearch.
     * Get the search result by using the `-onPoiCategoriesResult(PoiCategoryResult poiCategoryResult)` callback method.

### Search POI in the specified scene

* File name：[SearchPoiInBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoiInBuildingActivity.java)

* Summary：Search POI in the specified scene.

* Detail：

     * Creating an instance of the search parameter class PoiInBuildingSearchOption.
     * Create an instance of the search class PoiSearch.
     * Get the search result by using the `-onGetPoiResult(PoiResult poiResult)` callback method.
     
### Search POI in the specified area

* File name：[SearchPoiInboundActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoiInboundActivity.java)

* Summary：Search POI in the specified retangular area.

* Detail：

     * Creating an instance of the search parameter class PoiBoundSearchOption.
     * Create an instance of the search class PoiSearch.
     * Get the search result by using the `-onGetPoiResult(PoiResult poiResult)` callback method.


### Search POI nearby

* File name：[SearchPoiNearbyActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoiNearbyActivity.java)

* Summary：Search POI in the specified circular area.

* Detail：

     * Creating an instance of the search parameter class PoiNearbySearchOption.
     * Create an instance of the search class PoiSearch.
     * Get the search result by using the `-onGetPoiResult(PoiResult poiResult)` callback method.

### Search POI by POI ID

* File name：[SearchPoiDetailActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoiDetailActivity.java)

* Summary：Search POI by POI ID.

* Detail：

     * Creating an instance of the search parameter class DetailSearchOption.
     * Create an instance of the search class PoiSearch.
     * Get the search result by using the `-onGetPoiDetailResult(PoiDetailResult poiDetailResult)` callback method.


## Integration Cases

### Surrounding environment recognition

* File name：[SearchPoiWithOrientationActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/integrationcases/SearchPoiWithOrientationActivity.java)

* Summary：Make a virtual location and identify POI information around the location.

* Detail：

     * Set virtual location points.
     * Creating an instance of the search parameter class PoiOrientationSearchOption.
     * Create an instance of the search class PoiSearch.
     * Get the search result by using the `-onGetPoiByOrientationResult(PoiOrientationResult poiOrientationResult)` callback method.


### Route planning and navigation

* File name：[RoutePlanningActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/integrationcases/route/RoutePlanningActivity.java)

* Summary：Search the route between the starting point and end point, and show the road adsorption.

* Detail：

     * Creating an instance of the search parameter class RoutePlanningRequest.
     * Create an instance of the search class RoutePlanning.
     * Get the search result by using the `-onGetRoutePlanningResult(RoutePlanningResult routePlanningResult)` callback method.
     * Create an instance of WalkRouteOverlay for route mapping.
     * Creating an instance of Navigation for route adsorption.
     * Create an instance of RouteShortener to follow a shortened route.


### Visual map

* File name：[DisplayVisualActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/integrationcases/DisplayVisualActivity.java)

* Summary：Integration of Visual map.

* Detail：

     * Create an instance of VisualPolylineOverlay to plot information points on a map.
     * Create an instance of MapxusVisual to show Visual map.
     
* Before using

   *  Add visual map sdk dependency to your project's build.gradle file:
```groovy
dependencies {
    	implementation 'com.mapxus.visual:mapxusvisual:0.2.3'
	}
```


### Explore building

* File name：[ExploreBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/integrationcases/explore/ExploreBuildingActivity.java)

* Summary：Common case of POI search in the building.

* Detail：

     * Select an interior building.      
     * Creating an instance of the search parameter class PoiCategorySearchOption.
     * Create an instance of the search class PoiSearch.
     * Get the search result by using the `-onPoiCategoriesResult(PoiCategoryResult poiCategoryResult)` callback method.
     * Creating an instance of PoiInBuildingSearchOption by specifying a category.
     * Get the search result by using the `-onGetPoiResult(PoiResult poiResult)` callback method.
     * Click on POI for details.