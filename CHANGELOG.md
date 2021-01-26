# Mapxus Map SDK Change Log

## 4.1.0

### Features
* Add multilingual street names and venue id in IndoorBuilding model

## 4.0.9

### Features
* Add get the latest introductions api when in navigation mode.
* Add change navigation track resource size function.
* Compatible with Android 11.
* Use alias to customize style instead of full address.

## 4.0.8

### Bugs
* Fix bugs caused by location layer.

### Features
* Dynamically change the opacity of the building base.
* Add change navigation track resource function.

## 4.0.7

### Bugs
* Fix Nullpointer Exception in new marker function.

## 4.0.6

### Features
* New marker function.
* Make new map style as default

## 4.0.5

### Features
* New map style.

## 4.0.4

### Features
* Add navigation function include route shortener , route adsorber and callback when the destination is reached.

## 4.0.3

### Bugs
* Fix bugs caused by caching.

### Features
* Distinguish indoor and outdoor line colors when drawing navigation lines.

## 4.0.2

### Bugs
* Fix map rotation animation problem when positioning in heading mode.
* Fix the visibility of the building selector.
* Fix the callback of finishing drawing on the map when the point is outdoor.
* Fix can not search by multiple id (include building and poi) problem.

### Features
* Add set map label language function.
* Add setBuildingGestureSwitch method to turn off the ability to switch buildings by clicking on a map.
* Add setBuildingAutoSwitch method to turn off the ability to switch buildings when moving on the map.
* Add long click on the map callback.
* Add region field in IndoorBuildingInfo.
* Add poi search result sorting function (just in search nearby).
* Make compass icon is displayed by default.

## 4.0.1

### Features
* Add venue name and venue id in building search callback.

## 4.0.0

### Features
* Migrate to AndroidX.
* Use mapxus positioning SDK 2.0.0.
* Simplify Mapxus SDK dependencies.


## 3.2.6

### Bugs
* Fix can not use pointsearch.
* Fix Buildingsearch parsing exception.

### Features
* Add multi-language tag named "accessibilityDetail" of POI.

* Now you can get multi-language title and description of POI category search API in SDK.


## 3.2.5

### Bugs
* Fix multi-language data return.

### Features
* Add Korean and Japanese support.


## 3.2.4

### Bugs
* Fix can not show some building data.(Add multiple layouts to search building data)


## 3.2.3

### Bugs
* Fix floor filtering rules.

### Features
* Upgrade to mims2.


## 3.2.2

### Features
* Add buildingName tag in pointinfo class.


## 3.2.1

### Features
* Add init route method in WalkRouteOverlay class.
* Highlight instruction when selected.
* Update wayfinding connector icon.
* Users can use pointsearch to search buildings and pois at the same time.


## 3.2.0

### Bugs
* Fix wayfinding instructions.(the first point of instruction not match it's instruction)
* Fix map TalkBack.(do not read the logo)

### Features
* User can config the map element language and not dependence on system language.
* When we trigger the even of buildingChange,we can get the properties like 'bbox' and 'mult language name' from building.
* When we click the poi.We can get the buildingId、floor、type and location.
* Wayfinding add vehicle param.Support foot and wheelchair.
